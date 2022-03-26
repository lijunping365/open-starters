package com.lightcode.starter.captcha.properties;

import lombok.Data;


/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 */
@Data
public class ScanCodeProperties {

    /** 二维码宽度 **/
    private int width = 300;

    /** 二维码高度 **/
    private int height = 300;

    /** 二维码外边距，0到4 **/
    private int margin = 1;

    /** 二维码有效期 /s**/
    private int expireTime = 120;
}
