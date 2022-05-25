package com.saucesubfresh.starter.cache.selector;

import com.saucesubfresh.starter.cache.aspect.CacheAspect;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author lijunping on 2022/5/25
 */
public class CacheConfigurationSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{CacheAspect.class.getName()};
    }
}
