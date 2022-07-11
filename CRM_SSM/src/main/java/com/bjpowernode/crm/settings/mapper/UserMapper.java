package com.bjpowernode.crm.settings.mapper;

import com.bjpowernode.crm.settings.bean.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    /**
     * 根据账号和密码查询用户
     * @param map
     * @return
     */
    User selectUserByLoginActAndPwd(Map<String,Object> map);

    /**
     * 查询所有的用户
     * @return
     */
    List<User> selectAllUsers();
}
