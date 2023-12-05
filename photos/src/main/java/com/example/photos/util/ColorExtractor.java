package com.example.photos.util;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.text.StrBuilder;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static com.example.photos.constant.CommonConstant.SIX;
import static com.example.photos.constant.CommonConstant.THREE;

/**
 * @Auther: Arrow
 * @Date: 2023/6/27
 * @Description: 获取图片中最多3种主要颜色
 */
public class ColorExtractor {

    public static String getMainColor(URL url) throws IOException {

        BufferedImage image = ImgUtil.toBufferedImage(ImgUtil.scale(ImgUtil.read(url), 0.3f));
        List<Color> pixels = getPixels(image); // 获取所有像素

        // 进行聚类
        int k = SIX; // 聚类数量
        List<Color> centroids = kMeans(pixels, k);

        // 计算每个聚类的权重
        Map<Color, Integer> weights = new HashMap<>();
        for (Color pixel : pixels) {
            Color centroid = getClosestCentroid(pixel, centroids);
            weights.put(centroid, weights.getOrDefault(centroid, 0) + 1);
        }

        // 按权重排序，获取主要颜色
        Collections.sort(centroids, new Comparator<Color>() {
            @Override
            public int compare(Color o1, Color o2) {
                return weights.getOrDefault(o2, 0)-weights.getOrDefault(o1, 0);
            }
        });
        Set<String>colorNameList=new HashSet<>();

        for (Color centroid:centroids) {
            if(colorNameList.size()>=THREE){
                break;
            }
            colorNameList.add(getClosestColorName(centroid));
        }
        StrBuilder strBuilder = new StrBuilder();
        for(String s:colorNameList){
            strBuilder.append("_"+s);
        }
        strBuilder.del(0,1);

        System.out.println("主要颜色为：" +strBuilder.toString());
        return strBuilder.toString();
    }
    // 获取离指定颜色最近的颜色名称
    private static String getClosestColorName(Color color) {
        Map<Double, String> colorMap = new TreeMap<>();
        colorMap.put(getColorDistance(color, Color.RED), "红色");
        colorMap.put(getColorDistance(color, new Color(255, 0, 63)), "红色");
        colorMap.put(getColorDistance(color, new Color(255, 165, 0)), "橙色");
        colorMap.put(getColorDistance(color, new Color(255, 173, 51)), "橙色");
        colorMap.put(getColorDistance(color, new Color(255, 215, 0)), "黄色");
        colorMap.put(getColorDistance(color, new Color(255, 234, 0)), "黄色");
        colorMap.put(getColorDistance(color, Color.yellow), "黄色");
        colorMap.put(getColorDistance(color, new Color(50, 205, 50)), "绿色");
        colorMap.put(getColorDistance(color, new Color(29 ,179 ,29)), "绿色");
        colorMap.put(getColorDistance(color, Color.green), "绿色");
        colorMap.put(getColorDistance(color, new Color(0, 235, 232)), "青色");
        colorMap.put(getColorDistance(color, new Color(28, 178, 176)), "青色");
        colorMap.put(getColorDistance(color, Color.BLUE), "蓝色");
        colorMap.put(getColorDistance(color, new Color(0, 128, 240)), "蓝色");
        colorMap.put(getColorDistance(color, new Color(13, 62, 232)), "蓝色");

        colorMap.put(getColorDistance(color, new Color(128, 0, 128)), "紫色");
        colorMap.put(getColorDistance(color, new Color(238, 56, 243)), "淡紫色");
        colorMap.put(getColorDistance(color, Color.BLACK), "黑色");
        colorMap.put(getColorDistance(color, Color.WHITE), "白色");
        colorMap.put(getColorDistance(color,new Color(239, 239, 239)),"白色");
        colorMap.put(getColorDistance(color, new Color(187, 187, 187)), "灰色");
        colorMap.put(getColorDistance(color, new Color(248, 193, 255)), "粉色");
        colorMap.put(getColorDistance(color, new Color(139, 69, 19)), "棕色");
        return colorMap.values().iterator().next();
    }

    // 计算两个颜色之间的距离
    private static double getColorDistance(Color color1, Color color2) {
        return Math.sqrt(Math.pow(color1.getRed() - color2.getRed(), 2)
                + Math.pow(color1.getGreen() - color2.getGreen(), 2)
                + Math.pow(color1.getBlue() - color2.getBlue(), 2));
    }

    // 获取图像中所有像素的颜色
    private static List<Color> getPixels(BufferedImage image) {
        List<Color> pixels = new ArrayList<>();
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                pixels.add(new Color(image.getRGB(x, y)));
            }
        }
        return pixels;
    }

    // 使用K-means算法进行聚类
    private static List<Color> kMeans(List<Color> pixels, int k) {
        List<Color> centroids = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            centroids.add(pixels.get((int) (Math.random() * pixels.size())));
        }
        while (true) {
            List<List<Color>> clusters = new ArrayList<>(k);
            for (int i = 0; i < k; i++) {
                clusters.add(new ArrayList<>());
            }

            for (Color pixel : pixels) {
                Color centroid = getClosestCentroid(pixel, centroids);
                clusters.get(centroids.indexOf(centroid)).add(pixel);
            }
            List<Color> newCentroids = new ArrayList<>();
            for (List<Color> cluster : clusters) {
                if (cluster.isEmpty()) {
                    // 如果一个簇为空，随机选择一个像素作为新的中心点
                    newCentroids.add(pixels.get((int) (Math.random() * pixels.size())));
                } else {
                    // 计算簇内所有像素的平均值作为新的中心点
                    int redSum = 0;
                    int greenSum = 0;
                    int blueSum = 0;
                    for (Color pixel : cluster) {
                        redSum += pixel.getRed();
                        greenSum += pixel.getGreen();
                        blueSum += pixel.getBlue();
                    }
                    int red = redSum / cluster.size();
                    int green = greenSum / cluster.size();
                    int blue = blueSum / cluster.size();
                    newCentroids.add(new Color(red, green, blue));
                }
            }
            if (newCentroids.equals(centroids)) {
                // 如果新中心点和旧中心点相同，说明聚类已经收敛
                break;
            } else {
                centroids = newCentroids;
            }
        }

        return centroids;
    }

    // 获取离指定颜色最近的中心点
    private static Color getClosestCentroid(Color color, List<Color> centroids) {
        Color closestCentroid = null;
        double minDistance = Double.MAX_VALUE;
        for (Color centroid : centroids) {
            double distance = Math.sqrt(Math.pow(color.getRed() - centroid.getRed(), 2)
                    + Math.pow(color.getGreen() - centroid.getGreen(), 2)
                    + Math.pow(color.getBlue() - centroid.getBlue(), 2));
            if (distance < minDistance) {
                closestCentroid = centroid;
                minDistance = distance;
            }
        }
        return closestCentroid;
    }

}