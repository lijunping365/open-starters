package com.saucesubfresh.starter.lottery.interceptor;

import com.saucesubfresh.starter.lottery.domain.LotteryRequest;
import com.saucesubfresh.starter.lottery.filter.LotteryFilterChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 默认的任务的前置拦截器
 */
@Slf4j
public class DefaultLotteryBeforeInterceptor<Req extends LotteryRequest> implements LotteryBeforeInterceptor<Req> {

  @Autowired
  private LotteryFilterChain<Req> filterChain;

  /**
   * 1 执行过滤器链
   * @param request request
   */
  @Override
  public void before(Req request) throws Throwable {
    log.info("[抽奖前置拦截器]:{}", request);
    filterChain.doFilter(request);
  }
}
