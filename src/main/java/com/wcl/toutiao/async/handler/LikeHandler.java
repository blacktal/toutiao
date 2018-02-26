package com.wcl.toutiao.async.handler;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.wcl.toutiao.async.EventHandler;
import com.wcl.toutiao.async.EventModel;
import com.wcl.toutiao.async.EventType;

/**
 * @ClassName: LikeHandler 
 * @Description: TODO 
 * @author Lulu
 * @date 2018年1月17日 下午2:29:27 
 */
@Component
public class LikeHandler implements EventHandler{

    @Override
    public void doHandle(EventModel model) {
        System.out.println("Liked");
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        // 点赞event所关心的event
        return Arrays.asList(EventType.LIKE);
    }

}
