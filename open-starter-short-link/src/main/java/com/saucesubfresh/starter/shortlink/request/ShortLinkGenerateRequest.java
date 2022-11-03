package com.saucesubfresh.starter.shortlink.request;

import lombok.Data;

/**
 * @author lijunping on 2022/11/3
 */
@Data
public class ShortLinkGenerateRequest {

    /**
     * 原始链接
     */
    private String originLink;
}
