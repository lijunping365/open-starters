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
import com.saucesubfresh.starter.crawler.utils.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 默认结果处理器
 *
 * @author lijunping
 */
public class DefaultResultHandler extends AbstractResultHandler {

    @Override
    protected <T> List<T> doHandler(SpiderRequest request, String body, Class<T> clazz) {
        final List<FieldExtractor> fieldExtractors = request.getExtract();
        if (StringUtils.isBlank(body) || CollectionUtils.isEmpty(fieldExtractors)){
            return null;
        }

        Map<String, Object> parseResult = parse(body, fieldExtractors);
        if (CollectionUtils.isEmpty(parseResult)){
            return null;
        }

        List<Map<String, Object>> formatResult = format(parseResult, fieldExtractors);
        return JSON.parseList(JSON.toJSON(formatResult), clazz);
    }

    /**
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
        List<Map<String, Object>> formatResult;
        // 数据格式化策略（当勾选的字段数量 == 全部字段数量，则进行转置，否则不进行）
        if (fieldExtractors.size() == count){
            formatResult = formatList(parseResult);
        }else {
            formatResult = new ArrayList<>();
            formatResult.add(parseResult);
        }
        return formatResult;
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
        Map<String, Object> parseResult;
        if (ExpressionType.of(fieldExtractors.get(0).getExpressionType()) == ExpressionType.JsonPath){
            parseResult = parseJson(body, fieldExtractors);
        } else {
            parseResult = parseHtml(body, fieldExtractors);
        }
        return parseResult;
    }
}
