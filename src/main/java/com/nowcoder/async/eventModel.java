package com.nowcoder.async;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 12274 on 2018/3/7.
 */
public class eventModel {
    private eventType type;
    private int actorId;
    private int entityType;
    private int entityId;
    private int entityOwnerId;
    Map<String,String> map=new HashMap<String,String>();

    public eventModel(){

    }
    public eventModel(eventType type){
        this.type=type;
    }

    public eventType getType() {
        return type;
    }

    public void setType(eventType type) {
        this.type = type;
    }

    public int getActorId() {
        return actorId;
    }

    public eventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public eventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public eventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public eventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String,String> getMap(){
        return map;
    }
    public eventModel setMap(Map<String,String> map){
        this.map=map;
        return this;
    }
    public String getMapValue(String key){
        return this.map.get(key);
    }

    public eventModel setValue(String key,String value){
        this.map.put(key,value);
        return this;
    }

}
