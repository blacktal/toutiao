package com.wcl.toutiao.model;

import org.springframework.stereotype.Component;

/**
 * @ClassName: HostHolder 
 * @Description: TODO 
 * @author Lulu
 * @date 2017年11月27日 下午8:15:05 
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<>();
    
    public User getUser() {
        return users.get();
    }
    
    public void setUser(User user) {
        users.set(user);
    }
    
    public void clear() {
        users.remove();
    }
}
