package com.nowcoder.interceptors;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import com.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by 12274 on 2018/5/15.
 */
@Component
public class testi implements HandlerInterceptor{

    @Autowired
    HostHolder hostHolder;
    @Autowired
    LoginTicketDAO loginTicketDAO;
    @Autowired
    UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket=null;
        for(Cookie cookie:httpServletRequest.getCookies()){
            if(cookie.getName().equals("ticket")){
                ticket=cookie.getValue();
                break;
            }
        }
        if(ticket==null){
            return true;
        }
        LoginTicket loginTicket=loginTicketDAO.selectByTicket(ticket);
        if(loginTicket==null || loginTicket.getStatus()==1 || loginTicket.getExpired().before(new Date())){
            return true;
        }
        User user=userService.getUser(loginTicket.getUserId());
        hostHolder.setUser(user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if(hostHolder.getUser()!=null && modelAndView!=null){
            modelAndView.addObject("user",hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}
