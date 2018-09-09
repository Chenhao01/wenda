package com.nowcoder.async;

import com.alibaba.fastjson.JSON;
import com.nowcoder.Util.JedisAdapter;
import com.nowcoder.Util.JedisKeys;
import org.aspectj.lang.annotation.After;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 12274 on 2018/3/7.
 */
@Service
public class eventConsummer implements InitializingBean,ApplicationContextAware{
    private Logger logger= LoggerFactory.getLogger(eventConsummer.class);
    private static Map<eventType,List<eventHandler>> map=new HashMap<>();
    private ApplicationContext applicationContext;
    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String,eventHandler> handlers=applicationContext.getBeansOfType(eventHandler.class);
        for(Map.Entry<String,eventHandler> entry:handlers.entrySet()){
            List<eventType> typeList=entry.getValue().getSupportEventType();
            for(eventType type:typeList){
                if(!map.containsKey(type)){
                    map.put(type,new ArrayList<eventHandler>());
                }
                map.get(type).add(entry.getValue());
            }
        }

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    String eventKey= JedisKeys.getEventQueueKey();
                    List<String> events=jedisAdapter.brpop(0,eventKey);
                    for(String event:events){
                        if(event.equals(eventKey)){
                            continue;
                        }
                        eventModel model= JSON.parseObject(event,eventModel.class);
                        if(!map.containsKey(model.getType())){
                            logger.error("不存在该事件类型");
                        }
                        for(eventHandler handler:map.get(model.getType())){
                            handler.doHandler(model);
                        }
                    }
                }
            }
        });
        thread.start();
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
