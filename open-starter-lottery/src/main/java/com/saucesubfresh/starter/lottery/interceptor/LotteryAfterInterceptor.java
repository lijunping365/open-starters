package com.saucesubfresh.starter.lottery.interceptor;


import com.saucesubfresh.starter.lottery.domain.LotteryRequest;
import com.saucesubfresh.starter.lottery.domain.LotteryResponse;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 任务的后拦截器
 */
public interface LotteryAfterInterceptor<Req extends LotteryRequest, Resp extends LotteryResponse> {

  /**
   * 任务执行之后的处理，任务可能成功，也可能失败
   * <p>
   * 如果可以的话，请尽量保证该方法是异步的
   *  @param request    request
   * @param throwable 失败时抛出的异常
   */
  default void after(Req request, Resp response, Throwable throwable) {
  }

}
