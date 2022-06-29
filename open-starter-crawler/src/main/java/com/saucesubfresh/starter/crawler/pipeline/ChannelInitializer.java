package com.saucesubfresh.starter.crawler.pipeline;

import java.util.List;

/**
 * @author lijunping on 2022/4/15
 */
public interface ChannelInitializer {

    List<Pipeline> initChannel();
}
