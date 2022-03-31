package com.lightcode.starter.http.executor;


import com.lightcode.starter.http.constants.HttpConstant;
import com.lightcode.starter.http.exception.HttpException;
import com.lightcode.starter.http.request.HttpRequest;

/**
 * @author: 李俊平
 * @Date: 2022-01-25 23:12
 */
public abstract class AbstractHttpExecutor implements HttpExecutor{

    @Override
    public String execute(HttpRequest httpRequest) throws HttpException {
        HttpConstant.Method method = HttpConstant.Method.valueOf(httpRequest.getMethod());
        switch (method){
            case GET:{
                return doGet(httpRequest);
            }
            case POST:{
                return doPost(httpRequest);
            }
        }
        throw new HttpException("Unsupported method：" + httpRequest.getMethod());
    }

    public abstract String doGet(HttpRequest httpRequest) throws HttpException;

    public abstract String doPost(HttpRequest httpRequest) throws HttpException;
}
