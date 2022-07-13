package com.saucesubfresh.starter.crawler.parser;

import java.util.List;

/**
 * @author lijunping on 2022/4/18
 */
public interface Selector {

    /**
     * Extract single result in text.<br>
     * If there are more than one result, only the first will be chosen.
     *
     * @param text text
     * @return result
     */
    String select(String text);

    /**
     * Extract all results in text.<br>
     *
     * @param text text
     * @return results
     */
    List<String> selectList(String text);
}
