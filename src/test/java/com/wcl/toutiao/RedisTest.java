package com.wcl.toutiao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wcl.toutiao.model.User;
import com.wcl.toutiao.util.JedisAdapter;

/**
 * @ClassName: RedisTest 
 * @Description: TODO 
 * @author Lulu
 * @date 2018年1月15日 上午11:32:17 
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ToutiaoApplication.class)
public class RedisTest {
    @Autowired
    JedisAdapter jedisAdapter;
    
    @Test
    public void testSetUser() {
        User user = new User();
        user.setHeadUrl("http://laidawoya/89757");
        user.setName("lais");
        user.setPassword("sdfasdg");
        user.setSalt("ggg");
        jedisAdapter.setObject("user", user);
    }
    
    @Test
    public void testGetUser() {
        User user = jedisAdapter.getObject("user", User.class);
        System.out.println(user.getName());
    }
}
