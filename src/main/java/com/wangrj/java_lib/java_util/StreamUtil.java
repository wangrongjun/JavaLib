package com.wangrj.java_lib.java_util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * by 王荣俊 on 2016/5/20.
 */
public class StreamUtil {

    public static String readInputStream(InputStream is) throws IOException {
        return readInputStream(is, null);
    }

    public static String readInputStream(InputStream is, String charsetName) throws IOException {
        if (is == null) {
            return null;
        }

        InputStreamReader isr;
        if (!TextUtil.isEmpty(charsetName)) {
            isr = new InputStreamReader(is, charsetName);
        } else {
            isr = new InputStreamReader(is);
        }

        char[] buf = new char[1024];
        StringBuilder builder = new StringBuilder();
        int len;
        while ((len = isr.read(buf)) != -1) {
            builder.append(new String(buf, 0, len));
        }
        isr.close();

        return builder.toString();
    }

    public static byte[] toBytes(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;
        while ((len = is.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }
        baos.flush();
        byte[] data = baos.toByteArray();
        baos.close();
        return data;
    }

    public static InputStream toInputStream(String text) {
        if (TextUtil.isEmpty(text)) {
            return null;
        }
        return new ByteArrayInputStream(text.getBytes());
    }

}
