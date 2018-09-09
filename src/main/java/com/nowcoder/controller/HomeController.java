package com.nowcoder.controller;

import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Question;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.FollowService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12274 on 2018/1/15.
 */
@Controller
public class HomeController {
    private Logger logger= LoggerFactory.getLogger(HomeController.class);

    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    FollowService followService;
    @Autowired
    CommentService commentService;

    @RequestMapping(path = {"/","/home"},method = RequestMethod.GET)
    public String home(Model model,
                       @RequestParam(value = "userId",defaultValue = "0")int userId){
        model.addAttribute("vos",listQuestion(userId,0,10));
        logger.info("打开首页");
        return "index";
    }

    @RequestMapping(path = {"/user/{userId}"},method = RequestMethod.GET)
    public String userPage(Model model,
                           @PathVariable("userId")int userId){
        model.addAttribute("vos",listQuestion(userId,0,10));
        ViewObject vo=new ViewObject();
        vo.set("user",userService.getUser(userId));
        vo.set("followerCount",followService.getFollowerCount(EntityType.ENTITY_USER,userId));
        vo.set("commentCount",commentService.getCommentCount(EntityType.ENTITY_USER,userId));
        vo.set("followeeCount",followService.getFolloweeCount(userId,EntityType.ENTITY_USER));
        vo.set("followed",followService.isFollower(EntityType.ENTITY_USER,userId,hostHolder.getUser().getId()));
        model.addAttribute("profileUser",vo);
        return "profile";
    }

    public List<ViewObject> listQuestion(int userId,int offset,int limit){
        List<Question> questions=questionService.listlatestQuestion(userId,offset,limit);
        List<ViewObject> vos=new ArrayList<ViewObject>();
        for(Question question:questions){
            ViewObject vo=new ViewObject();
            vo.set("question",question);
            vo.set("followCount",followService.getFollowerCount(EntityType.ENTITY_QUESTION,question.getId()));
            vo.set("user",userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }
}
