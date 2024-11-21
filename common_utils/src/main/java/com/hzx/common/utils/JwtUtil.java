package com.hzx.common.utils;

import com.hzx.common.constant.JwtClaimsConstant;
import com.hzx.common.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;

@Component
public class JwtUtil {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 生成jwt
     * 使用Hs256算法, 私匙使用固定秘钥
     * @param userId 用户id
     * @return  jwt令牌
     */
    public String createJWT_user(Integer userId) {
        // 指定签名的时候使用的签名算法，也就是header那部分
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 生成JWT的过期时间
        long expMillis = System.currentTimeMillis() + jwtProperties.getUserTtl();
        Date expData = new Date(expMillis);

        //为用户生成jwt令牌
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, userId);

        // 设置jwt的body
        JwtBuilder builder = Jwts.builder()
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, jwtProperties.getUserSecretKey().getBytes(StandardCharsets.UTF_8))
                // 设置过期时间
                .setExpiration(expData)
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims);

        return builder.compact();
    }

    /**
     * Token解密
     * @param secretKey jwt秘钥 此秘钥一定要保留好在服务端, 不能暴露出去, 否则sign就可以被伪造, 如果对接多个客户端建议改造成多个
     * @param token     加密后的token
     * @return
     */
    public Claims parseJWT(String secretKey, String token) {
        // 得到DefaultJwtParser
        Claims claims = Jwts.parser()
                // 设置签名的秘钥
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                // 设置需要解析的jwt
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

}
