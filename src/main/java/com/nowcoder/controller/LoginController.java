package com.nowcoder.controller;

import com.nowcoder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 12274 on 2018/1/15.
 */
@Controller
public class LoginController {
    private Logger logger= LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserService userService;

    @RequestMapping(path={"/reglogin"},method = RequestMethod.GET)
    public String regLogin(Model model,
                           @RequestParam(value="next",required = false,defaultValue = "/home")String next){
        model.addAttribute("next",next);
        return "login";
    }
    @RequestMapping(path={"/reg/"},method = {RequestMethod.POST,RequestMethod.GET})
    public String reg(@RequestParam("username")String username,
                      @RequestParam("password")String password,
                      @RequestParam(value = "rememberme",defaultValue = "false")String rememberme,
                      @RequestParam(value="next",required = false,defaultValue = "/home")String next,
                      HttpServletResponse response,
                      Model model){
        try{
            Map<String,String> map=userService.reg(username,password);
            if(map.containsKey("ticket")){
                String ticket=map.get("ticket");
                Cookie cookie=new Cookie("ticket",ticket);
                cookie.setPath("/");
                if(rememberme.equals("true")){
                    cookie.setMaxAge(1000*60*60*24);
                }else{
                    cookie.setMaxAge(-1);
                }
                response.addCookie(cookie);
                return "redirect:"+next;
            }else{
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }
        }catch (Exception e){
            logger.error("注册异常"+e.getMessage());
            return "login";
        }
    }
    @RequestMapping(path={"/login/"},method = {RequestMethod.GET,RequestMethod.POST})
    public String login(@RequestParam("username")String username,
                        @RequestParam("password")String password,
                        @RequestParam(value = "rememberme",defaultValue = "false")String rememberme,
                        @RequestParam(value="next",required = false,defaultValue = "/home")String next,
                        HttpServletResponse response,
                        Model model){
        try{
            Map<String,String> map=userService.login(username,password);
            if(map.containsKey("ticket")){
                String ticket=map.get("ticket");
                Cookie cookie=new Cookie("ticket",ticket);
                cookie.setPath("/");
                if(rememberme.equals("true")){
                    cookie.setMaxAge(1000*60*60*24);
                }else{
                    cookie.setMaxAge(-1);
                }
                response.addCookie(cookie);
                return "redirect:"+next;
            }else{
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }
        }catch (Exception e){
            logger.error("登陆异常"+e.getMessage());
            return "login";
        }
    }

    @RequestMapping(path={"/logout"})
    public String logout(@CookieValue("ticket")String ticket){
        userService.logOut(ticket);
        return "redirect:/home";
    }
}
