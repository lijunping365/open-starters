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
package com.saucesubfresh.starter.crawler.pipeline.provider;

import com.saucesubfresh.starter.crawler.domain.FieldExtractor;
import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.enums.ExpressionType;
import com.saucesubfresh.starter.crawler.exception.CrawlerException;
import com.saucesubfresh.starter.crawler.parser.ParserProcessor;
import com.saucesubfresh.starter.crawler.pipeline.CrawlerHandler;
import com.saucesubfresh.starter.crawler.pipeline.CrawlerHandlerContext;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lijunping
 */
public class ResultSetExtractorHandler implements CrawlerHandler {

    @Override
    public void handler(CrawlerHandlerContext ctx, SpiderRequest request, Object msg) throws CrawlerException {
        final List<FieldExtractor> fieldExtractors = request.getExtract();
        if (Objects.isNull(msg) || CollectionUtils.isEmpty(fieldExtractors)){
            return;
        }

        Map<String, Object> parseResult = parse((String) msg, fieldExtractors);
        ctx.fireCrawlerHandler(request, parseResult);
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
}
