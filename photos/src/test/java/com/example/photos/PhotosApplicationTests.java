package com.example.photos;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.photos.entity.Picture;
import com.example.photos.entity.User;
import com.example.photos.mapper.*;
import com.example.photos.model.dto.UserDetailsDTO;
import com.example.photos.service.IndexPicRankService;
import com.example.photos.service.PictureService;
import com.example.photos.service.RedisService;
import com.example.photos.service.UserService;
import com.example.photos.strategy.contenxt.UploadStrategyContext;
import com.example.photos.util.PictureUtil;
import com.example.photos.util.StringTools;
import com.example.photos.util.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import javax.annotation.Resource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.photos.constant.RedisConstant.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PhotosApplicationTests {

//    @Resource
//    FavoritesMapper favoritesMapper;
    @Resource
    FollowMapper followMapper;

    @Resource
    RedisService redisService;

    @Resource
    UploadStrategyContext uploadStrategyContext;

    @Resource
    IndexPicRankService indexPicRankService;

    @Resource
    UserService userService;

    @Resource
    PictureMapper pictureMapper;


    @Test
    void contextLoads() {
//        uploadStrategyContext.executeDeleteStrategy("photos/photos/ad8cdebcf427f75286cd48972c2805a5.jpg");

//        favoritesMapper.selectFavIdList(1).forEach(System.out::println);
//        roleMapper.listResourceRoles().forEach(System.out::println);
    }
    @Test
    void pictureUtilTest() throws IOException {
//        System.out.println(PictureUtil.getMainRGB("https://plus.hutool.cn/images/ad/mingdao/mingdao_banner.png"));
        List<Picture>pictures=pictureMapper.selectList(new LambdaQueryWrapper<Picture>().orderByDesc(Picture::getPicId));

        for(Picture p:pictures){
            URL url = new URL(p.getPicName());
            BufferedImage image = ImageIO.read(url);

            int width = image.getWidth();
            int height = image.getHeight();
            long size = url.openConnection().getContentLengthLong();

            p.setSize(size);
            p.setWidth(width);
            p.setHeight(height);
            pictureMapper.update(p,new LambdaUpdateWrapper<Picture>().eq(Picture::getPicId,p.getPicId()));
//            System.out.println("Width: " + width);
//            System.out.println("Height: " + height);
//            System.out.println("Size: " + size + " bytes");
        }


        log.info(pictures.toString());

    }

    @Resource
    TagMapper tagMapper;
    @Test
    void tagMapperTest(){
        List<String>tags = new ArrayList<>();
        tags.add("人物");
        tags.add("景观");
        tags.add("学生");
        tagMapper.insertTagsIfNotExists(tags);
    }

    @Test
    void IndexPicTest(){
        indexPicRankService.setHotPictures();
        indexPicRankService.setNewPictures();
    }
    @Test
    void redisTest(){
//        redisService.set();

        redisService.hDel(DOWNLOAD_USER,"1");
//        Object o = redisService.hGet(LOGIN_USER, "1");
//        System.out.println(o);
//        userService.logoutUser();
    }

    @Test
    void followMapperTest(){
        followMapper.selectFansDTOList(1).forEach(System.out::println);
    }
    @Test
    void pictureColorTest() throws IOException {

        List<Picture>pictures=pictureMapper.selectList(new LambdaQueryWrapper<Picture>());

        for(Picture picture:pictures){
            String color=PictureUtil.getMainRGB(picture.getPicName());
            picture.setPicColor(color);
            pictureMapper.update(picture,new LambdaQueryWrapper<Picture>()
                    .eq(Picture::getPicId,picture.getPicId())
            );

        }

    }
    @Test
    void webSocketTest() {

//        WebSocketUtil.sendOneMessage("95","发送信息");

    }

    @Test
    void StringUtilTest(){
        System.out.println(StringTools.isConnection("123", "12"));

    }

}
