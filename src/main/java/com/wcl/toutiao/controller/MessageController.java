package com.wcl.toutiao.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wcl.toutiao.model.HostHolder;
import com.wcl.toutiao.model.Message;
import com.wcl.toutiao.model.User;
import com.wcl.toutiao.model.ViewObject;
import com.wcl.toutiao.service.MessageService;
import com.wcl.toutiao.service.UserService;
import com.wcl.toutiao.util.ToutiaoUtil;

@Controller
public class MessageController {
    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    
    @Autowired
    MessageService messageService;
    
    @Autowired
    HostHolder hostHolder;
    
    @Autowired
    UserService userService;
    
    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId, @RequestParam("toId") int toId,
                             @RequestParam("content") String content) {
        try {
            Message message = new Message();
            message.setFromId(fromId);
            message.setToId(toId);
            message.setContent(content);
            message.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
            message.setHasRead(0);
            message.setCreatedDate(new Date());
            messageService.addMessage(message);
            return ToutiaoUtil.getJSONString(0, message.getConversationId());
        } catch (Exception e) {
            logger.error("发送消息失败: " + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "发送消息失败");
        }
    }
    
    
    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
    public String conversationDetails(Model model, @RequestParam("conversationId") String conversationId) {
        try {
            List<ViewObject> vos = new ArrayList<>();
            List<Message> messageList = messageService.getConversationDetails(conversationId);
            for (Message message : messageList) {
                ViewObject msg = new ViewObject();
                msg.set("message", message);
                // 消息来往的对方用户
//                msg.set("user", (hostHolder.getUser().getId() == message.getFromId()
//                        ? userService.getUser(message.getToId()) : userService.getUser(message.getFromId())));
                
                User user = userService.getUser(message.getFromId());
                // 如果该id对应的user不存在，跳过该条message对话框
                if (user == null) {
                    continue;
                }
                msg.set("user", user);
                vos.add(msg);
            }
            model.addAttribute("messages", vos);
            
        } catch (Exception e) {
            logger.error("查找会话失败： " + e.getMessage());
        }
        
        return "letterDetail";
    }
    
    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    // 这里把userId作为requestParam是为了测试方便，因为登陆框弹不出来，不方便获取登陆用户的权限
    public String conversationList(Model model, @RequestParam("userId") int userId) {
        try {
            List<Message> conversationList = messageService.getConversationList(userId);
            List<ViewObject> vos = new ArrayList<>();
            for (Message cvs : conversationList) {
                ViewObject msg = new ViewObject();
                String conversationId = cvs.getConversationId();
                msg.set("conversation", cvs);
                msg.set("user", userService.getUser((cvs.getFromId()) == userId ? cvs.getToId() : cvs.getFromId()));
                msg.set("totalCount", messageService.countTotalMsg(conversationId));
                msg.set("unreadCount", messageService.countUnreadMsg(userId, conversationId));
                vos.add(msg);
            }
            model.addAttribute("conversations", vos);
        } catch (Exception e) {
            logger.error("获取对话列表失败： " + e.getMessage());
        }
        return "letter";
        
        
    }
}
