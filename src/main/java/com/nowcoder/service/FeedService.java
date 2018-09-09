package com.nowcoder.service;

import com.nowcoder.dao.FeedDAO;
import com.nowcoder.model.Feed;
import com.nowcoder.model.User;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 12274 on 2018/4/27.
 */
@Service
public class FeedService {
    @Autowired
    FeedDAO feedDAO;
     public Feed getFeed(int id){
        return feedDAO.getFeed(id);
     }
     public List<Feed> getFeeds(int maxId, List<Integer> users,int count){
         return feedDAO.selectUserFeeds(maxId,users,count);
     }
     public int ddFeed(Feed feed){
         return feedDAO.addFeed(feed);
     }
     public List<Feed> getFeedByUserId(int userId){
         return feedDAO.getFeedByUserId(userId);
     }
}
