package com.nowcoder.controller;

import com.nowcoder.Util.util;
import com.nowcoder.async.eventModel;
import com.nowcoder.async.eventProducer;
import com.nowcoder.async.eventType;
import com.nowcoder.model.*;
import com.nowcoder.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 12274 on 2018/1/15.
 */
@Controller
public class QuestionController {
    private Logger logger=LoggerFactory.getLogger(QuestionController.class);
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;
    @Autowired
    eventProducer eventProducer;
    @Autowired
    LikeService likeService;
    @Autowired
    FollowService followService;


    @RequestMapping(path={"/question/add"},method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@RequestParam("title")String title,
                              @RequestParam("content")String content){
        try{
            Question question=new Question();
            question.setTitle(title);
            question.setContent(content);
            question.setCreatedDate(new Date());
            question.setUserId(hostHolder.getUser().getId());
            questionService.addQuestion(question);
            boolean fireOk=eventProducer.fireEvent(new eventModel(eventType.QUESTION)
                                                    .setActorId(hostHolder.getUser().getId())
                                                    .setValue("title",title).setValue("content",content)
                                                    .setEntityId(question.getId()));
            if(fireOk){
                return util.getJSONString(0,"提问成功");
            }else {
                return util.getJSONString(1,"提问失败");
            }
        }catch (Exception e){
            logger.error("提问异常"+e.getMessage());
            return util.getJSONString(1,"提问失败");
        }
    }

    @RequestMapping(path={"/question/{qid}"},method = RequestMethod.GET)
    public String detail(Model model,
                         @PathVariable("qid")int qid){
            List<Comment> comments=commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
            List<ViewObject> vos=new ArrayList<ViewObject>();
            for(Comment comment : comments){
                ViewObject vo=new ViewObject();
                int liked=likeService.getLikeStatus(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT,comment.getId());
                long likeCount=likeService.getLikeCount(EntityType.ENTITY_COMMENT,comment.getId());
                vo.set("comment",comment);
                vo.set("user",userService.getUser(comment.getUserId()));
                vo.set("liked",liked);
                vo.set("likeCount",likeCount);
                vos.add(vo);
            }
            Question question=questionService.selectById(qid);
            model.addAttribute("question",question);
            List<User> followUsers=new ArrayList<>();
            for(int id:followService.getFollowers(EntityType.ENTITY_QUESTION,question.getId(),0,10)){
                followUsers.add(userService.getUser(id));
            }
            model.addAttribute("followUsers",followUsers);
            model.addAttribute("followed",followService.isFollower(EntityType.ENTITY_QUESTION,question.getId(),hostHolder.getUser().getId()));
            model.addAttribute("user",userService.getUser(question.getUserId()));
            model.addAttribute("comments",vos);
            return "detail";
    }
}
