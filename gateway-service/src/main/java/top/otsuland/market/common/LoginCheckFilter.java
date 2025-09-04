// package top.otsuland.market.common;

// import java.io.IOException;
// import java.util.List;

// import org.springframework.cloud.gateway.filter.GatewayFilterChain;
// import org.springframework.cloud.gateway.filter.GlobalFilter;
// import org.springframework.http.server.reactive.ServerHttpRequest;
// import org.springframework.stereotype.Component;
// import org.springframework.util.StringUtils;
// import org.springframework.web.server.ServerWebExchange;

// import com.alibaba.fastjson2.JSON;

// import io.jsonwebtoken.Claims;
// import jakarta.servlet.Filter;
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.ServletRequest;
// import jakarta.servlet.ServletResponse;
// import jakarta.servlet.annotation.WebFilter;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import lombok.extern.slf4j.Slf4j;
// import reactor.core.publisher.Mono;

// /**
//  * 拦截器
//  * ok
//  */
// @Slf4j
// @Component
// @WebFilter(urlPatterns = "/**")
// // @Profile("prod")
// public class LoginCheckFilter implements GlobalFilter{

//     private final JwtUtils jwtUtils;
//     private final PathMatcherUtil pathMatcherUtil;

//     public LoginCheckFilter(JwtUtils jwtUtils, PathMatcherUtil pathMatcherUtil) {
//         this.jwtUtils = jwtUtils;
//         this.pathMatcherUtil = pathMatcherUtil;
//     }

//     private String extractToken(ServerHttpRequest request) {
//         List<String> authHeaders = request.getHeaders().get("Authorization");
//         if(authHeaders != null && !authHeaders.isEmpty()) {
//             String bearerToken = authHeaders.get(0);
//             if(bearerToken.startsWith("Bearer ")) {
//                 return bearerToken.substring(7);
//             }
//         }
//         return null;
//     }


//     @Override
//     public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//             throws IOException, ServletException {
//         HttpServletRequest req = (HttpServletRequest) request;
//         HttpServletResponse resp = (HttpServletResponse) response;

//         String url = req.getRequestURL().toString();
//         log.info("请求的 URL: {}", url);
//         // 判断是否是登录或注册请求
//         if(url.contains("/api/users/login") || url.contains("/api/users/register")) {
//             log.info("login or register, ok!");
// 			chain.doFilter(request, response);
//             return;
//         }

//         // 判断令牌是否存在
//         String jwt = req.getHeader("authorization");
//         if(!StringUtils.hasLength(jwt)) {
//             log.info("missing token");
// 			String result = JSON.toJSONString(Result.set(-10, "missing token"));
//             resp.getWriter().write(result);
//             return;
//         }

//         // 解析 token
//         try {
//             JwtUtils ju = new JwtUtils();
//             Claims claims = ju.checkJWT(jwt);
//             // jjwt 会将 Integer 存储为 Double，因为 json 本身没有整型和浮点数的区分，所以要注意 JWT 库的 JSON 解析行为！
//             Double idDouble = claims.get("userId", Double.class);
//             Integer id = idDouble.intValue();
//             log.info("valid token, success");
//             // 将用户 id 存入 request attribute
//             request.
//             request.setAttribute("id", id); // TODO
//         } catch (Exception e) {
//             log.info("invalid token, failed");
//             String result = JSON.toJSONString(Result.set(-10, "invalid token, failed"));
//             resp.getWriter().write(result);
//             return;
//         }
//         chain.doFilter(request, response);
//     }
// }
package top.otsuland.market.common;

import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Gateway 登录校验过滤器（GlobalFilter：全局生效，拦截所有请求）
 */
@Slf4j
@Component // 注入 Spring 容器，无需 @WebFilter（Gateway 不支持 Servlet 注解）
public class LoginCheckFilter implements GlobalFilter, Ordered {

    // 注入你已有的 JwtUtils 和 PathMatcherUtil（路径匹配工具）
    private final JwtUtils jwtUtils;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // 构造器注入（避免使用 @Autowired，更符合 Spring 最佳实践）
    public LoginCheckFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    /**
     * 核心过滤逻辑（响应式模型，返回 Mono<Void>）
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 获取请求和响应对象（Gateway 专用，非 Servlet 的 HttpServletRequest）
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String requestUrl = request.getPath().value(); // 获取请求路径（如 /api/users/login）
        log.info("拦截到请求：{}", requestUrl);

        // 2. 白名单过滤：登录/注册接口直接放行（也可通过 PathMatcherUtil 批量匹配）
        List<String> whiteList = List.of(
                "/api/users/login",  // 登录接口
                "/api/users/register", // 注册接口
                "/api/nacos/**" // Nacos 健康检查等接口（按需添加）
        );
        // 方式1：直接判断路径是否在白名单
        boolean isWhiteList = false;
        for (String whitePath : whiteList) {
            if (pathMatcher.match(whitePath, requestUrl)) {
                isWhiteList = true;
                break;
            }
        }
        if (isWhiteList) {
            log.info("请求在白名单内，直接放行：{}", requestUrl);
            return chain.filter(exchange);
        }

        // 3. 非白名单请求：提取 Token（从 Authorization 头）
        String token = extractToken(request);
        if (!StringUtils.hasLength(token)) {
            log.info("请求缺失 Token：{}", requestUrl);
            return buildErrorResponse(exchange, HttpStatus.UNAUTHORIZED, -10, "missing token");
        }

        // 4. 校验 Token 有效性（调用你已有的 JwtUtils.checkJWT 方法）
        Claims claims;
        try {
            claims = jwtUtils.checkJWT(token); // 你的 JwtUtils 已实现的校验方法
        } catch (Exception e) {
            log.error("Token 无效或已过期：{}，错误：{}", token, e.getMessage());
            return buildErrorResponse(exchange, HttpStatus.UNAUTHORIZED, -10, "invalid token, failed");
        }

        // 5. Token 校验通过：解析用户信息，传递给下游服务
        // （注意：JJWT 可能将 Integer 转为 Double，需强制转换，与你原始逻辑一致）
        Double userIdDouble = claims.get("userId", Double.class);
        if (userIdDouble == null) {
            log.error("Token 中未包含 userId：{}", token);
            return buildErrorResponse(exchange, HttpStatus.UNAUTHORIZED, -10, "token missing userId");
        }
        Integer userId = userIdDouble.intValue();
        log.info("Token 校验通过，用户 ID：{}", userId);

        // 6. 将用户 ID 存入请求属性（下游服务通过 @RequestAttribute("userId") 获取）
        // 注意：Gateway 需通过 mutate() 重建请求，才能添加属性
        exchange.getAttributes().put("id", userId);

        // 替换 exchange 中的请求为新构建的请求

        // 7. 放行到下一个过滤器/目标服务
        return chain.filter(exchange);
    }

    /**
     * 从请求头 Authorization 中提取 Token（格式：Bearer <token>）
     */
    private String extractToken(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        List<String> authHeaders = headers.get("Authorization"); // 注意：header 名不区分大小写，但取值时建议统一
        if (authHeaders != null && !authHeaders.isEmpty()) {
            String bearerToken = authHeaders.get(0);
            if (bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7).trim(); // 截取 "Bearer " 后面的 Token（去除空格）
            }
        }
        return null;
    }

    /**
     * 构建错误响应（返回 JSON 格式，适配前后端分离）
     * @param exchange 交换对象
     * @param status HTTP 状态码（401 未授权，403 禁止访问）
     * @param code 业务错误码
     * @param msg 错误信息
     */
    private Mono<Void> buildErrorResponse(ServerWebExchange exchange, HttpStatus status, int code, String msg) {
        ServerHttpResponse response = exchange.getResponse();
        // 1. 设置响应状态码和 Content-Type
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // 2. 构建错误响应体（与你原始逻辑的 Result 格式一致）
        Result errorResult = Result.set(code, msg);
        String jsonResult = JSON.toJSONString(errorResult);

        // 3. 将 JSON 转为 DataBuffer（Gateway 响应式输出）
        DataBuffer buffer = response.bufferFactory().wrap(
                jsonResult.getBytes(StandardCharsets.UTF_8)
        );

        // 4. 写入响应并结束（响应式结束信号）
        return response.writeWith(Mono.just(buffer));
    }

    /**
     * 设置过滤器优先级：值越小，优先级越高（确保登录校验在其他过滤器前执行）
     */
    @Override
    public int getOrder() {
        return -100; // 优先级高于默认过滤器（建议设置为负数，确保先执行登录校验）
    }
}