package com.saucesubfresh.starter.shortlink.generator;

import com.saucesubfresh.starter.shortlink.request.ShortLinkGenerateRequest;
import com.saucesubfresh.starter.shortlink.request.ShortLinkGenerateResponse;

/**
 * 根据长链生成短链
 *
 * @author lijunping on 2022/11/3
 */
public interface ShortLinkGenerator {

    ShortLinkGenerateResponse generator(ShortLinkGenerateRequest request);
}
