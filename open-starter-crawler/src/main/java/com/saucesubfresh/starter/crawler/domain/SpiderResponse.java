package com.saucesubfresh.starter.crawler.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 爬虫爬取响应
 */
@Data
public class SpiderResponse implements Serializable {

    private static final long serialVersionUID = 9078244810505397098L;
    /**
     * 爬取内容（原始数据）
     */
    private String body;

    /**
     * 数据解析结果
     * 返回类似此结构的 json
     * <p>
     * {
     *  "time" : [ "18:36", "18:31"],
     *  "title" : [ "【长春发布若干政策措施支持市场主体纾困促进经济恢复发展】长春发布《关于支持市场主体纾困促进经济恢复发展的若干政策措施》。一是疫情防控专项支持措施，包括加大疫情防控资金支持力度、推动防疫产品研发及产业化、确保生产生活平稳有序3项具体措施。二是普惠性纾困支持措施，包括全面落实减税降费系列政策、推动减免房屋租金、延缓缴纳水电气费用等10项具体措施。（财联社）", "【佩斯科夫：克宫不支持无差别对“不友好国家”公司资产进行国有化】俄罗斯总统新闻秘书佩斯科夫当地时间21日表示，克宫不支持将所有来自“不友好国家”的公司资产国有化的提议，需要根据具体情况逐案处理。（央视新闻客户端）"],
     *  "url" : [ "http://finance.eastmoney.com/a/202204212352230938.html", "http://finance.eastmoney.com/a/202204212352227421.html"]
     * }
     * </p>
     *
     */
    private Map<String, Object> parseResult;

    /**
     * 数据格式化结果
     * 返回类似此结构的 json
     * <p>
     *  [{
     *      "time" : "17:53",
     *      "id" : "1-17:53",
     *      "title" : "【俄央行行长：计划明年起在实体经济中试行数字卢布结算】当地时间21日，俄罗斯央行行长纳比乌琳娜表示，俄央行将从2023年开始在实体经济中试行数字卢布结算，并尝试在国际结算中使用。目前俄央行正在进行数字卢布样本的测试，共有5家银行参与测试工作。正在研究数字卢布和数字钱包的发行，以及个人间转账。（央视新闻客户端）",
     *      "url" : "http://finance.eastmoney.com/a/202204212352188819.html"
     *    },
     *    {
     *      "time" : "16:33",
     *      "id" : "1-16:33",
     *      "title" : "【行情】在岸人民币兑美元收盘报6.4500，较上一交易日跌347个基点。（财联社）",
     *      "url" : "http://finance.eastmoney.com/a/202204212352070339.html"
     *    }]
     * </p>
     *
     *
     */
    private List<Map<String, Object>> formatResult;

    /**
     * 解析规则
     */
    private List<FieldExtractor> fieldExtractors;
}
