package com.wcl.toutiao.model;

import java.util.Date;

/**
 * @ClassName: Message 
 * @Description: TODO 
 * @author Lulu
 * @date 2017年12月10日 上午11:10:45 
 */
public class Message {

    int id;
    String content;
    int fromId;
    int toId;
    int hasRead;
    Date createdDate;
    String conversationId;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getFromId() {
        return fromId;
    }
    public void setFromId(int fromId) {
        this.fromId = fromId;
    }
    public int getToId() {
        return toId;
    }
    public void setToId(int toId) {
        this.toId = toId;
    }
    public int getHasRead() {
        return hasRead;
    }
    public void setHasRead(int hasRead) {
        this.hasRead = hasRead;
    }
    public Date getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    public String getConversationId() {
        return conversationId;
    }
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
    
    
}
