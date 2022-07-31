package com.saucesubfresh.starter.lottery.test;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author lijunping on 2022/1/7
 */
public class HandLuckTest {

    /**
     * 红包最小金额
     */
    private long minAmount = 1L;

    /**
     * 最大的红包是平均值的N倍
     */
    private static final long N = 2;

    /**
     * 红包最大金额
     */
    private long maxAmount;

    /**
     * 红包总金额
     */
    private long packageAmount;

    /**
     * 红包个数
     */
    private long packageSize;

    /**
     * 是否抢完
     */
    private boolean finish;

    /**
     * 存储红包的金额顺序
     */
    private final TreeMap<Long, Long> treeMap = Maps.newTreeMap((o1, o2) -> o1 > o2 ? 1 : o1.equals(o2) ? 0 : -1);

    /**
     * 构造函数不写业务逻辑
     */
    public HandLuckTest(long packageAmount, int packageSize){
        this.packageAmount = packageAmount;
        this.packageSize = packageSize;
        maxAmount = (packageAmount * N)/ packageSize;
    }

    public HandLuckTest(long packageAmount, int packageSize, long minAmount){
        this.packageAmount = packageAmount;
        this.packageSize = packageSize;
        this.minAmount = minAmount;
    }

    /**
     * 获取金额
     */
    public synchronized void nextAmount(){
        // 前置校验，初始化
        if(treeMap.size() == 0){
            treeMap.put(packageAmount, 0L);
            for (int i = 0; i < packageSize - 1; i++) {
                // 随机抽取切片
                long splitNum = RandomUtils.nextLong(minAmount, packageAmount);
                Long higher = treeMap.higherKey(splitNum);
                higher = higher == null ? packageAmount : higher;
                Long lower = treeMap.lowerKey(splitNum);
                lower = lower == null ? 0 : lower;
                // 相同切片重新生成,和上一个或者下一个切片间隔小于minAmount的重新生成
                while (higher - splitNum <= minAmount
                        || splitNum - lower <= minAmount
                        || treeMap.containsKey(splitNum)){
                    splitNum = RandomUtils.nextLong(minAmount, packageAmount);
                }
                // value放入上一个entry的key,组成链条，防止再次循环
                treeMap.put(splitNum, lower);
                treeMap.put(higher, splitNum);
            }
            System.out.println("init finish");
        }

        List<Long> result = new ArrayList<>();

        for (int i = 0; i < packageSize; i++) {
            Map.Entry<Long, Long> entry = treeMap.pollFirstEntry();
            if(treeMap.size() == 0){
                if(entry == null){
                    break;
                }
            }
            result.add(entry.getKey() - entry.getValue());
        }


//        for (int i = 0; i < packageSize; i++) {
//            Map.Entry<Long, Long> entry = treeMap.pollFirstEntry();
//            if(treeMap.size() != 0){
//                result.add(entry.getKey() - entry.getValue());
//            }
//        }

        System.out.println(StringUtils.join(result, "，"));
    }

    public static void main(String[] args) {
        HandLuckTest redPackage = new HandLuckTest(1500L, 10, 10L);
        redPackage.nextAmount();

    }
}
