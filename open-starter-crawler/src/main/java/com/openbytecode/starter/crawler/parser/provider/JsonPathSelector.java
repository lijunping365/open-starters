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
package com.openbytecode.starter.crawler.parser.provider;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ReadContext;
import com.openbytecode.starter.crawler.utils.JSON;
import com.openbytecode.starter.crawler.parser.Selector;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * JsonPath selector.<br>
 * Used to extract content from JSON.<br>
 *
 * @see <a href="https://github.com/code4craft/webmagic">...</a>
 */
public class JsonPathSelector implements Selector {

    private final ReadContext ctx;

    public JsonPathSelector(String jsonPathStr) {
        Configuration conf = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
        ctx = JsonPath.using(conf).parse(jsonPathStr);
    }

    @Override
    public String select(String text) {
        Object object = ctx.read(text);
        if (object == null) {
            return null;
        }
        if (object instanceof List) {
            List<?> list = (List<?>) object;
            if (!CollectionUtils.isEmpty(list)) {
                return toString(list.iterator().next());
            }
        }
        return object.toString();
    }

    @Override
    public List<String> selectList(String text) {
        List<String> list = new ArrayList<String>();
        Object object = ctx.read(text);
        if (object == null) {
            return list;
        }
        if (object instanceof List) {
            List<Object> items = (List<Object>) object;
            for (Object item : items) {
                list.add(toString(item));
            }
        } else {
            list.add(toString(object));
        }
        return list;
    }

    private String toString(Object object) {
        if (object instanceof Map) {
            return JSON.toJSON(object);
        } else {
            return String.valueOf(object);
        }
    }
}
