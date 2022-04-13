package com.saucesubfresh.starter.captcha.core.image.kaptcha.components;


import java.awt.image.BufferedImage;

/**
 * {@link BackgroundProducer} is responsible for adding background to an image.
 *
 * Copyright https://github.com/penggle/kaptcha
 */
public interface BackgroundProducer {

    BufferedImage addBackground(BufferedImage image);
}
