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
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 默认数据格式化
 *
 * @author lijunping
 */
@Slf4j
public class DefaultFormatPipeline extends AbstractFormatPipeline {

    @Override
    protected List<Map<String, Object>> doFormat(SpiderRequest request, SpiderResponse response) {
        final Map<String, Object> parseResult = response.getParseResult();
        final List<FieldExtractor> fieldExtractors = request.getExtract();
        if (CollectionUtils.isEmpty(parseResult) || CollectionUtils.isEmpty(fieldExtractors)){
            return null;
        }
        long count = fieldExtractors.stream().filter(FieldExtractor::isMulti).count();
        List<Map<String, Object>> formatResult;
        // 如果所有字段的值解析后都是 List，则转换成 List
        if (fieldExtractors.size() == count){
            formatResult = formatList(parseResult);
        }else {
            formatResult = formatObject(parseResult);
        }
        return formatResult;
    }


}
