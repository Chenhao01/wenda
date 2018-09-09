package com.nowcoder.Util;

/**
 * Created by 12274 on 2018/3/7.
 */
public class JedisKeys {
    private static String eventKey="EVENT";
    private static String likeKey="LIKE_KEY";
    private static String disLikeKey="DISLIKE_KEY";
    private static String split=":";
    private static String follower="FOLLOWER";
    private static String followee="FOLLOWEE";
    private static String timeLine="TIMELINE";

    public static String getLikeKey(int entityType,int entityId){
        return likeKey+split+entityType+split+entityId;
    }
    public static String getDisLikeKey(int entityType,int entityId){
        return disLikeKey+split+entityType+split+entityId;
    }
    public static String getFollowerKey(int entityType,int entityId){
        return follower+split+entityType+split+entityId;
    }
    public static String getFolloweeKey(int userId,int entityType){
        return followee+split+userId+split+entityType;
    }
    public static String getTimeLineKey(int userId){
        return timeLine+split+userId;
    }
    public static String getEventQueueKey(){
        return eventKey;
    }

}
