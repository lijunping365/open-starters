package com.saucesubfresh.starter.crawler.config;

import com.saucesubfresh.starter.crawler.generator.DefaultKeyGenerator;
import com.saucesubfresh.starter.crawler.generator.KeyGenerator;
import com.saucesubfresh.starter.crawler.handler.CrawlerSpiderHandler;
import com.saucesubfresh.starter.crawler.handler.DefaultCrawlerSpiderHandler;
import com.saucesubfresh.starter.crawler.pipeline.*;
import com.saucesubfresh.starter.crawler.properties.CrawlerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lijunping on 2022/7/13
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CrawlerProperties.class)
public class CrawlerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public KeyGenerator keyGenerator(){
        return new DefaultKeyGenerator();
    }

    @Bean
    @ConditionalOnMissingBean
    public DownloadPipeline downloadPipeline(){
        return new DefaultDownloadPipeline();
    }

    @Bean
    @ConditionalOnMissingBean
    public ParserPipeline parserPipeline(){
        return new DefaultParserPipeline();
    }

    @Bean
    @ConditionalOnMissingBean
    public FormatPipeline formatPipeline(){
        return new DefaultFormatPipeline();
    }

    @Bean
    @ConditionalOnMissingBean
    public FillPipeline fillPipeline(KeyGenerator keyGenerator){
        return new DefaultFillPipeline(keyGenerator);
    }

    @Bean
    @ConditionalOnMissingBean
    public PersistencePipeline persistencePipeline(){
        return new DefaultPersistencePipeline();
    }

    @Bean
    @ConditionalOnMissingBean
    public CrawlerSpiderHandler crawlerSpiderHandler(){
        return new DefaultCrawlerSpiderHandler();
    }
}
