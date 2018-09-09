package com.nowcoder.controller;

import com.nowcoder.model.EntityType;
import com.nowcoder.model.Question;
import com.nowcoder.model.User;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.FollowService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.SearchService;
import com.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12274 on 2018/5/10.
 */
@Controller
public class SearchControll{
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    SearchService searchService;
    @Autowired
    FollowService followService;

    @RequestMapping(path = {"/search"}, method = {RequestMethod.GET})
    public String search (Model model, @RequestParam("q") String keyword,
                          @RequestParam(value = "offset", defaultValue = "0") int offset,
                          @RequestParam(value = "count", defaultValue = "10") int count)throws Exception{
        List<ViewObject> vos=new ArrayList<>();
        List<Question> questions=searchService.search(keyword,offset,count,"<i>","</i>");
        for(Question question:questions){
            Question q=questionService.selectById(question.getId());
            ViewObject vo=new ViewObject();
            if(question.getTitle()!=null){
                q.setTitle(question.getTitle());
            }
            if(question.getContent()!=null){
                q.setContent(question.getContent());
            }
            vo.set("question",q);
            User user=userService.getUser(q.getUserId());
            vo.set("user",user);
            vo.set("followCount",followService.getFollowerCount(EntityType.ENTITY_USER,user.getId()));
            vos.add(vo);
        }
        model.addAttribute("vos",vos);
        model.addAttribute("keyword",keyword);
        return "result";
    }
}
