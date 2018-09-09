package com.nowcoder.async.handler;

import com.nowcoder.async.eventConsummer;
import com.nowcoder.async.eventHandler;
import com.nowcoder.async.eventModel;
import com.nowcoder.async.eventType;
import com.nowcoder.model.Comment;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by 12274 on 2018/3/7.
 */
@Component
public class commentHandler implements eventHandler {
    @Autowired
    CommentService commentService;
    @Autowired
    QuestionService questionService;
    @Autowired
    HostHolder hostHolder;
    private Logger logger= LoggerFactory.getLogger(commentHandler.class);

    @Override
    public void doHandler(eventModel model) {
        try{
            Comment comment = new Comment();
            comment.setContent(model.getMapValue("content"));
            comment.setUserId(model.getActorId());
            comment.setCreatedDate(new Date());
            comment.setEntityType(model.getEntityType());
            comment.setEntityId(model.getEntityId());
            int count=commentService.getCommentCount(comment.getEntityId(),comment.getEntityType())+1;
            questionService.updateCommentCount(comment.getEntityId(),count);
            commentService.addComment(comment);
        }catch (Exception e){
            logger.error("handler异常"+e.getMessage());
        }
    }

    @Override
    public List<eventType> getSupportEventType() {
        return Arrays.asList(eventType.COMMENT);
    }
}
