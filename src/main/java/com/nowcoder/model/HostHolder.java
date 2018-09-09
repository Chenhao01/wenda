package com.nowcoder.model;

import org.springframework.stereotype.Component;

/**
 * Created by 12274 on 2018/1/16.
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users=new ThreadLocal<User>();
    public void setUser(User user){
        users.set(user);
    }
    public User getUser(){
        return users.get();
    }
    public void clear(){
        users.remove();
    }
}
