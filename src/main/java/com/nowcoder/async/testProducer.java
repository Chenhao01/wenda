package com.nowcoder.async;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nowcoder.Util.JedisAdapter;
import com.nowcoder.Util.JedisKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 12274 on 2018/7/13.
 */
@Service
public class testProducer {
    @Autowired
    JedisAdapter jedisAdapter;
    private Logger logger= LoggerFactory.getLogger(testProducer.class);
    private String QueueKey= JedisKeys.getEventQueueKey();
    public boolean fireEvent(eventModel model){
        try{
            String jsonString=JSON.toJSONString(model);
            jedisAdapter.lpush(QueueKey,jsonString);
            return true;
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
    }
}
