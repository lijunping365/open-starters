# 前言

关于 HTTPS 

在 java 中，客户端请求工具比较多，我们就以 HttpClient 为例，版本 4.5.13。

# 创建默认的 HttpClient

在本 starter 中以 HttpClient 为例

## 1.获得SSL连接工厂以及域名校验器

```java
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(HttpProperties.class)
public class HttpAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public PoolingHttpClientConnectionManager connectionManager(HttpProperties properties){
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(properties.getMaxPerRoute());
        connectionManager.setMaxTotal(properties.getPoolSize());
        return connectionManager;
    }

    @Bean
    @ConditionalOnMissingBean
    public CloseableHttpClient httpClient(HttpProperties properties,
                                          PoolingHttpClientConnectionManager connectionManager){
        RequestConfig requestConfig = RequestConfig.custom()
                //指客户端和服务器建立连接后，客户端从服务器读取数据的timeout，超出后会抛出SocketTimeOutException
                .setSocketTimeout(properties.getTimeOut())
                //指从连接池获取连接的 timeout
                .setConnectionRequestTimeout(properties.getTimeOut())
                //指客户端和服务器建立连接的timeout，就是http请求的三个阶段，一：建立连接；二：数据传送；三，断开连接。超时后会ConnectionTimeOutException
                .setConnectTimeout(properties.getTimeOut())
                .build();
        return HttpClients.custom().setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setUserAgent(HttpConstant.UserAgent.USER_AGENT_CHROME)
                .disableAutomaticRetries()
                .build();
    }
}
```

这里自定义了连接池管理器 PoolingHttpClientConnectionManager，我们看下 PoolingHttpClientConnectionManager 的构造方法。

```java
@Contract(threading = ThreadingBehavior.SAFE_CONDITIONAL)
public class PoolingHttpClientConnectionManager implements HttpClientConnectionManager, ConnPoolControl<HttpRoute>, Closeable {
    
    public PoolingHttpClientConnectionManager() {
        this(getDefaultRegistry());
    }
    
    private static Registry<ConnectionSocketFactory> getDefaultRegistry() {
        return RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
    }
}
```

在 PoolingHttpClientConnectionManager 的构造方法中会注册 http 和 https 两种协议，我们在看下 SSLConnectionSocketFactory.getSocketFactory() 做了什么

```java
@Contract(threading = ThreadingBehavior.SAFE)
@SuppressWarnings("deprecation")
public class SSLConnectionSocketFactory implements LayeredConnectionSocketFactory {
    /**
     * Obtains default SSL socket factory with an SSL context based on the standard JSSE
     * trust material ({@code cacerts} file in the security properties directory).
     * System properties are not taken into consideration.
     *
     * @return default SSL socket factory
     */
    public static SSLConnectionSocketFactory getSocketFactory() throws SSLInitializationException {
        return new SSLConnectionSocketFactory(SSLContexts.createDefault(), getDefaultHostnameVerifier());
    }

    /**
     * @since 4.4
     */
    public static HostnameVerifier getDefaultHostnameVerifier() {
        return new DefaultHostnameVerifier(PublicSuffixMatcherLoader.getDefault());
    }
}
```

在 getSocketFactory 方法中构造了 SSLConnectionSocketFactory，参数有两个分别是 SSLContext 和 HostnameVerifier

```java
public class SSLContexts {

    /**
     * Creates default factory based on the standard JSSE trust material
     * ({@code cacerts} file in the security properties directory). System properties
     * are not taken into consideration.
     *
     * @return the default SSL socket factory
     */
    public static SSLContext createDefault() throws SSLInitializationException {
        try {
            final SSLContext sslContext = SSLContext.getInstance(SSLContextBuilder.TLS);
            sslContext.init(null, null, null);
            return sslContext;
        } catch (final NoSuchAlgorithmException ex) {
            throw new SSLInitializationException(ex.getMessage(), ex);
        } catch (final KeyManagementException ex) {
            throw new SSLInitializationException(ex.getMessage(), ex);
        }
    }
}
```

SSLContext 是用的默认的，我们看到 sslContext.init 方法的三个参数都是 null。接下来我们看看 HttpClients 的 build 方法，代码在 org.apache.http.impl.client.HttpClientBuilder

```java
public class HttpClientBuilder {
    public CloseableHttpClient build() {
        //此处省略前面的代码......
        HttpClientConnectionManager connManagerCopy = this.connManager;
        //如果指定了连接池管理器则使用指定的，否则新建一个默认的
        if (connManagerCopy == null) {
            LayeredConnectionSocketFactory sslSocketFactoryCopy = this.sslSocketFactory;
            //如果指定 sslSocketFactory 则使用指定的，否则新建一个默认的
            if (sslSocketFactoryCopy == null) {
                //如果开启了使用环境变量，则获取系统支持的 https 协议，否则为 null
                final String[] supportedProtocols = systemProperties ? split(System.getProperty("https.protocols")) : null;
                //如果开启了使用环境变量，获取系统支持的 https 加密套件，否则为 null
                final String[] supportedCipherSuites = systemProperties ? split(System.getProperty("https.cipherSuites")) : null;
                //如果没有指定，使用默认的域名验证器，会根据ssl会话中服务端返回的证书来验证与域名是否匹配
                HostnameVerifier hostnameVerifierCopy = this.hostnameVerifier;
                if (hostnameVerifierCopy == null) {
                    hostnameVerifierCopy = new DefaultHostnameVerifier(publicSuffixMatcherCopy);
                }
                //如果指定了 SslContext 则生成定制的SSL连接工厂，否则使用默认的连接工厂
                if (sslContext != null) {
                    sslSocketFactoryCopy = new SSLConnectionSocketFactory(sslContext, supportedProtocols, supportedCipherSuites, hostnameVerifierCopy);
                } else {
                    if (systemProperties) {
                        sslSocketFactoryCopy = new SSLConnectionSocketFactory(
                                (SSLSocketFactory) SSLSocketFactory.getDefault(),
                                supportedProtocols, supportedCipherSuites, hostnameVerifierCopy);
                    } else {
                        sslSocketFactoryCopy = new SSLConnectionSocketFactory(
                                SSLContexts.createDefault(),
                                hostnameVerifierCopy);
                    }
                }
            }
            //将Ssl连接工厂注册到连接池管理器中，当需要产生Https连接的时候，会根据上面的SSL连接工厂生产SSL连接
            @SuppressWarnings("resource")
            final PoolingHttpClientConnectionManager poolingmgr = new PoolingHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                            .register("https", sslSocketFactoryCopy)
                            .build(),
                    null,
                    null,
                    dnsResolver,
                    connTimeToLive,
                    connTimeToLiveTimeUnit != null ? connTimeToLiveTimeUnit : TimeUnit.MILLISECONDS);
        }
    }
    //此处省略后面的代码......
}

```

这里我们注意到如果我们没有自定义 ConnectionManager 就会使用默认的，而且会创建一个 SSL 连接工厂SSLConnectionSocketFactory，并注册到了连接池管理器中，供之后生产 SSL 连接使用。

这里在配置 SSLConnectionSocketFactory 时用到了几个关键的组件，域名验证器HostnameVerifier 以及上下文SSLContext。

其中 HostnameVerifier 用来验证服务端证书与域名是否匹配，有多种实现，DefaultHostnameVerifier采用的是默认的校验规则，替代了之前版本中的BrowserCompatHostnameVerifier与StrictHostnameVerifier。NoopHostnameVerifier替代了AllowAllHostnameVerifier，采用的是不验证域名的策略。

> 注意，这里有一些区别，BrowserCompatHostnameVerifier可以匹配多级子域名，"*.foo.com"可以匹配"a.b.foo.com"。StrictHostnameVerifier不能匹配多级子域名，只能到"a.foo.com"。
>
> 而4.4之后的HttpClient使用了新的DefaultHostnameVerifier替换了上面的两种策略，只保留了一种严格策略及StrictHostnameVerifier。因为严格策略是IE6与JDK本身的策略，非严格策略是curl与firefox的策略。即默认的HttpClient实现是不支持多级子域名匹配策略的。

## 2.如何获得SSL连接

在从连接池中获得一个连接后，如果这个连接不处于establish状态，就需要先建立连接。

源码位置：org.apache.http.impl.conn.DefaultHttpClientConnectionOperator

```java
@Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
public class DefaultHttpClientConnectionOperator implements HttpClientConnectionOperator {
    @Override
    public void connect(
            final ManagedHttpClientConnection conn,
            final HttpHost host,
            final InetSocketAddress localAddress,
            final int connectTimeout,
            final SocketConfig socketConfig,
            final HttpContext context) throws IOException {
        //之前在HttpClientBuilder中register了http与https不同的连接池实现，这里lookup获得Https的实现，即SSLConnectionSocketFactory
        final Lookup<ConnectionSocketFactory> registry = getSocketFactoryRegistry(context);
        final ConnectionSocketFactory sf = registry.lookup(host.getSchemeName());
        if (sf == null) {
            throw new UnsupportedSchemeException(host.getSchemeName() +
                    " protocol is not supported");
        }
        //如果是ip形式的地址可以直接使用，否则使用dns解析器解析得到域名对应的ip
        final InetAddress[] addresses = host.getAddress() != null ?
                new InetAddress[] { host.getAddress() } : this.dnsResolver.resolve(host.getHostName());
        final int port = this.schemePortResolver.resolve(host);
        //一个域名可能对应多个Ip,按照顺序尝试连接
        for (int i = 0; i < addresses.length; i++) {
            final InetAddress address = addresses[i];
            final boolean last = i == addresses.length - 1;
            //这里只是生成一个socket，还并没有连接
            Socket sock = sf.createSocket(context);
            //设置一些tcp层的参数
            sock.setSoTimeout(socketConfig.getSoTimeout());
            sock.setReuseAddress(socketConfig.isSoReuseAddress());
            sock.setTcpNoDelay(socketConfig.isTcpNoDelay());
            sock.setKeepAlive(socketConfig.isSoKeepAlive());
            if (socketConfig.getRcvBufSize() > 0) {
                sock.setReceiveBufferSize(socketConfig.getRcvBufSize());
            }
            if (socketConfig.getSndBufSize() > 0) {
                sock.setSendBufferSize(socketConfig.getSndBufSize());
            }

            final int linger = socketConfig.getSoLinger();
            if (linger >= 0) {
                sock.setSoLinger(true, linger);
            }
            conn.bind(sock);

            final InetSocketAddress remoteAddress = new InetSocketAddress(address, port);
            if (this.log.isDebugEnabled()) {
                this.log.debug("Connecting to " + remoteAddress);
            }
            try {
                //通过SSLConnectionSocketFactory建立连接并绑定到conn上
                sock = sf.connectSocket(
                        connectTimeout, sock, host, remoteAddress, localAddress, context);
                conn.bind(sock);
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Connection established " + conn);
                }
                return;
            } catch (final SocketTimeoutException ex) {
                if (last) {
                    throw new ConnectTimeoutException(ex, host, addresses);
                }
            } catch (final ConnectException ex) {
                if (last) {
                    final String msg = ex.getMessage();
                    throw "Connection timed out".equals(msg)
                            ? new ConnectTimeoutException(ex, host, addresses)
                            : new HttpHostConnectException(ex, host, addresses);
                }
            } catch (final NoRouteToHostException ex) {
                if (last) {
                    throw ex;
                }
            }
            if (this.log.isDebugEnabled()) {
                this.log.debug("Connect to " + remoteAddress + " timed out. " +
                        "Connection will be retried using another IP address");
            }
        }
    }
}
```

在上面的代码中，我们看到了是建立SSL连接之前的准备工作，这是通用流程，普通HTTP连接也一样。SSL连接的特殊流程体现在哪里呢？

源码位置：org.apache.http.conn.ssl.SSLConnectionSocketFactory

```java
@Contract(threading = ThreadingBehavior.SAFE)
@SuppressWarnings("deprecation")
public class SSLConnectionSocketFactory implements LayeredConnectionSocketFactory {
    
    @Override
    public Socket connectSocket(
            final int connectTimeout,
            final Socket socket,
            final HttpHost host,
            final InetSocketAddress remoteAddress,
            final InetSocketAddress localAddress,
            final HttpContext context) throws IOException {
        Args.notNull(host, "HTTP host");
        Args.notNull(remoteAddress, "Remote address");
        final Socket sock = socket != null ? socket : createSocket(context);
        if (localAddress != null) {
            sock.bind(localAddress);
        }
        try {
            if (connectTimeout > 0 && sock.getSoTimeout() == 0) {
                sock.setSoTimeout(connectTimeout);
            }
            if (this.log.isDebugEnabled()) {
                this.log.debug("Connecting socket to " + remoteAddress + " with timeout " + connectTimeout);
            }
            //建立连接
            sock.connect(remoteAddress, connectTimeout);
        } catch (final IOException ex) {
            try {
                sock.close();
            } catch (final IOException ignore) {
            }
            throw ex;
        }
        // Setup SSL layering if necessary（如果当前是SslSocket则进行SSL握手与域名校验）
        if (sock instanceof SSLSocket) {
            final SSLSocket sslsock = (SSLSocket) sock;
            this.log.debug("Starting handshake");
            sslsock.startHandshake();
            verifyHostname(sslsock, host.getHostName());
            return sock;
        }
        //如果不是SslSocket则将其包装为SslSocket
        return createLayeredSocket(sock, host.getHostName(), remoteAddress.getPort(), context);
    }

    @Override
    public Socket createLayeredSocket(
            final Socket socket,
            final String target,
            final int port,
            final HttpContext context) throws IOException {
        //将普通socket包装为SslSocket,socketfactory是根据 HttpClientBuilder中的SSLContext生成的，其中包含密钥信息
        final SSLSocket sslsock = (SSLSocket) this.socketfactory.createSocket(
                socket,
                target,
                port,
                true);
        //如果制定了SSL层协议版本与加密算法，则使用指定的，否则使用默认的
        if (supportedProtocols != null) {
            sslsock.setEnabledProtocols(supportedProtocols);
        } else {
            // If supported protocols are not explicitly set, remove all SSL protocol versions
            final String[] allProtocols = sslsock.getEnabledProtocols();
            final List<String> enabledProtocols = new ArrayList<String>(allProtocols.length);
            for (final String protocol: allProtocols) {
                if (!protocol.startsWith("SSL")) {
                    enabledProtocols.add(protocol);
                }
            }
            if (!enabledProtocols.isEmpty()) {
                sslsock.setEnabledProtocols(enabledProtocols.toArray(new String[enabledProtocols.size()]));
            }
        }
        if (supportedCipherSuites != null) {
            sslsock.setEnabledCipherSuites(supportedCipherSuites);
        } else {
            // If cipher suites are not explicitly set, remove all insecure ones
            final String[] allCipherSuites = sslsock.getEnabledCipherSuites();
            final List<String> enabledCipherSuites = new ArrayList<String>(allCipherSuites.length);
            for (final String cipherSuite : allCipherSuites) {
                if (!isWeakCipherSuite(cipherSuite)) {
                    enabledCipherSuites.add(cipherSuite);
                }
            }
            if (!enabledCipherSuites.isEmpty()) {
                sslsock.setEnabledCipherSuites(enabledCipherSuites.toArray(new String[enabledCipherSuites.size()]));
            }
        }

        if (this.log.isDebugEnabled()) {
            this.log.debug("Enabled protocols: " + Arrays.asList(sslsock.getEnabledProtocols()));
            this.log.debug("Enabled cipher suites:" + Arrays.asList(sslsock.getEnabledCipherSuites()));
        }

        prepareSocket(sslsock);
        this.log.debug("Starting handshake");
        //Ssl连接握手
        sslsock.startHandshake();
        //握手成功后校验返回的证书与域名是否一致
        verifyHostname(sslsock, target);
        return sslsock;
    }

    private void verifyHostname(final SSLSocket sslsock, final String hostname) throws IOException {
        try {
            SSLSession session = sslsock.getSession();
            if (session == null) {
                // In our experience this only happens under IBM 1.4.x when
                // spurious (unrelated) certificates show up in the server'
                // chain.  Hopefully this will unearth the real problem:
                final InputStream in = sslsock.getInputStream();
                in.available();
                // If ssl.getInputStream().available() didn't cause an
                // exception, maybe at least now the session is available?
                session = sslsock.getSession();
                if (session == null) {
                    // If it's still null, probably a startHandshake() will
                    // unearth the real problem.
                    sslsock.startHandshake();
                    session = sslsock.getSession();
                }
            }
            if (session == null) {
                throw new SSLHandshakeException("SSL session not available");
            }

            if (this.log.isDebugEnabled()) {
                this.log.debug("Secure session established");
                this.log.debug(" negotiated protocol: " + session.getProtocol());
                this.log.debug(" negotiated cipher suite: " + session.getCipherSuite());

                try {

                    final Certificate[] certs = session.getPeerCertificates();
                    final X509Certificate x509 = (X509Certificate) certs[0];
                    final X500Principal peer = x509.getSubjectX500Principal();

                    this.log.debug(" peer principal: " + peer.toString());
                    final Collection<List<?>> altNames1 = x509.getSubjectAlternativeNames();
                    if (altNames1 != null) {
                        final List<String> altNames = new ArrayList<String>();
                        for (final List<?> aC : altNames1) {
                            if (!aC.isEmpty()) {
                                altNames.add((String) aC.get(1));
                            }
                        }
                        this.log.debug(" peer alternative names: " + altNames);
                    }

                    final X500Principal issuer = x509.getIssuerX500Principal();
                    this.log.debug(" issuer principal: " + issuer.toString());
                    final Collection<List<?>> altNames2 = x509.getIssuerAlternativeNames();
                    if (altNames2 != null) {
                        final List<String> altNames = new ArrayList<String>();
                        for (final List<?> aC : altNames2) {
                            if (!aC.isEmpty()) {
                                altNames.add((String) aC.get(1));
                            }
                        }
                        this.log.debug(" issuer alternative names: " + altNames);
                    }
                } catch (final Exception ignore) {
                }
            }

            if (!this.hostnameVerifier.verify(hostname, session)) {
                final Certificate[] certs = session.getPeerCertificates();
                final X509Certificate x509 = (X509Certificate) certs[0];
                final List<SubjectName> subjectAlts = DefaultHostnameVerifier.getSubjectAltNames(x509);
                throw new SSLPeerUnverifiedException("Certificate for <" + hostname + "> doesn't match any " +
                        "of the subject alternative names: " + subjectAlts);
            }
            // verifyHostName() didn't blowup - good!
        } catch (final IOException iox) {
            // close the socket before re-throwing the exception
            try { sslsock.close(); } catch (final Exception x) { /*ignore*/ }
            throw iox;
        }
    }
}
```

可以看到，对于一个SSL通信而言。首先是建立普通socket连接，然后进行ssl握手，之后验证证书与域名一致性。

通过上面的源码分析，我们知道默认创建的 HttpClient，是会对服务器传来的证书进行校验的。

# 创建绕过 SSL 验证的 HttpClient（本 starter 未提供）

如果我们服务器的证书是非认证机构颁发的 (例如12306)或者自签名证书，那么我们是无法直接访问到服务器的，直接访问通常会抛出如下异常：javax.net.ssl.SSLHandshakeException。

网上很多解决SSLHandshakeException异常的方案是自定义 TrustManager 忽略证书校验，代码如下：

```java
public class HttpclientUtil {

    public static CloseableHttpClient getSSLClient () {
        CloseableHttpClient client = null;
        try {
            SSLContext ctx = getSSLContext();

            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(ctx, new NoopHostnameVerifier());

            // 配置请求参数
            RequestConfig reqConfig = RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.STANDARD_STRICT)
                    .setExpectContinueEnabled(Boolean.TRUE)
                    .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                    .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                    .setConnectionRequestTimeout(10000)
                    .setConnectTimeout(10000)
                    .setSocketTimeout(10000)
                    .build();

            // 配置Registry
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", socketFactory).build();

            // 配置Connection
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            // 连接池最大连接数
            connectionManager.setMaxTotal(1000);
            // 每个路由最大连接数
            connectionManager.setDefaultMaxPerRoute(20);

            client = HttpClients.custom()
                    .setConnectionManager(connectionManager)
                    .setDefaultRequestConfig(reqConfig).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return client;
    }
    
    /**
     * 创建一个不验证证书链的证书信任管理器。
     * 
     * @return SSL上下文对象
     * @throws Exception
     */
    private static SSLContext getSSLContext() throws Exception {
        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager x509TrustManager = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers () {
                return null;
            }

            @Override
            public void checkClientTrusted (X509Certificate[] cert, String oauthType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted (X509Certificate[] cert, String oauthType) throws CertificateException {
            }
        };

        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, new TrustManager[]{x509TrustManager}, null);
        return ctx;
    }
}
```

我们这里取消了校验服务端证书的流程，即绕过了证书认证环节，对于这样的处理方式虽然解决了SSLHandshakeException异常，但是却存在更大的安全隐患。

因为此种做法直接使我们的客户端信任了所有证书（包括CA机构颁发的证书和非CA机构颁发的证书以及自签名证书），因此，这样配置具有一定的危险性。

# 总结

默认创建的 HttpClient 会校验服务器的证书，此时我们可以自定义TrustManager 忽略证书校验，但是该方式存在一定的安全隐患。

1. HttpClient在build的时候，连接池管理器注册了两个SslSocketFactory，用来匹配http或者https字符串

2. https对应的socket建立原则是先建立，后验证域名与证书一致性

3. ssl层加解密由jdk自身完成，不需要httpClient进行额外操作
