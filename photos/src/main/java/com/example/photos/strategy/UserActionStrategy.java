package com.example.photos.strategy;

import com.example.photos.model.event.UserActionEvent;
import com.example.photos.model.vo.UserActionVO;

/**
 * @Auther: raolongxiang
 * @Date: 2023/12/20
 * @Description: com.example.photos.strategy
 */
public interface UserActionStrategy {

    void execute(UserActionEvent userActionEvent);

}
