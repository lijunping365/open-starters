package com.saucesubfresh.starter.lottery.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author lijunping on 2021/12/9
 */
@Getter
public class LotteryTaskEvent extends ApplicationEvent {
    private final LotteryEventType lotteryEventType;
    private final LotteryEventInformation lotteryEventInformation;

    public LotteryTaskEvent(Object source, LotteryEventType lotteryEventType, LotteryEventInformation lotteryEventInformation){
        super(source);
        this.lotteryEventType = lotteryEventType;
        this.lotteryEventInformation = lotteryEventInformation;
    }
}
