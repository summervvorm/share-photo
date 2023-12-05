package com.example.photos.util;

import cn.hutool.core.img.ImgUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.photos.entity.Picture;
import lombok.extern.slf4j.Slf4j;
import org.deeplearning4j.nn.api.Model;
import org.deeplearning4j.zoo.PretrainedType;
import org.deeplearning4j.zoo.ZooModel;
import org.deeplearning4j.zoo.model.VGG16;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Arrow
 * @Date: 2023/5/22
 * @Description: com.example.photos.util
 */
@Slf4j
public class PictureUtil {
//    pub    public static final String colorString = "000 黑色," +
//            "011 黑色," +
//            "110 黑色," +
//            "001 黑色," +
//            "100 黑色," +
//            "111 黑色," +
//            "101 黑色," +
//            "010 黑色," +
//            "544 浅粉色," +
//            "533 粉色," +
//            "534 浅粉色," +
//            "523 粉色," +
//            "535 浅粉色," +
//            "524 粉色," +
//            "513 深粉色," +
//            "525 粉色," +
//            "514 深粉色," +
//            "503 深粉色," +
//            "515 深粉色," +
//            "504 深粉色," +
//            "554 白色," +
//            "555 白色," +
//            "545 白色," +
//            "222 灰色," +
//            "333 灰色," +
//            "444 灰色," +
//            "200 红色," +
//            "310 红色," +
//            "201 红色," +
//            "300 红色," +
//            "311 红色," +
//            "410 红色," +
//            "520 浅红色," +
//            "202 深红色," +
//            "301 深红色," +
//            "400 红色," +
//            "411 红色," +
//            "510 红色," +
//            "312 浅红色," +
//            "521 浅红色," +
//            "401 红色," +
//            "500 红色," +
//            "511 红色," +
//            "302 浅红色," +
//            "412 浅红色," +
//            "522 浅红色," +
//            "501 红色," +
//            "402 浅红色," +
//            "512 浅红色," +
//            "502 浅红色," +
//            "440 浅橙色," +
//            "430 深橙色," +
//            "441 浅橙色," +
//            "420 深橙色," +
//            "431 深橙色," +
//            "530 橙色," +
//            "442 浅橙色," +
//            "421 深橙色," +
//            "432 深橙色," +
//            "531 橙色," +
//            "422 深橙色," +
//            "532 橙色," +
//            "550 黄色," +
//            "540 深黄色," +
//            "551 黄色," +
//            "541 深黄色," +
//            "552 浅黄色," +
//            "542 深黄色," +
//            "553 浅黄色," +
//            "543 浅黄色," +
//            "130 青色," +
//            "230 深青色," +
//            "054 青色," +
//            "131 青色," +
//            "033 深青色," +
//            "231 深青色," +
//            "055 青色," +
//            "132 青色," +
//            "154 浅青色," +
//            "133 深青色," +
//            "232 深青色," +
//            "155 浅青色," +
//            "233 深青色," +
//            "255 浅青色," +
//            "355 浅青色," +
//            "455 浅青色," +
//            "140 青色," +
//            "141 青色," +
//            "120 深青色," +
//            "043 青色," +
//            "142 青色," +
//            "044 深青色," +
//            "121 深青色," +
//            "143 深青色," +
//            "122 深青色," +
//            "144 深青色," +
//            "045 青色," +
//            "225 深青色," +
//            "050 绿色," +
//            "150 绿色," +
//            "051 绿色," +
//            "030 深绿色," +
//            "250 绿色," +
//            "151 绿色," +
//            "052 绿色," +
//            "031 深绿色," +
//            "251 绿色," +
//            "152 绿色," +
//            "053 绿色," +
//            "350 浅绿色," +
//            "032 深绿色," +
//            "252 绿色," +
//            "153 绿色," +
//            "450 浅绿色," +
//            "351 浅绿色," +
//            "253 绿色," +
//            "451 浅绿色," +
//            "352 浅绿色," +
//            "254 绿色," +
//            "452 浅绿色," +
//            "353 浅绿色," +
//            "453 浅绿色," +
//            "354 浅绿色," +
//            "454 浅绿色," +
//            "040 绿色," +
//            "041 绿色," +
//            "020 深绿色," +
//            "240 绿色," +
//            "042 绿色," +
//            "340 深绿色," +
//            "021 深绿色," +
//            "241 绿色," +
//            "341 深绿色," +
//            "022 深绿色," +
//            "242 绿色," +
//            "342 深绿色," +
//            "243 绿色," +
//            "343 深绿色," +
//            "012 深蓝色," +
//            "034 浅蓝色," +
//            "013 深蓝色," +
//            "112 深蓝色," +
//            "035 浅蓝色," +
//            "134 浅蓝色," +
//            "113 深蓝色," +
//            "014 蓝色," +
//            "135 浅蓝色," +
//            "234 浅蓝色," +
//            "015 蓝色," +
//            "114 蓝色," +
//            "235 浅蓝色," +
//            "115 蓝色," +
//            "023 深蓝色," +
//            "002 深蓝色," +
//            "024 蓝色," +
//            "123 蓝色," +
//            "244 浅蓝色," +
//            "145 浅蓝色," +
//            "003 深蓝色," +
//            "102 深蓝色," +
//            "025 蓝色," +
//            "124 蓝色," +
//            "344 浅蓝色," +
//            "245 浅蓝色," +
//            "223 浅蓝色," +
//            "103 深蓝色," +
//            "004 蓝色," +
//            "125 蓝色," +
//            "345 浅蓝色," +
//            "224 浅蓝色," +
//            "005 蓝色," +
//            "104 蓝色," +
//            "105 蓝色," +
//            "212 深紫色," +
//            "213 深紫色," +
//            "334 浅紫色," +
//            "214 深紫色," +
//            "313 紫色," +
//            "335 浅紫色," +
//            "434 浅紫色," +
//            "215 深紫色," +
//            "314 紫色," +
//            "413 紫色," +
//            "435 浅紫色," +
//            "315 紫色," +
//            "414 紫色," +
//            "415 紫色," +
//            "323 浅紫色," +
//            "203 深紫色," +
//            "324 浅紫色," +
//            "423 浅紫色," +
//            "445 浅紫色," +
//            "204 深紫色," +
//            "303 紫色," +
//            "325 浅紫色," +
//            "424 浅紫色," +
//            "205 深紫色," +
//            "304 紫色," +
//            "403 紫色," +
//            "425 浅紫色," +
//            "305 紫色," +
//            "404 紫色," +
//            "405 紫色," +
//            "505 紫色," +
//            "220 深棕色," +
//            "330 浅棕色," +
//            "210 深棕色," +
//            "221 深棕色," +
//            "320 棕色," +
//            "331 浅棕色," +
//            "211 深棕色," +
//            "321 棕色," +
//            "332 浅棕色," +
//            "322 棕色," +
//            "443 浅棕色," +
//            "433 浅棕色 ";lic static final Map<String, String> colorMaps = new HashMap<>();
//

//    public static Map<String, String> getColorMap() {
////        String colorString =
////        System.out.println(colorString);
//
//        if (colorMaps.size() == 0) {
//            String[] split = colorString.split(",");
//            for (String colorMap : split) {
//                String[] codeAndName = colorMap.split(" ");
//                colorMaps.put(codeAndName[0], codeAndName[1]);
//            }
//        }
//        return colorMaps;
//
//    }

    public static float colorAvgVal = 53.5f;

//    public static String getAvgRGB(URL url) {
//        BufferedImage bi = ImgUtil.toBufferedImage(ImgUtil.scale(ImgUtil.read(url), 0.3f));
//        int w = bi.getWidth();
//        int h = bi.getHeight();
//        float[] dots = new float[]{0.15f, 0.35f, 0.5f, 0.7f, 0.85f};
//        int R = 0;
//        int G = 0;
//        int B = 0;
//        for (float dw : dots) {
//            for (float dh : dots) {
//                int rgbVal = bi.getRGB((int) (w * dw), (int) (h * dh));
//                Color color = ImgUtil.getColor(rgbVal);
//                R += color.getRed();
//                G += color.getGreen();
//                B += color.getBlue();
//            }
//        }
//        int cn = dots.length * dots.length;
//        Color color = new Color(R / cn, G / cn, B / cn);
//
//        int r = Math.round(color.getRed() / colorAvgVal);
//        int g = Math.round(color.getGreen() / colorAvgVal);
//        int b = Math.round(color.getBlue() / colorAvgVal);
//        String colorKey = String.format("%s%s%s", r, g, b);
//        return getColorMap().get(colorKey);
//    }

    public static String getMainRGB(String url) throws IOException {

        return ColorExtractor.getMainColor(new URL(url));
//        int height = bufferedImage.getHeight();
//        int width = bufferedImage.getWidth();
////        log.info(height+" "+width);
//        int RInt[] = new int[266];
//        int GInt[] = new int[266];
//        int BInt[] = new int[266];
//        int r = 0, g = 0, b = 0;
//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//                Color color = new Color(bufferedImage.getRGB(j, i));
//                r = color.getRed();
//                g = color.getGreen();
//                b = color.getBlue();
//                RInt[r]++;
//                GInt[g]++;
//                BInt[b]++;
//            }
//        }
//        int rm = 0, gm = 0, bm = 0;
//        int ri = 0, gi = 0, bi = 0;
////        1.0，简单计算每个像素点出现的数量，取最大值
//        /**
//         * for (int i = 0; i < 255; i++) {
//         *             if (RInt[i] > rm) {
//         *                 rm = RInt[i];
//         *                 ri = i;
//         *             }
//         *             if (GInt[i] > gm) {
//         *                 gm = GInt[i];
//         *                 gi = i;
//         *             }
//         *             if (BInt[i] > bm) {
//         *                 bm = BInt[i];
//         *                 bi = i;
//         *             }
//         *         }
//         */
//
////       分部分计算区间中像素点的数量，取最大值
//        for (int j = 0; j < 5; j++) {
//            int rSize=0;
//            int gSize=0;
//            int bSize=0;
//            for(int i=0;i<51;i++){
//                rSize+=RInt[j*51+i];
//                gSize+=GInt[j*51+i];
//                bSize+=BInt[j*51+i];
//            }
//            if(rSize>rm){
//                rm=rSize;
//                ri=j;
//            }
//            if(gSize>gm){
//                gm=gSize;
//                gi=j;
//            }if(bSize>bm){
//                bm=bSize;
//                bi=j;
//            }
//        }
//
//
////        int rx = Math.round((ri) / colorAvgVal);
////        int gx = Math.round((gi) / colorAvgVal);
////        int bx = Math.round((bi) / colorAvgVal);
//        String colorKey = String.format("%s%s%s", ri, gi, bi);
//        log.info(ri*51+" "+gi*51+" "+bi*51);
//        return getColorMap().get(colorKey);
//
    }

    public static Map<String,Long> getPicSize(URL url) throws IOException {

        BufferedImage image = ImageIO.read(url);

        int width = image.getWidth();
        int height = image.getHeight();
        long size = url.openConnection().getContentLengthLong();

        HashMap<String,Long>map=new HashMap<>();
        map.put("width", (long) width);
        map.put("height", (long) height);
        map.put("size", (long) size);


        return map;

    }

    public static String checkImg(String url)throws IOException {
        BufferedImage image = ImageIO.read(new URL(url));
        int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        for (int i = 0; i < pixels.length; i++) {
            int r = (pixels[i] >> 16) & 0xff;
            int g = (pixels[i] >> 8) & 0xff;
            int b = pixels[i] & 0xff;
            int gray = (int) (0.2989 * r + 0.5870 * g + 0.1140 * b);
            pixels[i] = gray;
        }
        INDArray input = Nd4j.create(pixels);
//        INDArray input = Nd4j.create(ImageUtil.toPixels(image));
        ZooModel zooModel = VGG16.builder().build();;
        Model net = zooModel.initPretrained(PretrainedType.IMAGENET);
        INDArray output = net.input();

        int predictedLabelIndex = Nd4j.argMax(output, 1).getInt(0);
//        String predictedLabel = labels.get(predictedLabelIndex);
        log.info(predictedLabelIndex+"");
        return null;
    }

}