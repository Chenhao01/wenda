package com.nowcoder.async;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nowcoder.Util.JedisAdapter;
import com.nowcoder.Util.JedisKeys;
import javafx.fxml.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.net.URL;
import java.util.*;

/**
 * Created by 12274 on 2018/5/8.
 */
@Service
public class consumer implements InitializingBean,ApplicationContextAware{

    private ApplicationContext applicationContext;
    private Map<eventType,List<eventHandler>> map=new HashMap<>();
    private String QueueKey=JedisKeys.getEventQueueKey();
    private Logger logger =LoggerFactory.getLogger(consumer.class);
    @Autowired
    JedisAdapter jedisAdapter;
    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String,eventHandler> handlers=applicationContext.getBeansOfType(eventHandler.class);
        for(Map.Entry<String,eventHandler> entry : handlers.entrySet()){
            List<eventType> types=entry.getValue().getSupportEventType();
            for(eventType type : types){
                if(!map.containsKey(type)){
                    map.put(type,new ArrayList<eventHandler>());
                }
                map.get(type).add(entry.getValue());
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    List<String> models=jedisAdapter.brpop(0,QueueKey);
                    for(String model : models){
                        if(model.equals(QueueKey)){
                            continue;
                        }
                        eventModel modelObject=JSON.parseObject(model,eventModel.class);
                        for(eventHandler handler : map.get(modelObject.getType())){
                            handler.doHandler(modelObject);
                        }
                    }
                }catch(Exception e){
                    logger.error("错误"+e.getMessage());
                }
            }
        }).start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
