package com.lightcode.starter.oauth.test;

import com.lightcode.starter.oauth.authentication.Authentication;
import com.lightcode.starter.oauth.domain.UserDetails;
import com.lightcode.starter.oauth.exception.AuthenticationException;
import com.lightcode.starter.oauth.properties.token.TokenProperties;
import com.lightcode.starter.oauth.token.AccessToken;
import com.lightcode.starter.oauth.utils.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Date;


/**
 * @author lijunping on 2022/3/31
 */
public class JwtTest {

    private static final long EXPIRE = 24 * 3600 * 1000;

    public static AccessToken generateToken(Authentication authentication) {
        TokenProperties tokenProperties = new TokenProperties();
        tokenProperties.setSecretKey("lijunpinglijunpinglijunpingpengguifangpengguifang");


        AccessToken token = new AccessToken();
        long now = System.currentTimeMillis();
        Date expiredDate = new Date(now + EXPIRE);
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

    public static Authentication readAuthentication(String accessToken) {
        TokenProperties tokenProperties = new TokenProperties();
        tokenProperties.setSecretKey("lijunpinglijunpinglijunpingpengguifangpengguifang");

        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(tokenProperties.getSecretKeyBytes()).build().parseClaimsJws(accessToken).getBody();
            final String subject = claims.getSubject();
            UserDetails userDetails = JSON.parse(subject, UserDetails.class);
            Authentication authentication = new Authentication();
            authentication.setUserDetails(userDetails);
            return authentication;
        }catch (Exception e){
            throw new AuthenticationException(e.getMessage());
        }
    }

//    public static void main(String[] args) {
//        Authentication authentication = new Authentication();
//        UserDetails userDetails = new UserDetails();
//        userDetails.setId(1L);
//        authentication.setUserDetails(userDetails);
//        final AccessToken accessToken = generateToken(authentication);
//        System.out.println(JSON.toJSON(accessToken));
//    }

    public static void main(String[] args) {
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XHJcbiAgXCJpZFwiIDogMSxcclxuICBcInVzZXJuYW1lXCIgOiBudWxsLFxyXG4gIFwicGFzc3dvcmRcIiA6IG51bGwsXHJcbiAgXCJtb2JpbGVcIiA6IG51bGwsXHJcbiAgXCJhY2NvdW50TG9ja2VkXCIgOiBudWxsXHJcbn0iLCJleHAiOjE2NDg3ODMxNzB9.7gf1iuLNAcQcLKjuJgHgb0JOwuSj079xXW4uOO6cDcs";
        final Authentication authentication = readAuthentication(accessToken);
        System.out.println(JSON.toJSON(authentication));
    }

}
