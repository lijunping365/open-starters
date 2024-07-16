package com.openbytecode.starter.captcha.core.image.kaptcha;

import java.awt.image.BufferedImage;

/**
 * Responsible for creating captcha image with a text drawn on it.
 *
 * Copyright https://github.com/penggle/kaptcha
 */
public interface KaptchaProducer {
    /**
     * Create an image which will have written a distorted text.
     *
     * @param text
     *            the distorted characters
     * @return image with the text
     */
    BufferedImage createImage(String text);

    /**
     * @return the text to be drawn
     */
    String createText();
}
