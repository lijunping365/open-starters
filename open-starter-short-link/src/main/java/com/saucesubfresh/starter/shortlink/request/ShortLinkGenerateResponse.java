package com.saucesubfresh.starter.shortlink.request;

import lombok.Data;

/**
 * @author lijunping on 2022/11/3
 */
@Data
public class ShortLinkGenerateResponse {

    /**
     * 短链
     */
    private String shortLink;
}
