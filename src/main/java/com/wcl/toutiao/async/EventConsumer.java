package com.wcl.toutiao.async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.wcl.toutiao.util.JedisAdapter;
import com.wcl.toutiao.util.RedisKeyUtil;

/**
 * @ClassName: EventConsumer 
 * @Description: Service层，在event队列处理之初，初始化时将
 *               该event所要处理的handler组织起来，并开启线程不断从队列中取出event进行处理
 * @author Lulu
 * @date 2018年1月17日 下午7:36:11 
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    
    @Autowired
    JedisAdapter jedisAdapter;
    
    /**
     * @Fields handlerConfig : 每种EventType对应的要关注的handler配置 
     */
    private Map<EventType, List<EventHandler>> handlerConfig = new HashMap<>();
    
    /**
     * @Fields applicationContext : Spring的applicationContext，用于操作bean等相关信息
     */
    private static ApplicationContext applicationContext;
    
    // implements 实现InitializingBean接口的类在初始化bean时要实现after方法
    // 意图是在初始化的最后,执行after方法将所要处理的handler都组织起来
    @Override
    public void afterPropertiesSet() throws Exception {
        
        logger.info("开始配置handler");
        
        // 用于获取某类的所有实例类，这里获取到所有实现EventHandler的实例类
        Map<String, EventHandler> handlerBeans = applicationContext.getBeansOfType(EventHandler.class);
        if (handlerBeans != null) {
            for(EventHandler handler : handlerBeans.values()) {
                // 每个handler都有自己要支持的EventType
                List<EventType> types = handler.getSupportEventTypes();
                for(EventType type : types) {
                    if (!handlerConfig.containsKey(type)) {
                        // 如果handlerConfig配置中还没有该类型，加上
                        handlerConfig.put(type, new ArrayList<EventHandler>());
                    }
                    // 有的话，在list中加入该handler
                    handlerConfig.get(type).add(handler);
                }
            }
        }
        logger.info("配置handler完成");
        logger.info("开启线程读取event队列");
        // 配置完成后，开启新线程，不断地去redis中读取event队列
        Thread thread = new Thread(new Runnable() {
            
            @Override
            public void run() {
                // 一直循环读取
                while (true) {
                    String key = RedisKeyUtil.getEventQueueKey();
                    // brpop获取到的是list，首先是key，然后是对应的value
                    List<String> events = jedisAdapter.brpop(key, 0);
                    logger.info("读取event:" + events.get(0) + " " + events.get(1));
                    for (String message : events) {
                        // 如果是key,不处理
                        if (message.equals(key)) {
                            continue;
                        }
                        // 如果是value，则把它parse成原本的对象
                        EventModel eventModel = JSON.parseObject(message, EventModel.class);
                        
                        // 如果message中的type是config中没有注册过的，则不能识别，继续
                        if (!handlerConfig.containsKey(eventModel.getEventType())) {
                            logger.error("不能识别的事件：" + eventModel.getEventType().toString());
                            continue;
                        }
                        List<EventHandler> handlers = handlerConfig.get(eventModel.getEventType());
                        for (EventHandler handler : handlers) {
                            handler.doHandle(eventModel);
                        }
                    }
                }
                
            }
            
        });
        
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        this.applicationContext = arg0;
    }
    
}
