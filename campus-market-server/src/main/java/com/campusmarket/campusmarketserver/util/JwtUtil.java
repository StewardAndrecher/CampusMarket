
package com.campusmarket.campusmarketserver.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtil {

    // 教学简化：密钥写死在代码里。生产环境应放配置文件或环境变量！
    private static final String SECRET = "campus-market-jwt-secret-key-0123456789-0123456789";
    private static final long EXPIRE_MILLIS = 7 * 24 * 60 * 60 * 1000L; // 7 天

    private static SecretKey key() {
        // HS256 要求密钥至少 32 字节
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    // 用 userId 生成 token
    public static String createToken(Long userId) {
        Date now = new Date();
        return Jwts.builder()
                .subject(String.valueOf(userId))          // 主体：用户 ID
                .issuedAt(now)                            // 签发时间
                .expiration(new Date(now.getTime() + EXPIRE_MILLIS)) // 过期时间
                .signWith(key())                          // 签名
                .compact();
    }

    // 解析 token，取出 userId（过期或伪造会抛异常）
    public static Long parseUserId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Long.valueOf(claims.getSubject());
    }
}