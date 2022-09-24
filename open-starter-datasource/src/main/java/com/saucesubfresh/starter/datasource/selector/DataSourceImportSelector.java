package com.saucesubfresh.starter.datasource.selector;

import com.saucesubfresh.starter.datasource.aspect.SwitchAspect;
import com.saucesubfresh.starter.datasource.config.DynamicDataSourceAutoConfig;
import com.saucesubfresh.starter.datasource.support.DynamicDataSource;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author: 李俊平
 * @Date: 2021-05-18 21:54
 */
public class DataSourceImportSelector implements ImportSelector {
  @Override
  public String[] selectImports(AnnotationMetadata importingClassMetadata) {
    return new String[]{
        SwitchAspect.class.getName(),
        DynamicDataSource.class.getName(),
        DynamicDataSourceAutoConfig.class.getName()
    };
  }
}
