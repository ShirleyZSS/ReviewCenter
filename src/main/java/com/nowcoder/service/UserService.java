package com.nowcoder.service;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import com.nowcoder.util.ToutiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Shirley on 2017/7/13.
 */
@Service
public class UserService {
    private static final org.slf4j.Logger logger= LoggerFactory.getLogger(UserService.class);
    @Autowired
    UserDAO userDAO;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    public Map<String,Object> register(String username, String password){
        Map<String,Object> map=new HashMap<String,Object>();
        if(StringUtils.isBlank(username)){//借助包装好的对象 判断用户名不能为空
            map.put("msgname","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){//借助包装好的对象 判断密码是否为空
            map.put("msgpwd","密码不能为空");
            return map;
        }

        User user=userDAO.selectByName(username);
        if(user!=null){
            map.put("msgname","用户名真的已经被注册了");
            return map;

        }
        //密码强度
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));//截取随机生成的前5个字符
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);

        //登录
        String ticket =addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;

    }

    public Map<String,Object> login(String username, String password){
        Map<String,Object> map=new HashMap<String,Object>();
        if(StringUtils.isBlank(username)){//借助包装好的对象 判断用户名不能为空
            map.put("msgname","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){//借助包装好的对象 判断密码是否为空
            map.put("msgpwd","密码不能为空");
            return map;
        }


        User user=userDAO.selectByName(username);
        if(user==null){
            map.put("msgname","用户名不存在");
            return map;

        }

        if(!ToutiaoUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msgpwd","密码不正确");
            return map;
        }

        String ticket= addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;

    }

    private  String addLoginTicket(int userId){//通过userId添加ticket
        LoginTicket ticket=new LoginTicket();
        ticket.setUserId(userId);
        Date date=new Date();
        date.setTime(date.getTime()+1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();


    }
    public User getUser(int id){
       return  userDAO.selectById(id);

    }

    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);
    }

}
