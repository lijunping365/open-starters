package com.lightcode.starter.oauth.token.support.jwt;


import com.lightcode.starter.oauth.authentication.Authentication;
import com.lightcode.starter.oauth.domain.UserDetails;
import com.lightcode.starter.oauth.exception.AuthenticationException;
import com.lightcode.starter.oauth.properties.OAuthProperties;
import com.lightcode.starter.oauth.properties.token.TokenProperties;
import com.lightcode.starter.oauth.token.AccessToken;
import com.lightcode.starter.oauth.token.TokenStore;
import com.lightcode.starter.oauth.utils.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: Jwt token store，把 token 加密传给客户端
 */
@Slf4j
public class JwtTokenStore implements TokenStore {

    private final OAuthProperties oauthProperties;

    public JwtTokenStore(OAuthProperties oauthProperties) {
        this.oauthProperties = oauthProperties;
    }

    @Override
    public AccessToken generateToken(Authentication authentication) {
        final TokenProperties tokenProperties = oauthProperties.getToken();
        AccessToken token = new AccessToken();
        long now = System.currentTimeMillis();
        Date expiredDate = new Date(now + tokenProperties.getAccessTokenExpiresIn() * 1000);
        String userDetailsStr = JSON.toJSON(authentication.getUserDetails());
        Claims claims = Jwts.claims().setSubject(userDetailsStr);
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(expiredDate)
                .signWith(Keys.hmacShaKeyFor(tokenProperties.getSecretKeyBytes()), SignatureAlgorithm.HS256)
                .compact();
        token.setExpiredTime(String.valueOf(expiredDate.getTime()));
        token.setAccessToken(accessToken);
        return token;
    }

    @Override
    public Authentication readAuthentication(String accessToken) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(oauthProperties.getToken().getSecretKeyBytes()).build().parseClaimsJws(accessToken).getBody();
            final String subject = claims.getSubject();
            UserDetails userDetails = JSON.parse(subject, UserDetails.class);
            Authentication authentication = new Authentication();
            authentication.setUserDetails(userDetails);
            return authentication;
        }catch (Exception e){
            throw new AuthenticationException(e.getMessage());
        }
    }
}
