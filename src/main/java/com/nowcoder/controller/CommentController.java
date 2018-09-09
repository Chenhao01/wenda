package com.nowcoder.controller;

import com.nowcoder.Util.JedisAdapter;
import com.nowcoder.async.eventModel;
import com.nowcoder.async.eventProducer;
import com.nowcoder.async.eventType;
import com.nowcoder.model.Comment;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * Created by 12274 on 2018/3/7.
 */


@Controller
public class CommentController {
    private static Logger logger= LoggerFactory.getLogger(JedisAdapter.class);
    @Autowired
    CommentService commentService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    eventProducer eventProducer;
    @Autowired
    QuestionService questionService;


    @RequestMapping(path="/addComment",method = RequestMethod.POST)
    public String addComment(@RequestParam("questionId")int questionId,
                            @RequestParam("content")String content){
        try {
            int userId=hostHolder.getUser()==null?1:hostHolder.getUser().getId();
            eventProducer.fireEvent(new eventModel(eventType.COMMENT).setActorId(userId)
                    .setEntityId(questionId).setEntityType(EntityType.ENTITY_QUESTION)
                    .setEntityOwnerId(questionService.selectById(questionId).getUserId())
                    .setValue("content",content)
                   );
        } catch (Exception e) {
            logger.error("增加评论失败" + e.getMessage());
        }
        return "redirect:/question/" + questionId;
    }

}
