package com.nowcoder.service;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.testUserDao;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import com.nowcoder.model.testUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by 12274 on 2018/9/3.
 */
@Service
public class testUserService {
    @Autowired
    testUserDao testUserDao;
    @Autowired
    LoginTicketDAO loginTicketDAO;

    public int addUser(testUser user){
        return testUserDao.addUser(user);
    }
    public List<testUser> getAllUser(){
        return testUserDao.selectAllUser();
    }
    public testUser getUserById(int id){
        return testUserDao.selectUserById(id);
    }

    public Map<String,String> reg(String username,String password){
        Map<String,String> map=new HashMap<String,String>();
        if(StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }
        testUser user=testUserDao.selectByName(username);
        if(user!=null){
            map.put("msg","用户名已存在");
            return map;
        }
        user=new testUser();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,6));
        user.setPassword(password);
        user.setHeadUrl("headUrl");
        testUserDao.addUser(user);
        String ticket=getTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public Map<String,String> login(String username,String password) {
        Map<String, String> map = new HashMap<String, String>();
        if(StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }
        testUser user=testUserDao.selectByName(username);
        if(user==null){
            map.put("msg","用户名不存在");
            return map;
        }
        if(!user.getPassword().equals(password)){
            map.put("msg","密码错误");
            return map;
        }
        String ticket=getTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public String getTicket(int userId){
        LoginTicket ticket=new LoginTicket();
        ticket.setUserId(userId);
        ticket.setStatus(1);
        Date date=new Date();
        date.setTime(date.getTime()+1000*60*60);
        ticket.setExpired(date);
        ticket.setTicket(UUID.randomUUID().toString().substring(0,6));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }
}
