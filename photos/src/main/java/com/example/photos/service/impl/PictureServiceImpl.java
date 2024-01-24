package com.example.photos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.photos.constant.NotificationConstant;
import com.example.photos.entity.*;
import com.example.photos.enums.FilePathEnum;
import com.example.photos.enums.StatusCodeEnum;
import com.example.photos.exception.CommonJsonException;
import com.example.photos.mapper.*;
import com.example.photos.model.dto.*;
import com.example.photos.model.event.UserActionEvent;
import com.example.photos.model.vo.ConditionVO;
import com.example.photos.model.vo.NotificationVO;
import com.example.photos.model.vo.PictureUploadVO;
import com.example.photos.model.vo.ReportPicVO;
import com.example.photos.service.IndexPicRankService;
import com.example.photos.service.PictureService;
import com.example.photos.service.PictureTagService;
import com.example.photos.service.RedisService;
import com.example.photos.strategy.contenxt.RecommendStrategyContext;
import com.example.photos.strategy.contenxt.UploadStrategyContext;
import com.example.photos.util.*;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.photos.constant.AuthConstant.EXPIRE_TIME;
import static com.example.photos.constant.CommonConstant.DEFAULT_COLOR;
import static com.example.photos.constant.NotificationConstant.*;
import static com.example.photos.constant.RedisConstant.*;
import static com.example.photos.util.UserUtil.getUserDetailsDTO;

/**
 * @Auther: Arrow
 * @Date: 2023/5/4
 * @Description: com.example.photos.service.impl
 */
@Service
@Slf4j
public class PictureServiceImpl implements PictureService {
    @Resource
    PictureMapper pictureMapper;

    @Resource
    private UploadStrategyContext uploadStrategyContext;

    @Resource
    private IndexPicRankService indexPicRankService;

    @Resource
    private FavoritesMapper favoritesMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private PictureLikesMapper pictureLikesMapper;

    @Resource
    private PictureTagService pictureTagService;

    @Resource
    private ReportPicMapper reportPicMapper;

    @Resource
    private NotificationMapper notificationMapper;

    @Resource
    private RedisService redisService;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Resource
    private PictureTagMapper pictureTagMapper;

    @Resource
    private TagMapper tagMapper;

    @Resource
    private RecommendStrategyContext strategyContext;

    @Resource
    private CommentMapper commentMapper;

    @Override
    public PictureInfoDTO getPicInfoById(Integer picId) {
        Picture picture = pictureMapper.selectById(picId);
        if(picture==null){
            return null;
        }

        List<Tag> tags = pictureTagService.getTagByPic(picture);

        return new PictureInfoDTO(picture, tags);
    }

    @Override
    public PageResultDTO<?> getPicPageByCondition(ConditionVO conditionVO) {
        log.info(conditionVO.toString());
        Page<Picture> page = new Page<>(PageUtil.getCurrent(), PageUtil.getSize());
//        如果category=='1'就按照点赞数量排序
        if(conditionVO.getStartTime()==null&&conditionVO.getEndTime()==null){
            conditionVO.setEndTime(CommonUtil.getLocalDateTime());
            conditionVO.setStartTime(CommonUtil.getLocalDateTime().minusYears(1));
        }
        conditionVO.setPicWidth(conditionVO.getPicWidth()==null?0: conditionVO.getPicWidth());
        conditionVO.setPicHeight(conditionVO.getPicHeight()==null?0: conditionVO.getPicHeight());
        conditionVO.setStatus(conditionVO.getStatus()==null?0:conditionVO.getStatus());
        Page<Picture> picPage = pictureMapper.selectPicturesByKeywordWithSort(page,
                conditionVO.getKeywords(),conditionVO.getColor(),
                conditionVO.getCategory()!=null&&conditionVO.getCategory().equals("1")
        ,conditionVO.getStartTime(),conditionVO.getEndTime(),
                conditionVO.getPicWidth(),conditionVO.getPicHeight(),conditionVO.getStatus()
        );

        List<PictureInfoDTO> photos = new ArrayList<>();
        if(picPage.getRecords().size()==0){
            return new PageResultDTO<>(photos, (int) picPage.getTotal());
        }
        Map<Integer,List<Tag>>tags=pictureTagService.getTagsByPicList(picPage.getRecords());
        List<Picture>pictures=picPage.getRecords();
        for(int i=0;i<pictures.size();i++){
            Picture picture = pictures.get(i);
            photos.add(new PictureInfoDTO(picture,tags.get(picture.getPicId())));
        }
        return new PageResultDTO<>(photos, (int) picPage.getTotal());
//
    }

    @Override
    public String uploadPic(PictureUploadVO pictureUploadVO) {
        MultipartFile file = pictureUploadVO.getFile();
        String header = uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.PHOTO.getPath());
        UserDetailsDTO userDetailsDTO = getUserDetailsDTO();
        String color = DEFAULT_COLOR;
        Map<String,Long>map=new HashMap<>();
        try {
            color = PictureUtil.getMainRGB(header);
            map=PictureUtil.getPicSize(new URL(header));

        } catch (IOException e) {
            e.printStackTrace();
        }
        Picture picture = Picture.builder().picById(userDetailsDTO.getUserId())
                .picSign(pictureUploadVO.getPicSign())
                .picCreatTime(LocalDateTime.now()).picName(header).picColor(color)
                        .size(map.get("size")).width(Math.toIntExact(map.get("width")))
                        .height(Math.toIntExact(map.get("height"))).status(2).build();//2表示审核中
        log.info(picture.toString());

        pictureMapper.insertOne(picture);
        int picId = pictureMapper.selectIdByName(header);
        pictureTagService.addPicTag(pictureUploadVO, picId);

        userMapper.addExperience(UserUtil.getUserDetailsDTO().getUserId());

        UserActionEvent userActionEvent = new UserActionEvent();
        userActionEvent.setPicId(new ArrayList<>());
        userActionEvent.getPicId().add(picId);
        userActionEvent.setUserId(UserUtil.getUserDetailsDTO().getUserId());
        userActionEvent.setActionType(6);
        applicationEventPublisher.publishEvent(userActionEvent);

        return header;
    }

    @Override
    public void deletePicById(Integer picId) {
        Picture picture = pictureMapper.selectById(picId);
        if (!Objects.isNull(picture)) {
            String fileName = picture.getPicName();
            uploadStrategyContext.executeDeleteStrategy(fileName);
        } else {
            throw new CommonJsonException("图片不存在");
        }
        pictureMapper.deleteById(picId);

    }


    /**
     * 功能描述:获取首页的图片信息，首先检查redis中是否存在，如果不存在就从mysql中获取信息，如果存在就从redis中直接获取
     *
     * @param: 图片信息的分类，目前只有最新和最热
     * @return: 图片信息和相应的标签信息
     * @auther: Arrow
     * @Date: 2023/6/7
     */
    @Override
    public List<PictureInfoDTO> getPicInfoByType(String type) {
        if (type != null && type.equals("hot"))
            return indexPicRankService.getHotPictures();
        else if (type != null && type.equals("new")) {
            return indexPicRankService.getNewPictures();
        }
        return null;
    }

    @Override
    public Map<String, BigDecimal> countLikesAndFavoritesByUserId(int userId) {
        return pictureMapper.countLikesAndFavoritesByUserId(userId);
    }

    @Override
    public Map<String, Boolean> checkUserLikesOrFavPic(int userId, int picId) {
        PictureLikes pictureLikes = pictureLikesMapper.selectOne(
                new LambdaQueryWrapper<PictureLikes>()
                        .eq(PictureLikes::getPicId, picId)
                        .eq(PictureLikes::getUserId, userId)
        );
        Favorites favorites = favoritesMapper.selectOne(
                new LambdaQueryWrapper<Favorites>()
                        .eq(Favorites::getFavUserId, userId)
                        .eq(Favorites::getFavPicId, picId)
                        .last("LIMIT 1")
        );
        Map<String, Boolean> map = new HashMap<>();
        if (pictureLikes != null) {
            map.put("checkLikes", true);
        } else {
            map.put("checkLikes", false);
        }
        if (favorites != null) {
            map.put("checkFav", true);
        } else
            map.put("checkFav", false);

        return map;
    }

    @Override
    public int insertPicLike(int userId, int picId) {
        int res = pictureLikesMapper.insert(PictureLikes.builder().
                picId(picId).
                userId(userId).
                createTime(LocalDateTime.now()).
                build());
        if(res==0){
            throw new CommonJsonException(StatusCodeEnum.FAIL);
        }else{
            pictureMapper.updatePicLikes(picId,1);
        }
        UserActionEvent userActionEvent = new UserActionEvent();
        userActionEvent.setPicId(new ArrayList<>(picId));
        userActionEvent.setUserId(userId);
        userActionEvent.setActionType(1);
        applicationEventPublisher.publishEvent(userActionEvent);

        return res;
    }

    @Override
    public int deletePicLike(int userId, int picId) {
        int res = pictureLikesMapper.delete(new LambdaQueryWrapper<PictureLikes>()
                .eq(PictureLikes::getUserId,userId)
                .eq(PictureLikes::getPicId,picId)
        );
        if(res==0){
            throw new CommonJsonException(StatusCodeEnum.FAIL);

        }else
            pictureMapper.updatePicLikes(picId,-1);

        UserActionEvent userActionEvent = new UserActionEvent();
        userActionEvent.setPicId(new ArrayList<>(picId));
        userActionEvent.setUserId(userId);
        userActionEvent.setActionType(2);
        applicationEventPublisher.publishEvent(userActionEvent);
        return res;
    }

    @Override
    public Map<String, List<Integer>> getLikeAndFavPicIdList() {
        int userId= UserUtil.getUserDetailsDTO().getUserId();
        Map<String, List<Integer>>map=new HashMap<>();
        map.put("likePicIdList",
               new ArrayList<>());
        map.put("favPicIdList",new ArrayList<>());

        map.put("likePicIdList",
          pictureLikesMapper.getPicIdListByUserId(userId));
        map.put("favPicIdList",favoritesMapper.getFavPicIdByUserId(userId));

        return map;
    }

    @Override
    public PageResultDTO<?> getUploadPicList(ConditionVO conditionVO,int userId) {
        Page<Picture> page = new Page<>(PageUtil.getCurrent(), PageUtil.getSize());

        Page<Picture>picPage = pictureMapper.selectUploadPicturesByCondition(page,conditionVO.getKeywords(),
                conditionVO.getCategory()!=null&&conditionVO.getCategory().equals("1"),conditionVO.getStatus(),userId);

        List<PictureInfoDTO> photos = new ArrayList<>();
        if(picPage.getRecords().size()==0){
            return new PageResultDTO<>(photos, (int) picPage.getTotal());
        }
        Map<Integer,List<Tag>>tags=pictureTagService.getTagsByPicList(picPage.getRecords());
        List<Picture>pictures=picPage.getRecords();
        for(int i=0;i<pictures.size();i++){
            Picture picture = pictures.get(i);
            photos.add(new PictureInfoDTO(picture,tags.get(picture.getPicId())));
        }
        return new PageResultDTO<>(photos, (int) picPage.getTotal());

//        return new PageResultDTO<>(res.getRecords(),(int)res.getTotal());
    }

    @Override
    public PageResultDTO<?> getReportPicList(ConditionVO conditionVO) {
        Page<ReportPicDTO> page = new Page<>(PageUtil.getCurrent(), PageUtil.getSize());

        Page<ReportPicDTO> res = reportPicMapper.getReportPicList(page,conditionVO.getKeywords());

        return new PageResultDTO<>(res.getRecords(), (int) res.getTotal());
    }

    @Override
    public void takeDownPicture(List<ReportPicDTO> reportPicDTOList) {

//发送通知信息
        List<Notification>notifications = new ArrayList<>();

        for(ReportPicDTO reportPicDTO:reportPicDTOList){

//            String customString = reportTagsDTO.getTags();
            WebSocketUtil.sendOneMessage(""+reportPicDTO.getUserId(), NotificationVO.ok(MESSAGE,IMAGE_FEEDBACK_PROCESSING_NOTIFICATIONS));

            notifications.add(Notification.builder()
                    .createTime(CommonUtil.getLocalDateTime())
                    .state(UNREAD).title(IMAGE_FEEDBACK_TITLE).content(IMAGE_FEEDBACK_PROCESSING_NOTIFICATIONS)
                    .receiverId(reportPicDTO.getUserId()).sender(SENDER_SYSTEM).senderId(SENDER_SYSTEM_ID).build()
            );


            log.info(reportPicDTO.toString());
        }

//删除reportPic
        reportPicMapper.batchDeleteReportPic(reportPicDTOList);
//删除picture
        List<Integer>picIds =reportPicDTOList.stream().map(ReportPicDTO::getPicId).collect(Collectors.toList());;

        pictureMapper.delete(new LambdaQueryWrapper<Picture>()
                .in(Picture::getPicId,picIds)
        );
//将通知对象插入到数据库中
        notificationMapper.insertList(notifications);


    }

    @Override
    public void deleteReportPic(List<ReportPicDTO> reportPicDTOList) {
//删除reportPic
        reportPicMapper.batchDeleteReportPic(reportPicDTOList);



    }

    @Override
    public void addReportPic(ReportPicVO reportPicVO) {

        reportPicMapper.insert(ReportPic.builder().picId(reportPicVO.getPicId())
                .createTime(CommonUtil.getLocalDateTime()).content(reportPicVO.getContent())
                .category(reportPicVO.getCategory()).userId(reportPicVO.getUserId()).build()
        );

    }

    @Override
    public int downloadPic(int i) {
        int userId=UserUtil.getUserDetailsDTO().getUserId();

        Object temp=redisService.hGet(DOWNLOAD_USER, String.valueOf(userId));

        int count=0;

        if(temp==null){
            redisService.hSet(DOWNLOAD_USER, String.valueOf(userId), 0, DOWNLOAD_EXPIRE_TIME);
            return 0;
        }
        count=(int)temp+i;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long seconds = (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;

        redisService.hSet(DOWNLOAD_USER, String.valueOf(userId), count, (System.currentTimeMillis() / 1000) + seconds);

        return count;
    }

    @Override
    public void updatePictureInfo(PictureInfoDTO pInfo) {

        Picture picture = Picture.builder().height(pInfo.getPicHeight()).width(pInfo.getPicWidth())
                .picSign(pInfo.getPicSign()).picName(pInfo.getPicName())
                .picById(pInfo.getUserId()).picColor(pInfo.getColor())
                .picLikes(pInfo.getPicLikes()).picCollections(pInfo.getPicCollections())
                .picId(pInfo.getPicId()).picCreatTime(pInfo.getPicCreatTime()).size(pInfo.getPicSize()).build();
        pictureMapper.update(picture,new LambdaUpdateWrapper<Picture>().eq(Picture::getPicId,picture.getPicId()));

        List<String> tagNames=pInfo.getTags().stream()
                .map(Tag::getTagName).collect(Collectors.toList());
        pictureTagMapper.delete(new LambdaQueryWrapper<PictureTag>().in(PictureTag::getTagName,tagNames));

        pictureTagService.addPicTag(PictureUploadVO.builder().picTags(tagNames).build(),pInfo.getPicId());

    }

    @Override
    public List<PictureStatsDTO> getPictureStatsDTO() {
        return pictureMapper.getPicStats();
    }

    @Override
    public List<Integer> getDataCount() {

        List<Integer>list=new ArrayList<>();
        list.add(Math.toIntExact(userMapper.selectCount(new LambdaQueryWrapper<>())));
        list.add(Math.toIntExact(tagMapper.selectCount(new LambdaQueryWrapper<>())));

        list.add(Math.toIntExact(pictureMapper.selectCount(new LambdaQueryWrapper<>())));
        list.add(Math.toIntExact(commentMapper.selectCount(new LambdaQueryWrapper<>())));


        return list;

    }

    @Override
    public void passAuditPic(List<PictureInfoDTO> pictureInfoDTOList) {

        List<Integer> picIds = pictureInfoDTOList.stream()
                .map(PictureInfoDTO::getPicId)
                .collect(Collectors.toList());
//        List<Integer>userId = pictureInfoDTOList.stream()
//                .map(PictureInfoDTO::getUserId)
//                .collect(Collectors.toList());

        pictureMapper.update(null,new LambdaUpdateWrapper<Picture>()
                .in(Picture::getPicId,picIds).set(Picture::getStatus,0)
        );

//        发送信息

        List<Notification> notifications = new ArrayList<>();

        for(PictureInfoDTO pictureInfoDTO:pictureInfoDTOList){
            String notification = IMAGE_AUDIT_PROCESSING_NOTIFICATIONS;

            WebSocketUtil.sendOneMessage(pictureInfoDTO.getUserId()+"",NotificationVO.ok(MESSAGE,notification));

            notifications.add(Notification.builder()
                    .createTime(CommonUtil.getLocalDateTime())
                    .state(UNREAD).title(IMAGE_AUDIT_TITLE).content(notification)
                    .receiverId(pictureInfoDTO.getUserId()).sender(AUDIT_SYSTEM).senderId(SENDER_SYSTEM_ID).build()
            );

        }
        notificationMapper.insertList(notifications);


    }

    @Override
    public void refuseAuditPic(List<PictureInfoDTO> pictureInfoDTOList) {
        List<Integer> picIds = pictureInfoDTOList.stream()
                .map(PictureInfoDTO::getPicId)
                .collect(Collectors.toList());

        pictureMapper.delete(new LambdaQueryWrapper<Picture>().in(Picture::getPicId,picIds));
        //        发送信息
        List<Notification> notifications = new ArrayList<>();

        for(PictureInfoDTO pictureInfoDTO:pictureInfoDTOList){
            String notification = IMAGE_AUDIT_FAILED_PROCESSING_NOTIFICATIONS;

            WebSocketUtil.sendOneMessage(pictureInfoDTO.getUserId()+"",NotificationVO.ok(MESSAGE,notification));

            notifications.add(Notification.builder()
                    .createTime(CommonUtil.getLocalDateTime())
                    .state(UNREAD).title(IMAGE_AUDIT_TITLE).content(notification)
                    .receiverId(pictureInfoDTO.getUserId()).sender(AUDIT_SYSTEM).senderId(SENDER_SYSTEM_ID).build()
            );

        }
        notificationMapper.insertList(notifications);

    }

    @Override
    public List<PictureInfoDTO> getRecommendPic(Integer userId) {



        return strategyContext.executeRecommendStrategy(userId);
    }


}
