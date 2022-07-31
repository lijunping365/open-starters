package com.saucesubfresh.starter.lottery.component;

import com.saucesubfresh.starter.lottery.domain.LotteryRequest;
import com.saucesubfresh.starter.lottery.event.LotteryEventType;
import com.saucesubfresh.starter.lottery.event.LotteryTaskEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 默认任务执行失败处理器
 */
@Slf4j
public class DefaultLotteryExecuteFailureHandler<Req extends LotteryRequest> implements LotteryExecuteFailureHandler<Req> {

  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;

  @Override
  public void onDrawExecuteFailure(Req request, String msg) {
    log.info("[抽奖执行失败]：{}-{}", request, msg);
    LotteryTaskEvent lotteryTaskEvent = new LotteryTaskEvent(this, LotteryEventType.EXECUTE_FAILURE, null);
    applicationEventPublisher.publishEvent(lotteryTaskEvent);
  }
}
