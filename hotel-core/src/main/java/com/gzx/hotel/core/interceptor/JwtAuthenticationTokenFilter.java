package com.gzx.hotel.core.interceptor;

import com.gzx.hotel.base.pojo.ResponseBean;
import com.gzx.hotel.base.pojo.ResponseCode;
import com.gzx.hotel.core.pojo.LoginUser;
import com.gzx.hotel.core.utils.JwtUtil;
import com.gzx.hotel.core.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. 获取token
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            token = request.getHeader("Sec-WebSocket-Protocol");
            if (!StringUtils.hasText(token)) {
                // 放行，接下来是做token解析工作，放行给后面的处理器进行异常抛出
                filterChain.doFilter(request, response);
                return;
            }
        }
        // 2. 解析token
        String username;
        try {
            username = JwtUtil.parseToken(token.substring(7));
        } catch (Exception e) {
//            e.printStackTrace();
            filterChain.doFilter(request, response);
            return;
        }
        // 3. 从缓存中获取用户信息
        String redisKey = "token:" + username;
        LoginUser loginUser = redisCache.getCacheObject(redisKey);
        if (Objects.isNull(loginUser)) {
            throw new RemoteException("用户未登陆");
        }
        // 4. 存入 SecurityContextHolder，为了做授权使用 FilterSecurityInterceptor
        // TODO 获取权限信息封装到 Authentication 中
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 5.放行
        filterChain.doFilter(request, response);
    }

}