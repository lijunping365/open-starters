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

import com.openbytecode.starter.crawler.domain.SpiderRequest;
import com.openbytecode.starter.crawler.domain.SpiderResponse;
import com.openbytecode.starter.crawler.exception.CrawlerException;
import com.openbytecode.starter.crawler.pipeline.CrawlerHandler;
import com.openbytecode.starter.crawler.pipeline.CrawlerHandlerContext;
import org.apache.commons.lang3.ObjectUtils;

import java.util.*;

/**
 * @author lijunping
 */
public class ResultSetTransformHandler implements CrawlerHandler {
    @Override
    public void handler(CrawlerHandlerContext ctx, SpiderRequest request, SpiderResponse response) throws CrawlerException {
        Object data = response.getData();
        if (ObjectUtils.isEmpty(data)){
            return;
        }
        Map<String, Object> parseResult = (Map<String, Object>) data;
        List<Map<String, Object>> transformData = format(parseResult, request.isMulti());
        response.setData(transformData);
        ctx.fireCrawlerHandler(request, response);
    }

    /**
     * 数据格式化策略 (multi == true)
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
    private List<Map<String, Object>> format(Map<String, Object> parseResult, boolean multi){
        if (multi){
            return formatList(parseResult);
        }else {
            return Collections.singletonList(parseResult);
        }
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
