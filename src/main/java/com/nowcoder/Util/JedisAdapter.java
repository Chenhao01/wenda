package com.nowcoder.Util;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by 12274 on 2018/3/7.
 */
@Component
public class JedisAdapter implements InitializingBean{
    private static Logger logger= LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool jedisPool=null;

    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool=new JedisPool("127.0.0.1",6379);
    }
    public Jedis getJedis(){
        return jedisPool.getResource();
    }
    public void set(String key,String value){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            jedis.set(key,value);
        }catch (Exception e){
            logger.error("getJedis错误"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public void setex(String key,int sec,String value){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            jedis.setex(key,sec,value);
        }catch (Exception e){
            logger.error("setex错误"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }
    public String get(String key){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            return jedis.get(key);
        }catch (Exception e){
            logger.error("get错误"+e.getMessage());
            return null;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }
    public long lpush(String key,String value){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            return jedis.lpush(key,value);
        }catch (Exception e){
            logger.error("lpush错误"+e.getMessage());
            return 0;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }
    public List<String> brpop(int timeout,String key){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            return jedis.brpop(timeout,key);
        }catch (Exception e){
            logger.error("brpop错误"+e.getMessage());
            return null;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }
    public long sadd(String key,String mumber){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            return jedis.sadd(key,mumber);
        }catch (Exception e){
            logger.error("sadd错误"+e.getMessage());
            return 0;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }
    public void srem(String key,String mumber){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            jedis.srem(key,mumber);
        }catch (Exception e){
            logger.error("srem错误"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }
    public long scard(String key){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("scard错误"+e.getMessage());
            return 0;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }
    public boolean ismember(String key,String member){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            return jedis.sismember(key,member);
        }catch (Exception e){
            logger.error("ismember错误"+e.getMessage());
            return false;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public Transaction multi(Jedis jedis){
        try{
            return jedis.multi();
        }catch (Exception e){
            logger.error("multi错误"+e.getMessage());
            return null;
        }
    }
    public List<Object> exc(Transaction tx,Jedis jedis){
        try{
            return tx.exec();
        }catch (Exception e ){
            logger.error("exc错误"+e.getMessage());
            tx.discard();
            return null;
        }finally{
            if(tx!=null){
                try{
                    tx.close();
                }catch (IOException ioe){
                    logger.error("错误"+ioe.getMessage());
                }
            }
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public long zadd(String key,Double score,String member){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            return jedis.zadd(key,score,member);
        }catch (Exception e){
            logger.error("zadd错误"+e.getMessage());
            return 0;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }
    public List<Integer> setToList(Set<String> set){
        List<Integer> list=new ArrayList<>();
        for(String s:set){
            list.add(Integer.parseInt(s));
        }
        return list;
    }
    public List<Integer> zrange(String key,long start,long end){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            return setToList(jedis.zrange(key,start,end));
        }catch (Exception e){
            logger.error("zrange错误"+e.getMessage());
            return null;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public List<Integer> zrevrange(String key,long start,long end){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            return setToList(jedis.zrevrange(key,start,end));
        }catch (Exception e){
            logger.error("zrevrange错误"+e.getMessage());
            return null;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }
    public long zcard(String key){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            return jedis.zcard(key);
        }catch (Exception e){
            logger.error("zcard错误"+e.getMessage());
            return 0;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }
    public double zscore(String key,String member){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            return jedis.zscore(key,member)==null?0:jedis.zscore(key,member);
        }catch (Exception e){
            logger.error("zscore错误"+e.getMessage()+e.toString());
            return 0;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }
}
