package top.otsuland.user.common;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import top.otsuland.user.entity.User;

/**
 * 令牌工具类
 */
public class JwtUtils {

    private static final Long EXPIRE = 60000 * 60 * 24 * 7L;
    private static final String SECRET = "Grimmoire.of.Alices.spell";
    private static final SecretKey KEY = Jwts.SIG.HS256.key()
                .random(new SecureRandom(SECRET.getBytes()))
                .build();
    private static final String SUBJECT = "AliceInWonderland";

    /**
     * 生成令牌
     * ok
     */
    public static String geneJWT(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        String token = Jwts.builder().claims()
                .add(claims)      // 添加内容
                .subject(SUBJECT) // 声明主题
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRE))
                .and()            // 返回 JwtBuilder 配置
                .signWith(KEY)
                .compact();
        return token;
    }

    /**
     * 解析令牌
     */
    public static Claims checkJWT(String token) {
        final Claims claims = Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parse(token).accept(Jws.CLAIMS) // 解析 JWS
                .getPayload(); // JWT 有效载荷
        return claims;
    }
}