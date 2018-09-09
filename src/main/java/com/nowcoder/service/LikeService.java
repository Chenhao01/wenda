package com.nowcoder.service;

import com.nowcoder.Util.JedisAdapter;
import com.nowcoder.Util.JedisKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 12274 on 2018/3/8.
 */
@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;


    public long like(int userId,int entityType,int entityId){
        String likeKey= JedisKeys.getLikeKey(entityType,entityId);
        String disLikeKey=JedisKeys.getDisLikeKey(entityType,entityId);
        if(!jedisAdapter.ismember(likeKey,String.valueOf(userId))){
            jedisAdapter.sadd(likeKey,String.valueOf(userId));
        }
        if(jedisAdapter.ismember(disLikeKey,String.valueOf(userId))){
            jedisAdapter.srem(disLikeKey,String.valueOf(userId));
        }
        return jedisAdapter.scard(likeKey);
    }
    public long disLike(int userId,int entityType,int entityId){
        String likeKey= JedisKeys.getLikeKey(entityType,entityId);
        String disLikeKey=JedisKeys.getDisLikeKey(entityType,entityId);
        if(!jedisAdapter.ismember(disLikeKey,String.valueOf(userId))){
            jedisAdapter.sadd(disLikeKey,String.valueOf(userId));
        }
        if(jedisAdapter.ismember(likeKey,String.valueOf(userId))){
            jedisAdapter.srem(likeKey,String.valueOf(userId));
        }
        return jedisAdapter.scard(likeKey);
    }
    public int getLikeStatus(int userId,int entityType,int entityId){
        String likeKey= JedisKeys.getLikeKey(entityType,entityId);
        String disLikeKey=JedisKeys.getDisLikeKey(entityType,entityId);
        if(jedisAdapter.ismember(likeKey,String.valueOf(userId))){
            return 1;
        }else if(jedisAdapter.ismember(disLikeKey,String.valueOf(userId))) {
            return -1;
        }else{
            return 0;
        }
    }
    public long getLikeCount(int entityType,int entityId){
        String likeKey= JedisKeys.getLikeKey(entityType,entityId);
        return jedisAdapter.scard(likeKey);
    }
}
