package com.example.photos.constant;

/**
 * @Auther: Arrow
 * @Date: 2023/4/26
 * @Description: com.example.photos.config.constant
 */
public interface RedisConstant {
    long CODE_EXPIRE_TIME = 15 * 60;
    long PICTURE_EXPIRE_TIME = 60*60*24;

    long DOWNLOAD_EXPIRE_TIME=60*60*24;

    String USER_CODE_KEY = "code:";

    String BLOG_VIEWS_COUNT = "blog_views_count";

    String ARTICLE_VIEWS_COUNT = "article_views_count";

    String WEBSITE_CONFIG = "website_config";

    String USER_AREA = "user_area";

    String VISITOR_AREA = "visitor_area";

    String ABOUT = "about";

    String UNIQUE_VISITOR = "unique_visitor";

    String LOGIN_USER = "login_user";

    String DOWNLOAD_USER="download_user";

    String ARTICLE_ACCESS = "article_access:";

    String HOT_PICTURE="hot_picture";

    String NEW_PICTURE="new_picture";

}
