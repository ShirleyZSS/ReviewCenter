package com.nowcoder.controller;

import com.nowcoder.dao.MessageDAO;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Shirley on 2017/7/20.
 */
@Controller
public class MessageController {
    private static final Logger logger= LoggerFactory.getLogger(MessageController.class);
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @RequestMapping(path={"/msg/detail"},method = {RequestMethod.GET})
    public String conversationDetail(Model model, @RequestParam("conversationId")String conversationId){
        try {
            List<Message> conversationList = messageService.getConversationDetail(conversationId, 0, 10);
            List<ViewObject> messages=new ArrayList<ViewObject>();
        for(Message msg:conversationList){
            ViewObject vo=new ViewObject();
            vo.set("message",msg);
            User user=userService.getUser(msg.getFromId());
            if(user==null){
                continue;
            }
            vo.set("headUrl",user.getHeadUrl());
            vo.set("userName",user.getName());
            messages.add(vo);
        }
        model.addAttribute("messages",messages);
            return "letterDetail";
        }catch(Exception e){
            logger.error("获取详情消息失败"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"获取详情消息失败");
        }

    }

    @RequestMapping(path={"/msg/addMessage"},method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,@RequestParam("toId")int toId,
                             @RequestParam("content") String content) {
        try {
            Message msg = new Message();
            msg.setFromId(fromId);
            msg.setToId(toId);
            msg.setContent(content);
            msg.setCreatedDate(new Date());
            msg.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
            messageService.addMessage(msg);
            return ToutiaoUtil.getJSONString(msg.getId());
        } catch (Exception e) {
            logger.error("添加会话失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1,"添加会话失败");
        }
    }

}
