package com.saucesubfresh.starter.http.selector;

import com.saucesubfresh.starter.http.annotation.EnableHttpExecutor;
import com.saucesubfresh.starter.http.enums.HttpExecutorType;
import com.saucesubfresh.starter.http.exception.HttpException;
import com.saucesubfresh.starter.http.executor.support.HttpClientExecutor;
import com.saucesubfresh.starter.http.executor.support.OkHttpExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @author lijunping on 2022/1/20
 */
@Slf4j
public class HttpExecutorSelector implements ImportSelector {

    /**
     * The name of {@link HttpExecutorType} attributes in {@link EnableHttpExecutor}
     */
    private static final String CLIENT_TYPE_ATTRIBUTE_NAME = "clientType";

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> annotationAttributes =
                importingClassMetadata.getAnnotationAttributes(EnableHttpExecutor.class.getName());
        HttpExecutorType clientType = (HttpExecutorType) annotationAttributes.get(CLIENT_TYPE_ATTRIBUTE_NAME);
        log.info("Use the [{}] to execute http request", clientType);
        switch (clientType) {
            case OKHTTP:
                return new String[]{OkHttpExecutor.class.getName()};
            case HTTP_CLIENT:
                return new String[]{HttpClientExecutor.class.getName()};
        }
        throw new HttpException("Unsupported ClientType：" + clientType);
    }
}
