package com.person.hero.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class UserManager {

    private static final Map<Integer,User> _userMap = new HashMap<>();

    private UserManager(){
    }

    /**
     * 添加用户
     * @param user
     */
    public static void addUser(User user){
        if(null != user){
            _userMap.put(user.getUserId(),user);
        }
    }

    /**
     * 删除用户
     * @param userId
     */
    public static void removeUserById(int userId){
        _userMap.remove(userId);
    }

    /**
     * 获取用户列表
     * @return
     */
    public static Collection<User> listUser(){
        return _userMap.values();
    }

}
