package com.wzh.liquid.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author WZH
 * @Description 生成和校验的工具类
 * @date 2021/12/25 - 20:58
 **/
@Slf4j
@Data
@Configuration
@Component
@ConfigurationProperties(prefix = "wzh.jwt")
public class JwtUtils {
    private String secret;
    private long expire;
    private String header;

    /**
     * 生成token
     * @param userId
     * @return
     */
    public String generateToken(long userId){
        Date NowDate=new Date();
        //过期时间
        Date expireDate = new Date(NowDate.getTime() + expire * 1000);
        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setSubject(userId+"")
                .setIssuedAt(NowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512,secret)
                //调用compact()方法进行压缩和签名，生成最终的jws
                .compact();

    }
    //解析Token
    public  Claims getClaimByToken(String token){
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            log.debug("calidate is token error ",e);
            return null;
        }


    }

    /**
     * token 是否过期
     * @param expiration
     * @return true:过期
     */
    public boolean isTokenExpire(Date expiration) {
        return expiration.before(new Date());
    }

}
