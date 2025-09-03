package top.otsuland.market.common;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * 自定义过滤器
 */
@Component
public class LoggingFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // TODO Auto-generated method stub
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
