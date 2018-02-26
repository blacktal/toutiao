package com.wcl.toutiao.async;

import java.util.HashMap;
import java.util.Map;

import com.wcl.toutiao.util.EntityType;

/**
 * @ClassName: EventModel 
 * @Description: TODO 
 * @author Lulu
 * @date 2018年1月15日 上午11:32:27 
 */
public class EventModel {
    // eventModel中需要有什么属性？
    
    /**
     * @Fields eventType : 事件的类型，枚举类eventType  
     */
    private EventType eventType;
    
    /**
     * @Fields actorId : 事件的触发者actorId  
     */
    private int actorId;
    
    /**
     * @Fields entityType : 事件的触发对象entity的类型
     */
    private EntityType entityType;
    
    /**
     * @Fields entityId : 触发对象的id  
     */
    private int entityId;
    
    /**
     * @Fields entityOwnerId : 触发对象的拥有者id（如点赞一条资讯，该资讯的作者）  
     */
    private int entityOwnerId;
    
    /**
     * @Fields exts : 触发事件现场的一些参数，保存在map中作为额外信息  
     */
    private Map<String, String> exts = new HashMap<>();
    
    // 加一个带type参数的构造函数
    public EventModel(EventType eventType) {
        this.eventType = eventType;
    }
    
    // 默认构造函数
    public EventModel() {}
    
    // set exts，方便存取extends信息
    public EventModel setExt(String key, String value) {
        this.exts.put(key, value);
        return this;
    }
    
    public String getExt(String key) {
        return this.exts.get(key);
    }

    public EventType getEventType() {
        return eventType;
    }

    public EventModel setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(EntityType entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public void setExts(Map<String, String> exts) {
        this.exts = exts;
    }
    
    
}
