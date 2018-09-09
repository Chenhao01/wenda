package com.nowcoder.service;

import com.nowcoder.Util.JedisAdapter;
import com.nowcoder.Util.JedisKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.Date;
import java.util.List;

/**
 * Created by 12274 on 2018/3/13.
 */
@Service
public class FollowService {
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean follow(int userId,int entityType,int entityId){
        String followerKey= JedisKeys.getFollowerKey(entityType,entityId);
        String followeeKey= JedisKeys.getFolloweeKey(userId,entityType);
        Jedis jedis=jedisAdapter.getJedis();
        Transaction tx=jedisAdapter.multi(jedis);
        Date date=new Date();
        tx.zadd(followerKey,date.getTime(),String.valueOf(userId));
        tx.zadd(followeeKey,date.getTime(),String.valueOf(entityId));
        List<Object> re=jedisAdapter.exc(tx,jedis);
        return re.size()>2 && (long)re.get(0)>0 && (long)re.get(1)>0;
    }
    public boolean unfollow(int userId,int entityType,int entityId){
        String followerKey= JedisKeys.getFollowerKey(entityType,entityId);
        String followeeKey= JedisKeys.getFolloweeKey(userId,entityType);
        Jedis jedis=jedisAdapter.getJedis();
        Transaction tx=jedisAdapter.multi(jedis);
        Date date=new Date();
        tx.zrem(followerKey,String.valueOf(userId));
        tx.zrem(followeeKey,String.valueOf(entityId));
        List<Object> re=jedisAdapter.exc(tx,jedis);
        return re.size()>2 && (long)re.get(0)>0 && (long)re.get(1)>0;
    }

    public List<Integer> getFollowers(int entityType,int entityId,long count){
        String followerKey= JedisKeys.getFollowerKey(entityType,entityId);
        return jedisAdapter.zrange(followerKey,0,count);
    }
    public List<Integer> getFollowees(int entityType,int userId,long count){
        String followeeKey= JedisKeys.getFolloweeKey(userId,entityType);
        return jedisAdapter.zrange(followeeKey,0,count);
    }

    public List<Integer> getFollowers(int entityType,int entityId,long offset,long count){
        String followerKey= JedisKeys.getFollowerKey(entityType,entityId);
        return jedisAdapter.zrange(followerKey,offset,count);
    }
    public List<Integer> getFollowees(int entityType,int userId,long offset,long count){
        String followeeKey= JedisKeys.getFolloweeKey(userId,entityType);
        return jedisAdapter.zrange(followeeKey,offset,count);
    }

    public long getFollowerCount(int entityType,int entityId){
        String followerKey= JedisKeys.getFollowerKey(entityType,entityId);
        return jedisAdapter.zcard(followerKey);
    }
    public long getFolloweeCount(int userId,int entityType){
        String followeeKey= JedisKeys.getFolloweeKey(userId,entityType);
        return jedisAdapter.zcard(followeeKey);
    }

    public boolean isFollower(int entityType,int entityId,int userId){
        String followerKey= JedisKeys.getFollowerKey(entityType,entityId);
        return jedisAdapter.zscore(followerKey,String.valueOf(userId))!=0;
    }
}
