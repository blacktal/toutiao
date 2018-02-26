package com.wcl.toutiao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName: SettingController 
 * @Description: TODO 
 * @author Lulu
 * @date 2017年11月28日 上午9:59:48 
 */
@Controller
public class SettingController {
    
    @RequestMapping(path = {"/setting/", "/setting"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String setting() {
        return "Setting: OK.";
    }
}
