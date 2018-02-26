package com.wcl.toutiao.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wcl.toutiao.dao.TicketDao;
import com.wcl.toutiao.dao.UserDao;
import com.wcl.toutiao.model.LoginTicket;
import com.wcl.toutiao.model.User;
import com.wcl.toutiao.util.ToutiaoUtil;

/**
 * @ClassName: UserService
 * @Description: TODO
 * @author Lulu
 * @date 2017年11月20日 上午10:00:03
 */
@Service
public class UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    TicketDao ticketDao;


    public Map<String, Object> register(String userName, String password) {
        // 这里要检验一下用户名和密码是否符合要求
        // 用map<,>来存储返回信息
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(userName)) {
            map.put("msgname", "用户名不能为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msgpwd", "密码不能为空!");
            return map;
        }
        if (userDao.getUserByName(userName) != null) {
            map.put("msgname", "用户名已经被注册！");
            return map;
        }
        // if (StringUtils.length(userName) > 8) {
        // map.put("msgname", "用户名不能超过8个字符！");
        // return map;
        // }
        User user = new User();
        user.setName(userName);
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dm.png", new Random().nextInt(1000)));
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));// 取随机uuid的前五位作为salt
        user.setPassword(ToutiaoUtil.md5(password + user.getSalt()));
        userDao.addUser(user);

        // 注册完立刻登陆，也就是下发一个ticket
        String ticket = this.addLoginTicket(user.getId());
        map.put("ticket", ticket);

        return map;
    }

    public Map<String, Object> login(String userName, String password) {
        Map<String, Object> map = new HashMap<>();

        if (StringUtils.isBlank(userName)) {
            map.put("msgname", "用户名不能为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msgpwd", "密码不能为空!");
            return map;
        }
        // if (StringUtils.length(userName) > 8) {
        // map.put("msgname", "用户名不能超过8个字符！");
        // return map;
        // }

        User user = userDao.getUserByName(userName);
        if (user == null) {
            map.put("msgname", "用户名不存在！");
            return map;
        }

        if (!user.getPassword().equals(ToutiaoUtil.md5(password + user.getSalt()))) {
            map.put("msgpwd", "密码不正确！");
            return map;
        }
        
        //校验成功，给用户下发ticket，放进map中
        String ticket = this.addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }
    
    /**   
     * @Title: addLoginTicket   
     * @Description: 为用户添加一个ticket   
     * @param: @param userId
     * @param: @return      
     * @return: String      
     * @throws   
     */  
    private String addLoginTicket(int userId) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        ticket.setStatus(0);
        Date date = new Date();
        date.setTime(date.getTime() + 1000*3600*24);//多一天的有限期
        ticket.setExpired(date);
        ticketDao.addTicket(ticket);
        return ticket.getTicket();
    }
    
    public void logout(String ticket) {
        // 登出：将status设为1，无效
        ticketDao.updateStatus(ticket, 1);
    }

    public User getUser(int id) {
        return userDao.getUserById(id);
    }
}
