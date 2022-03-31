package com.lightcode.starter.http.exception;

/**
 * @author: 李俊平
 * @Date: 2022-01-26 11:44
 */
public class HttpException extends RuntimeException{
    public HttpException(String msg) {
        super(msg);
    }
}
