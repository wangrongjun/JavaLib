package com.wangrg.java_lib.java_program.image_maker.merge;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * by wangrongjun on 2017/3/14.
 * http://blog.csdn.net/mr__fang/article/details/8002881  BufferedImage.getRGB  绘制和提取图像
 */
public class ImageMerger {

    public static void verticalMerge() throws IOException {

        File dir = new File(".");
        File outputFile = new File("output.png");
        System.out.println("directory : " + dir.getAbsolutePath());
        System.out.println();

        File[] imageFileList = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".png") || name.endsWith(".jpg");
            }
        });

        if (imageFileList == null || imageFileList.length == 0) {
            System.out.println("image(png or jpg) not exists in current directory");
            return;
        }

        int totalHeight = 0;
        int maxWidth = 0;

        // 1.先获取最大宽度和高度总和
        for (File imageFile : imageFileList) {
            BufferedImage image = ImageIO.read(imageFile);
            int w = image.getWidth();
            int h = image.getHeight();
            if (w > maxWidth) {
                maxWidth = w;
            }
            totalHeight += h;
        }

        // 2.根据上面获得的目标图片的宽高创建其数组
        int[] outputRGB = new int[totalHeight * maxWidth];

        // 3.把每张图片依次绘制到目标图片中
        int currLine = 0;// 指向目标图像的数组的当前行
        for (File imageFile : imageFileList) {
            BufferedImage image = ImageIO.read(imageFile);
            int w = image.getWidth();
            int h = image.getHeight();
            // 若第五个参数为空（offset必须为0），则以返回值的形式获取结果，否则以指针的形式得到结果。
            // scansize是图像中相邻两行中具有相同行索引的像素的索引偏移值。一般与宽度相同。
            int[] rgb = image.getRGB(0, 0, w, h, null, 0, w);
            for (int i = 0; i < rgb.length; i++) {
                outputRGB[currLine * maxWidth + i % w] = rgb[i];
                // 如果已保存了图像当前行的最后一个像素，则目标图像保存位置移到下一行。
                if (i % w == w - 1) {
                    currLine++;
                }
            }
        }

        BufferedImage outputImage = new BufferedImage(maxWidth, totalHeight,
                BufferedImage.TYPE_INT_ARGB);
        outputImage.setRGB(0, 0, maxWidth, totalHeight, outputRGB, 0, maxWidth);
        ImageIO.write(outputImage, "png", outputFile);

        System.out.println("vertical merge " + imageFileList.length + " images");
        System.out.println("output : " + outputFile.getAbsolutePath());
    }

}
