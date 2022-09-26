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

import com.saucesubfresh.starter.crawler.parser.Selector;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Replace selector.<br>
 *
 * @see <a href="https://github.com/code4craft/webmagic">...</a>
 */
public class ReplaceSelector implements Selector {

    private String regexStr;

    private String replacement;

    private Pattern regex;

    public ReplaceSelector(String regexStr, String replacement) {
        this.regexStr = regexStr;
        this.replacement = replacement;
        try {
            regex = Pattern.compile(regexStr);
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("invalid regex", e);
        }
    }

    @Override
    public String select(String text) {
        Matcher matcher = regex.matcher(text);
        return matcher.replaceAll(replacement);
    }

    @Override
    public List<String> selectList(String text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return regexStr + "_" + replacement;
    }

}
