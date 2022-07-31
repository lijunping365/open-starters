package com.saucesubfresh.starter.lottery.interceptor;

import com.saucesubfresh.starter.lottery.domain.LotteryRequest;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 任务的前拦截器
 */
public interface LotteryBeforeInterceptor<Req extends LotteryRequest> {

  /**
   * 在任务执行之前执行，如果出错请抛出 异常
   *
   * @param request request
   * @throws Throwable 前置请求出错的异常
   */
  default void before(Req request) throws Throwable {
  }

}
