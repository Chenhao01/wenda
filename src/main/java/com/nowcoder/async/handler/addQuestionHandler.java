package com.nowcoder.async.handler;

import com.nowcoder.async.eventHandler;
import com.nowcoder.async.eventModel;
import com.nowcoder.async.eventType;
import com.nowcoder.model.Question;
import com.nowcoder.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by 12274 on 2018/3/8.
 */
@Component
public class addQuestionHandler implements eventHandler {
    @Autowired
    QuestionService questionService;

    @Override
    public void doHandler(eventModel model) {
        Question question=new Question();
        question.setTitle(model.getMapValue("title"));
        question.setContent(model.getMapValue("content"));
        question.setCreatedDate(new Date());
        question.setUserId(model.getActorId());
        questionService.addQuestion(question);
    }

    @Override
    public List<eventType> getSupportEventType() {
        return Arrays.asList(/*eventType.QUESTION*/);
    }
}
