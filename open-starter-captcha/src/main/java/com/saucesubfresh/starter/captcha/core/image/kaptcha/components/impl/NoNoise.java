package com.saucesubfresh.starter.captcha.core.image.kaptcha.components.impl;

import com.saucesubfresh.starter.captcha.core.image.kaptcha.components.NoiseProducer;

import java.awt.image.BufferedImage;

/**
 * Imlemention of NoiseProducer that does nothing.
 * 
 * Copyright https://github.com/penggle/kaptcha
 */
public class NoNoise implements NoiseProducer {
	/**
	 */
	public void makeNoise(BufferedImage image, float factorOne, float factorTwo, float factorThree, float factorFour) {
		//Do nothing.
	}
}
