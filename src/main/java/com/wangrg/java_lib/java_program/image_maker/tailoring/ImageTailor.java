package com.wangrg.java_lib.java_program.image_maker.tailoring;

import com.wangrg.java_lib.java_util.TextUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * by wangrongjun on 2017/7/19.
 */

public class ImageTailor {

    public static void main(String[] args) throws IOException {
        verticalTailoring(2);
    }

    /**
     * @param n 纵向分成n分
     */
    public static void verticalTailoring(int n) throws IOException {
        File dir = new File(".");
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

        for (File imageFile : imageFileList) {
            String prefixName = TextUtil.getTextExceptLastPoint(imageFile.getName());//文件名（无前缀）
            String extendName = TextUtil.getTextAfterLastPoint(imageFile.getName());//后缀名（无点）
            BufferedImage image = ImageIO.read(imageFile);
            int width = image.getWidth();
            int heightEach = image.getHeight() / n;//每个小图片的高度
            for (int i = 0; i < n; i++) {
                int[] rgb = image.getRGB(0, heightEach * i, width, heightEach, null, 0, width);
                BufferedImage outputImage = new BufferedImage(width, heightEach,
                        BufferedImage.TYPE_INT_ARGB);
                outputImage.setRGB(0, 0, width, heightEach, rgb, 0, width);
                File outputFile = new File(prefixName + "(" + (i + 1) + ")." + extendName);
                ImageIO.write(outputImage, "png", outputFile);
            }
            System.out.println("image vertical tailoring succeed : " + imageFile.getName());
        }

    }

}