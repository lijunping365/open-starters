package com.saucesubfresh.starter.lottery.component;

import com.saucesubfresh.starter.lottery.domain.LotteryRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 默认任务执行失败处理器
 */
@Slf4j
public class DefaultLotteryExecuteFailureHandler<Req extends LotteryRequest> implements LotteryExecuteFailureHandler<Req> {

  @Override
  public void onDrawExecuteFailure(Req request, String msg) {
    log.info("[抽奖执行失败]：{}-{}", request, msg);
  }
}
