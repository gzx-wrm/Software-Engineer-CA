package com.gzx.hotel.core.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gzx.hotel.base.service.ICrudService;
import com.gzx.hotel.base.service.impl.ICrudServiceImpl;
import com.gzx.hotel.core.dao.UserDao;
import com.gzx.hotel.core.po.User;
import com.gzx.hotel.core.pojo.LoginUser;
import com.gzx.hotel.core.service.UserService;
import com.gzx.hotel.core.utils.JwtUtil;
import com.gzx.hotel.core.utils.RedisCache;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ICrudServiceImpl<User> implements UserService, UserDetailsService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisCache redisCache;

    @Override
    public LoginUser login(String username, String password) throws Exception{
        // 1. 进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        if (Objects.isNull(authenticate)) {
            // 2. 如果认证没通过，给出对应的提示
            throw new RuntimeException("账号或密码错误！");
        }
        // 擦除密码
        ((LoginUser) authenticate.getPrincipal()).erasePassword();
        return ((LoginUser) authenticate.getPrincipal());
//        return generateToken(((LoginUser) authenticate.getPrincipal()));
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
        User user = ((UserDao) baseMapper).getUserWithRoles(s);

        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误！");
        }

        // 返回用户信息，封装成UserDetails对象
        return new LoginUser(user.getUsername(), user.getPassword(), user.getLocked(), AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_" + user.getRoleName().toUpperCase()).stream().map((a) -> (SimpleGrantedAuthority) a).collect(Collectors.toSet()));
    }

    public String generateToken(LoginUser loginUser) {
        // 如果认证通过了，使用 userId 生成一个 jwt，jwt 存入 返回体 返回
        String username = loginUser.getUsername();
        String jwt = JwtUtil.generateToken(username);
        // 以 userId 为key,用户信息为 value 放入缓存
        redisCache.setCacheObject("token:" + username, loginUser);
        return jwt;
    }

    @Override
    public boolean logout() throws Exception{
        // 获取 SecurityContextHolder 中的用户 id
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String username = loginUser.getUsername();
        // 删除 redis 中的值
        return redisCache.deleteObject("token:"+username);
    }
}
