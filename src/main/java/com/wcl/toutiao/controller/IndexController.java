package com.wcl.toutiao.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wcl.toutiao.model.User;

/**
 * @author Lulu
 *
 */
//@Controller
public class IndexController {
	
	//首页处理
	//@RequestMapping("/")
	@RequestMapping(path = {"/", "index"})
	@ResponseBody
	public String index() {
		return "hello";		
	}
	
	@RequestMapping(path = "/profile/{groupId}/{userId}")
	@ResponseBody
	public String profile(@PathVariable("groupId") String groupId,
			@PathVariable("userId") int userId,
			@RequestParam(value = "type", defaultValue = "1") int type,
			@RequestParam(value = "key", defaultValue = "wangchenlu") String key) {
				return String.format("GroupId{%s}, UserId{%d}, Type{%d}, Key{%s}",groupId, userId, type, key);
		
	}
	
	@RequestMapping(path = {"/news"})
	public String news() {
	    return "news";
	}
	
	@RequestMapping(path = {"/shows"})
	public String showModel(Model model) {
	    model.addAttribute("animal", "dog");
	    model.addAttribute("human", "atrists");
	    List<String> colors = Arrays.asList(new String[] {"RED", "BLUE", "BLACK"});
	    model.addAttribute("colors", colors); 
	    Map<String, String> map = new HashMap<>();
	    for (int i = 0; i < 4; i++) {
	        map.put(String.valueOf(i), String.valueOf(i * i));
	    }
	    model.addAttribute("map", map);
	    
	    //model.addAttribute("newUser", new User("SG"));
        return "shows";
	}
	

}
