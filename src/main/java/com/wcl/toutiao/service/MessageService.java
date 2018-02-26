package com.wcl.toutiao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcl.toutiao.dao.MessageDao;
import com.wcl.toutiao.dao.UserDao;
import com.wcl.toutiao.model.Message;

/**
 * @ClassName: MessageService 
 * @Description: TODO 
 * @author Lulu
 * @date 2017年12月15日 下午9:48:02 
 */
@Service
public class MessageService {
    @Autowired
    MessageDao messageDao;
    
    @Autowired
    UserDao userDao;
    
    public int addMessage(Message message) throws Exception {
        if (userDao.getUserById(message.getFromId()) != null && 
                userDao.getUserById(message.getToId()) != null) {
            return messageDao.addMessage(message);
        }
        // 失败抛异常
        throw new Exception("用户id不合法");
    }
    
    public List<Message> getConversationDetails(String conversationId) {
        return messageDao.selectMessageByConversationId(conversationId);
    }
    
    public List<Message> getConversationList(int userId) {
        return messageDao.selectConversationList(userId);
    }
    
    /**   
     * @Title: countTotalMsg   
     * @Description: 查询一个对话中双方互发的所有message   
     * @param: @param conversationId
     * @param: @return      
     * @return: int      
     * @throws   
     */  
    public int countTotalMsg(String conversationId) {
        return messageDao.countMsgByConversationId(conversationId);
    }
    
    /**   
     * @Title: countUnreadMsg   
     * @Description: 查询一个对话中对方发来的消息中，未读消息的数量   
     * @param: @param userId
     * @param: @param conversationId
     * @param: @return      
     * @return: int      
     * @throws   
     */  
    public int countUnreadMsg(int userId, String conversationId) {
        return messageDao.countUnreadMsg(userId, conversationId);
    }
}
