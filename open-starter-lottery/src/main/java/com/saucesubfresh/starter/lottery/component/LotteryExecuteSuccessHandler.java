package com.saucesubfresh.starter.lottery.component;


import com.saucesubfresh.starter.lottery.domain.LotteryRequest;
import com.saucesubfresh.starter.lottery.domain.LotteryResponse;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 任务执行成功处理器
 */
public interface LotteryExecuteSuccessHandler<Req extends LotteryRequest, Resp extends LotteryResponse> {

  void onDrawExecuteSuccess(Req request, Resp response);
}
