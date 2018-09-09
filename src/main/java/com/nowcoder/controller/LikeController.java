package com.nowcoder.controller;

import com.nowcoder.Util.util;
import com.nowcoder.async.eventModel;
import com.nowcoder.async.eventProducer;
import com.nowcoder.async.eventType;
import com.nowcoder.model.Comment;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.LikeService;
import org.aspectj.lang.annotation.After;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 12274 on 2018/3/9.
 */
@Controller
public class LikeController {
    private static Logger logger= LoggerFactory.getLogger(LikeController.class);
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;
    @Autowired
    eventProducer eventProducer;
    @Autowired
    LikeService likeService;

    @RequestMapping(path="/like",method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId")int commentId){
        if(hostHolder.getUser()==null){
            return util.getJSONString(999);
        }
        Comment comment=commentService.getCommentById(commentId);
        eventProducer.fireEvent(new eventModel(eventType.LIKE)
                                    .setEntityType(EntityType.ENTITY_COMMENT)
                                    .setEntityId(commentId)
                                    .setActorId(hostHolder.getUser().getId())
                                    .setEntityOwnerId(comment.getUserId())
                                    .setValue("questionId",String.valueOf(comment.getEntityId())));
        long likeCount=likeService.like(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT,commentId);
        logger.info("执行了");
        return util.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(path="/dislike",method = {RequestMethod.POST})
    @ResponseBody
    public String disLike(@RequestParam("commentId")int commentId){
        if(hostHolder.getUser()==null){
            return util.getJSONString(999);
        }
        long dislikeCount=likeService.disLike(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT,commentId);
        return util.getJSONString(0,String.valueOf(dislikeCount));
    }
}
