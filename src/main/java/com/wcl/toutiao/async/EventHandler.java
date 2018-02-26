package com.wcl.toutiao.async;

import java.util.List;

/**
 * @ClassName: EventHandler 
 * @Description: event的处理接口
 * @author Lulu
 * @date 2018年1月17日 上午11:26:45 
 */
public interface EventHandler {
    // 处理接口
    void doHandle(EventModel model);
    
    // 返回event handler所关心的type类型
    List<EventType> getSupportEventTypes(); 
}
