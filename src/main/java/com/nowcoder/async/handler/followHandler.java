package com.nowcoder.async.handler;

import com.nowcoder.Util.JedisAdapter;
import com.nowcoder.Util.util;
import com.nowcoder.async.eventHandler;
import com.nowcoder.async.eventModel;
import com.nowcoder.async.eventType;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by 12274 on 2018/3/13.
 */
@Component
public class followHandler implements eventHandler {
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void doHandler(eventModel model) {
        Message message=new Message();
        message.setFromId(util.getSystemUserid());
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        message.setConversationId();
        message.setHasRead(0);
        User user=userService.getUser(model.getActorId());
        message.setContent("用户" + user.getName()
                + "关注了你" );

        messageService.addMessage(message);
    }

    @Override
    public List<eventType> getSupportEventType() {
        return Arrays.asList(eventType.FOLLOW);
    }
}
