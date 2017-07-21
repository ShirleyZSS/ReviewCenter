package com.nowcoder;

import com.nowcoder.dao.CommentDAO;
import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.NewsDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.*;
import com.nowcoder.service.CommentService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

/**
 * Created by Shirley on 2017/7/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=ToutiaoApplication.class)
@Sql("/init-schema.sql")
public class InitDatabaseTests {
    @Autowired
    UserDAO userDAO;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    NewsDAO newsDAO;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    CommentDAO commentDAO;

    @Test
    public void initData(){
        Random random = new Random();
        for(int i=0;i<11;++i){
            User user=new User();
            user.setName(String.format("USER%d",i));
            user.setPassword("");
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",random.nextInt(1000)));
            user.setSalt("");
            userDAO.addUser(user);

            News news=new News();
            news.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime()+1000*3600*5*i);
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png",random.nextInt(1000)));
            news.setLikeCount(i+1);
            news.setTitle(String.format("TITLE{%d}",i));
            news.setUserId(i+1);
            news.setLink(String.format("http://www.nowcoder.com/%d.html",i));
            newsDAO.addNews(news);
            for(int j=0;j<3;j++){
                Comment comment=new Comment();
                comment.setUserId(i+1);
                comment.setEntityId(news.getId());
                comment.setEntityType(EntityType.ENTITY_NEWS);
                comment.setContent("这里是一条评论啊！"+String.valueOf(j));
                comment.setStatus(0);
                comment.setCreatedDate(new Date());
                commentDAO.addComment(comment);
            }


            user.setPassword("newpassword");
            userDAO.updatePassword(user);

            LoginTicket ticket=new LoginTicket();
            ticket.setStatus(0);
            ticket.setUserId(i+1);
            ticket.setExpired(date);
            ticket.setTicket(String.format("TICKET%d",i+1));
            loginTicketDAO.addTicket(ticket);

            loginTicketDAO.updateStatus(ticket.getTicket(),2);


        }
        Assert.assertEquals("newpassword",userDAO.selectById(1).getPassword());//代码测试 不相等则会报错
        userDAO.deleteById(1);
        Assert.assertNull(userDAO.selectById(1));//代码测试 不为空则会报错

        Assert.assertEquals(1,loginTicketDAO.selectByTicket("TICKET1").getUserId());
        Assert.assertEquals(2,loginTicketDAO.selectByTicket("TICKET1").getStatus());
        Assert.assertNotNull(commentDAO.selectByEntity(1,EntityType.ENTITY_NEWS).get(0));
    }

}
