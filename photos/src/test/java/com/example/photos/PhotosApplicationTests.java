//package com.example.photos;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
//import com.example.photos.entity.Picture;
//import com.example.photos.mapper.FollowMapper;
//import com.example.photos.mapper.PictureMapper;
//import com.example.photos.mapper.TagMapper;
//import com.example.photos.service.IndexPicRankService;
//import com.example.photos.service.RedisService;
//import com.example.photos.service.UserService;
//import com.example.photos.strategy.contenxt.UploadStrategyContext;
//import com.example.photos.util.PictureUtil;
//import com.example.photos.util.StringTools;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.redisson.api.RedissonClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.annotation.Resource;
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.IOException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.example.photos.constant.RedisConstant.DOWNLOAD_USER;
//
//@Slf4j
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class PhotosApplicationTests {
//
////    @Resource
////    FavoritesMapper favoritesMapper;
//    @Resource
//    FollowMapper followMapper;
//
//    @Resource
//    RedisService redisService;
//
//    @Resource
//    UploadStrategyContext uploadStrategyContext;
//
//    @Resource
//    IndexPicRankService indexPicRankService;
//
//    @Resource
//    UserService userService;
//
//    @Resource
//    PictureMapper pictureMapper;
//
//    @Autowired
//    private RedissonClient redissonClient;
//
//    @Test
//    void contextLoads() {
//        redissonClient.getBucket("hello").set("bug");
//        String test = (String) redissonClient.getBucket("hello").get();
//        System.out.println(test);
//
//    }
//    @Test
//    void pictureUtilTest() throws IOException {
////        System.out.println(PictureUtil.getMainRGB("https://plus.hutool.cn/images/ad/mingdao/mingdao_banner.png"));
////        List<Picture>pictures=pictureMapper.selectList(new LambdaQueryWrapper<Picture>().orderByDesc(Picture::getPicId));
////
////        for(Picture p:pictures){
////            URL url = new URL(p.getPicName());
////            BufferedImage image = ImageIO.read(url);
////
////            int width = image.getWidth();
////            int height = image.getHeight();
////            long size = url.openConnection().getContentLengthLong();
////
////            p.setSize(size);
////            p.setWidth(width);
////            p.setHeight(height);
////            pictureMapper.update(p,new LambdaUpdateWrapper<Picture>().eq(Picture::getPicId,p.getPicId()));
////            System.out.println("Width: " + width);
////            System.out.println("Height: " + height);
////            System.out.println("Size: " + size + " bytes");
//        }
//
//
//        log.info(pictures.toString());
//
//    }
//
//    @Resource
//    TagMapper tagMapper;
//    @Test
//    void tagMapperTest(){
////        List<String>tags = new ArrayList<>();
////        tags.add("人物");
////        tags.add("景观");
////        tags.add("学生");
////        tagMapper.insertTagsIfNotExists(tags);
//    }
//
//    @Test
//    void IndexPicTest(){
////        indexPicRankService.setHotPictures();
////        indexPicRankService.setNewPictures();
//    }
//    @Test
//    void redisTest(){
////        redisService.set();
//
////        redisService.hDel(DOWNLOAD_USER,"1");
////        Object o = redisService.hGet(LOGIN_USER, "1");
////        System.out.println(o);
////        userService.logoutUser();
//    }
//
//    @Test
//    void followMapperTest(){
////        followMapper.selectFansDTOList(1).forEach(System.out::println);
//    }
//    @Test
//    void pictureColorTest() throws IOException {
//
//        List<Picture>pictures=pictureMapper.selectList(new LambdaQueryWrapper<Picture>());
//
//        for(Picture picture:pictures){
//            String color=PictureUtil.getMainRGB(picture.getPicName());
//            picture.setPicColor(color);
//            pictureMapper.update(picture,new LambdaQueryWrapper<Picture>()
//                    .eq(Picture::getPicId,picture.getPicId())
//            );
//
//        }
//
//    }
//    @Test
//    void webSocketTest() {
//
////        WebSocketUtil.sendOneMessage("95","发送信息");
//
//    }
//
//    @Test
//    void StringUtilTest(){
//        System.out.println(StringTools.isConnection("123", "12"));
//
//    }
//
//}
