package com.wcl.toutiao.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.wcl.toutiao.util.JedisAdapter;
import com.wcl.toutiao.util.RedisKeyUtil;

/**
 * @ClassName: EventProducer 
 * @Description: 将event对象放进redis队列,也是service层
 * @author Lulu
 * @date 2018年1月17日 下午2:29:37 
 */
@Service
public class EventProducer {
    
    private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);
    
    @Autowired
    JedisAdapter jedisAdapter;
    
    
    public boolean fireEvent(EventModel model) {
        logger.info("fire event开始");
        try {
            String json = JSONObject.toJSONString(model);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception e) {
            logger.error("fire event出现错误：" + e.getMessage());
            return false;
        }
    }
}
