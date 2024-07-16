/*
 * Copyright © 2022 organization openbytecode
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
package com.openbytecode.starter.crawler.pipeline.provider;

import com.openbytecode.starter.crawler.domain.FieldExtractor;
import com.openbytecode.starter.crawler.domain.SpiderRequest;
import com.openbytecode.starter.crawler.domain.SpiderResponse;
import com.openbytecode.starter.crawler.exception.CrawlerException;
import com.openbytecode.starter.crawler.generator.KeyGenerator;
import com.openbytecode.starter.crawler.pipeline.CrawlerHandler;
import com.openbytecode.starter.crawler.pipeline.CrawlerHandlerContext;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lijunping
 */
public class ResultSetWrapperHandler implements CrawlerHandler {

    private static final String ID = "id";
    private static final String CREATE_TIME = "createTime";
    private final KeyGenerator keyGenerator;

    public ResultSetWrapperHandler(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    @Override
    public void handler(CrawlerHandlerContext ctx, SpiderRequest request, SpiderResponse response) throws CrawlerException {
        Object data = response.getData();
        if (ObjectUtils.isEmpty(data)){
            return;
        }
        List<Map<String, Object>> rows = (List<Map<String, Object>>) data;
        List<Map<String, Object>> wrapperRows = fillValue(request, rows);
        response.setData(wrapperRows);
        ctx.fireCrawlerHandler(request, response);
    }

    /**
     * 填充值
     * @param request
     * @param formatResult
     */
    private List<Map<String, Object>> fillValue(SpiderRequest request, List<Map<String, Object>> formatResult) {
        List<FieldExtractor> fieldExtractors = request.getExtract();
        List<String> uniqueKeys = getUniqueKeys(fieldExtractors);
        long time = System.currentTimeMillis();
        for (Map<String, Object> rowData : formatResult) {
            String uniqueId = getUniqueId(rowData, uniqueKeys, request.getSpiderId());
            rowData.putIfAbsent(ID, uniqueId);
            rowData.putIfAbsent(CREATE_TIME, time);
            fieldExtractors.stream().filter(e-> StringUtils.isNotBlank(e.getDefaultValue())).forEach(e->{
                rowData.putIfAbsent(e.getFieldName(), e.getDefaultValue());
            });
        }
        return formatResult;
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
