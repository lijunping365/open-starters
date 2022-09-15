# 简单介绍下组件中使用的密码加密工具

依赖 pom 如下：

```xml
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-crypto</artifactId>
</dependency>
```

密码加密接口 PasswordEncoder，默认注入的实现类是 BCryptPasswordEncoder

```java
public interface PasswordEncoder {
   String encode(CharSequence rawPassword);

   boolean matches(CharSequence rawPassword, String encodedPassword);

   default boolean upgradeEncoding(String encodedPassword) {
      return false;
   }
}
```

这个接口有三个方法：

- encode 方法接受的参数是原始密码字符串，返回值是经过加密之后的 hash 值，hash 值是不能被逆向解密的。这个方法通常在为系统添加用户，或者用户注册的时候使用。

- matches 方法是用来校验用户输入密码 rawPassword，和加密后的 hash 值 encodedPassword 是否匹配。如果能够匹配返回 true，表示用户输入的密码 rawPassword 是正确的，反之返回 false。也就是说虽然这个 hash 值不能被逆向解密，但是可以判断是否和原始密码匹配。这个方法通常在用户登录的时候进行用户输入密码的正确性校验。

- upgradeEncoding 设计的用意是，判断当前的密码是否需要升级。也就是是否需要重新加密？需要的话返回 true，不需要的话返回 false。默认实现是返回 false。

## 哈希（Hash）与加密（Encrypt）

- 哈希（Hash）是将目标文本转换成**具有相同长度**的、**不可逆**的杂凑字符串（或叫做消息摘要）

- 加密（Encrypt）是将目标文本转换成**具有不同长度**的、**可逆**的密文。

### 两者区别和联系：

哈希算法往往被设计成生成具有相同长度的文本，而加密算法生成的文本长度与明文本身的长度有关。

哈希算法是不可逆的，而加密算法是可逆的。

HASH 算法是一种消息摘要算法，不是一种加密算法，但由于其单向运算，具有一定的不可逆性，成为加密算法中的一个构成部分。

### Hash 典型案例

1. MD5消息摘要算法

MD5消息摘要算法

一种被广泛使用的密码散列函数，可以产生出一个128位（16字节）的散列值（hash value），用于确保信息传输完整一致。

MD5典型应用

1）MD5的典型应用是对一段信息（Message）产生信息摘要（Message-Digest），以防止被篡改。

2）MD5的典型应用是对一段Message(字节串)产生fingerprint(指纹），以防止被“篡改”。

3）MD5还广泛用于操作系统的登陆认证上，如Unix、各类BSD系统登录密码、数字签名等诸多方面。

注意： 使用md5的方式对文件进行加密，以获取md5值，可以知道该文件的内容是否被修改过， 因为修改过文件内容，再次对该文件进行加密的话，获取的md5值将发生变化。

md5加盐加密: md5(md5(str) + md5(salt))

加盐的目的就是防止相同的字符串加密出来的结果相同，例如，多个人的密码可能相同为了防止加密结果相同所以加盐

2. Bcrypt

BCrypt 是由 Niels Provos 和 David Mazières 设计的密码哈希函数，他是基于 Blowfish 密码而来的，并于1999年在 USENIX 上提出。

除了加盐来抵御 rainbow table 攻击之外，bcrypt 的一个非常重要的特征就是自适应性，可以保证加密的速度在一个特定的范围内，即使计算机的运算能力非常高，可以通过增加迭代次数的方式，使得加密速度变慢，从而可以抵御暴力搜索攻击。

Bcrypt 可以简单理解为它内部自己实现了随机加盐处理。使用 Bcrypt，每次加密后的密文是不一样的。

对一个密码，Bcrypt 每次生成的 hash 都不一样，那么它是如何进行校验的？

1）虽然对同一个密码，每次生成的 hash 不一样，但是 hash 中包含了 salt（hash 产生过程：先随机生成 salt，salt 跟 password 进行 hash）；

2）在下次校验时，从 hash 中取出 salt，salt 跟 password 进行 hash；得到的结果跟保存在 DB 中的 hash 进行比对。

生成的加密字符串格式如下：

```
$2a$[cost]$[22 character salt][31 character hash]
```

比如：原始密码 123456，加密后字符串为

```
$2a$10$3oNlO/vvXV3FPsmimv0x3ePTcwpe/E1xl86TDC0iLKwukWkJoRIyK
\__/\/ \____________________/\_____________________________/
 Alg Cost      Salt                        Hash

```

上面例子中，$2a$ 表示的hash算法的唯一标志。这里表示的是Bcrypt算法。

10 表示的是代价因子，这里是2的10次方，也就是1024轮。

3oNlO/vvXV3FPsmimv0x3e 是16个字节（128bits）的salt经过base64编码得到的22长度的字符。

最后的PTcwpe/E1xl86TDC0iLKwukWkJoRIyK是24个字节（192bits）的hash，经过bash64的编码得到的31长度的字符。

对于同一个原始密码，每次加密之后的hash密码都是不一样的，这正是BCryptPasswordEncoder的强大之处，它不仅不能被破解，想通过常用密码对照表进行大海捞针你都无从下手

BCrypt 产生随机盐（盐的作用就是每次做出来的菜味道都不一样）。这一点很重要，因为这意味着每次encode将产生不同的结果。

### 加密典型案例

1. 对称加密 AES

高级加密标准（英语：Advanced Encryption Standard，缩写：AES），在密码学中又称Rijndael加密法，是美国联邦政府采用的一种区块加密标准。

这个标准用来替代原先的DES，已经被多方分析且广为全世界所使用。经过五年的甄选流程，高级加密标准由美国国家标准与技术研究院（NIST）于2001年11月26日发布于FIPS PUB 197，并在2002年5月26日成为有效的标准。

2006年，高级加密标准已然成为对称密钥加密中最流行的算法之一。

AES加密原理

采用对称分组密码体制，密钥的长度最少支持为128、192、256位；加密分组长度128位，如果数据块及密钥长度不足时，会补齐进行加密。

2. 非对称加密 RSA

可使用PKCS#1、PKCS#8格式的公私钥，对数据进行加密解密。

RSA 算法广泛应用与加密与认证两个领域

1）加密（保证数据安全性）

使用公钥加密，需使用私钥解密。

这种广泛应用在保证数据的安全性的方面，用户将自己的公钥广播出去，所有人给该用户发数据时使用该公钥加密，但是只有该用户可以使用自己的私钥解密，保证了数据的安全性。

2）认证（用于身份判断）

使用私钥签名，需使用公钥验证签名。

用户同样将自己的公钥广播出去，给别人发送数据时，使用私钥加密，在这里，我们更乐意称它为签名，然后别人用公钥验证签名，如果解密成功，则可以判断对方的身份。

注意下面方法中的加密解密方法

如果是使用公钥加密则需要私钥进行解密

如果是使用私钥加密则需要公钥进行解密

# 简单介绍下组件中使用的 jwt 认证工具

依赖 pom 如下：

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

生成 jwt token

```java
public class JwtTest {

    public static void main(String[] args) {
        generate("lijunping");
    }

    public static void generate(String json) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,1);
        Date time = calendar.getTime();

        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        Claims claims = Jwts.claims().setSubject(json);
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(time)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        System.out.println(accessToken);

    }
}
```

生成 jwt token 如下：

```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsaWp1bnBpbmciLCJleHAiOjE2NjU3NTc1OTd9.b1Eqsp2y703QoDTGMYOYKVr5bQ5mEr_RvyjnmuirIq0
\__________________/\_______________________________________________/\__________________________________________/
     Base64(Header)            Base64(Payload)                        HMACSHA256(base64UrlEncode(header)+"."+base64UrlEncode(payload),secret)
```

JWT结构

JWT由3部分组成：标头(Header)、有效载荷(Payload)和签名(Signature)。

1.Header
  
> JWT头是一个描述JWT元数据的JSON对象，alg属性表示签名使用的算法，默认为HMAC SHA256（写为HS256）；typ属性表示令牌的类型，JWT令牌统一写为JWT。最后，使用Base64 URL算法将上述JSON对象转换为字符串保存

2.Payload

> 有效载荷部分，是JWT的主体内容部分，也是一个JSON对象，包含需要传递的数据。 JWT指定七个默认字段供选择

```
iss：发行人
exp：到期时间
sub：主题
aud：用户
nbf：在此之前不可用
iat：发布时间
jti：JWT ID用于标识该JWT
```

这些预定义的字段并不要求强制使用。除以上默认字段外，我们还可以自定义私有字段，一般会把包含用户信息的数据放到payload中，如下例：

```json
{
  "sub": "1234567890",
  "name": "Helen",
  "admin": true
}

```

3. Signature

前面两部分都是使用 Base64 进行编码的，即前端可以解开知道里面的信息。Signature 需要使用编码后的 header 和 payload 以及我们提供的一个密钥，然后使用 header 中指定的签名算法（HS256）进行签名。签名的作用是保证 JWT 没有被篡改过。

三个部分通过.连接在一起就是我们的 JWT 了，它可能长这个样子，长度貌似和你的加密算法和私钥有关系。

签名的目的

最后一步签名的过程，实际上是对头部以及负载内容进行签名，防止内容被窜改。如果有人对头部以及负载的内容解码之后进行修改，再进行编码，最后加上之前的签名组合形成新的JWT的话，那么服务器端会判断出新的头部和负载形成的签名和JWT附带上的签名是不一样的。如果要对新的头部和负载进行签名，在不知道服务器加密时用的密钥的话，得出来的签名也是不一样的。


信息暴露

在这里大家一定会问一个问题：Base64是一种编码，是可逆的，那么我的信息不就被暴露了吗？

是的。所以，在JWT中，不应该在负载里面加入任何敏感的数据。在上面的例子中，我们传输的是用户的User ID。这个值实际上不是什么敏感内容，一般情况下被知道也是安全的。但是像密码这样的内容就不能被放在JWT中了。如果将用户的密码放在了JWT中，那么怀有恶意的第三方通过Base64解码就能很快地知道你的密码了。

因此JWT适合用于向Web应用传递一些非敏感信息。JWT还经常用于设计用户认证和授权系统，甚至实现Web应用的单点登录。

当用户与服务器通信时，客户在请求中发回JSON对象。服务器仅依赖于这个JSON对象来标识用户。为了防止用户篡改数据，服务器将在生成对象时添加签名。服务器不保存任何会话数据，即服务器变为无状态，使其更容易扩展。

在传输的时候，会将JWT的3部分分别进行Base64编码后用.进行连接形成最终传输的字符串

JWTString=Base64(Header).Base64(Payload).HMACSHA256(base64UrlEncode(header)+"."+base64UrlEncode(payload),secret)

Base64是一种编码，也就是说，它是可以被翻译回原来的样子来的。它并不是一种加密过程。

> 如前所述，JWT头和有效载荷序列化的算法都用到了Base64URL。该算法和常见Base64算法类似，稍有差别。
  作为令牌的JWT可以放在URL中（例如api.example/?token=xxx）。 Base64中用的三个字符
  是"+"，"/"和"="，由于在URL中有特殊含义，因此Base64URL中对他们做了替换："="去掉，"+"用"-"替换，"/"用"_"替换，这就是Base64URL算法。

因此不要构建隐私信息字段，存放保密信息，以防止信息泄露。

校验 token

```java

public class JwtTokenService implements TokenService {

    private final SecurityProperties securityProperties;

    public JwtTokenService(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public Authentication readAuthentication(String accessToken) {
        String subject;
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(securityProperties.getSecretKeyBytes()).build().parseClaimsJws(accessToken).getBody();
            subject = claims.getSubject();
        }catch (Exception e){
            throw new SecurityException(SecurityExceptionEnum.UNAUTHORIZED);
        }
        UserDetails userDetails = JSON.parse(subject, UserDetails.class);
        Authentication authentication = new Authentication();
        authentication.setUserDetails(userDetails);
        return authentication;
    }
}
```