package com.example.photos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.photos.constant.NotificationConstant;
import com.example.photos.entity.*;
import com.example.photos.enums.StatusCodeEnum;
import com.example.photos.exception.CommonJsonException;
import com.example.photos.mapper.NotificationMapper;
import com.example.photos.mapper.PictureTagMapper;
import com.example.photos.mapper.ReportTagMapper;
import com.example.photos.mapper.TagMapper;
import com.example.photos.model.dto.ImageLabelResultDTO;
import com.example.photos.model.dto.PageResultDTO;
import com.example.photos.model.dto.PictureTagsWordCloudDTO;
import com.example.photos.model.dto.ReportTagsDTO;
import com.example.photos.model.vo.ConditionVO;
import com.example.photos.model.vo.NotificationVO;
import com.example.photos.model.vo.PictureUploadVO;
import com.example.photos.model.vo.ReportTagsVO;
import com.example.photos.service.PictureTagService;
import com.example.photos.util.*;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.photos.constant.NotificationConstant.*;
import static com.example.photos.enums.StatusCodeEnum.FAIL;

/**
 * @Auther: Arrow
 * @Date: 2023/5/31
 * @Description: com.example.photos.service.impl
 */
@Service
@Slf4j
public class PictureTagServiceImpl implements PictureTagService {

    @Resource
    TagMapper tagMapper;
    @Resource
    PictureTagMapper pictureTagMapper;
    @Resource
    ReportTagMapper reportTagMapper;

    @Resource
    NotificationMapper notificationMapper;

    @Resource
    BaiduUtil baiduUtil;

    @Override
    public void addPicTag(PictureUploadVO pictureUploadVO, int picId) {
        tagMapper.insertTagsIfNotExists(pictureUploadVO.getPicTags());
        List<Tag> tagIds = tagMapper.selectTagsId(pictureUploadVO.getPicTags());

        for (Tag i : tagIds) {
            log.info("这是tagIds：" + i);
            pictureTagMapper.insertOne(PictureTag.builder().tagName(i.getTagName()).tagId(i.getId()).picId(picId).createTime(LocalDateTime.now()).build());
        }
    }

    @Override
    public Map<Integer, List<Tag>> getTagsByPicList(List<Picture> pictures) {
        Map<Integer, List<Tag>> lists = new HashMap<>();

        List<PictureTag> tags = tagMapper.selectTagsByPicIds(pictures);
        for (PictureTag pictureTag : tags) {
            if (!lists.containsKey(pictureTag.getPicId())) {
                lists.put(pictureTag.getPicId(), new ArrayList<>());
            }
            lists.get(pictureTag.getPicId()).add(Tag.builder().id(pictureTag.getTagId()).tagName(pictureTag.getTagName())
                    .createTime(pictureTag.getCreateTime()).build());
        }
        return lists;
    }

    @Override
    public List<Tag> getTagByPic(Picture picture) {
        List<PictureTag> pictureTags = new ArrayList<>();
        pictureTags = pictureTagMapper.selectList(new LambdaQueryWrapper<PictureTag>().eq(PictureTag::getPicId, picture.getPicId()));
        List<Tag> tags = new ArrayList<>();
        for (int j = 0; j < pictureTags.size(); j++) {
            tags.add(tagMapper.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getId, pictureTags.get(j).getTagId())));
        }

        return tags;
    }

    @Override
    public void addReportTag(ReportTagsVO reportTagsVO) {

        List<ReportTag> reportTagList = new ArrayList<>();

        for (String st : reportTagsVO.getTags()) {
            reportTagList.add(ReportTag.builder().userId(reportTagsVO.getUserId())
                    .createTime(CommonUtil.getLocalDateTime()).picId(reportTagsVO.getPicId())
                    .tags(st).build()
            );
        }
        reportTagMapper.batchInsertReportTags(reportTagList);
    }

    @Override
    public PageResultDTO<?> getReportTagsList(ConditionVO conditionVO) {
        Page<ReportTagsDTO> page = new Page<>(PageUtil.getCurrent(), PageUtil.getSize());

        Page<ReportTagsDTO> res = reportTagMapper.getReportTagsList(page, conditionVO.getKeywords());

        return new PageResultDTO<>(res.getRecords(), (int) res.getTotal());
    }

    @Override
    public void addTagsByReportTags(List<ReportTagsDTO> reportTagsDTOList) {

        if (reportTagsDTOList == null || reportTagsDTOList.size() == 0) {

            throw new CommonJsonException(FAIL);
        }

//获取tagNameList
        List<String> tagNames = reportTagsDTOList.stream()
                .map(obj -> obj.getTags())
                .collect(Collectors.toList());
        tagMapper.insertTagsIfNotExists(tagNames);
//获取tagNameList对应的TagList
        List<Tag> tags = tagMapper.selectTagsId(tagNames);
        Map<String, Tag> map = new HashMap<>();
        for (Tag tag : tags) {
            map.put(tag.getTagName(), tag);
        }
        List<PictureTag> pictureTags = new ArrayList<>();
        List<Notification> notifications = new ArrayList<>();
        for (ReportTagsDTO reportTagsDTO : reportTagsDTOList) {
            //发送通知给用户
            String customString = reportTagsDTO.getTags();
            String notification = String.format(NotificationConstant.LABEL_FEEDBACK_PROCESSING_NOTIFICATIONS, customString);
            WebSocketUtil.sendOneMessage("" + reportTagsDTO.getUserId(), NotificationVO.ok(MESSAGE, notification));

            notifications.add(Notification.builder()
                    .createTime(CommonUtil.getLocalDateTime())
                    .state(UNREAD).title(LABEL_FEEDBACK_TITLE).content(notification)
                    .receiverId(reportTagsDTO.getUserId()).sender(SENDER_SYSTEM).senderId(SENDER_SYSTEM_ID).build()
            );

            pictureTags.add(PictureTag.builder().tagName(reportTagsDTO.getTags()).
                    picId(reportTagsDTO.getPicId()).createTime(reportTagsDTO.getCreateTime())
                    .tagId(map.get(reportTagsDTO.getTags()).getId()).build()
            );
            log.info(reportTagsDTO.toString());
        }
//将pictureTags添加
        pictureTagMapper.insertList(pictureTags);
//删除reportTag表中内容
        reportTagMapper.batchDeleteReportTag(reportTagsDTOList);
//将通知对象插入到数据库中
        notificationMapper.insertList(notifications);

    }

    @Override
    public void deleteReportTags(List<ReportTagsDTO> reportTagsDTOList) {
        if (reportTagsDTOList == null || reportTagsDTOList.size() == 0) {

            throw new CommonJsonException(FAIL);
        }
        reportTagMapper.batchDeleteReportTag(reportTagsDTOList);
    }

    @Override
    public List<PictureTagsWordCloudDTO> selectTagsWordCloudDate() {
        return pictureTagMapper.selectTagsWordCloudDate();
    }

    @Override
    public boolean autoAudit(ReportTagsDTO reportTagsDTO) {

        ImageLabelResultDTO resultDTO = baiduUtil.advancedGeneral(reportTagsDTO.getPicName());

        log.info(resultDTO.toString());
        List<ReportTagsDTO>list=new ArrayList<>();
        list.add(reportTagsDTO);
        String str2 = reportTagsDTO.getTags();

        for (ImageLabelResultDTO.ResultItem resultItem : resultDTO.getResult()) {
            if (StringTools.isConnection(resultItem.getKeyword(), str2) || StringTools.isConnection(resultItem.getRoot(), str2)) {


                addTagsByReportTags(list);

                return true;


            }
        }

        deleteReportTags(list);

        return false;
    }
}
