package top.otsuland.market.common;

import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.netty.util.internal.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

import java.io.IOException;

/**
 * 自定义过滤器
 */
@Slf4j
@Component
public class LoggingFilter implements GlobalFilter {
    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        // TODO Auto-generated method stub
        HttpServletRequest req = (HttpServletRequest) exchange.getRequest();
        HttpServletResponse res = (HttpServletResponse) exchange.getResponse();

        String url =req.getRequestURI().toString();
        log.info("请求的URL: {}",url);

        if(isWhitelisted(url))
        {
            log.info("login or register, ok!");
            return chain.filter(exchange);
        }

        String jwt = req.getHeader("authorization");
        if(!StringUtils.hasLength(jwt))
        {
            log.info("not login, fail");
            String notLogin= JSON.toJSONString(Result.set(-10,"missing token"));
            res.getWriter().write(notLogin);
            return null;
        }
        try
        {
            Claims claims = JwtUtils.checkJWT(jwt);
            Double idDouble =claims.get("userId", Double.class);
            Integer id = idDouble.intValue();
            log.info("令牌合法，放行");
            req.setAttribute("id", id);
            chain.filter(exchange);
        } catch (JwtException e)
        {
            e.printStackTrace();
            log.info("解析令牌失败，返回未登录错误信息");
            String notLogin = JSON.toJSONString(Result.set(-10, "not_login_error"));
            res.getWriter().write(notLogin);
            return null;
        } catch (Exception e)
        {
            e.printStackTrace();
            log.info("服务器错误");
            String error = JSON.toJSONString(Result.set(0, "failed"));
            res.getWriter().write(error);
            return null;
        }
        return null;
    }

    private boolean isWhitelisted(String path) {
        if(path.contains("/api/users/login") |
            path.contains("/api/users/register")
        ) {
            return true;
        }
        return false;
    }

}
