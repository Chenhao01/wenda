package com.nowcoder.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.Util.JedisAdapter;
import com.nowcoder.Util.JedisKeys;
import com.nowcoder.async.eventHandler;
import com.nowcoder.async.eventModel;
import com.nowcoder.async.eventType;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.Feed;
import com.nowcoder.model.Question;
import com.nowcoder.model.User;
import com.nowcoder.service.FeedService;
import com.nowcoder.service.FollowService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by 12274 on 2018/3/8.
 */
@Component
public class FeedHandler implements eventHandler {
    @Autowired
    QuestionService questionService;
    @Autowired
    FeedService feedService;
    @Autowired
    UserService userService;
    @Autowired
    FollowService followService;
    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void doHandler(eventModel model) {
        Feed feed=new Feed();
        feed.setCreatedDate(new Date());
        feed.setUserId(model.getActorId());
        feed.setType(model.getType().getValue());
        feed.setData(buildData(model));
        feedService.ddFeed(feed);

        //推
        List<Integer> followers=followService.getFollowers(EntityType.ENTITY_USER,model.getActorId(),Integer.MAX_VALUE);
        for(Integer userId : followers){
            String timeLineKey= JedisKeys.getTimeLineKey(userId);
            jedisAdapter.lpush(timeLineKey,String.valueOf(feed.getId()));
        }
    }
    public String buildData(eventModel model){
        Map<String,String> map=new HashMap<String,String>();
        User actor=userService.getUser(model.getActorId());
        Question question=questionService.selectById(model.getEntityId());
        if(actor==null || question==null){
            return null;
        }
        map.put("userId",String.valueOf(actor.getId()));
        map.put("userHead",actor.getHeadUrl());
        map.put("userName",actor.getName());
        if(model.getEntityType()== EntityType.ENTITY_QUESTION){
            map.put("questionTitle",question.getTitle());
            map.put("questionUserId",String.valueOf(question.getUserId()));
            return JSONObject.toJSONString(map);
        }
        return null;
    }
    @Override
    public List<eventType> getSupportEventType() {
        return Arrays.asList(new eventType[]{eventType.COMMENT,eventType.FOLLOW});
    }
}
