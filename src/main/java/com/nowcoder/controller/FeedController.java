package com.nowcoder.controller;

import com.nowcoder.Util.JedisAdapter;
import com.nowcoder.Util.JedisKeys;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.Feed;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.User;
import com.nowcoder.service.FeedService;
import com.nowcoder.service.FollowService;
import com.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12274 on 2018/4/28.
 */
@Controller
public class FeedController {
    @Autowired
    FeedService feedService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    FollowService followService;
    @Autowired
    JedisAdapter jedisAdapter;

    @RequestMapping(path="/pullFeed",method = RequestMethod.GET)
    public String pullFeed(Model model){
        User localUser= hostHolder.getUser()==null ? userService.getUser(17) : hostHolder.getUser();
        List<Integer> followees=new ArrayList<>();
        if(localUser.getId()!=17){
            followees=followService.getFollowees(EntityType.ENTITY_USER,localUser.getId(),Integer.MAX_VALUE);
        }
        List<Feed> feeds=feedService.getFeeds(Integer.MAX_VALUE,followees,10);
        model.addAttribute("feeds",feeds);
        return "feeds";
    }

    @RequestMapping(path="/pushFeed",method = RequestMethod.GET)
    public String pushFeed(Model model){
        User localUser= hostHolder.getUser()==null ? userService.getUser(17) : hostHolder.getUser();
        List<Feed> feeds=new ArrayList<>();
        Jedis jedis=jedisAdapter.getJedis();
        List<String> feedIds=jedis.lrange(JedisKeys.getTimeLineKey(localUser.getId()),0,10);
        for(String id:feedIds){
            feeds.add(feedService.getFeed(Integer.valueOf(id).intValue()));
        }
        model.addAttribute("feeds",feeds);
        jedis.close();
        return "feeds";
    }
}
