package com.saucesubfresh.starter.lottery.filter;


import com.saucesubfresh.starter.lottery.domain.LotteryRequest;
import com.saucesubfresh.starter.lottery.service.LotteryService;
import com.saucesubfresh.starter.lottery.utils.MapComparable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lijunping on 2022/1/10
 */
public class FilterFactory<T extends LotteryRequest> implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    private LotteryService lotteryService;

    private final Map<String, LotteryFilter<T>> filterMap = new HashMap<>();

    private final Map<String, Integer> filterSortMap = new HashMap<>();

    private final ConcurrentMap<Long, List<LotteryFilter<T>>> LOTTERY_FILTER = new ConcurrentHashMap<>();

    public List<LotteryFilter<T>> getFilters(Long actId){
        List<LotteryFilter<T>> lotteryFilters = LOTTERY_FILTER.get(actId);
        if (!CollectionUtils.isEmpty(lotteryFilters)){
            return lotteryFilters;
        }

        List<String> actFilterChain = lotteryService.getActFilterChain(actId);
        if (CollectionUtils.isEmpty(actFilterChain)){
            return Collections.emptyList();
        }

        Map<String, LotteryFilter<T>> map = new HashMap<>();
        actFilterChain.forEach(key-> map.put(filterSortMap.get(key) + key, filterMap.get(key)));
        Map<String, LotteryFilter<T>> sortByKey = MapComparable.sortByKey(map, true);
        List<LotteryFilter<T>> values = (List<LotteryFilter<T>>) sortByKey.values();
        LOTTERY_FILTER.put(actId, values);
        return values;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void afterPropertiesSet() {
        this.lotteryService = applicationContext.getBean(LotteryService.class);
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Filter.class);
        if (CollectionUtils.isEmpty(beansWithAnnotation)){
            return;
        }
        beansWithAnnotation.forEach((k,v)->{
            Filter annotation = v.getClass().getAnnotation(Filter.class);
            filterMap.put(annotation.value(), (LotteryFilter<T>) v);
            filterSortMap.put(annotation.value(), annotation.order());
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
