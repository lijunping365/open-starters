/*
 * Copyright © 2022 organization SauceSubFresh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.saucesubfresh.starter.crawler.interceptor;

import com.saucesubfresh.starter.crawler.domain.FieldExtractor;
import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.generator.KeyGenerator;
import com.saucesubfresh.starter.crawler.handler.ResultSetHandler;
import com.saucesubfresh.starter.crawler.plugin.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 插件示例
 * @author lijunping on 2022/10/27
 */
@Slf4j
@Intercepts({@Signature(type = ResultSetHandler.class, method = "handler", args = {SpiderRequest.class, String.class})})
public class ResultSetInterceptor implements Interceptor {

    private static final String ID = "id";
    private static final String CREATE_TIME = "createTime";
    private final KeyGenerator keyGenerator;

    public ResultSetInterceptor(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = invocation.proceed();
        if (Objects.isNull(result)){
            return null;
        }
        final Object arg = invocation.getArgs()[0];
        SpiderRequest spiderRequest = (SpiderRequest) arg;
        List<Map<String, Object>> list = (List<Map<String, Object>>) result;
        fillValue(spiderRequest, list);
        return list;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }

    /**
     * 填充值
     * @param request
     * @param formatResult
     */
    private void fillValue(SpiderRequest request, List<Map<String, Object>> formatResult) {
        List<FieldExtractor> fieldExtractors = request.getExtract();
        List<String> uniqueKeys = getUniqueKeys(fieldExtractors);
        for (Map<String, Object> rowData : formatResult) {
            String uniqueId = getUniqueId(rowData, uniqueKeys, request.getSpiderId());
            long time = new Date().getTime();
            rowData.putIfAbsent(ID, uniqueId);
            rowData.putIfAbsent(CREATE_TIME, time);
            fieldExtractors.stream().filter(e-> StringUtils.isNotBlank(e.getDefaultValue())).forEach(e->{
                rowData.putIfAbsent(e.getFieldName(), e.getDefaultValue());
            });
        }
    }

    /**
     * 注意：如果未指定唯一 id，将使用第一个字段为唯一 id
     * @return
     */
    public static List<String> getUniqueKeys(List<FieldExtractor> fieldExtractors){
        List<String> uniqueKeys = fieldExtractors.stream()
                .filter(FieldExtractor::isUnique)
                .map(FieldExtractor::getFieldName)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(uniqueKeys)){
            uniqueKeys = Collections.singletonList(fieldExtractors.get(0).getFieldName());
        }
        return uniqueKeys;
    }

    /**
     * 此方法返回数据的唯一 id，
     * 目的是在存入 ES 或其他数据库时防止重复，如果在 ES 或其他数据库中有此 id，就说明该条数据已经存在了，则应该进行更新操作
     * @param rowData
     * @param uniqueKeys
     * @param spiderId
     * @return
     */
    protected String getUniqueId(Map<String, Object> rowData, List<String> uniqueKeys, Long spiderId){
        StringBuilder sb = new StringBuilder();
        uniqueKeys.forEach(e->{
            final Object o = rowData.get(e);
            sb.append(Objects.isNull(o) ? StringUtils.EMPTY : o.toString());
        });
        return keyGenerator.generate(spiderId + sb.toString());
    }
}
