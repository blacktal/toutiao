package com.wcl.toutiao.model;

import java.util.Date;

/**
 * @ClassName: Comment 
 * @Description: TODO 
 * @author Lulu
 * @date 2017年12月10日 上午10:16:33 
 */
public class Comment {
    int id;
    String content;
    Date createdDate;
    /**
     * @Fields entityType : 关于何种model的评论  
     */
    int entityType;
    /**
     * @Fields entityId : 该model的id  
     */
    int entityId;
    /**
     * @Fields userId : 发表评论的用户id  
     */
    int userId;
    
    /**
     * @Fields status : 是否删除，0为未删除，1为删除  
     */
    int status;
    
    
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
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
    public Date getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    public int getEntityType() {
        return entityType;
    }
    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }
    public int getEntityId() {
        return entityId;
    }
    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    
}
