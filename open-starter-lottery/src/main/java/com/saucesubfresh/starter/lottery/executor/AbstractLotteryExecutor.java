package com.saucesubfresh.starter.lottery.executor;

import com.saucesubfresh.starter.lottery.component.LotteryExecuteFailureHandler;
import com.saucesubfresh.starter.lottery.component.LotteryExecuteSuccessHandler;
import com.saucesubfresh.starter.lottery.domain.LotteryRequest;
import com.saucesubfresh.starter.lottery.domain.LotteryResponse;
import com.saucesubfresh.starter.lottery.exception.LotteryException;
import com.saucesubfresh.starter.lottery.interceptor.LotteryAfterInterceptor;
import com.saucesubfresh.starter.lottery.interceptor.LotteryBeforeInterceptor;

import java.util.Optional;


/**
 * @author: 李俊平
 * @Date: 2021-12-28 22:48
 */
public abstract class AbstractLotteryExecutor<Req extends LotteryRequest, Resp extends LotteryResponse> implements LotteryExecutor<Req, Resp> {

    private final LotteryBeforeInterceptor<Req> lotteryBeforeInterceptor;
    private final LotteryAfterInterceptor<Req, Resp> lotteryAfterInterceptor;
    private final LotteryExecuteSuccessHandler<Req, Resp> lotteryExecuteSuccessHandler;
    private final LotteryExecuteFailureHandler<Req> lotteryExecuteFailureHandler;

    protected AbstractLotteryExecutor(LotteryBeforeInterceptor<Req> lotteryBeforeInterceptor,
                                      LotteryAfterInterceptor<Req, Resp> lotteryAfterInterceptor,
                                      LotteryExecuteSuccessHandler<Req, Resp> lotteryExecuteSuccessHandler,
                                      LotteryExecuteFailureHandler<Req> lotteryExecuteFailureHandler) {
        this.lotteryBeforeInterceptor = lotteryBeforeInterceptor;
        this.lotteryAfterInterceptor = lotteryAfterInterceptor;
        this.lotteryExecuteSuccessHandler = lotteryExecuteSuccessHandler;
        this.lotteryExecuteFailureHandler = lotteryExecuteFailureHandler;
    }

    @Override
    public Resp doDraw(Req request) throws LotteryException {
        Throwable throwable = null;
        Resp drawResult = null;
        try {
            onBefore(request);
            drawResult = execDraw(request);
            onSuccess(request, drawResult);
        }catch (Throwable t){
            throwable = t;
            onError(request, throwable);
        }finally {
            onComplete(request, drawResult, throwable);
        }
        return drawResult;
    }

    @Override
    public void doDrawAsync(Req request) throws LotteryException {
        this.doDraw(request);
    }

    protected void onBefore(Req request) throws Throwable {
        lotteryBeforeInterceptor.before(request);
    }

    protected void onSuccess(Req request, Resp drawResult) {
        lotteryExecuteSuccessHandler.onDrawExecuteSuccess(request, drawResult);
    }

    protected void onError(Req request, Throwable throwable) {
        lotteryExecuteFailureHandler.onDrawExecuteFailure(request, throwable.getMessage());
    }

    protected void onComplete(Req request, Resp response, Throwable throwable) {
        lotteryAfterInterceptor.after(request, response, throwable);
    }

    /**
     * 执行抽奖
     */
    protected abstract Resp execDraw(Req request);

    /**
     * 领取奖品
     */
    protected abstract <T> Optional<T> take(Long actId);

}
