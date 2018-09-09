package com.nowcoder.interceptors;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by 12274 on 2018/9/4.
 */
@Component
public class interceptor implements HandlerInterceptor{
    @Autowired
    LoginTicketDAO ticketDAO;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        Cookie[] cookies=httpServletRequest.getCookies();
        String ticket=null;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("ticket")){
                ticket=cookie.getValue();
                break;
            }
        }
        if(ticket!=null){
            LoginTicket loginTicket=ticketDAO.selectByTicket(ticket);
            if(loginTicket==null || loginTicket.getStatus()==1 || loginTicket.getExpired().after(new Date())){
                return true;
            }
            hostHolder.setUser(userService.getUser(loginTicket.getUserId()));
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if(modelAndView!=null && hostHolder.getUser()!=null){
            modelAndView.addObject("USER",hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}
