package com.nowcoder.async;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12274 on 2018/3/7.
 */
public interface eventHandler {
    void doHandler(eventModel model);
    List<eventType> getSupportEventType();
}
