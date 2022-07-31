package com.saucesubfresh.starter.lottery.algorithm;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.RandomUtils;

import java.util.*;

/**
 * 以下算法均为参考
 *
 * @author lijunping on 2022/1/10
 */
public class RedPacketDivideAlgorithm {

    /**
     * 红包最小金额
     */
    private static final int MIN = 1;

    /**
     * 最大的红包是平均值的N倍
     */
    private static final int N = 2;

    /**
     *  参考微信群红包算法：二倍均值法
     *
     *  实际上，微信群红包的 min 是 1 分钱，max 是剩余红包金额均值的两倍，为什么是这两个值，因为这么做会保证随机值的期望值等于均值，来保证不会因为抢红包的先后顺序而造成不公平。
     *
     *  这两个值是算法内设的，不提供给用户指定。另外总金额 sum 和数量 num 是由用户指定的。
     *
     *  为什么微信群红包要搞一个最大上限，因为如果不设置一个最大上限，会出现一种不公平的现象。就是越在前边领取红包的同学，其可随机范围越大，获得大额红包的几率也越高。
     *
     *  一旦前边的同学随机到一个较大的金额，后边的同学可以随机的范围就逐步收窄，抢红包就变成了一个拼手速的游戏了。
     *
     *  实际上，微信群红包采用的是二倍均值法，也就是每次随机上限为剩余红包金额均值的两倍。
     *
     *  微信群红包金额分配算法是这样的：每次抢红包直接随机，随机的范围是[1, 剩余红包金额均值的两倍]，单位分
     *
     *  但是，我们发现微信这种算法有时会出现这种情况：除了最后一次，任何一次抢到的金额都要小于人均金额的两倍，所以最后两个之一红包金额非常大的情况（例如：共 88 元，4 个红包，前两个红包金额分别是 3.97， 2.74，理论上都应该小于 44，但是如果第三个红包是 5.71， 那最后一个人只能是 75.58 ）
     *
     *  当然，出现这种情况的概率非常小，需要除了最后两次之前的所有结果都小于剩余均值，即小金额。
     *
     * （1）所有人抢到金额之和等于红包总金额，不能超过，也不能少于；
     * （2）抢到的红包金额至少是一分钱；
     * （3）要保证抢到红包的人获取到的红包金额是随机的。
     * @param num 红包数量
     * @param totalAmount 红包总金额
     */
    public static List<Integer> divide(Integer num, Integer totalAmount){
        List<Integer> divideResult = new ArrayList<>();
        Integer restAmount = totalAmount;
        Integer restNum = num;
        Random random = new Random();
        for (int i = 0; i < num -1; i++) {
            // 随机范围：[1，剩余人均金额的两倍)，左闭右开
            int amount = random.nextInt(restAmount / restNum * N -1) + MIN;
            restAmount -= amount;
            restNum --;
            divideResult.add(amount);
        }
        divideResult.add(restAmount);
        return divideResult;
    }

    /**
     * 线段切割法
     *
     * 何谓线段切割法？我们可以把红包总金额想象成一条很长的线段，而每个人抢到的金额，则是这条主线段所拆分出的若干子线段。
     *
     * 如何确定每一条子线段的长度呢？由“切割点”来决定。当N个人一起抢红包的时候，就需要确定N-1个切割点。
     *
     * 因此，当N个人一起抢总金额为M的红包时，我们需要做N-1次随机运算，以此确定N-1个切割点。随机的范围区间是（1， M）。
     *
     * 当所有切割点确定以后，子线段的长度也随之确定。这样每个人来抢红包的时候，只需要顺次领取与子线段长度等价的红包金额即可。
     *
     * 这个算法可以把总金额想象成一条线段，每个人都有机会切一刀，前面的人切剩下的后面的人再接着切，这样越是前面的人截取的长度（理解成领取到的红包金额）越大的概率就越大。
     *
     * @param minAmount 红包最小值
     * @param num 红包数量
     * @param totalAmount 红包总金额
     */
    public static List<Long> divide(Long minAmount, Integer num, Long totalAmount){
        TreeMap<Long, Long> treeMap = Maps.newTreeMap((o1, o2) -> o1 > o2 ? 1 : o1.equals(o2) ? 0 : -1);
        treeMap.put(totalAmount, 0L);
        for (int i = 0; i < num - 1; i++) {
            // 随机抽取切片
            long splitNum = RandomUtils.nextLong(minAmount, totalAmount);
            Long higher = treeMap.higherKey(splitNum);
            higher = higher == null ? totalAmount : higher;
            Long lower = treeMap.lowerKey(splitNum);
            lower = lower == null ? 0 : lower;
            // 相同切片重新生成,和上一个或者下一个切片间隔小于minAmount的重新生成
            while (higher - splitNum <= minAmount
                    || splitNum - lower <= minAmount
                    || treeMap.containsKey(splitNum)){
                splitNum = RandomUtils.nextLong(minAmount, totalAmount);
            }
            // value放入上一个entry的key,组成链条，防止再次循环
            treeMap.put(splitNum, lower);
            treeMap.put(higher, splitNum);
        }

        List<Long> result = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Map.Entry<Long, Long> entry = treeMap.pollFirstEntry();
            if(treeMap.size() == 0){
                if(entry == null){
                    break;
                }
            }
            result.add(entry.getKey() - entry.getValue());
        }
        return result;
    }
}
