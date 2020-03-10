package org.zxs.test.step030.entity;

public class UserEntity {
    /**
     * 用户ID
     */
    @Column(name = "user_id")
    public long userId;

    /**
     * 用户名称
     */
    @Column(name = "user_name")
    public String username;

    /**
     * 用户密码
     */
    @Column(name = "password")
    public String passwrod;

}
