package com.nowcoder.controller;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.service.testUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 12274 on 2018/9/3.
 */
@Controller
public class testLoginController {
    @Autowired
    testUserService userService;
    @Autowired
    LoginTicketDAO loginTicketDAO;

    @RequestMapping(path="/reg",method = RequestMethod.POST)
    public String reg(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam("rem") int rem,
                      @RequestParam("next") String next,
                      HttpServletResponse response,
                      Model model){
        Map<String,String> map=userService.reg(username,password);
        if(map.containsKey("ticket")){
            Cookie cookie=new Cookie("ticket",map.get("ticket"));
            cookie.setPath("/");
            if(rem>1){
                cookie.setMaxAge(-1);
            }
            response.addCookie(cookie);
            return "redirect:"+next;
        }else{
            model.addAttribute("msg",map.get("msg"));
            return "reg";
        }
    }

    @RequestMapping(path=("/login"),method = RequestMethod.POST)
    public String login(@RequestParam("username")String username,
                        @RequestParam("password")String password,
                        @RequestParam("rem") int rem,
                        @RequestParam("next") String next,
                        HttpServletResponse response,
                        Model model){
        Map<String,String> map=userService.login(username,password);
        if(map.containsKey("ticket")){
            Cookie cookie=new Cookie("ticket",map.get("ticket"));
            cookie.setPath("/");
            if(rem>1){
                cookie.setMaxAge(-1);
            }
            response.addCookie(cookie);
            return "redirect:"+next;
        }else{
            model.addAttribute("msg",map.get("msg"));
            return "login";
        }
    }

    @RequestMapping(path="/logout")
    public String logout(@CookieValue("ticket")String ticket){
        LoginTicket loginTicket=loginTicketDAO.selectByTicket(ticket);
        loginTicket.setStatus(0);
        return "/home";
    }
}
