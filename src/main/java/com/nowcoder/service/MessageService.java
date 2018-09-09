package com.nowcoder.service;

import com.nowcoder.dao.MessageDAO;
import com.nowcoder.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by 12274 on 2018/3/8.
 */
@Service
public class MessageService {
    private static Logger logger= LoggerFactory.getLogger(MessageService.class);
    @Autowired
    MessageDAO messageDAO;

    public int addMessage(Message message){
        return messageDAO.addMessage(message);
    }
    public List<Message> getMessageList(int userId,int offset,int limit){
        return messageDAO.selectByUser(userId,offset,limit);
    }
    public List<Message> getConversationById(String conversationId,int offset,int limit){
        return messageDAO.selectByConversationId(conversationId,offset,limit);
    }
    public int getUnreadCount(int userId,String conversationId){
        return messageDAO.getUnreadCount(userId,conversationId);
    }
    public int getConversationCount(String conversationId){
        return messageDAO.getConversationCount(conversationId);
    }
}
