package com.example.photos.constant;

/**
 * @Auther: Arrow
 * @Date: 2023/7/5
 * @Description: com.example.photos.constant
 */
public interface NotificationConstant {

    String LABEL_FEEDBACK_PROCESSING_NOTIFICATIONS="你的反馈的%s标签已通过，感谢您对本网站的帮助";

    String IMAGE_FEEDBACK_PROCESSING_NOTIFICATIONS="您举报的图片已下架，感谢您对本网站的帮助！";

    String IMAGE_AUDIT_PROCESSING_NOTIFICATIONS="您上传的图片已通过审核,感谢您对本网站的帮助！";

    String IMAGE_AUDIT_FAILED_PROCESSING_NOTIFICATIONS="您上传的图片未通过审核,请勿上传非法图片！";

    String MESSAGE = "message";

    String UNREAD = "未读";

    String LABEL_FEEDBACK_TITLE="标签反馈处理通知";

    String IMAGE_FEEDBACK_TITLE="图片反馈处理通知";

    String IMAGE_AUDIT_TITLE="审核处理通知";

    String SENDER_SYSTEM="系统通知";
    String AUDIT_SYSTEM="审核通知";


    int SENDER_SYSTEM_ID=0;

}
