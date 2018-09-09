package com.nowcoder.controller;

import com.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by 12274 on 2018/5/15.
 */
@Controller
public class testlogin {
    @Autowired
    UserService userService;
    @RequestMapping(path="/login",method = RequestMethod.POST)
    @ResponseBody
    public String reg(@RequestParam("username")String username,
                      @RequestParam("password")String password,
                      @RequestParam("rememberme")int rememberme,
                      HttpServletResponse response){
        Map<String ,String> map=userService.reg(username,password);
        if(map.containsKey("ticket")){
            String ticket=map.get("ticket");
            Cookie cookie=new Cookie("ticket",ticket);
            cookie.setPath("/");
            if(rememberme>0){
                cookie.setMaxAge(1000*60*60);
            }
            response.addCookie(cookie);
        }
        return null;
    }
}
