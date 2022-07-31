package com.saucesubfresh.starter.lottery.component;

import com.saucesubfresh.starter.lottery.domain.LotteryRequest;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 任务执行失败处理器
 */
public interface LotteryExecuteFailureHandler<Req extends LotteryRequest> {

  /**
   * 抽奖执行失败执行
   *
   * @param request 抽奖执行失败
   * @param msg  任务执行失败原因
   */
  void onDrawExecuteFailure(Req request, String msg);
}
