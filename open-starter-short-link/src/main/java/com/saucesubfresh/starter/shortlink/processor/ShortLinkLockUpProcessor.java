package com.saucesubfresh.starter.shortlink.processor;

import com.saucesubfresh.starter.shortlink.request.ShortLinkLockUpRequest;
import com.saucesubfresh.starter.shortlink.request.ShortLinkLockUpResponse;

/**
 * @author lijunping on 2022/11/3
 */
public interface ShortLinkLockUpProcessor {

    ShortLinkLockUpResponse lockUp(ShortLinkLockUpRequest request);
}
