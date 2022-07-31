package com.saucesubfresh.starter.lottery.component;


import com.saucesubfresh.starter.lottery.domain.LotteryRequest;
import com.saucesubfresh.starter.lottery.domain.LotteryResponse;
import com.saucesubfresh.starter.lottery.event.LotteryEventInformation;
import com.saucesubfresh.starter.lottery.event.LotteryEventType;
import com.saucesubfresh.starter.lottery.event.LotteryTaskEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;


/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 默认任务执行成功处理器
 */
@Slf4j
public class DefaultLotteryExecuteSuccessHandler<Req extends LotteryRequest, Resp extends LotteryResponse> implements LotteryExecuteSuccessHandler<Req, Resp> {

  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;

  @Override
  public void onDrawExecuteSuccess(Req request, Resp response) {
    log.info("[抽奖执行成功]：{}-{}", request, response);
    LotteryEventInformation eventInformation = new LotteryEventInformation();
    eventInformation.setLotteryRequest(request);
    eventInformation.setLotteryResponse(response);
    LotteryTaskEvent lotteryTaskEvent = new LotteryTaskEvent(this, LotteryEventType.EXECUTE_SUCCESS, eventInformation);
    applicationEventPublisher.publishEvent(lotteryTaskEvent);
  }
}
