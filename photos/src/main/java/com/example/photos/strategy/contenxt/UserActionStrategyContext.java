package com.example.photos.strategy.contenxt;

import com.example.photos.model.event.UserActionEvent;
import com.example.photos.strategy.UserActionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

import static com.example.photos.enums.ActionTypeEnum.getStrategy;

/**
 * @Auther: raolongxiang
 * @Date: 2024/2/1
 * @Description: com.example.photos.strategy.contenxt
 */
@Service
public class UserActionStrategyContext {

    @Autowired
    Map<String, UserActionStrategy> strategyMap;


    public void executeUserActionStrategy(UserActionEvent userActionEvent){

        UserActionStrategy userActionStrategy = strategyMap.get(getStrategy(userActionEvent.getActionType()));
        if (!Objects.isNull(userActionStrategy)) {
            userActionStrategy.execute(userActionEvent);
        }

    }


}
