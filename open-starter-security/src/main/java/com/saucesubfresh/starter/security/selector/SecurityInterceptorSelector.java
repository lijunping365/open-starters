package com.saucesubfresh.starter.security.selector;

import com.saucesubfresh.starter.security.interceptor.SecurityInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;


/**
 * @author lijunping on 2022/1/20
 */
@Slf4j
public class SecurityInterceptorSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{SecurityInterceptor.class.getName()};
    }
}
