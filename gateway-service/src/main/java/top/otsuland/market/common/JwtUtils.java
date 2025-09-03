package top.otsuland.market.common;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * 令牌工具类
 */
public class JwtUtils {

    @Value("${jwt.secret}")
    static private String SECRET;

    @Value("${jwt.expiration}")
    static private Long EXPIRATION;

    private static final String SUBJECT = "AliceInWonderland";

    private static SecretKey getStringKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // 生成令牌
    static public String geneJWT(Integer uid) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", uid);
        String token = Jwts.builder().claims()
                .add(claims)      // 添加内容
                .subject(SUBJECT) // 声明主题
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .and()            // 返回 JwtBuilder 配置
                .signWith(getStringKey())
                .compact();
        return token;
    }

    // 解析令牌
    static public Claims checkJWT(String token) {
        final Claims claims = Jwts.parser()
                .verifyWith(getStringKey())
                .build()
                .parse(token).accept(Jws.CLAIMS) // 解析 JWS
                .getPayload(); // JWT 有效载荷
        return claims;
    }
}