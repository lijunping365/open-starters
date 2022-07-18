package com.saucesubfresh.starter.cache.executor;

import com.saucesubfresh.starter.cache.message.CacheMessage;

/**
 * 主要功能： 对缓存进行操作
 * @author lijunping on 2022/6/16
 */
public interface CacheExecutor {

    /**
     * 操作缓存
     * @param message 操作参数
     */
    void execute(CacheMessage message);
}
