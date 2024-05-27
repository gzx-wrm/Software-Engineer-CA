package com.gzx.hotel.core.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author gzx
 * @date 2023/11/4
 * @Description
 */
public class JwtUtil {
    // 加密的密钥
    private static final String SECRET = "hotellll~";

    public static String generateToken(String userId) {
        return  Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 过期时间
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }


    public static String parseToken(String token) throws Exception {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}
