package com.wcl.toutiao;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.wcl.toutiao.dao.CommentDao;
import com.wcl.toutiao.dao.NewsDao;
import com.wcl.toutiao.dao.TicketDao;
import com.wcl.toutiao.dao.UserDao;
import com.wcl.toutiao.model.Comment;
import com.wcl.toutiao.model.LoginTicket;
import com.wcl.toutiao.model.News;
import com.wcl.toutiao.model.User;
import com.wcl.toutiao.util.EntityType;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ToutiaoApplication.class)
@Sql(value = "/sql/init_schema.sql")
public class ToutiaoApplicationTests {

    @Autowired
    UserDao userDao;
    @Autowired
    NewsDao newsDao;
    @Autowired
    TicketDao ticketDao;
    @Autowired
    CommentDao commentDao;
    
    @Test
    public void contextLoads() {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("User%d", i));
            user.setPassword("");
            user.setSalt("");
            userDao.addUser(user);

            // 测试：每个用户产生一条新鲜事
            News news = new News();
            news.setCommentCount(i);
            Date date = new Date();
            // 修改date的时间，使其每次循环递增5小时
            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
            news.setTitle(String.format("Title%d", i));
            news.setLikeCount(i + 10);
            news.setUserId(i + 1);
            news.setLink(String.format("http://baidu.com/%d", i + 1));
            newsDao.addNews(news);

            // user.setPassword("thisisapassword");
            // userDao.updatePassword(user);

            LoginTicket ticket = new LoginTicket();
            ticket.setUserId(i + 1);
            ticket.setExpired(new Date());
            ticket.setTicket(String.format("Ticket%d", i + 1));
            ticket.setStatus(0);
            ticketDao.addTicket(ticket);
            
            ticketDao.updateStatus(ticket.getTicket(), 2);
            
            //每条新闻增加1个评论信息
            Comment comment = new Comment();
            comment.setContent("Test comment: tianlelu");
            comment.setCreatedDate(new Date());
            comment.setEntityId(news.getId());
            comment.setEntityType(EntityType.ENTITY_NEWS.ordinal());
            comment.setUserId(user.getId());
            commentDao.addComment(comment);
        }

        User user = userDao.getUserById(5);
        System.out.println(user.getName());

        User newUser = new User();
        newUser.setId(4);
        newUser.setPassword("thisisapassword");
        userDao.updatePassword(newUser);
        Assert.assertEquals("thisisapassword", userDao.getUserById(4).getPassword());

        // userDao.deleteUserById(9);
        // Assert.assertNull(userDao.getUserById(9));

        List<News> newsList = newsDao.selectByUserIdAndOffset(0, 0, 2);
        for (News news : newsList) {
            System.out.println(news);
        }
        
        Assert.assertEquals(1, ticketDao.selectByTicket("Ticket1").getUserId());
        
        List<Comment> commentList = commentDao.selectByEntityIndex(5, EntityType.ENTITY_NEWS.ordinal());
        for (Comment comment : commentList) {
            System.out.println(comment.getContent() + " : " + comment.getStatus());
            commentDao.deleteComment(comment.getId());
        }
        
        
    }

}
