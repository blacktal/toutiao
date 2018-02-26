package com.wcl.toutiao.interceptor;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.wcl.toutiao.dao.TicketDao;
import com.wcl.toutiao.dao.UserDao;
import com.wcl.toutiao.model.HostHolder;
import com.wcl.toutiao.model.LoginTicket;
import com.wcl.toutiao.model.User;

/**
 * @ClassName: PassportInterceptor 
 * @Description: TODO 
 * @author Lulu
 * @date 2017年11月27日 下午7:10:53 
 */
@Component
public class PassportInterceptor extends HandlerInterceptorAdapter {
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private TicketDao ticketDao;
    
    @Autowired
    private HostHolder hostholder;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String ticket = null;
        // 请求封装的request中有cookie，如果cookie中包含ticket，则取出验证是否是合法可用的ticket
        // 如果cookie中没有ticket，则该请求的发起用户没有登陆
        if (request.getCookies() != null) {
            // 判断cookies中是否有ticket
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("ticket")) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        // 如果ticket存在，验证该ticket的有效性，防止伪造ticket
        if (ticket != null) {
            LoginTicket loginTicket = ticketDao.selectByTicket(ticket);
            // 如果通过该ticket找到的loginTicket为空，或已经过期，或已经被标记不可用，则不登陆
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) 
                    || loginTicket.getStatus() != 0) {
                return true;// 直接进入controller
            }
            
            // 如果是有效ticket，取出该ticket对应的User信息
            User user = userDao.getUserById(loginTicket.getUserId());
            // 可以把用户存放在某处以便取用,request中可以，但并不是所有地方都可用(如service层)
            //request.setAttribute("user", user);
            // 可以使用Threadlocal变量，每个线程都有独立的一份
            hostholder.setUser(user);
        }
        
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // 渲染之前，把用户存进modelAndView(该方法只有对应的preHandle方法返回为true才会执行)
        if (modelAndView != null && hostholder.getUser() != null) {
            modelAndView.addObject(hostholder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // 本次请求结束，清理hostholder
        if (hostholder.getUser() != null) {
            hostholder.clear();
        }
        
    }
    
}
