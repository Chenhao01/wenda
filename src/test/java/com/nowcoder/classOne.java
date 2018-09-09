package com.nowcoder;

import com.nowcoder.Util.JedisAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by 12274 on 2018/1/15.
 */
public class classOne {
    public static void print(int i,Object obj){
        System.out.println(String.format("%d : %s",i,obj));
    }
    public  static void main(String[] args){
           /* String i="dasdasda";
            print(1,i);*/
           //JedisPool jedisPool=new JedisPool("127.0.0.1",6379);
        //Jedis jedis=jedisPool.getResource();
        try{
            JedisAdapter jedisAdapter=new JedisAdapter();
            jedisAdapter.lpush("list","222");
            //jedis.lpush("list","111");
        }catch (Exception e){
            System.out.println("错误");
        }
    }
}
