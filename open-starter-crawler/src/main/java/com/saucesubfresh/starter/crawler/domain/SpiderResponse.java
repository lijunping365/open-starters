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
package com.saucesubfresh.starter.crawler.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * 爬虫爬取响应
 *
 * @author lijunping
 */
@Data
public class SpiderResponse implements Serializable {

    private static final long serialVersionUID = 9078244810505397098L;

    /**
     * 爬取内容（原始数据）
     */
    private String body;

    /**
     * 数据解析结果，类似此结构的 json
     * <pre>
     * {
     *  "time" : [ "18:36", "18:31"],
     *  "title" : [ "【长春发布若干政策措施支持市场主体纾困促进经济恢复发展】", "【佩斯科夫：克宫不支持无差别对“不友好国家”公司资产进行国有化】"],
     *  "url" : [ "http://finance.eastmoney.com/a/202204212352230938.html", "http://finance.eastmoney.com/a/202204212352227421.html"]
     * }
     * </pre>
     *
     */
    private Map<String, Object> parseResult;

    /**
     * 数据格式化结果，类似此结构的 json
     * <pre>
     * [{
     *    "id" : "1",
     *    "time" : "18:36",
     *    "title" : "【长春发布若干政策措施支持市场主体纾困促进经济恢复发展】",
     *    "url" : "http://finance.eastmoney.com/a/202204212352230938.html"
     *  },
     *  {
     *    "id" : "2",
     *    "time" : "18:31",
     *    "title" : "【佩斯科夫：克宫不支持无差别对“不友好国家”公司资产进行国有化】",
     *    "url" : "http://finance.eastmoney.com/a/202204212352227421.html"
     *  }]
     * </pre>
     *
     *
     */
    private List<Map<String, Object>> formatResult;
}
