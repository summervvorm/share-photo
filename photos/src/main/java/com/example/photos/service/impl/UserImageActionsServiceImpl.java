package com.example.photos.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.photos.assembler.UserImageActionAssembler;
import com.example.photos.entity.UserImageActions;
import com.example.photos.exception.CommonJsonException;
import com.example.photos.mapper.UserImageActionsMapper;
import com.example.photos.model.vo.UserActionVO;
import com.example.photos.service.UserImageActionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.example.photos.enums.StatusCodeEnum.FAIL;

/**
 * @Auther: raolongxiang
 * @Date: 2023/12/6
 * @Description: com.example.photos.service.impl
 */
@Service
@Slf4j
public class UserImageActionsServiceImpl extends ServiceImpl<UserImageActionsMapper,UserImageActions> implements UserImageActionsService {

    @Resource
    UserImageActionAssembler userImageActionAssembler;

    @Override
    public Boolean insertActions(UserActionVO userActionVO) {

        if (userActionVO == null) {
            throw new CommonJsonException(FAIL);
        }
        UserImageActions userImageActions= userImageActionAssembler.toPO(userActionVO);

        return this.save(userImageActions);

    }
}
