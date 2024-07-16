/*
 * Copyright © 2022 organization openbytecode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.openbytecode.starter.cache.generator;

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
 * @author lijunping
 */
public class SimpleKeyGenerator implements KeyGenerator{

    private static final String COLON = ":";
    private static final String UNDER_LINE = "_";
    private static final ExpressionParser PARSER = new SpelExpressionParser();
    @Override
    public String generate(String cacheKey, Method method, Object[] args) {
        Assert.notNull(method, "Method must not be null");
        String prefix = method.getName();
        String suffix = "";
        if (args.length > 0 && StringUtils.isNotBlank(cacheKey)){
            suffix = buildSuffix(method, args, cacheKey);
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
                continue;
            }
            suffix.append(value).append(UNDER_LINE);
        }

        suffix.delete(suffix.length() - 1, suffix.length());
        return suffix.toString();
    }
}
