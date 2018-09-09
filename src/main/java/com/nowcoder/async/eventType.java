package com.nowcoder.async;

/**
 * Created by 12274 on 2018/3/7.
 */
public enum eventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3),
    QUESTION(4),
    FOLLOW(5),
    UNFOLLOW(6),
    SOLR(7);
    private int value;
    eventType(int value){
        this.value=value;
    }
    public int getValue(){
        return value;
    }
}
