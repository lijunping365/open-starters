package com.saucesubfresh.starter.captcha.core.math;

import com.saucesubfresh.starter.captcha.properties.CaptchaProperties;
import com.saucesubfresh.starter.captcha.properties.MathImageCodeProperties;

import java.security.SecureRandom;
import java.util.*;


/**
 * {@link DefaultMathTextCreator} creates random text from an array of characters
 * with specified length.
 */
public class DefaultMathTextCreator implements MathTextProducer {

	private final CaptchaProperties captchaProperties;

	public DefaultMathTextCreator(CaptchaProperties captchaProperties) {
		this.captchaProperties = captchaProperties;
	}

	/**
	 * @return the random text
	 */
	public List<String> getText() {
		List<String> result = new ArrayList<>();
		MathImageCodeProperties mathProps = captchaProperties.getMath();
		int length = mathProps.getLength();
		char[] chars = mathProps.getResourceText().toCharArray();
		Random rand = new SecureRandom();
		int sum = 0;
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			char number = chars[rand.nextInt(chars.length)];
			sum += number;
			buffer.append(number);
			if (i != length -1){
				buffer.append("+");
			}
		}
		buffer.append("=?");
		result.add(buffer.toString());
		result.add(String.valueOf(sum));
		return result;
	}
}
