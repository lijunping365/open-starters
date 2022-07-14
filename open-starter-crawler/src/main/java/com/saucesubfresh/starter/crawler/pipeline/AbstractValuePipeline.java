package com.saucesubfresh.starter.crawler.pipeline;

import com.saucesubfresh.starter.crawler.domain.FieldExtractor;
import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.domain.SpiderResponse;
import com.saucesubfresh.starter.crawler.generator.KeyGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据填充
 *  1. id 生成策略
 *  2. 创建时间
 * @author lijunping on 2022/7/13
 */
public abstract class AbstractValuePipeline implements ValuePipeline {

    private static final String ID = "id";
    private static final String CREATE_TIME = "createTime";
    private final KeyGenerator keyGenerator;

    protected AbstractValuePipeline(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    @Override
    public void process(SpiderRequest request, SpiderResponse response) {
        List<FieldExtractor> fieldExtractors = request.getExtract();
        List<Map<String, Object>> formatResult = response.getFormatResult();
        if (CollectionUtils.isEmpty(formatResult)){
            return;
        }

        List<String> uniqueKeys = getUniqueKeys(fieldExtractors);
        for (Map<String, Object> rowData : formatResult) {
            String uniqueId = getUniqueId(rowData, uniqueKeys, request.getSpiderId());
            long time = new Date().getTime();
            rowData.putIfAbsent(ID, uniqueId);
            rowData.putIfAbsent(CREATE_TIME, time);
            fieldExtractors.stream().filter(e-> StringUtils.isNotBlank(e.getDefaultValue())).forEach(e->{
                rowData.putIfAbsent(e.getFieldName(), e.getDefaultValue());
            });
            doFillValue(request, rowData);
        }
    }

    /**
     * 如果未指定唯一 id，将使用第一个字段为唯一 id
     *
     * @return
     */
    protected List<String> getUniqueKeys(List<FieldExtractor> fieldExtractors){
        List<String> uniqueKeys = fieldExtractors.stream()
                .filter(FieldExtractor::isUnique)
                .map(FieldExtractor::getFieldName)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(uniqueKeys)){
            uniqueKeys = Collections.singletonList(fieldExtractors.get(0).getFieldName());
        }
        return uniqueKeys;
    }

    /**
     * 此方法返回数据的唯一 id，
     * 目的是在存入 ES 或其他数据库时防止重复，如果在 ES 或其他数据库中有此 id，就说明该条数据已经存在了，则应该进行更新操作
     * @param rowData
     * @param uniqueKeys
     * @return
     */
    protected String getUniqueId(Map<String, Object> rowData, List<String> uniqueKeys, Long spiderId){
        StringBuilder sb = new StringBuilder();
        uniqueKeys.forEach(e->{
            final Object o = rowData.get(e);
            sb.append(Objects.isNull(o) ? StringUtils.EMPTY : o.toString());
        });
        return keyGenerator.generate(spiderId + sb.toString());
    }

    protected abstract void doFillValue(SpiderRequest request, Map<String, Object> rowData);

}
