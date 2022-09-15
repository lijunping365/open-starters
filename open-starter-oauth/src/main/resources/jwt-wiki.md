# 简单介绍下组件中使用的 jwt 认证工具

## JWT 原理

JWT 的原理是，服务器认证以后，生成一个 JSON 对象，发回给用户，就像下面这样。

```json
{  "姓名": "张三",  "角色": "管理员",  "到期时间": "2020年9月1日0点0分"}
```

以后，用户与服务端通信的时候，都要发回这个 JSON 对象。服务器完全只靠这个对象认定用户身份。为了防止用户篡改数据，服务器在生成这个对象的时候，会加上签名(详见后文)。

服务器就不保存任何 session 数据了，也就是说，服务器变成无状态了，从而比较容易实现扩展。

## JWT 结构

它是一个很长的字符串，中间用点(.)分隔成三个部分，分别是：标头(Header)、有效载荷(Payload)和签名(Signature)。

写成一行，就是下面的样子。

```
Header.Payload.Signature
```

下面依次介绍这三个部分。

1. Header

Header 部分是一个 JSON 对象，描述 JWT 的元数据，通常是下面的样子。

```json
{  "alg": "HS256",  "typ": "JWT"}
```

上面代码中，alg属性表示签名的算法(algorithm)，默认是 HMAC SHA256(写成 HS256)；typ属性表示这个令牌(token)的类型(type)，JWT 令牌统一写为JWT。

最后，将上面的 JSON 对象使用 Base64URL 算法转成字符串。

2. Payload

Payload 部分也是一个 JSON 对象，用来存放实际需要传递的数据。JWT 规定了7个官方字段，供选用。

```
iss：发行人
exp：到期时间
sub：主题
aud：用户
nbf：在此之前不可用
iat：发布时间
jti：JWT ID用于标识该JWT
```

除了官方字段，你还可以在这个部分定义私有字段，下面就是一个例子。

```json
{
  "sub": "1234567890",
  "name": "Helen",
  "admin": true
}

```

注意，JWT 默认是不加密的，任何人都可以读到，所以不要把秘密信息放在这个部分。

最后，这个 JSON 对象也要使用 Base64URL 算法转成字符串。

3. Signature

Signature 部分是对前两部分的签名，防止数据篡改。

首先，需要指定一个密钥(secret)。这个密钥只有服务器才知道，不能泄露给用户。然后，使用 Header 里面指定的签名算法(默认是 HMAC SHA256)，按照下面的公式产生签名。

```
HMACSHA256(  base64UrlEncode(header) + "." +  base64UrlEncode(payload),  secret)
```

算出签名以后，把 Header、Payload、Signature 三个部分拼成一个字符串，每个部分之间用"点"(.)分隔，就可以返回给用户，长度貌似和你的加密算法和私钥有关系。

```
JWTString=Base64URL(Header).Base64URL(Payload).HMACSHA256(base64UrlEncode(header)+"."+base64UrlEncode(payload),secret)
```

## 签名的目的

最后一步签名的过程，实际上是对头部以及负载内容进行签名，防止内容被窜改。如果有人对头部以及负载的内容解码之后进行修改，再进行编码，最后加上之前的签名组合形成新的JWT的话，

那么服务器端会判断出新的头部和负载形成的签名和JWT附带上的签名是不一样的。如果要对新的头部和负载进行签名，在不知道服务器加密时用的密钥的话，得出来的签名也是不一样的。

## 信息暴露

在这里大家一定会问一个问题：Base64URL 是一种编码，是可逆的，那么我的信息不就被暴露了吗？

是的。所以，在JWT中，不应该在负载里面加入任何敏感的数据。我们可以传输用户的User ID。这个值实际上不是什么敏感内容，一般情况下被知道也是安全的。

但是像密码这样的内容就不能被放在JWT中了。如果将用户的密码放在了JWT中，那么怀有恶意的第三方通过 Base64URL 解码就能很快地知道你的密码了。

因此JWT适合用于向Web应用传递一些非敏感信息。JWT还经常用于设计用户认证和授权系统，甚至实现Web应用的单点登录。

## Base64URL 

Base64URL 是一种编码，也就是说，它是可以被翻译回原来的样子来的。它并不是一种加密过程。

如前所述，JWT头和有效载荷序列化的算法都用到了Base64URL。该算法和常见Base64算法类似，稍有差别。

作为令牌的JWT可以放在URL中（例如api.example/?token=xxx）。 Base64中用的三个字符

是"+"，"/"和"="，由于在URL中有特殊含义，因此Base64URL中对他们做了替换："="去掉，"+"用"-"替换，"/"用"_"替换，这就是Base64URL算法。

因此不要构建隐私信息字段，存放保密信息，以防止信息泄露。

## JWT 认证的优势

1. 有效使用 JWT，可以降低服务器查询数据库的次数。

2. 不需要在服务端保存会话信息，也就是说不依赖于cookie和session，所以没有了传统session认证的弊端，特别适用于分布式微服务

3. JWT 可能包含了用户信息，一旦泄露，任何人都可以获得该令牌的所有权限。为了减少盗用，JWT 的有效期应该设置得比较短。

4. 单点登录友好：使用Session进行身份认证的话，由于cookie无法跨域，难以实现单点登录。但是，使用token进行认证的话， token可以被保存在客户端的任意位置的内存中，不一定是cookie，所以不依赖cookie，不会存在这些问题

5. 适合移动端应用：使用Session进行身份认证的话，需要保存一份信息在服务器端，而且这种方式会依赖到Cookie（需要 Cookie 保存 SessionId），所以不适合移动端

## JWT的种类

其实JWT(JSON Web Token)指的是一种规范，这种规范允许我们使用JWT在两个组织之间传递安全可靠的信息，JWT的具体实现可以分为以下几种：

- nonsecure JWT：未经过签名，不安全的 JWT

- JWS：经过签名的 JWT

- JWE：payload 部分经过加密的 JWT

1. nonsecure JWT

未经过签名，不安全的JWT。其header部分没有指定签名算法

```json
{
  "alg": "none",
  "typ": "JWT"
}

```

并且也没有 Signature 部分

2. JWS

JWS，也就是JWT Signature，其结构就是在之前nonsecure JWT的基础上，在头部声明签名算法，并在最后添加上签名。

创建签名，是保证jwt不能被他人随意篡改。我们通常使用的JWT一般都是JWS

为了完成签名，除了用到header信息和payload信息外，还需要算法的密钥，也就是secretKey。加密的算法一般有2类：

- 对称加密：secretKey指加密密钥，可以生成签名与验签

- 非对称加密：secretKey指私钥，只用来生成签名，不能用来验签(验签用的是公钥)

JWT的密钥或者密钥对，一般统一称为JSON Web Key，也就是JWK

## JWT 的签名算法

到目前为止，jwt的签名算法有三种：

- HMAC【哈希消息验证码(对称)】：HS256/HS384/HS512

- RSASSA【RSA签名算法(非对称)】（RS256/RS384/RS512）

- ECDSA【椭圆曲线数据签名算法(非对称)】（ES256/ES384/ES512）

JWT中最常用的两种算法为HMAC和RSA

HMAC(HS256):是一种对称加密算法，使用秘密密钥对每条消息进行签名和验证

RSA(RS256)：是一种非对称加密算法，使用私钥加密明文，公钥解密密文。

## jwt 使用案例

### 依赖 pom 如下

注意 jjwt在0.10版本以后发生了较大变化，pom依赖要引入多个

```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>${jjwt.version}</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>${jjwt.version}</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>${jjwt.version}</version>
</dependency>
```

### jwt 新版要求

1. 标准规范中对各种加密算法的secretKey的长度有如下要求：

- HS256：要求至少 256 bits (32 bytes)

- HS384：要求至少384 bits (48 bytes)

- HS512：要求至少512 bits (64 bytes)

- RS256 and PS256：至少2048 bits

- RS384 and PS384：至少3072 bits

- RS512 and PS512：至少4096 bits

- ES256：至少256 bits (32 bytes)

- ES384：至少384 bits (48 bytes)

- ES512：至少512 bits (64 bytes)

在jjwt0.10版本之前，没有强制要求，secretKey长度不满足要求时也可以签名成功。但是0.10版本后强制要求secretKey满足规范中的长度要求，否则生成jws时会抛出异常

2. 新版本的jjwt中，之前的签名和验签方法都是传入密钥的字符串，已经过时。最新的方法需要传入Key对象

### 对称签名

```java
public class JwtUtils {
    // token时效：24小时
    public static final long EXPIRE = 1000 * 60 * 60 * 24;
    // 签名哈希的密钥，对于不同的加密算法来说含义不同
    public static final String APP_SECRET = "ukc8BDbRigUDaY6pZFfWus2jZWLPHOsdadasdasfdssfeweee";

    /**
     * 根据用户id和昵称生成token
     * @param id  用户id
     * @param nickname 用户昵称
     * @return JWT规则生成的token
     */
    public static String getJwtToken(String id, String nickname){
        String JwtToken = Jwts.builder()
                .setSubject("baobao-user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .claim("id", id)
                .claim("nickname", nickname)
                // 传入Key对象
                .signWith(Keys.hmacShaKeyFor(APP_SECRET.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
        return JwtToken;
    }

    /**
     * 判断token是否存在与有效
     * @param jwtToken token字符串
     * @return 如果token有效返回true，否则返回false
     */
    public static Jws<Claims> decode(String jwtToken) {
        // 传入Key对象
        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(APP_SECRET.getBytes(StandardCharsets.UTF_8))).build().parseClaimsJws(jwtToken);
        return claimsJws;
    }
}

```

生成 jwt token 如下：

```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsaWp1bnBpbmciLCJleHAiOjE2NjU3NTc1OTd9.b1Eqsp2y703QoDTGMYOYKVr5bQ5mEr_RvyjnmuirIq0
\__________________/\_______________________________________________/\__________________________________________/
  Base64URL(Header)                Base64URL(Payload)                   HMACSHA256(base64UrlEncode(header)+"."+base64UrlEncode(payload),secret)
```

### 非对称签名

生成jwt串的时候需要指定私钥，解析jwt串的时候需要指定公钥

```java

public class JwtUtils {
    private static final String RSA_PRIVATE_KEY = "...";
    private static final String RSA_PUBLIC_KEY = "...";

    /**
     * 根据用户id和昵称生成token
     * @param id  用户id
     * @param nickname 用户昵称
     * @return JWT规则生成的token
     */
    public static String getJwtTokenRsa(String id, String nickname){
        // 利用hutool创建RSA
        RSA rsa = new RSA(RSA_PRIVATE_KEY, null);
        RSAPrivateKey privateKey = (RSAPrivateKey) rsa.getPrivateKey();
        String JwtToken = Jwts.builder()
                .setSubject("baobao-user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .claim("id", id)
                .claim("nickname", nickname)
                // 签名指定私钥
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
        return JwtToken;
    }

    /**
     * 判断token是否存在与有效
     * @param jwtToken token字符串
     * @return 如果token有效返回true，否则返回false
     */
    public static Jws<Claims> decodeRsa(String jwtToken) {
        RSA rsa = new RSA(null, RSA_PUBLIC_KEY);
        RSAPublicKey publicKey = (RSAPublicKey) rsa.getPublicKey();
        // 验签指定公钥
        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(jwtToken);
        return claimsJws;
    }
}

```