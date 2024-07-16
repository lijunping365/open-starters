package com.openbytecode.starter.captcha.core.image.kaptcha;

import com.openbytecode.starter.captcha.core.image.kaptcha.components.BackgroundProducer;
import com.openbytecode.starter.captcha.core.image.kaptcha.components.GimpyEngine;
import com.openbytecode.starter.captcha.core.image.kaptcha.components.TextProducer;
import com.openbytecode.starter.captcha.core.image.kaptcha.components.WordRenderer;
import com.openbytecode.starter.captcha.properties.CaptchaProperties;
import com.openbytecode.starter.captcha.properties.ImageCodeProperties;
import com.openbytecode.starter.captcha.utils.KaptchaConfigHelper;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;


/**
 * Default {@link KaptchaProducer} implementation which draws a captcha image using
 * {@link WordRenderer}, {@link GimpyEngine}, {@link BackgroundProducer}.
 * Text creation uses {@link TextProducer}.
 *
 * Copyright https://github.com/penggle/kaptcha
 */
public class DefaultKaptchaProducer implements KaptchaProducer {

	private final CaptchaProperties captchaProperties;
	private final BackgroundProducer backgroundProducer;
	private final WordRenderer wordRenderer;
	private final GimpyEngine gimpyEngine;
	private final TextProducer textProducer;


	public DefaultKaptchaProducer(CaptchaProperties captchaProperties,
								  BackgroundProducer backgroundProducer,
								  WordRenderer wordRenderer,
								  GimpyEngine gimpyEngine,
								  TextProducer textProducer) {
		this.captchaProperties = captchaProperties;
		this.backgroundProducer = backgroundProducer;
		this.wordRenderer = wordRenderer;
		this.gimpyEngine = gimpyEngine;
		this.textProducer = textProducer;
	}

	/**
	 * Create an image which will have written a distorted text.
	 * 
	 * @param text
	 *            the distorted characters
	 * @return image with the text
	 */
	public BufferedImage createImage(String text) {
		final ImageCodeProperties imageProps = captchaProperties.getImage();
		boolean isBorderDrawn = imageProps.isBorder();
		int width = imageProps.getWidth();
		int height = imageProps.getHeight();

		BufferedImage bi = wordRenderer.renderWord(text, width, height);
		bi = gimpyEngine.getDistortedImage(bi);
		bi = backgroundProducer.addBackground(bi);
		Graphics2D graphics = bi.createGraphics();
		if (isBorderDrawn) {
			drawBox(graphics, imageProps.getBorderColor(), imageProps.getBorderThickness(), imageProps.getWidth(), imageProps.getHeight());
		}
		return bi;
	}

	private void drawBox(Graphics2D graphics, String borderColorStr, int borderThickness, int width, int height) {
		Color borderColor = KaptchaConfigHelper.getColor(borderColorStr, Color.BLACK);
		graphics.setColor(borderColor);
		if (borderThickness != 1) {
			BasicStroke stroke = new BasicStroke((float) borderThickness);
			graphics.setStroke(stroke);
		}

		Line2D line1 = new Line2D.Double(0, 0, 0, width);
		graphics.draw(line1);
		Line2D line2 = new Line2D.Double(0, 0, width, 0);
		graphics.draw(line2);
		line2 = new Line2D.Double(0, height - 1, width, height - 1);
		graphics.draw(line2);
		line2 = new Line2D.Double(width - 1, height - 1, width - 1, 0);
		graphics.draw(line2);
	}

	/**
	 * @return the text to be drawn
	 */
	public String createText() {
		return textProducer.getText();
	}
}
