package com.wcl.toutiao.async;

/**
 * @ClassName: EventType 
 * @Description: TODO 
 * @author Lulu
 * @date 2018年1月15日 上午11:32:06 
 */
public enum EventType {
    COMMENT(0),
    LIKE(1),
    LOGIN(2),
    MAIL(3);
    
    private int value;
    
    EventType(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return this.value;
    }
}
