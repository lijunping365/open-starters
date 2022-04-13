package com.saucesubfresh.starter.captcha.utils;

import com.saucesubfresh.starter.captcha.exception.ValidateCodeException;

import java.awt.*;
import java.lang.reflect.Field;

/**
 * This class provides helper methods in parsing configuration values.
 *
 * Copyright https://github.com/penggle/kaptcha
 */
public class KaptchaConfigHelper {

    public static Color getColor(String paramValue, Color defaultColor) {
        Color color;
        if ("".equals(paramValue) || paramValue == null) {
            color = defaultColor;
        } else if (paramValue.indexOf(",") > 0) {
            color = createColorFromCommaSeparatedValues(paramValue);
        } else {
            color = createColorFromFieldValue(paramValue);
        }
        return color;
    }

    public static Color createColorFromCommaSeparatedValues(String paramValue) {
        Color color;
        String[] colorValues = paramValue.split(",");
        try {
            int r = Integer.parseInt(colorValues[0]);
            int g = Integer.parseInt(colorValues[1]);
            int b = Integer.parseInt(colorValues[2]);
            if (colorValues.length == 4) {
                int a = Integer.parseInt(colorValues[3]);
                color = new Color(r, g, b, a);
            } else if (colorValues.length == 3) {
                color = new Color(r, g, b);
            } else {
                throw new ValidateCodeException("Color can only have 3 (RGB) or 4 (RGB with Alpha) values.");
            }
        } catch (Exception exception) {
            throw new ValidateCodeException(exception.getMessage());
        }
        return color;
    }

    public static Color createColorFromFieldValue(String paramValue) {
        Color color;
        try {
            Field field = Class.forName("java.awt.Color").getField(paramValue);
            color = (Color) field.get(null);
        } catch (Exception exception) {
            throw new ValidateCodeException(exception.getMessage());
        }
        return color;
    }

    public static Font[] getFonts(String paramValue, int fontSize, Font[] defaultFonts) {
        Font[] fonts;
        if ("".equals(paramValue) || paramValue == null) {
            fonts = defaultFonts;
        } else {
            String[] fontNames = paramValue.split(",");
            fonts = new Font[fontNames.length];
            for (int i = 0; i < fontNames.length; i++)
            {
                fonts[i] = new Font(fontNames[i], Font.BOLD, fontSize);
            }
        }
        return fonts;
    }
}
