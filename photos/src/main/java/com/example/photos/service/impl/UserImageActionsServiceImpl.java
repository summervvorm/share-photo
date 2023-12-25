package com.example.photos.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.photos.assembler.UserImageActionAssembler;
import com.example.photos.entity.UserImageActions;
import com.example.photos.exception.CommonJsonException;
import com.example.photos.mapper.UserImageActionsMapper;
import com.example.photos.model.vo.UserActionVO;
import com.example.photos.service.UserImageActionsService;
import com.example.photos.strategy.UserActionStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

import static com.example.photos.enums.ActionTypeEnum.getStrategy;
import static com.example.photos.enums.StatusCodeEnum.FAIL;

/**
 * @Auther: raolongxiang
 * @Date: 2023/12/6
 * @Description: com.example.photos.service.impl
 */
@Service
@Slf4j
public class UserImageActionsServiceImpl extends ServiceImpl<UserImageActionsMapper, UserImageActions> implements UserImageActionsService {

    @Autowired
    private UserImageActionAssembler userImageActionAssembler;

    @Autowired
    Map<String, UserActionStrategy> strategyMap;

    @Override
    public Boolean insertActions(UserActionVO userActionVO) {

        return false;
    }

    @Override
    public Boolean userActionStrategy(UserActionVO userActionVO) {

        /**
        *@Description:记录用户行为。
        */
        if (userActionVO == null) {
            throw new CommonJsonException(FAIL);
        }
        UserImageActions userImageActions = userImageActionAssembler.toPO(userActionVO);

        if(!this.save(userImageActions)){
            return false;
        }

        /**
        *@Description:根据不同行为实现不同策略。
        */
        UserActionStrategy userActionStrategy = strategyMap.get(getStrategy(userActionVO.getActionType()));
        if (!Objects.isNull(userActionStrategy)) {
            userActionStrategy.execute(userActionVO);
        } else {
            return false;
        }

        return true;
    }
}
