package com.saucesubfresh.starter.crawler.pipeline.provider;

import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.domain.SpiderResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author lijunping on 2022/4/15
 */
@Slf4j
@Component
@AllArgsConstructor
public class DataRepositoryPipeline implements ChannelHandler {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    @Override
    public void handler(SpiderRequest request, SpiderResponse response) {
        List<Map<String, Object>> data = response.getFormatResult();
        log.info("异步持久化操作-发送给消息队列 {}", data);
        try {
            CrawlerMessage crawlerMessage = CrawlerMessage
                    .builder()
                    .spiderId(request.getSpiderId())
                    .data(data)
                    .build();
            kafkaTemplate.send("crawler-data", SerializationUtils.serialize(crawlerMessage));
        }catch (Exception e){
            throw new SpiderException(e.getMessage());
        }
    }
}
