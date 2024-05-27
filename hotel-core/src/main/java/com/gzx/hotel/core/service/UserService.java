package com.gzx.hotel.core.service;

import com.gzx.hotel.base.service.ICrudService;
import com.gzx.hotel.core.po.User;
import com.gzx.hotel.core.pojo.LoginUser;

public interface UserService extends ICrudService<User> {

    LoginUser login(String username, String password) throws Exception;

    boolean logout() throws Exception;
}
