package com.saucesubfresh.starter.lottery.interceptor;

import com.saucesubfresh.starter.lottery.domain.LotteryRequest;
import com.saucesubfresh.starter.lottery.domain.LotteryResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 默认的任务的后置拦截器
 */
@Slf4j
public class DefaultLotteryAfterInterceptor<Req extends LotteryRequest, Resp extends LotteryResponse> implements LotteryAfterInterceptor<Req, Resp> {
  /**
   * 包装返回结果
   * 扣减库存
   * @param request    request
   * @param response   抽奖结果
   * @param throwable 失败时抛出的异常
   */
  @Override
  public void after(Req request, Resp response, Throwable throwable) {
    log.info("[抽奖后置拦截器]:{}", request);
  }

}
