package com.example.photos.util;

import com.alibaba.fastjson.JSON;
import com.example.photos.config.properties.BaiduApiConfigProperties;
import com.example.photos.model.dto.ImageLabelResultDTO;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * 获取token类
 */
@Component
public class BaiduUtil {

    @Resource
    private  BaiduApiConfigProperties baiduApiConfigProperties;

//    public static void main(String[] args) {
//        System.out.println(advancedGeneral());
////        getAuth();
//    }

    //百度技术文档里面是没有主方法的，这里需要自己创建，然后调用getAuth()

    /**
     * 获取权限token
     * @return 返回示例：
     * {
     * "access_token": "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567",
     * "expires_in": 2592000
     * }
     */
    public  String getAuth() {
        // 官网获取的 API Key 更新为你注册的(唯一需要收到的更改的地方  将百度云应用的AK---->API Key)
        String clientId = baiduApiConfigProperties.getApikey();
        // 官网获取的 Secret Key 更新为你注册的(唯一需要收到的更改的地方  将百度云应用的AK---->Secret Key)
        String clientSecret = "ywFGrkamLBdVqn8u2krZ3y87goem9KT6";
        return getAuth(clientId, clientSecret);
    }

    /**
     * 获取API访问token
     * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
     * @param ak - 百度云官网获取的 API Key
     * @param sk - 百度云官网获取的 Securet Key
     * @return assess_token 示例：
     * "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
     */
    public  String getAuth(String ak, String sk) {
        // 获取token地址
        String authHost = baiduApiConfigProperties.getAuthHost();
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.err.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            /**
             * 返回结果示例
             */
            System.err.println("result:" + result);
            JSONObject jsonObject = new JSONObject(result);
            String access_token = jsonObject.getString("access_token");
            return access_token;
        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }


    /**
     * 通用物体和场景识别
     */


        /**
         * 重要提示代码中所需工具类
         * FileUtil,Base64Util,HttpUtil,GsonUtils请从
         * https://ai.baidu.com/file/658A35ABAB2D404FBF903F64D47C1F72
         * https://ai.baidu.com/file/C8D81F3301E24D2892968F09AE1AD6E2
         * https://ai.baidu.com/file/544D677F5D4E4F17B4122FBD60DB82B3
         * https://ai.baidu.com/file/470B3ACCA3FE43788B5A963BF0B625F3
         * 下载
         */
        public  ImageLabelResultDTO advancedGeneral(String imgUrl) {
            // 请求url
            String url = baiduApiConfigProperties.getUrl();
            try {
                // 本地文件路径
//                String imgUrl = "http://arrows.oss-cn-beijing.aliyuncs.com/photos/photos/ccc8b1513e33500d5c2d5defb81c67161.jpeg";

                // 打开 URL 连接
                URL urls = new URL(imgUrl);
                InputStream inputStream = urls.openStream();

                // 将图片数据读取为 BufferedImage 对象
                BufferedImage image = ImageIO.read(inputStream);

                // 计算图片的宽度和高度
                int width = image.getWidth();
                int height = image.getHeight();

                // 如果图片的宽度或高度超过 4096 像素，则缩放图片
                if (width > 4096 || height > 4096) {
                    double scale = Math.min(4096.0 / width, 4096.0 / height);
                    int newWidth = (int) (width * scale);
                    int newHeight = (int) (height * scale);
                    // 缩放图片
                    Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                    BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                    newImage.getGraphics().drawImage(scaledImage, 0, 0, null);
                }
                    // 将 BufferedImage 对象转换为字节数组
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(image, "jpg", baos);
                    byte[] imgData = baos.toByteArray();

                    // 将字节数组转换为 Base64 编码的字符串
                    String imgStr = Base64.getEncoder().encodeToString(imgData);
                    String imgParam = URLEncoder.encode(imgStr, "UTF-8");

                String param = "image=" + imgParam;

                // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
                String accessToken = baiduApiConfigProperties.getAccessToken();

                String result = HttpUtil.post(url, accessToken, param);

                return  JSON.parseObject(result,ImageLabelResultDTO.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }





}
