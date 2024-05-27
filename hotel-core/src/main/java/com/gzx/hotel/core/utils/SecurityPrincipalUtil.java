package com.gzx.hotel.core.utils;

import com.gzx.hotel.core.pojo.LoginUser;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author gzx
 * @date 2024/1/19
 * @Description 获取认证用户主体工具
 */
public class SecurityPrincipalUtil {

    public static LoginUser getLoginUser() {
        return (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
