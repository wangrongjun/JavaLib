package com.wangrg.java_program.lrc_editior;

import com.wangrg.java_util.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Inflater;

/**
 * by wangrongjun on 2017/5/20.
 * http://blog.csdn.net/ilyysys/article/details/8818097  krc转lrc 酷狗加密歌词转换
 */

public class KrcToLrc {

    private static final char key[] = {
            '@', 'G', 'a', 'w', '^', '2', 't', 'G', 'Q', '6', '1', '-', '\316', '\322', 'n', 'i'
    };

    static class Config {
        List<String> ignoreList = Arrays.asList(
                "\\[id:[^\\]]*\\]",
                "\\[by:[^\\]]*\\]",
                "\\[al:[^\\]]*\\]",
                "\\[qq:[^\\]]*\\]",
                "\\[total:[^\\]]*\\]",
                "\\[hash:[^\\]]*\\]",
                "\\[sign:[^\\]]*\\]",
                "\\[offset:[^\\]]*\\]",
                "\\[language:[^\\]]*\\]"
        );
    }

    /* 
     * 参数：文件名 函数作用：解密转换 
     */
    public static String convert(String fileName) throws Exception {
        // 我的修改：先加载正则表
        Config config = new Config();
        String configString = FileUtil.read("IgnoreRegexList.txt");
        if (configString == null) {// 如果为空，则正则表达式文件不存在，需要创建
            configString = "";
            for (String s : config.ignoreList) {
                configString += "\r\n" + s;
            }
            configString = configString.substring(2);// 去除头部的\r\n
            FileUtil.write(configString, "IgnoreRegexList.txt");
        } else {// 否则读取正则表达式文件
            String[] strings = configString.split("[\r\n]+");
            config.ignoreList = Arrays.asList(strings);
        }

        RandomAccessFile raf = new RandomAccessFile(fileName, "r");
        byte[] content = new byte[(int) (raf.length() - 4)];
        raf.skipBytes(4);
        raf.read(content);
        raf.close();

        for (int i = 0, length = content.length; i < length; i++) {
            int j = i % 16; // 循环异或解密  
            content[i] ^= key[j];
        }

        String lrc = new String(decompress(content), "utf-8"); // 解压为 utf8  

        String finalLrc = lrc.replaceAll("<([^>]*)>", "").replaceAll(",([^]]*)]", "] ");  
        /* 处理时间标签 */
        Pattern p = Pattern.compile("\\[\\d+?\\]");
        Matcher m = p.matcher(finalLrc);
        while (m.find()) {
            finalLrc = m.replaceFirst(toTime(m.group()));
            m = p.matcher(finalLrc);
        }

        // 我的修改：去掉没必要的标签和多余的空行
        for (String regex : config.ignoreList) {
            finalLrc = finalLrc.replaceAll(regex, "");
        }
        finalLrc = finalLrc.replace("\r\n\r\n", "");
        while (!finalLrc.startsWith("[")) {
            finalLrc = finalLrc.substring(1);
        }

        return finalLrc;
    }

    private static String toTime(String num) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        long time = Long.parseLong(num.substring(1, num.length() - 1));
//        return "[" + sdf.format(time) + "." + ((time % 1000) / 10) + "]";
        // 我的修改：补0，以凑齐两位数
        long n = (time % 1000) / 10;
        return "[" + sdf.format(time) + "." + (n >= 10 ? "" : "0") + n + "]";
    }

    private static byte[] decompress(byte[] data) throws Exception {
        byte[] output;
        Inflater decompression = new Inflater();
        decompression.reset();
        decompression.setInput(data);
        ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
        byte[] buf = new byte[1024];
        while (!decompression.finished()) {
            int i = decompression.inflate(buf);
            o.write(buf, 0, i);
        }
        output = o.toByteArray();
        o.close();
        decompression.end();
        return output;
    }

}
