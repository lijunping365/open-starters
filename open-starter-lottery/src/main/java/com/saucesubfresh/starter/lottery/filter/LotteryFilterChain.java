package com.saucesubfresh.starter.lottery.filter;

import com.saucesubfresh.starter.lottery.domain.LotteryRequest;

import java.util.List;

/**
 * 执行抽奖之前过滤器链
 * 一旦某个过滤器抛出异常就会中断抽奖的执行
 * @author lijunping on 2021/12/27
 */
public class LotteryFilterChain<T extends LotteryRequest>{

    private final FilterFactory<T> filterFactory;

    public LotteryFilterChain(FilterFactory<T> filterFactory) {
        this.filterFactory = filterFactory;
    }

    public void doFilter(T request){
        List<LotteryFilter<T>> filters = filterFactory.getFilters(request.getActId());
        filters.forEach(e->e.doFilter(request));
    }
}
