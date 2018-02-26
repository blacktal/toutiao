package com.wcl.toutiao.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wcl.toutiao.service.UserService;
import com.wcl.toutiao.util.ToutiaoUtil;

/**
 * @ClassName: LoginController 
 * @Description: TODO 
 * @author Lulu
 * @date 2017年11月24日 下午5:20:01 
 */
@Controller
public class LoginController {
    
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    
    @Autowired
    private UserService userService;
    
    @RequestMapping(path = "/reg/", method= {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody //代表返回是一个body，不要解析成模板名
    public String register(Model model, @RequestParam("username") String userName, 
                           @RequestParam("password") String password,
                           @RequestParam(value="rember",defaultValue="0") int rememberMe,
                           HttpServletResponse response) {
        /*为什么要加try-catch?
                      不加的话因为一些原因(如参数传递过去为空)导致操作失败，会返回未经处理的错误页面
                      加一个try-catch，可以catch到一些异常，记录日志并返回错误信息
        */
        try {
            //返回JSON串，包含了一些信息
            Map<String, Object> map = userService.register(userName, password);
            
            //如果map中包含ticket，则说明注册成功，并已经处于登陆状态
            if (map.containsKey("ticket")) {
                
                //需要将ticket存在浏览器cookies中
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                
                //设置cookie路径为全端有效
                cookie.setPath("/");
                
                if (rememberMe > 0) {
                    //设置cookie有效时间
                    cookie.setMaxAge(3600*24*5);
                }
                
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0, "注册成功");
            } else {
                return ToutiaoUtil.getJSONString(1, map);
            }
        } catch (Exception e) {
            logger.error("注册失败：" + e.getStackTrace());
            return ToutiaoUtil.getJSONString(1, "注册失败");
        }
    }
    
    @RequestMapping(path = "/login/", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String userName,
                        @RequestParam("password") String password,
                        @RequestParam("rember") int rememberMe,
                        HttpServletResponse response) {
        try {
            
            Map<String, Object> map = userService.login(userName, password);
            //如果map中包含ticket，则说明已经处于登陆状态
            if (map.containsKey("ticket")) {
                
                //需要将ticket存在浏览器cookies中
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                
                //设置cookie路径为全端有效
                cookie.setPath("/");
                
                // rememberMe控制cookie的有效时间，实际上还要跟后台ticket的有效期一起使用，否则取出来也是无效的
                if (rememberMe > 0) {
                    //设置cookie有效时间
                    cookie.setMaxAge(3600*24*5);
                }
                
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0, "登陆成功");
            } else {
                return ToutiaoUtil.getJSONString(1, map);
            }
        } catch (Exception e) {
            logger.error("登陆失败：" + e.getStackTrace());
            return ToutiaoUtil.getJSONString(1, "登录失败");
        }
    }
    
    @RequestMapping(path = {"/logout/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        // 登出，重定位到首页
        return "redirect:/";
    }
}
