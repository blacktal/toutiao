package com.wcl.toutiao.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.wcl.toutiao.model.News;
import com.wcl.toutiao.model.ViewObject;
import com.wcl.toutiao.service.NewsService;
import com.wcl.toutiao.service.UserService;

/**
 * @ClassName: HomeController 
 * @Description: TODO 
 * @author Lulu
 * @date 2017年11月20日 下午3:40:39 
 */
@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private NewsService newsService;
    
    /**   
     * @Title: getNewsAndUserViewObject   
     * @Description: TODO   
     * @param: @param userId
     * @param: @param offset
     * @param: @param limit
     * @param: @return      
     * @return: List<ViewObject>      
     * @throws   
     */  
    private List<ViewObject> getNewsAndUserViewObject(int userId, int offset, int limit) {
        List<News> newsList = newsService.getLastedNews(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<>();
        for (News news: newsList) {
            ViewObject vo = new ViewObject();
            vo.set("news", news);
            vo.set("user", userService.getUser(news.getUserId()));
            vos.add(vo);
        }
        return vos;
    }
    
    /**   
     * @Title: index   
     * @Description: TODO   
     * @param: @param model
     * @param: @return      
     * @return: String      
     * @throws   
     */  
    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model, @RequestParam(value="pop", defaultValue="0") int pop) {
        model.addAttribute("vos", this.getNewsAndUserViewObject(0, 0, 10));
        // 为了未登录跳转至弹出框的功能，增加一个pop请求参数，pop=0的时候不弹出，pop=1的时候弹出弹出框
        model.addAttribute("pop", pop);
        return "home";
    }
    
    /**   
     * @Title: userIndex   
     * @Description: TODO   
     * @param: @param model
     * @param: @param userId
     * @param: @return      
     * @return: String      
     * @throws   
     */  
    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("vos", this.getNewsAndUserViewObject(userId, 0, 10));
        return "home";
        
    }
}
