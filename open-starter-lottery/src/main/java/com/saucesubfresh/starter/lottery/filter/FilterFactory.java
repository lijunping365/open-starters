package com.saucesubfresh.starter.lottery.filter;


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

/**
 * @author lijunping on 2022/1/10
 */
public class FilterFactory implements ApplicationContextAware, InitializingBean {

    private LotteryService lotteryService;
    private ApplicationContext applicationContext;
    private final Map<String, LotteryFilter> filterMap = new ConcurrentHashMap<>();
    private final Map<String, Integer> filterSortMap = new ConcurrentHashMap<>();
    private final Map<Long, List<LotteryFilter>> LOTTERY_FILTER = new ConcurrentHashMap<>();

    public List<LotteryFilter> getFilters(Long actId){
        List<LotteryFilter> lotteryFilters = LOTTERY_FILTER.get(actId);
        if (!CollectionUtils.isEmpty(lotteryFilters)){
            return lotteryFilters;
        }

        List<String> actFilterChain = lotteryService.getActFilterChain(actId);
        if (CollectionUtils.isEmpty(actFilterChain)){
            return Collections.emptyList();
        }

        Map<String, LotteryFilter> map = new HashMap<>();
        actFilterChain.forEach(key-> map.put(filterSortMap.get(key) + key, filterMap.get(key)));
        Map<String, LotteryFilter> sortByKey = MapComparable.sortByKey(map, true);
        List<LotteryFilter> values = (List<LotteryFilter>) sortByKey.values();
        LOTTERY_FILTER.put(actId, values);
        return values;
    }

    @Override
    public void afterPropertiesSet() {
        this.lotteryService = applicationContext.getBean(LotteryService.class);
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Filter.class);
        if (CollectionUtils.isEmpty(beansWithAnnotation)){
            return;
        }
        beansWithAnnotation.forEach((k,v)->{
            Filter annotation = v.getClass().getAnnotation(Filter.class);
            filterMap.put(annotation.value(), (LotteryFilter) v);
            filterSortMap.put(annotation.value(), annotation.order());
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
