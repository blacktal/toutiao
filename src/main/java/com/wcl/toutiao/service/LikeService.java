package com.wcl.toutiao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcl.toutiao.util.EntityType;
import com.wcl.toutiao.util.JedisAdapter;
import com.wcl.toutiao.util.RedisKeyUtil;

/**
 * @ClassName: LikeService 
 * @Description: TODO 
 * @author Lulu
 * @date 2017年12月28日 下午1:45:41 
 */
@Service
public class LikeService {
    
    @Autowired
    JedisAdapter jedisAdapter;

    
    
    /**   
     * @Title: getLikeStatus   
     * @Description: 判断指定用户对指定entity的喜欢状态，喜欢返回1，不喜欢返回-1，无评价返回0
     * @param: @param userId
     * @param: @param entityId
     * @param: @param entityType
     * @param: @return      
     * @return: int      
     * @throws   
     */  
    public int getLikeStatus(int userId, int entityId, EntityType entityType) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        String userValue = String.valueOf(userId);
        if (jedisAdapter.simember(likeKey, userValue)) {
            return 1;
        }
        if (jedisAdapter.simember(dislikeKey, userValue)) {
            return -1;
        }
        return 0;
    }
    
    public long like(int userId, int entityId, EntityType entityType) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        String userValue = String.valueOf(userId);
        // 添加进喜欢的集合，从不喜欢的集合中删除
        jedisAdapter.sadd(likeKey, userValue);
        jedisAdapter.srem(dislikeKey, userValue);
        
        // 返回 最新的 喜欢的个数
        return jedisAdapter.scard(likeKey);
    }
    
    public long dislike(int userId, int entityId, EntityType entityType) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        String userValue = String.valueOf(userId);
        // 添加进不喜欢的集合，从喜欢的集合中删除
        jedisAdapter.sadd(dislikeKey, userValue);
        jedisAdapter.srem(likeKey, userValue);
        
        // 返回 最新的 喜欢的个数
        return jedisAdapter.scard(likeKey);
    }
}
