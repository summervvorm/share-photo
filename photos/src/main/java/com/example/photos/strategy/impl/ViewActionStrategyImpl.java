package com.example.photos.strategy.impl;

import com.example.photos.model.vo.UserActionVO;
import com.example.photos.strategy.UserActionStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Auther: raolongxiang
 * @Date: 2023/12/20
 * @Description: 实现用户浏览之后的业务
 */
@Service("viewActionStrategyImpl")
@Slf4j
public class ViewActionStrategyImpl implements UserActionStrategy {
    @Override
    public void execute(UserActionVO userActionVO) {

        log.info("调用了viewActionStrategyImpl,数据为:{}",userActionVO);

    }
}
