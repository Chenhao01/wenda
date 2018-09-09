package com.nowcoder.controller;

import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Message;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12274 on 2018/3/8.
 */
@Controller
public class MessageController {
    private static Logger logger= LoggerFactory.getLogger(MessageService.class);
    @Autowired
    MessageService messageService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;

    @RequestMapping(path="/msg/list")
    public String msgList(Model model){
        List<ViewObject> vos=new ArrayList<>();
        List<Message> messages=messageService.getMessageList(hostHolder.getUser().getId(),0,10);
        for(Message message:messages){
            ViewObject vo=new ViewObject();
            int userId=message.getFromId()==hostHolder.getUser().getId()?message.getToId():message.getFromId();
            int unreadCount=messageService.getUnreadCount(hostHolder.getUser().getId(),message.getConversationId());
            int conversationCount=messageService.getConversationCount(message.getConversationId());
            vo.set("message",message);
            vo.set("user",userService.getUser(userId));
            vo.set("unread",unreadCount);
            vo.set("conversationCount",conversationCount);
            vos.add(vo);
        }
        model.addAttribute("conversations",vos);
        return "letter";
    }

    @RequestMapping(path="/msg/detail")
    public String detail(@RequestParam("conversationId")String conversationId,Model model){
        List<ViewObject> vos=new ArrayList<>();
        List<Message> messages=messageService.getConversationById(conversationId,0,10);
        for(Message message:messages){
            ViewObject vo=new ViewObject();
            vo.set("message",message);
            vo.set("user",userService.getUser(message.getFromId()));
            vos.add(vo);
        }
        model.addAttribute("messages",vos);
        return "letterDetail";
    }
}
