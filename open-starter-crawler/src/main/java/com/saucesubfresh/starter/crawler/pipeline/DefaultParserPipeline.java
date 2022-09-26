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
package com.saucesubfresh.starter.crawler.pipeline;


import com.saucesubfresh.starter.crawler.domain.FieldExtractor;
import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.domain.SpiderResponse;
import com.saucesubfresh.starter.crawler.enums.ExpressionType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 动态规则解析（自动模式）
 *
 * @author lijunping
 */
public class DefaultParserPipeline extends AbstractParserPipeline {

    @Override
    protected Map<String, Object> doParse(SpiderRequest request, SpiderResponse response) {
        String body = response.getBody();
        final List<FieldExtractor> fieldExtractors = request.getExtract();
        if (StringUtils.isBlank(body) || CollectionUtils.isEmpty(fieldExtractors)){
            return null;
        }
        Map<String, Object> dataMap;
        ExpressionType type = ExpressionType.of(fieldExtractors.get(0).getExpressionType());
        if (type == ExpressionType.JsonPath){
            dataMap = parseJson(body, fieldExtractors);
        } else {
            dataMap = parseHtml(body, fieldExtractors);
        }
        return dataMap;
    }
}
