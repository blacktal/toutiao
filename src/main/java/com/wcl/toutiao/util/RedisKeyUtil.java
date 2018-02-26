package com.wcl.toutiao.util;

/**
 * @ClassName: RedisKeyUtil 
 * @Description: 生成redis key 
 * @author Lulu
 * @date 2017年12月28日 下午5:00:28 
 */
public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENT = "EVENT";
    
    public static String getLikeKey(EntityType entityType, int entityId) {
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
    
    public static String getDislikeKey(EntityType entityType, int entityId) {
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
    
    /**   
     * @Title: getEventQueueKey   
     * @Description: 获取异步队列中的event的key.   
     * @param: @return      
     * @return: String      
     * @throws   
     */  
    public static String getEventQueueKey() {
        return BIZ_EVENT;
    }
}
