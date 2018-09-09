package com.nowcoder.async.handler;

/**
 * Created by 12274 on 2018/7/13.
 */
public enum testEntityType {
    like(0),
    dislike(1);
    private int id;
    testEntityType(int id){
        this.id=id;
    }
    public int getId(){return id;}
}
