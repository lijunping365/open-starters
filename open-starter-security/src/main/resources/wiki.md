# 实现原理

## HandlerInterceptor

spring mvc 的拦截器是只拦截 controller，注意和 Filter 的区别

一个请求过来 ，先进行过滤器处理，看程序是否受理该请求。过滤器放过后，程序中的拦截器进行处理 。

HandlerInterceptor 拦截的是请求地址，所以针对请求地址做一些验证、预处理等操作比较合适。

## 原理   

HandlerInterceptor 的方法有一个handler参数。而 handler 可以进行强转 HandlerMethod handlerMethod = (HandlerMethod) handler;

然后获取到 handlerMethod.getMethod() 获取的是 Controller 中的方法，便可以解析上面的注解。然后根据注解值可以选择性的进行拦截。
