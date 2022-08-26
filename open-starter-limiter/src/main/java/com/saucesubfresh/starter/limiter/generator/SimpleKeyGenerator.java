package com.saucesubfresh.starter.limiter.generator;


import com.saucesubfresh.starter.limiter.annotation.Limiter;
import com.saucesubfresh.starter.limiter.exception.LimitException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

/**
 * @author lijunping on 2021/12/1
 */
public class SimpleKeyGenerator implements KeyGenerator{

  private static final String COLON = ":";
  private static final String UNDER_LINE = "_";
  private static final ExpressionParser PARSER = new SpelExpressionParser();

  @Override
  public String generate(Method method, Object[] args) {
      Assert.notNull(method, "Method must not be null");
      Limiter annotation = method.getAnnotation(Limiter.class);
      String prefix = annotation.prefix();
      if (StringUtils.isBlank(prefix)) {
          prefix = method.getName();
      }

      String key = annotation.key();
      String suffix = "";
      if (args.length > 0 && StringUtils.isNotBlank(key)){
          suffix = buildSuffix(method, args, key);
      }
      return StringUtils.isNotBlank(suffix) ? prefix + COLON + suffix : prefix;
  }

  private String buildSuffix(Method method, Object[] args, String key){
      EvaluationContext context = new StandardEvaluationContext();
      Parameter[] parameters = method.getParameters();
      for (int i = 0; i < parameters.length; i++) {
          String name = parameters[i].getName();
          Object value = args[i];
          context.setVariable(name, value);
      }

      String[] keys = StringUtils.split(key, UNDER_LINE);
      StringBuilder suffix = new StringBuilder();
      for (String s : keys) {
          Object value = PARSER.parseExpression(s).getValue(context);
          if (Objects.isNull(value)){
              throw new LimitException(String.format("%s value must not be null", s));
          }
          suffix.append(value).append(UNDER_LINE);
      }

      suffix.delete(suffix.length() - 1, suffix.length());
      return suffix.toString();
  }
}
