package com.saucesubfresh.starter.captcha.core.image.kaptcha.components;

import java.awt.image.BufferedImage;

/**
 * {@link GimpyEngine} is responsible for applying image distortion.
 *
 * Copyright https://github.com/penggle/kaptcha
 */
public interface GimpyEngine {
    /**
     * @param baseImage
     *            the base image
     * @return the image with distortion applied to the base image
     */
    BufferedImage getDistortedImage(BufferedImage baseImage);
}
