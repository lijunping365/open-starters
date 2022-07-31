package com.saucesubfresh.starter.lottery.listener;

import com.saucesubfresh.starter.lottery.domain.LotteryRequest;
import com.saucesubfresh.starter.lottery.domain.LotteryResponse;
import com.saucesubfresh.starter.lottery.event.LotteryEventInformation;
import com.saucesubfresh.starter.lottery.event.LotteryEventType;
import com.saucesubfresh.starter.lottery.event.LotteryTaskEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author lijunping on 2021/12/9
 */
@Component
public class LotteryEventListener implements ApplicationListener<LotteryTaskEvent> {

    @Override
    public void onApplicationEvent(LotteryTaskEvent event) {
        LotteryEventType lotteryEventType = event.getLotteryEventType();
        LotteryEventInformation lotteryEventInformation = event.getLotteryEventInformation();
        LotteryRequest lotteryRequest = lotteryEventInformation.getLotteryRequest();
        LotteryResponse lotteryResponse = lotteryEventInformation.getLotteryResponse();
        switch (lotteryEventType){
            case EXECUTE_SUCCESS:
                // 如果中奖
                //if (lotteryResponse.getLucky()){
                    // 1. 扣减库存，
                    // 2. 插入中奖名单表，
                //}

                break;
            case EXECUTE_FAILURE:
                // doSomething...
                break;
        }
    }
}
