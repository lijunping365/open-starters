package com.saucesubfresh.starter.lottery.manager;

import lombok.Data;

/**
 * @author lijunping on 2022/1/13
 */
@Data
public class WheelAwardStock extends AwardStock{

    /**
     * 奖品 id
     */
    private Long awardId;

    /**
     * 奖品库存
     */
    private Integer awardStock;
}
