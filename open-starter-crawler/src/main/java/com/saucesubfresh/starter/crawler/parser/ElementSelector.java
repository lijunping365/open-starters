/*
 * Copyright © the webmagic project
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
package com.saucesubfresh.starter.crawler.parser;

import org.jsoup.nodes.Element;

import java.util.List;

/**
 * Selector(extractor) for html elements
 *
 * @see <a href="https://github.com/code4craft/webmagic">...</a>
 */
public interface ElementSelector extends Selector{

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
