package com.nowcoder.service;

import com.nowcoder.Util.util;
import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.rmi.CORBA.Util;
import javax.xml.crypto.Data;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by 12274 on 2018/1/15.
 */
@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    LoginTicketDAO loginTicketDAO;

    public User getUser(int id ){
        return userDAO.selectById(id);
    }

    public User getUserByName(String name){
        return userDAO.selectByName(name);
    }

    public int addUser(User user){
        return userDAO.addUser(user);
    }

    public void updatePassword(User user){
        userDAO.updatePassword(user);
    }

    public void deleteUser(int id){
        userDAO.deleteById(id);
    }

    public Map<String,String> reg(String username, String password){
        Map<String,String> map=new HashMap<String,String>();
        if(StringUtils.isBlank(username)){
            map.put("msg","用户名不能空！");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能空！");
            return map;
        }
        User user=userDAO.selectByName(username);
        if(user!=null){
            map.put("msg","用户名已存在！");
            return map;
        }
        user=new User();
        user.setName(username);
        String salt=UUID.randomUUID().toString().replaceAll("-","");
        user.setSalt(salt);
        user.setPassword(util.MD5(salt+password));
        user.setHeadUrl("headUrl");
        userDAO.addUser(user);
        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public Map<String,String> login(String username, String password){
        Map<String,String> map=new HashMap<String,String>();
        if(StringUtils.isBlank(username)){
            map.put("msg","用户名不能空！");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能空！");
            return map;
        }
        User user=userDAO.selectByName(username);
        if(user==null){
            map.put("msg","用户不存在！");
            return map;
        }
        if(!user.getPassword().equals(util.MD5(user.getSalt()+password))){
            map.put("msg","密码错误！");
            return map;
        }

        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public void logOut(String ticket){
        loginTicketDAO.updateStatus(ticket,1);
    }

    public String addLoginTicket(int userId){
        LoginTicket loginTicket=new LoginTicket();
        loginTicket.setUserId(userId);
        Date date=new Date();
        date.setTime(date.getTime()+1000*60*60);
        loginTicket.setExpired(date);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(loginTicket);
        return loginTicket.getTicket();
    }
}
