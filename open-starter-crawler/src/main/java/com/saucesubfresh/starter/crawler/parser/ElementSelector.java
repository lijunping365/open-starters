package com.saucesubfresh.starter.crawler.parser;

import org.jsoup.nodes.Element;

import java.util.List;

/**
 * @author lijunping on 2022/4/18
 */
public interface ElementSelector {

    /**
     * Extract single result in text.<br>
     * If there are more than one result, only the first will be chosen.
     *
     * @param element element
     * @return result
     */
    String select(Element element);

    /**
     * Extract all results in text.<br>
     *
     * @param element element
     * @return results
     */
    List<String> selectList(Element element);
}
