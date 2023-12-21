package com.example.photos.assembler;

import com.example.photos.entity.UserImageActions;
import com.example.photos.model.vo.UserActionVO;
import org.mapstruct.Mapper;
/**
 * @Auther: raolongxiang
 * @Date: 2023/12/6
 * @Description: com.example.photos.assembler
 */
@Mapper(componentModel = "spring")
public interface UserImageActionAssembler {
    UserImageActions toPO(UserActionVO userActionVO);
}
