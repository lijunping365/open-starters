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
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Selector in regex.<br>
 *
 * @see <a href="https://github.com/code4craft/webmagic">...</a>
 */
public class RegexSelector implements Selector {

    private String regexStr;

    private Pattern regex;

    private int group = 1;

    public RegexSelector(String regexStr, int group) {
        this.compileRegex(regexStr);
        this.group = group;
    }

    private void compileRegex(String regexStr) {
        if (StringUtils.isBlank(regexStr)) {
            throw new IllegalArgumentException("regex must not be empty");
        }
        try {
            this.regex = Pattern.compile(regexStr, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
            this.regexStr = regexStr;
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("invalid regex "+regexStr, e);
        }
    }

    /**
     * Create a RegexSelector. When there is no capture group, the value is set to 0 else set to 1.
     * @param regexStr the regular expression.
     */
    public RegexSelector(String regexStr) {
        this.compileRegex(regexStr);
        if (regex.matcher("").groupCount() == 0) {
            this.group = 0;
        } else {
            this.group = 1;
        }
    }

    @Override
    public String select(String text) {
        return selectGroup(text).get(group);
    }

    @Override
    public List<String> selectList(String text) {
        List<String> strings = new ArrayList<String>();
        List<RegexResult> results = selectGroupList(text);
        for (RegexResult result : results) {
            strings.add(result.get(group));
        }
        return strings;
    }

    public RegexResult selectGroup(String text) {
        Matcher matcher = regex.matcher(text);
        if (matcher.find()) {
            String[] groups = new String[matcher.groupCount() + 1];
            for (int i = 0; i < groups.length; i++) {
                groups[i] = matcher.group(i);
            }
            return new RegexResult(groups);
        }
        return RegexResult.EMPTY_RESULT;
    }

    public List<RegexResult> selectGroupList(String text) {
        Matcher matcher = regex.matcher(text);
        List<RegexResult> resultList = new ArrayList<RegexResult>();
        while (matcher.find()) {
            String[] groups = new String[matcher.groupCount() + 1];
            for (int i = 0; i < groups.length; i++) {
                groups[i] = matcher.group(i);
            }
            resultList.add(new RegexResult(groups));
        }
        return resultList;
    }

    @Override
    public String toString() {
        return regexStr;
    }

}
