package com.nowcoder.interceptors;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by 12274 on 2018/7/11.
 */
@Component
public class test implements HandlerInterceptor{
    @Autowired
    HostHolder hostHolder;
    @Autowired
    LoginTicketDAO loginTicketDAO;
    @Autowired
    UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket=null;
        for(Cookie cookie : httpServletRequest.getCookies()){
            if(cookie.getName().equals("ticket")){
                ticket=cookie.getValue();
                break;
            }
        }
        if(ticket!=null){
            LoginTicket loginTicket=loginTicketDAO.selectByTicket(ticket);
            if(loginTicket!=null && loginTicket.getStatus()!=0 && loginTicket.getExpired().after(new Date())){
                hostHolder.setUser(userService.getUser(loginTicket.getUserId()));
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if(hostHolder!=null && modelAndView!=null){
            modelAndView.addObject("user",hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}
