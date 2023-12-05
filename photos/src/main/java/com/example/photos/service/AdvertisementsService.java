package com.example.photos.service;

import com.example.photos.entity.Advertisements;
import com.example.photos.model.dto.PageResultDTO;
import com.example.photos.model.vo.ConditionVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Auther: Arrow
 * @Date: 2023/8/10
 * @Description: com.example.photos.service
 */
public interface AdvertisementsService {

    List<Advertisements>getRandomAD();

    PageResultDTO<?>getAdListByPage(ConditionVO conditionVO);

    void deleteAdList(List<Advertisements>advertisements);

    void insertAd(Advertisements advertisements);

    void updateAd(Advertisements advertisements);

    String uploadAdImage(MultipartFile file);

}
