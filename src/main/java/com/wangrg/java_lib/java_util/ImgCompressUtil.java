package com.wangrg.java_lib.java_util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/*
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
*/

/**
 * http://cuisuqiang.iteye.com/blog/2045855
 * <p/>
 * Java中图片压缩处理，仅能生成JPEG格式
 * <p/>
 * 注意：com.sun.image.codec.jpeg包位于JDK目录/jre/lib/rt.jar包中，它不是公开的API，
 * 需要将其复制到工程lib下，又或者把其所在的路径添加到CLASS_PATH环境变量当中。
 */
public class ImgCompressUtil {

    public static void resize(String imgPath, String outputImgPath, int newWidth, int newHeight)
            throws IOException {
        BufferedImage oldImage = ImageIO.read(new File(imgPath));
//        compress(oldImage, outputImgPath, newWidth, newHeight);
    }

    /**
     * 按照宽度或高度进行等比例压缩
     */
    public static void resizeFix(String imgPath, String outputImgPath, int width, int height)
            throws IOException {
        BufferedImage oldImage = ImageIO.read(new File(imgPath));
        int oldWidth = oldImage.getWidth();
        int oldHeight = oldImage.getHeight();

        //如果原图宽高比值比目标图大，则以为基准，等比例放缩图片
        if (1.0 * oldWidth / oldHeight >= 1.0 * width / height) {
            resizeByHeight(imgPath, outputImgPath, height);
        } else {
            resizeByHeight(imgPath, outputImgPath, width);
        }

    }

    /**
     * 以宽度为基准，等比例放缩图片
     */
    public static void resizeByWidth(String imgPath, String outputImgPath, int newWidth)
            throws IOException {
        BufferedImage oldImage = ImageIO.read(new File(imgPath));
        int oldWidth = oldImage.getWidth();
        int oldHeight = oldImage.getHeight();
        int newHeight = (int) (oldHeight * (1.0 * newWidth / oldWidth));

//        compress(oldImage, outputImgPath, newWidth, newHeight);
    }

    /**
     * 以高度为基准，等比例放缩图片
     */
    public static void resizeByHeight(String imgPath, String outputImgPath, int newHeight)
            throws IOException {
        BufferedImage oldImage = ImageIO.read(new File(imgPath));
        int oldWidth = oldImage.getWidth();
        int oldHeight = oldImage.getHeight();
        int newWidth = (int) (oldWidth * (1.0 * newHeight / oldHeight));

//        compress(oldImage, outputImgPath, newWidth, newHeight);
    }

    /**
     * @param scale 按比例缩放。
     */
    public static void resizeScale(String imgPath, String outputImgPath, double scale)
            throws IOException {

        BufferedImage oldImage = ImageIO.read(new File(imgPath));
        int oldWidth = oldImage.getWidth();
        int oldHeight = oldImage.getHeight();
        int newWidth = (int) (oldWidth * scale);
        int newHeight = (int) (oldHeight * scale);

//        compress(oldImage, outputImgPath, newWidth, newHeight);
    }
/*    
    pub static void resizeByLength(String imgPath, String outputImgPath, int maxKb) throws IOException {
        File file = new File(imgPath);
        int length = (int) (file.length() / 1024);
        while (length > maxKb) {
            double scale;
            if (length > maxKb * 2) {
                scale = 0.8;
            } else if (length > maxKb * 4) {
                scale = 0.6;
            } else if (length > maxKb * 8) {
                scale = 0.5;
            } else if (length > maxKb * 10) {
                scale = 0.4;
            } else {
                scale = 0.9;
            }
            ImgCompressUtil.resizeScale(imgPath, outputImgPath, scale);
            file = new File(file.getAbsolutePath());
            length = (int) (file.length() / 1024);
        }
    }


    pub static void compress(BufferedImage oldImage, String outputPath, int newWidth,
                                int newHeight) throws IOException {

        BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        newImage.getGraphics().drawImage(oldImage, 0, 0, newWidth, newHeight, null);

        FileOutputStream fos = new FileOutputStream(outputPath);
        JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(fos);
        jpegEncoder.encode(newImage);
        fos.closeAndOpenMainView();
    }

*/
}
