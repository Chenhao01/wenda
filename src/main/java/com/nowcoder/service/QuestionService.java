package com.nowcoder.service;

import com.nowcoder.Util.SensitiveFilter;
import com.nowcoder.dao.QuestionDAO;
import com.nowcoder.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by 12274 on 2018/1/15.
 */
@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;
    @Autowired
    SensitiveFilter sensitiveFilter;


    public int addQuestion(Question question){
        question.setContent(sensitiveFilter.filter(HtmlUtils.htmlEscape(question.getContent())));
        question.setTitle(sensitiveFilter.filter(HtmlUtils.htmlEscape(question.getTitle())));
        return questionDAO.addQuesttion(question);
    }

    public List<Question> listAllQuestion(){
        return questionDAO.selectAllQuestion();
    }

    public List<Question> listlatestQuestion(int userId,int offset,int limit){
        return questionDAO.selectLatestQuestions(userId,offset,limit);
    }
    public Question selectById(int qid ){
        return questionDAO.selectById(qid);
    }

    public void updateCommentCount(int id ,int commentCount){
        questionDAO.updateCommentCount(id,commentCount);
    }
}
