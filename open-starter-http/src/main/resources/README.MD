# 提供两种请求方式 httpClient 和 okHttpClient

# 版本升级说明

1.0.1

1. 替换 httpClient post form 表单请求为 APPLICATION_JSON 式请求

1.0.2

1. OkHttp 去掉 retryOnConnectionFailure 属性

2. 优化注入方式以及配置，默认注入的是 OkHttpExecutor