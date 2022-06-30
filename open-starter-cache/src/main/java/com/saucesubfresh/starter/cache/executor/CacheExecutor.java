package com.saucesubfresh.starter.cache.executor;

import com.saucesubfresh.starter.cache.message.CacheMessage;

/**
 * 主要功能： 监听外部命令，对缓存进行操作
 * @author lijunping on 2022/6/16
 */
public interface CacheExecutor {

    /**
     * 缓存预热，远程数据同步到本地
     * @param message 缓存名称
     */
    void execute(CacheMessage message);
}
