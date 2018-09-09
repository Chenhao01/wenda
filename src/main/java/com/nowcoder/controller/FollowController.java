package com.nowcoder.controller;

import com.nowcoder.Util.JedisAdapter;
import com.nowcoder.Util.JedisKeys;
import com.nowcoder.Util.util;
import com.nowcoder.async.eventModel;
import com.nowcoder.async.eventProducer;
import com.nowcoder.async.eventType;
import com.nowcoder.model.*;
import com.nowcoder.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 12274 on 2018/3/13.
 */
@Controller
public class FollowController {
    @Autowired
    HostHolder hostHolder;
    @Autowired
    FollowService followService;
    @Autowired
    eventProducer eventProducer;
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    JedisAdapter jedisAdapter;
    @Autowired
    FeedService feedService;

    @RequestMapping(path="/followUser",method = RequestMethod.POST)
    @ResponseBody
    public String followUser(@RequestParam("userId")int userId){
        if(hostHolder.getUser()==null){
            return util.getJSONString(999);
        }
        boolean re=followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER,userId);
        eventProducer.fireEvent(new eventModel(eventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityOwnerId(userId)
                .setEntityType(EntityType.ENTITY_USER)
                .setEntityId(userId));
        return util.getJSONString(re?0:1,String.valueOf(followService.getFollowerCount( EntityType.ENTITY_USER,userId)));
    }
    @RequestMapping(path="/unfollowUser",method = RequestMethod.POST)
    @ResponseBody
    public String unfollowUser(@RequestParam("userId")int userId){
        if(hostHolder.getUser()==null){
            return util.getJSONString(999);
        }
        boolean re=followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER,userId);
        eventProducer.fireEvent(new eventModel(eventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityOwnerId(userId)
                .setEntityType(EntityType.ENTITY_USER)
                .setEntityId(userId));

        String timeLineKey= JedisKeys.getTimeLineKey(hostHolder.getUser().getId());
        Jedis jedis=jedisAdapter.getJedis();
        List<Feed> feeds=feedService.getFeedByUserId(userId);
        for(Feed feed:feeds){
            jedis.lrem(timeLineKey,1,String.valueOf(feed.getId()));
        }
        return util.getJSONString(re?0:1,String.valueOf(followService.getFollowerCount( EntityType.ENTITY_USER,userId)));
    }

    @RequestMapping(path="/followQuestion",method = RequestMethod.POST)
    @ResponseBody
    public String followQuestion(@RequestParam("questionId")int questionId){
        if(hostHolder.getUser()==null){
            return util.getJSONString(999);
        }
        Question question=questionService.selectById(questionId);
        if(question==null){
            return util.getJSONString(1,"问题不存在");
        }
        boolean re=followService.follow(hostHolder.getUser().getId(),EntityType.ENTITY_QUESTION,questionId);
        eventProducer.fireEvent(new eventModel(eventType.FOLLOW)
        .setEntityId(questionId).setEntityType(EntityType.ENTITY_QUESTION)
        .setEntityOwnerId(question.getUserId()).setActorId(hostHolder.getUser().getId()));
        Map<String,Object> info=new HashMap<>();
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", String.valueOf(hostHolder.getUser().getId()));
        info.put("count", String.valueOf(followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId)));
        return util.getJSONString(re ? 0 : 1,info);
    }
    @RequestMapping(path = {"/unfollowQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId) {
        if (hostHolder.getUser() == null) {
            return util.getJSONString(999);
        }

        Question q = questionService.selectById(questionId);
        if (q == null) {
            return util.getJSONString(1, "问题不存在");
        }

        boolean re = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);
        Map<String, Object> info = new HashMap<>();
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));
        return util.getJSONString(re ? 0 : 1,info);
    }
    @RequestMapping(path = {"/user/{uid}/followers"}, method = {RequestMethod.GET})
    public String followers(Model model, @PathVariable("uid") int userId) {
        List<Integer> followerIds = followService.getFollowers(EntityType.ENTITY_USER, userId, 0, 10);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getId(), followerIds));
        } else {
            model.addAttribute("followers", getUsersInfo(0, followerIds));
        }
        model.addAttribute("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followers";
    }

    @RequestMapping(path = {"/user/{uid}/followees"}, method = {RequestMethod.GET})
    public String followees(Model model, @PathVariable("uid") int userId) {
        List<Integer> followeeIds = followService.getFollowees(EntityType.ENTITY_USER,userId , 0, 10);

        if (hostHolder.getUser() != null) {
            model.addAttribute("followees", getUsersInfo(hostHolder.getUser().getId(), followeeIds));
        } else {
            model.addAttribute("followees", getUsersInfo(0, followeeIds));
        }
        model.addAttribute("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followees";
    }
    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
        List<ViewObject> userInfos = new ArrayList<ViewObject>();
        for (Integer uid : userIds) {
            User user = userService.getUser(uid);
            if (user == null) {
                continue;
            }
            ViewObject vo = new ViewObject();
            vo.set("user", user);
            vo.set("commentCount", commentService.getUserCommentCount(uid));
            vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, uid));
            vo.set("followeeCount", followService.getFolloweeCount(uid, EntityType.ENTITY_USER));
            if (localUserId != 0) {
                vo.set("followed", followService.isFollower(EntityType.ENTITY_USER,uid,localUserId));
            } else {
                vo.set("followed", false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }
}
