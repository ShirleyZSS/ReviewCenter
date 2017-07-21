package com.nowcoder.service;

import com.nowcoder.dao.MessageDAO;
import com.nowcoder.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Shirley on 2017/7/20.
 */
@Service
public class MessageService {
    @Autowired
    private MessageDAO messageDAO;


    public int addMessage(Message message){
        return messageDAO.addMessage(message);
    }

   /*
    public List<Message> getConversationList(int userId,int offset,int limit){
        return messageDAO.get
    }
    */
//获取两人对话的所有内容 分页显示
   public List<Message> getConversationDetail(String conversationId,int offset,int limit){
       return messageDAO.getConversationDetail(conversationId,offset,limit);

   }

}
