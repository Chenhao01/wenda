package com.nowcoder.async.handler;

import com.nowcoder.async.eventHandler;
import com.nowcoder.async.eventModel;
import com.nowcoder.async.eventType;
import com.nowcoder.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 12274 on 2018/5/10.
 */
@Component
public class solrHandler implements eventHandler {
    @Autowired
    SearchService searchService;

    @Override
    public void doHandler(eventModel model) {
        try {
            searchService.indexQuestion(model.getEntityId(), model.getMapValue("title"), model.getMapValue("content"));

        }catch (Exception e){

        }
    }
    @Override
    public List<eventType> getSupportEventType() {
        return Arrays.asList(eventType.QUESTION);
    }
}
