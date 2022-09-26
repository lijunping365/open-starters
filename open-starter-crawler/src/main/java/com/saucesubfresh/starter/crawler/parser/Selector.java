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

import java.util.List;

/**
 * Selector(extractor) for text.
 *
 * @see <a href="https://github.com/code4craft/webmagic">...</a>
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
