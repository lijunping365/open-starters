package com.saucesubfresh.starter.lottery.executor;

import com.saucesubfresh.starter.lottery.algorithm.WheelHashAlgorithm;
import com.saucesubfresh.starter.lottery.domain.LotteryAward;
import com.saucesubfresh.starter.lottery.domain.WheelAward;
import com.saucesubfresh.starter.lottery.domain.WheelLotteryRequest;
import com.saucesubfresh.starter.lottery.domain.WheelLotteryResponse;
import com.saucesubfresh.starter.lottery.exception.LotteryException;
import com.saucesubfresh.starter.lottery.manager.AwardStock;
import com.saucesubfresh.starter.lottery.manager.LotteryManager;
import com.saucesubfresh.starter.lottery.manager.WheelAwardStock;
import com.saucesubfresh.starter.lottery.service.AwardService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;


/**
 * 转盘抽奖执行器
 *
 * @author: 李俊平
 * @Date: 2021-12-30 22:56
 */
@Slf4j
public class DefaultWheelLotteryExecutor implements WheelLotteryExecutor, LotteryManager {

    private static final String LOTTERY_PREFIX = "WHEEL_LOTTERY_KEY:";
    private static final String STOCK_PREFIX = "WHEEL_STOCK_KEY:";

    private final AwardService awardService;
    private final RedissonClient redissonClient;
    private final RedisTemplate<String, Object> redisTemplate;

    public DefaultWheelLotteryExecutor(AwardService awardService,
                                       RedissonClient redissonClient,
                                       RedisTemplate<String, Object> redisTemplate) {
        this.awardService = awardService;
        this.redissonClient = redissonClient;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public WheelLotteryResponse doDraw(WheelLotteryRequest request) {
        WheelLotteryResponse response = new WheelLotteryResponse();
        final Optional<LotteryAward> takeResult = this.take(request.getActId());
        response.setAward(takeResult.orElse(new LotteryAward()));
        return response;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> take(Long actId) {
        // 随机索引
        int randomVal = WheelHashAlgorithm.generateSecureRandomIntCode(100);
        int idx = WheelHashAlgorithm.hashIdx(randomVal);
        final Object o = redisTemplate.opsForHash().get(LOTTERY_PREFIX + actId, idx);
        if (Objects.nonNull(o)){
            RSemaphore semaphore = redissonClient.getSemaphore(STOCK_PREFIX + o);
            try {
                semaphore.acquire(1);
            } catch (InterruptedException e) {
                log.error("转盘抽奖扣减奖品库存异常: {}", e.getMessage());
            }
        }
        return (Optional<T>) Optional.ofNullable(o);
    }

    /**
     * 使用分布式的信号量作为奖品数量
     * @param actId
     */
    @Override
    public void init(Long actId) {
        final Optional<List<WheelAward>> awardResult = awardService.getAwardList(actId);
        final List<WheelAward> wheelAwards = awardResult.orElseThrow(() -> new LotteryException("Lottery award must not be null"));
        final List<Long> awardIdList = WheelHashAlgorithm.init(wheelAwards);
        for (int i = 1; i < awardIdList.size(); i++) {
            redisTemplate.opsForHash().put(LOTTERY_PREFIX + actId, i, awardIdList.get(i));
        }
        wheelAwards.forEach(e -> {
            RSemaphore semaphore = redissonClient.getSemaphore(STOCK_PREFIX + e.getAwardId());
            semaphore.trySetPermits(e.getAwardStock());
        });
        log.info("{}:init finish", actId);
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T extends AwardStock> List<T> getAwardStock(Long actId) {
        Optional<List<WheelAward>> awardResult = awardService.getAwardList(actId);
        if (!awardResult.isPresent()){
            return Collections.emptyList();
        }
        List<WheelAwardStock> wheelAwardStocks = new ArrayList<>();
        final List<WheelAward> wheelAwards = awardResult.get();
        wheelAwards.forEach(e -> {
            WheelAwardStock wheelAwardStock = new WheelAwardStock();
            wheelAwardStock.setAwardId(e.getAwardId());
            RSemaphore semaphore = redissonClient.getSemaphore(STOCK_PREFIX + e.getAwardId());
            wheelAwardStock.setAwardStock(semaphore.availablePermits());
            wheelAwardStocks.add(wheelAwardStock);
        });
        return (List<T>) wheelAwardStocks;
    }
}
