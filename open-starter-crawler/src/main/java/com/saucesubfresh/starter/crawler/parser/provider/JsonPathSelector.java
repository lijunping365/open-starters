package com.saucesubfresh.starter.crawler.parser.provider;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.saucesubfresh.starter.crawler.parser.Selector;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * JsonPath selector.<br>
 * Used to extract content from JSON.<br>
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.2.1
 */
public class JsonPathSelector implements Selector {

    private final ReadContext ctx;

    public JsonPathSelector(String jsonPathStr) {
        ctx = JsonPath.parse(jsonPathStr);
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

    private String toString(Object object) {
        if (object instanceof Map) {
            return JSON.toJSON(object);
        } else {
            return String.valueOf(object);
        }
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
}
