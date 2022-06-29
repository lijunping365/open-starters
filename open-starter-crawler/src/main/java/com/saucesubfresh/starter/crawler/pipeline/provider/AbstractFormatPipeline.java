package com.saucesubfresh.starter.crawler.pipeline.provider;

import com.saucesubfresh.starter.crawler.domain.FieldExtractor;
import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.domain.SpiderResponse;
import com.saucesubfresh.starter.crawler.pipeline.Pipeline;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 抽象数据格式化类，子类实现包括
 * 1. 数据格式化策略（当勾选的字段数量 == 全部字段数量，则进行转置，否则不进行）
 * 2. id 生成策略
 * 3. 放入固定的字段
 *
 * @author lijunping on 2022/4/26
 */
public abstract class AbstractFormatPipeline implements Pipeline {

    @Override
    public void handler(SpiderRequest request, SpiderResponse response) {
        final Map<String, Object> parseResult = response.getParseResult();
        final List<FieldExtractor> fieldExtractors = response.getFieldExtractors();
        if (CollectionUtils.isEmpty(parseResult) || CollectionUtils.isEmpty(fieldExtractors)){
            return;
        }
        final List<Map<String, Object>> maps = doFormat(parseResult, fieldExtractors);
        response.setFormatResult(maps);
    }

    protected List<Map<String, Object>> formatObject(Map<String, Object> fields, List<String> uniqueKeys){
        List<Map<String, Object>> formatResult = new ArrayList<>();
        long time = new Date().getTime();
        fields.putIfAbsent(CommonConstant.ID, getUniqueIdForObj(fields, uniqueKeys));
        fields.putIfAbsent(CommonConstant.CREATE_TIME, time);
        formatResult.add(fields);
        return formatResult;
    }

    @SuppressWarnings("unchecked")
    protected List<Map<String, Object>> formatList(Map<String, Object> fields, List<String> uniqueKeys){
        List<Map<String, Object>> formatResult = new ArrayList<>();
        Map<String, List<String>> dataTmp = new HashMap<>();
        fields.forEach((key, value)-> dataTmp.put(key, (List<String>) value));
        int minSize = dataTmp.values().stream().map(List::size).min(Comparator.comparing(Integer::intValue)).orElse(0);

        for (int i = 0; i <minSize; i++) {
            Map<String, Object> result = new HashMap<>();
            int finalI = i;
            dataTmp.forEach((key, value)-> result.put(key, value.get(finalI)));
            long time = new Date().getTime();
            result.putIfAbsent(CommonConstant.ID, getUniqueIdForList(i, dataTmp, uniqueKeys));
            result.putIfAbsent(CommonConstant.CREATE_TIME, time);
            formatResult.add(result);
        }
        return formatResult;
    }

    /**
     * 此方法返回数据的唯一 id，
     * 目的是在存入 ES 或其他数据库时防止重复，如果在 ES 或其他数据库中有此 id，就说明该条数据已经存在了，则应该进行更新操作
     * @param index
     * @param dataTmp
     * @param uniqueKeys
     * @return 唯一 id
     */
    protected String getUniqueIdForList(int index, Map<String, List<String>> dataTmp, List<String> uniqueKeys){
        StringBuilder sb = new StringBuilder();
        uniqueKeys.forEach(e->{
            String s = dataTmp.get(e).get(index);
            sb.append(StringUtils.isBlank(s) ? StringUtils.EMPTY : s);
        });
        return hashUniqueId(sb.toString());
    }

    /**
     * 此方法返回数据的唯一 id，
     * 目的是在存入 ES 或其他数据库时防止重复，如果在 ES 或其他数据库中有此 id，就说明该条数据已经存在了，则应该进行更新操作
     * @param dataTmp
     * @param uniqueKeys
     * @return 唯一 id
     */
    protected String getUniqueIdForObj(Map<String, Object> dataTmp, List<String> uniqueKeys){
        StringBuilder sb = new StringBuilder();
        uniqueKeys.forEach(e->{
            String s = dataTmp.get(e).toString();
            sb.append(StringUtils.isBlank(s) ? StringUtils.EMPTY : s);
        });
        return hashUniqueId(sb.toString());
    }

    protected String hashUniqueId(String params){
        HashFunction hashFunction = Hashing.murmur3_32_fixed();
        return hashFunction.hashString(params, StandardCharsets.UTF_8).toString();
    }

    protected abstract List<Map<String, Object>> doFormat(Map<String, Object> parseResult, List<FieldExtractor> fieldExtractors);
}
