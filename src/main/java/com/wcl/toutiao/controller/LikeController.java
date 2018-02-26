package com.wcl.toutiao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wcl.toutiao.async.EventConsumer;
import com.wcl.toutiao.async.EventModel;
import com.wcl.toutiao.async.EventProducer;
import com.wcl.toutiao.async.EventType;
import com.wcl.toutiao.model.HostHolder;
import com.wcl.toutiao.service.LikeService;
import com.wcl.toutiao.service.NewsService;
import com.wcl.toutiao.util.EntityType;
import com.wcl.toutiao.util.ToutiaoUtil;

/**
 * @ClassName: LikeController 
 * @Description: TODO 
 * @author Lulu
 * @date 2017年12月28日 上午11:20:10 
 */
@Controller
public class LikeController {
    
    private static Logger logger = LoggerFactory.getLogger(LikeController.class);
    
    @Autowired
    LikeService likeService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    NewsService newsService;
    
    // 使用异步队列处理event后，增加event的处理对象
    @Autowired
    EventProducer eventProducer;
    
    // 使用异步队列处理event后，增加event的处理对象
    @Autowired
    EventConsumer eventConsumer;
    
    @RequestMapping(path = {"/like"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String likeNews(@RequestParam("newsId") int newsId) {
        //int userId = hostHolder.getUser().getId();
        int userId = 1;
        
        // 点赞，并更新数据库点赞数
        long likeCount = likeService.like(userId, newsId, EntityType.ENTITY_NEWS);
        newsService.updateLikeCount((int) likeCount, newsId);
        
        logger.info("进行异步处理");
        EventModel eventModel = new EventModel();
        eventModel.setActorId(userId)
        .setEntityType(EntityType.ENTITY_NEWS)
        .setEntityId(newsId)
        .setEventType(EventType.LIKE)
        .setEntityOwnerId(newsService.getById(newsId).getUserId());
        
        // 将like的eventmodel加入redis队列中
        eventProducer.fireEvent(eventModel);
        logger.info("fire event 成功.");
        
        // 自动调用
        //eventConsumer.afterPropertiesSet();
        
        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }
    
    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String dislikeNews(@RequestParam("newsId") int newsId) {
        //int userId = hostHolder.getUser().getId();
        int userId = 1;
        // 点赞，并更新数据库点赞数
        long likeCount = likeService.dislike(userId, newsId, EntityType.ENTITY_NEWS);
        newsService.updateLikeCount((int) likeCount, newsId);
        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }
}
