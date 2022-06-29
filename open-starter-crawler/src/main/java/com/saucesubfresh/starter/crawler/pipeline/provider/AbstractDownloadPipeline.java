package com.saucesubfresh.starter.crawler.pipeline.provider;

import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.domain.SpiderResponse;
import com.saucesubfresh.starter.crawler.pipeline.Pipeline;
import com.sun.deploy.net.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 网页下载抽象类
 */
@Slf4j
public abstract class AbstractDownloadPipeline implements Pipeline {

    @Override
    public void handler(SpiderRequest request, SpiderResponse response) {
        boolean success = false;
        int maxTimes = request.getRetryTimes();
        int currentTimes = 0;
        while (!success) {
            try {
                doDownload(request, response);
                success = true;
            }catch (RpcException e){
                log.error(e.getMessage(), e);
            }
            if (!success) {
                currentTimes++;
                if (currentTimes > maxTimes) {
                    throw new SpiderException("The number of download retries reaches the upper limit, " +
                            "the maximum number of times：" + maxTimes);
                }
                sleep(request.getSleepTime());
            }
        }
    }

    protected void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            log.error("Thread interrupted when sleep",e);
        }
    }

    protected HttpRequest buildHttpRequest(SpiderRequest request){
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(request.getUrl());
        String method = request.getMethod();
        httpRequest.setMethod(StringUtils.isNotBlank(method) ? method : HttpConstant.Method.GET.name());
        httpRequest.setParams(request.getParams());
        Map<String, String> headers = request.getHeaders();
        if (CollectionUtils.isEmpty(headers)){
            headers = new HashMap<>();
        }
        headers.putIfAbsent(HttpConstant.Header.CONTENT_TYPE, HttpConstant.ContentType.JSON);
        headers.putIfAbsent(HttpConstant.Header.USER_AGENT, HttpConstant.UserAgent.USER_AGENT_CHROME);
        httpRequest.setHeaders(headers);
        return httpRequest;
    }

    protected abstract void doDownload(SpiderRequest request, SpiderResponse response) throws SpiderException;
}
