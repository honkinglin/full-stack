package com.fullstack.mall.utils;

import java.io.IOException;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

/**
 * 描述：     图片工具类
 */
public class ImageUtil {

    public static void main(String[] args) throws IOException {
        String path = "/Users/kyrie/Desktop/";
        //切割
        Thumbnails.of(path + "f13-1.png").sourceRegion(Positions.BOTTOM_RIGHT, 200, 200).size(200, 200).toFile(path + "crop.jpg");

        //缩放
        Thumbnails.of(path + "f13-1.png").scale(0.7).toFile(path + "scale1.jpg");
        Thumbnails.of(path + "f13-1.png").scale(1.5).toFile(path + "scale2.jpg");
        Thumbnails.of(path + "f13-1.png").size(500, 500).keepAspectRatio(false).toFile(path + "size1.jpg");
        Thumbnails.of(path + "f13-1.png").size(500, 500).keepAspectRatio(true).toFile(path + "size2.jpg");

        //旋转
        Thumbnails.of(path + "f13-1.png").size(500, 500).keepAspectRatio(true).rotate(90).toFile(path + "rotate1.jpg");
        Thumbnails.of(path + "f13-1.png").size(500, 500).keepAspectRatio(true).rotate(180).toFile(path + "rotate2.jpg");

    }


}
