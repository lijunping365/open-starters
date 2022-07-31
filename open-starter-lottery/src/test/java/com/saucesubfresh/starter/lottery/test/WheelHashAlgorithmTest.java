package com.saucesubfresh.starter.lottery.test;

import com.saucesubfresh.starter.lottery.algorithm.WheelHashAlgorithm;
import com.saucesubfresh.starter.lottery.service.WheelAward;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: 李俊平
 * @Date: 2022-07-31 11:18
 */
public class WheelHashAlgorithmTest {

    public static void main(String[] args) {
        List<WheelAward> awardList = new ArrayList<>();

        WheelAward award1 = new WheelAward();
        award1.setAwardId(1L);
        award1.setAwardRate(BigDecimal.valueOf(0.2));

        WheelAward award2 = new WheelAward();
        award2.setAwardId(2L);
        award2.setAwardRate(BigDecimal.valueOf(0.1));

        WheelAward award3 = new WheelAward();
        award3.setAwardId(3L);
        award3.setAwardRate(BigDecimal.valueOf(0.2));

        WheelAward award4 = new WheelAward();
        award4.setAwardId(4L);
        award4.setAwardRate(BigDecimal.valueOf(0.3));

        awardList.add(award1);
        awardList.add(award2);
        awardList.add(award3);
        awardList.add(award4);

        List<Long> init = WheelHashAlgorithm.init(awardList);
        System.out.println(init);
    }
}
