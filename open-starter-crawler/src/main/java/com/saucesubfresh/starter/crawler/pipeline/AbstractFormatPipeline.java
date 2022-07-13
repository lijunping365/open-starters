package com.saucesubfresh.starter.crawler.pipeline;

import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.domain.SpiderResponse;
import com.saucesubfresh.starter.crawler.generator.KeyGenerator;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 抽象数据格式化类，子类实现包括
 *
 * 数据格式化策略（当勾选的字段数量 == 全部字段数量，则进行转置，否则不进行）
 *
 * @author lijunping on 2022/4/26
 */
public abstract class AbstractFormatPipeline implements FormatPipeline {

    @Override
    public void process(SpiderRequest request, SpiderResponse response) {
        List<Map<String, Object>> formatResult = doFormat(request, response);
        response.setFormatResult(formatResult);
    }

    protected List<Map<String, Object>> formatObject(Map<String, Object> fields){
        List<Map<String, Object>> formatResult = new ArrayList<>();
        formatResult.add(fields);
        return formatResult;
    }

    @SuppressWarnings("unchecked")
    protected List<Map<String, Object>> formatList(Map<String, Object> fields){
        List<Map<String, Object>> formatResult = new ArrayList<>();
        Map<String, List<String>> dataTmp = new HashMap<>();
        fields.forEach((key, value)-> dataTmp.put(key, (List<String>) value));
        int maxSize = dataTmp.values().stream().map(List::size).max(Comparator.comparing(Integer::intValue)).orElse(0);

        for (int i = 0; i <maxSize; i++) {
            Map<String, Object> rowData = new HashMap<>();
            int finalI = i;
            dataTmp.forEach((key, value)-> rowData.put(key, finalI >= value.size() ? null : value.get(finalI)));
            formatResult.add(rowData);
        }
        return formatResult;
    }

    protected abstract List<Map<String, Object>> doFormat(SpiderRequest request, SpiderResponse response);
}
