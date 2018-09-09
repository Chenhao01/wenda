package com.nowcoder.async;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nowcoder.Util.JedisAdapter;
import com.nowcoder.Util.JedisKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

/**
 * Created by 12274 on 2018/3/7.
 */
@Service
public class eventProducer {
    @Autowired
    JedisAdapter jedisAdapter;
    private static Logger logger=LoggerFactory.getLogger(eventProducer.class);
    private String eventKey= JedisKeys.getEventQueueKey();

    public boolean fireEvent(eventModel model){
        try {
            String json = JSON.toJSONString(model);
            jedisAdapter.lpush(eventKey, json);
            return true;
        } catch (Exception e) {
            logger.error("加入异步队列异常"+e.getMessage());
            return false;
        }
    }

}
