package com.bjsxt.music.project;

import java.io.Serializable;

/**
 * 封装用户
 */

public class User implements Serializable {
    private static final long serialVersionUID = 2948136383093587358L;//实现序列化
    //定义用户属性
    private String username;
    private String password;

    //空构造器
    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //有参构造器
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
