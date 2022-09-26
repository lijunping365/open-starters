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
package com.saucesubfresh.starter.crawler.parser.provider;

import com.saucesubfresh.starter.crawler.parser.BaseElementSelector;
import org.jsoup.nodes.Element;
import org.springframework.util.CollectionUtils;
import us.codecraft.xsoup.XPathEvaluator;
import us.codecraft.xsoup.Xsoup;

import java.util.List;

/**
 * XPath selector based on Xsoup.<br>
 *
 * @see <a href="https://github.com/code4craft/webmagic">...</a>
 */
public class XpathSelector extends BaseElementSelector {

    private final XPathEvaluator xPathEvaluator;

    public XpathSelector(String xpathStr) {
        this.xPathEvaluator = Xsoup.compile(xpathStr);
    }

    @Override
    public String select(Element element) {
        return xPathEvaluator.evaluate(element).get();
    }

    @Override
    public List<String> selectList(Element element) {
        return xPathEvaluator.evaluate(element).list();
    }

    @Override
    public Element selectElement(Element element) {
        List<Element> elements = selectElements(element);
        if (!CollectionUtils.isEmpty(elements)){
            return elements.get(0);
        }
        return null;
    }

    @Override
    public List<Element> selectElements(Element element) {
        return xPathEvaluator.evaluate(element).getElements();
    }

    @Override
    public boolean hasAttribute() {
        return xPathEvaluator.hasAttribute();
    }
}
