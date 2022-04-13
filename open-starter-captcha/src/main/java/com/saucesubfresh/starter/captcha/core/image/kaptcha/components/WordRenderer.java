package com.saucesubfresh.starter.captcha.core.image.kaptcha.components;

import java.awt.image.BufferedImage;

/**
 * {@link WordRenderer} is responsible for rendering words.
 *
 * Copyright https://github.com/penggle/kaptcha
 */
public interface WordRenderer {

	BufferedImage renderWord(String word, int width, int height);
}
