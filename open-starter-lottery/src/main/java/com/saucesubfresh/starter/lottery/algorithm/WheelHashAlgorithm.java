package com.saucesubfresh.starter.lottery.algorithm;


import com.saucesubfresh.starter.lottery.service.WheelAward;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

/**
 * @author lijunping on 2022/1/11
 */
public class WheelHashAlgorithm {

    /**
     * 斐波那契散列增量，逻辑：黄金分割点：(√5 - 1) / 2 = 0.6180339887，Math.pow(2, 32) * 0.6180339887 = 0x61c88647
     */
    private static final int HASH_INCREMENT = 0x61c88647;

    /**
     * 数组初始化长度 128，保证数据填充时不发生碰撞的最小初始化值
     */
    private static final int RATE_TUPLE_LENGTH = 128;

    public static List<Long> init(List<WheelAward> awardList){
        Long[] rateTuple = new Long[RATE_TUPLE_LENGTH];
        int cursorVal = 0;
        for (WheelAward awardRateInfo : awardList) {
            int rateVal = awardRateInfo.getAwardRate().multiply(new BigDecimal(100)).intValue();
            // 循环填充概率范围值
            for (int i = cursorVal + 1; i <= (rateVal + cursorVal); i++) {
                rateTuple[hashIdx(i)] = awardRateInfo.getAwardId();
            }
            cursorVal += rateVal;
        }
        return Arrays.asList(rateTuple);
    }

    /**
     * 斐波那契（Fibonacci）散列法，计算哈希索引下标值
     *
     * @param val 值
     * @return 索引
     */
    public static int hashIdx(int val) {
        int hashCode = val * HASH_INCREMENT + HASH_INCREMENT;
        return hashCode & (RATE_TUPLE_LENGTH - 1);
    }

    public static int generateSecureRandomIntCode(int bound) {
        return new SecureRandom().nextInt(bound) + 1;
    }
}
