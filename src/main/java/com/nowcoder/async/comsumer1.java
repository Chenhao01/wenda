package com.nowcoder.async;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nowcoder.Util.JedisAdapter;
import com.nowcoder.Util.JedisKeys;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 12274 on 2018/9/7.
 */
@Service
public class comsumer1 implements InitializingBean,ApplicationContextAware {
    private ApplicationContext applicationContext;
    private Map<eventType,List<eventHandler>> map=new HashMap<>();
    private String QueueKey= JedisKeys.getEventQueueKey();

    @Autowired
    JedisAdapter jedisAdapter;
    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String,eventHandler> handlers=applicationContext.getBeansOfType(eventHandler.class);
        for(Map.Entry<String,eventHandler> entry : handlers.entrySet()){
            List<eventType> types=entry.getValue().getSupportEventType();
            for(eventType type:types){
                if(!map.containsKey(type)){
                    List<eventHandler> list=new ArrayList<>();
                    map.put(type,list);
                }
                map.get(type).add(entry.getValue());
            }
        }

        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    List<String> models = jedisAdapter.brpop(0, QueueKey);
                    for(String String_model:models){
                        if(String_model.equals(QueueKey)){
                            continue;
                        }
                        eventModel model= JSON.parseObject(String_model,eventModel.class);
                        for(eventHandler handler:map.get(model.getType())){
                            handler.doHandler(model);
                        }
                    }
                }
            }
        });
        t.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
