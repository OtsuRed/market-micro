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
