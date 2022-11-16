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
package com.saucesubfresh.starter.crawler.handler;


import com.saucesubfresh.starter.crawler.domain.FieldExtractor;
import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.enums.ExpressionType;
import com.saucesubfresh.starter.crawler.parser.ParserProcessor;
import com.saucesubfresh.starter.crawler.plugin.UsePlugin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 默认结果处理器
 *
 * @author lijunping
 */
@UsePlugin
public class DefaultResultSetHandler implements ResultSetHandler{

    @Override
    public List<Map<String, Object>> handler(SpiderRequest request, String body) {
        final List<FieldExtractor> fieldExtractors = request.getExtract();
        if (StringUtils.isBlank(body) || CollectionUtils.isEmpty(fieldExtractors)){
            return null;
        }

        Map<String, Object> parseResult = parse(body, fieldExtractors);
        if (CollectionUtils.isEmpty(parseResult)){
            return null;
        }
        return format(parseResult, fieldExtractors);
    }

    /**
     * 数据格式化策略（当勾选的字段数量 == 全部字段数量，则进行转置，否则不进行处理）
     *
     * @return 数据格式化结果，类似此结构的 json
     *
     * <pre>
     * [{
     *    "time" : "18:36",
     *    "title" : "【长春发布若干政策措施支持市场主体纾困促进经济恢复发展】",
     *    "url" : "http://finance.eastmoney.com/a/202204212352230938.html"
     *  },
     *  {
     *    "time" : "18:31",
     *    "title" : "【佩斯科夫：克宫不支持无差别对“不友好国家”公司资产进行国有化】",
     *    "url" : "http://finance.eastmoney.com/a/202204212352227421.html"
     *  }]
     * </pre>
     *
     *
     */
    private List<Map<String, Object>> format(Map<String, Object> parseResult, List<FieldExtractor> fieldExtractors){
        long count = fieldExtractors.stream().filter(FieldExtractor::isMulti).count();
        if (fieldExtractors.size() == count){
            return formatList(parseResult);
        }else {
            return Collections.singletonList(parseResult);
        }
    }

    /**
     * @return 数据解析结果，类似此结构的 json
     *
     * <pre>
     * {
     *  "time" : [ "18:36", "18:31"],
     *  "title" : [ "【长春发布若干政策措施支持市场主体纾困促进经济恢复发展】", "【佩斯科夫：克宫不支持无差别对“不友好国家”公司资产进行国有化】"],
     *  "url" : [ "http://finance.eastmoney.com/a/202204212352230938.html", "http://finance.eastmoney.com/a/202204212352227421.html"]
     * }
     * </pre>
     *
     */
    private Map<String, Object> parse(String body, List<FieldExtractor> fieldExtractors){
        ExpressionType of = ExpressionType.of(fieldExtractors.get(0).getExpressionType());
        return ParserProcessor.processor(of, body, fieldExtractors);
    }

    /**
     * 格式化解析结果
     *
     * @param fields
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> formatList(Map<String, Object> fields){
        List<Map<String, Object>> formatResult = new ArrayList<>();
        Map<String, List<String>> dataTmp = new HashMap<>();
        fields.forEach((key, value)-> dataTmp.put(key, (List<String>) value));
        int maxSize = dataTmp.values().stream().map(List::size).max(Comparator.comparing(Integer::intValue)).orElse(0);
        for (int i = 0; i <maxSize; i++) {
            Map<String, Object> rowData = new HashMap<>();
            int finalI = i;
            dataTmp.forEach((key, value)-> rowData.put(key, finalI >= value.size() ? null : value.get(finalI)));
            formatResult.add(rowData);
        }
        return formatResult;
    }
}
