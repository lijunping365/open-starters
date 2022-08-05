package com.saucesubfresh.starter.lottery.executor;

import com.saucesubfresh.starter.lottery.algorithm.RedPacketDivideAlgorithm;
import com.saucesubfresh.starter.lottery.domain.RedPacketLotteryRequest;
import com.saucesubfresh.starter.lottery.domain.RedPacketLotteryResponse;
import com.saucesubfresh.starter.lottery.exception.LotteryException;
import com.saucesubfresh.starter.lottery.initializer.LotteryInitializer;
import com.saucesubfresh.starter.lottery.manager.AwardStock;
import com.saucesubfresh.starter.lottery.manager.LotteryManager;
import com.saucesubfresh.starter.lottery.manager.RedPacketAwardStock;
import com.saucesubfresh.starter.lottery.service.AwardService;
import com.saucesubfresh.starter.lottery.domain.RedPacketAward;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 红包抽奖执行器
 * @author lijunping on 2022/1/7
 */
@Slf4j
public class DefaultRedPacketLotteryExecutor implements RedPacketLotteryExecutor, LotteryManager {

    private static final String PREFIX = "RED_PACKET_QUEUE:";

    private final AwardService awardService;
    private final RedisTemplate<String, Object> redisTemplate;

    public DefaultRedPacketLotteryExecutor(AwardService awardService,
                                           RedisTemplate<String, Object> redisTemplate) {
        this.awardService = awardService;
        this.redisTemplate = redisTemplate;
    }


    @Override
    public RedPacketLotteryResponse doDraw(RedPacketLotteryRequest request){
        RedPacketLotteryResponse response = new RedPacketLotteryResponse();
        final Optional<Long> takeResult = this.take(request.getActId());
        response.setLuckyAmount(takeResult.orElse(0L));
        return response;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> take(Long actId) {
        final Object o = redisTemplate.opsForList().rightPop(PREFIX + actId);
        return (Optional<T>) Optional.ofNullable(o);
    }

    @Override
    public void init(Long actId) {
        final Optional<List<RedPacketAward>> awardList = awardService.getAwardList(actId);
        final List<RedPacketAward> redPacketAwards = awardList.orElseThrow(()-> new LotteryException("Lottery award must not be null"));
        final RedPacketAward redPacketAward = redPacketAwards.get(0);
        List<Long> divideResult = RedPacketDivideAlgorithm.divide(redPacketAward.getMinAmount(), redPacketAward.getPackageSize(), redPacketAward.getPackageAmount());
        redisTemplate.opsForList().leftPushAll(PREFIX + actId, divideResult);
        log.info("{}:init finish", actId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends AwardStock> List<T> getAwardStock(Long actId) {
        List<RedPacketAwardStock> wheelAwardStocks = new ArrayList<>();
        final List<Object> range = redisTemplate.opsForList().range(PREFIX + actId, 0, -1);
        if (!CollectionUtils.isEmpty(range)){
            range.forEach(e->{
                RedPacketAwardStock redPacket = new RedPacketAwardStock();
                redPacket.setAmount((Long) e);
                wheelAwardStocks.add(redPacket);
            });
        }
        return (List<T>) wheelAwardStocks;
    }
}
