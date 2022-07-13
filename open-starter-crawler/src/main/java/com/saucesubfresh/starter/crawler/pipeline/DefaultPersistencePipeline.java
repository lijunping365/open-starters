package com.saucesubfresh.starter.crawler.pipeline;

import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.domain.SpiderResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @author lijunping on 2022/4/15
 */
@Slf4j
public class DefaultPersistencePipeline implements PersistencePipeline {

    @Override
    public void process(SpiderRequest request, SpiderResponse response) {
        List<Map<String, Object>> data = response.getFormatResult();
        log.info("异步持久化操作-发送给消息队列 {}", data);
    }
}
