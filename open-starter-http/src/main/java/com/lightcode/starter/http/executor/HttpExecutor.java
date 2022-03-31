package com.lightcode.starter.http.executor;


import com.lightcode.starter.http.exception.HttpException;
import com.lightcode.starter.http.request.HttpRequest;

/**
 * @author: 李俊平
 * @Date: 2022-01-25 22:54
 */
public interface HttpExecutor {

    /**
     * 执行 http 请求
     * @param httpRequest
     * @return
     * @throws HttpException
     */
    String execute(HttpRequest httpRequest) throws HttpException;
}
