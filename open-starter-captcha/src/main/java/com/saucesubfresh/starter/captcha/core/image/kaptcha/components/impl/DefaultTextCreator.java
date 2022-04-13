package com.saucesubfresh.starter.captcha.core.image.kaptcha.components.impl;

import com.saucesubfresh.starter.captcha.core.image.kaptcha.components.TextProducer;
import com.saucesubfresh.starter.captcha.properties.CaptchaProperties;
import com.saucesubfresh.starter.captcha.properties.ImageCodeProperties;

import java.security.SecureRandom;
import java.util.Random;


/**
 * {@link DefaultTextCreator} creates random text from an array of characters
 * with specified length.
 *
 * Copyright https://github.com/penggle/kaptcha
 */
public class DefaultTextCreator implements TextProducer {

	private final CaptchaProperties captchaProperties;

	public DefaultTextCreator(CaptchaProperties captchaProperties) {
		this.captchaProperties = captchaProperties;
	}

	/**
	 * @return the random text
	 */
	public String getText() {
		final ImageCodeProperties imageProps = captchaProperties.getImage();
		int length = imageProps.getLength();
		char[] chars = imageProps.getResourceText().toCharArray();
		Random rand = new SecureRandom();
		StringBuffer text = new StringBuffer();
		for (int i = 0; i < length; i++) {
			text.append(chars[rand.nextInt(chars.length)]);
		}

		return text.toString();
	}
}
