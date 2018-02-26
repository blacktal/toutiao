package com.wcl.toutiao.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

/**
 * @ClassName: JedisAdapter 
 * @Description: TODO 
 * @author Lulu
 * @date 2017年12月27日 下午4:39:48 
 */
@Service
public class JedisAdapter implements InitializingBean{
    
    private JedisPool pool = null;
    
    private Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    
    @Override
    public void afterPropertiesSet() throws Exception {
        // 对象初始化完成后，进行pool的初始化
        pool = new JedisPool("localhost", 6379);
    }
    
    // 一般String的set方法
    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("jedis set 操作异常:" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    
    // 一般String的get
    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.get(key);
        } catch (Exception e) {
            logger.error("jedis sadd 操作异常:" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    
    
    // 不需要dao层，直接操作内存中的redis
    // 首先明确：赞踩功能用set实现
    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            // key-value添加进redis，成功返回1，已经存在则返回0
            return jedis.sadd(key, value);
            
        } catch (Exception e) {
            logger.error("jedis sadd 操作异常:" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    
    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("Jedis srem 操作异常：" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public boolean simember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("Jedis ismember 操作异常：" + e.getMessage());
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /*某个集合的元素数量*/
    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("Jedis scard 操作异常：" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    
    //-----------------对象存取，用于异步队列--------------------
    
    // 存储对象
    public void setObject(String key, Object obj) {
        set(key, JSON.toJSONString(obj));
    }
    
    // 取出对象
    public <T> T getObject(String key, Class<T> clazz) {
        String json = get(key);
        if (json != null) {
            return JSON.parseObject(json, clazz);
        }
        return null;
    }
    
    //brpop，阻塞pop，从队列尾部取出元素，如果队列为空则阻塞等待直至等待超时或有元素可以取出
    public List<String> brpop(String key, int timeout) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("Jedis brpop操作异常：" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    
    // lpush，从左边加入list
    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("Jedis lpush操作异常：" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    

    // main函数单元测试
    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        jedis.flushAll();// 数据库数据全部删除
        
        System.out.println("=============== 坐下，正常操作 ===============");
        jedis.set("xiaoming", "yeah");
        System.out.println(jedis.get("xiaoming"));
        jedis.rename("xiaoming", "xiaohong");// key重命名
        System.out.println(jedis.get("xiaohong"));
        jedis.setex("yanzhengma", 20, "2352"); // 设置过期时间，10秒后自动删除
        System.out.println(jedis.get("yanzhengma"));
        
        // pv：访问量
        jedis.set("pv", "100");
        // 对于数值型变量, +1操作
        jedis.incr("pv");
        System.out.println(jedis.get("pv"));
        // 步进增加
        jedis.incrBy("pv", 10);
        System.out.println(jedis.get("pv"));
        
        // 数据结构们
        // list
        System.out.println("=============== List ===============");
        String listName = "myList";
        for (int i = 0; i < 10 ; ++i) {
            // 从左边push
            jedis.lpush(listName, "a" + String.valueOf(i));
        }
        System.out.println(jedis.lrange(listName, 0, 5));// 从左边数5个，取出来
        System.out.println(jedis.llen(listName));// list长度
        System.out.println(jedis.lpop(listName));// 从左边取出
        System.out.println(jedis.llen(listName));
        System.out.println(jedis.lindex(listName, 5));// index位置的变量
        System.out.println(jedis.linsert(listName, LIST_POSITION.AFTER, "a4", "xx"));
        System.out.println(jedis.linsert(listName, LIST_POSITION.BEFORE, "a4", "yy"));
        System.out.println(jedis.lrange(listName, 0, 13));
        
        // 对象User，用Hashset实现  键值对
        System.out.println("=============== HashSet ===============");
        String userKey = "user111";
        jedis.hset(userKey, "name", "akira");
        jedis.hset(userKey, "age", "22");
        jedis.hset(userKey, "school", "shiyan");
        System.out.println(jedis.hget(userKey, "school"));
        System.out.println(jedis.hgetAll(userKey));
        jedis.hdel(userKey, "school");
        System.out.println(jedis.hgetAll(userKey));
        System.out.println(jedis.hkeys(userKey));
        System.out.println(jedis.hvals(userKey));
        System.out.println(jedis.hexists(userKey, "email"));
        System.out.println(jedis.hexists(userKey, "name"));
        // setnx，not exit，不存在则set，已经存在就不更改了
        jedis.hsetnx(userKey, "name", "James");
        
        //Set集合
        System.out.println("=============== Set ===============");
        String setName1 = "LikeSet1";
        String setName2 = "LikeSet2";
        for (int i = 0; i < 10; ++ i) {
            jedis.sadd(setName1, String.valueOf(i));
            jedis.sadd(setName2, String.valueOf(i * 2));
        }
        System.out.println(jedis.smembers(setName1));
        System.out.println(jedis.smembers(setName2));
        
        // 前一个跟后一个比，不同的元素（我1有你2没有）
        System.out.println(jedis.sdiff(setName1, setName2));
        // 交集
        System.out.println(jedis.sinter(setName1, setName2));
        // 并集
        System.out.println(jedis.sunion(setName1, setName2));
        System.out.println(jedis.sismember(setName1, "5"));// 是否在集合中
        jedis.srem(setName1, "5");// 删掉元素5
        System.out.println(jedis.sismember(setName1, "5"));
        // set中有多少个元素
        System.out.println(jedis.scard(setName1));
        jedis.smove(setName2, setName1, "18");
        System.out.println(jedis.smembers(setName1));
        
        // 优先队列 sorted set
        System.out.println("=============== SortedSet ===============");
        String rankKey = "rankKey";
        jedis.zadd(rankKey, 95, "siqiannian");
        jedis.zadd(rankKey, 80, "faka");
        jedis.zadd(rankKey, 70, "ahuang");
        jedis.zadd(rankKey, 60, "ergou");
        jedis.zadd(rankKey, 50, "dage");
        jedis.zadd(rankKey, 23, "yuezai");
        System.out.println(jedis.zcard(rankKey));// 元素总数
        System.out.println(jedis.zcount(rankKey, 60, 100));// 两个分数中间的元素个数
        System.out.println(jedis.zscore(rankKey, "ahuang"));// 某个元素的分数
        jedis.zincrby(rankKey, 10, "yuezai");// 某个元素的分数提高多少
        // 排序
        System.out.println(jedis.zrange(rankKey, 0, 3));
        System.out.println(jedis.zrevrange(rankKey, 0, 2));// 前三
        
        // 取出全部内容
        for (Tuple tuple : jedis.zrangeByScoreWithScores(rankKey, "0", "100")) {
            System.out.println(tuple.getElement() + ":" + String.valueOf(tuple.getScore()));
        }
        
        System.out.println(jedis.zrank(rankKey, "yuezai"));// 某个元素排第几
        System.out.println(jedis.zrevrank(rankKey, "yuezai"));
        
        // pool, Jedis连接池
        JedisPool pool = new JedisPool();
        // 循环
        for (int i = 0; i < 10; i ++) {
            Jedis jedis1 = pool.getResource();
            jedis1.get("a");
            System.out.println("Pool" + i);
            // 用完关掉jedis，一个pool最多只有8个jedis资源可用，否则就会阻塞在8条
            jedis1.close();
            
        }
        
    }
}
