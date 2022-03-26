package com.lightcode.starter.captcha.properties;

import lombok.Data;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 *
 * 验证码相关的封装类,之后还会有其他的验证码,所以将图片验证码，短信验证码等统一封装到该类里
 * */
@Data
public class ValidateCodeProperties {

	/**封装图片验证吗相关的属性*/
	private ImageCodeProperties image = new ImageCodeProperties();

	/**封装短信验证吗相关的属性*/
	private SmsCodeProperties sms = new SmsCodeProperties();

	/***封装扫码登陆相关的配置*/
	private ScanCodeProperties scan = new ScanCodeProperties();


}
