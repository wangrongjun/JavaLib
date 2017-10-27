package com.wangrj.java_lib.java_util;

import java.io.IOException;

import sun.misc.BASE64Decoder;

/**
 * http://wenku.baidu.com/link?url=mFM27h3pZRHLkB-bAaZPqERS7do1qHiv8tl7EVoyWzQUvPpNsb08eMqJPMyf2sM1jxVlV8KPmgKsE_Qvo8X5fDjMu1PkEE7D1HIRw09GRSC
 * 转化qq旋风、迅雷、快车的专用下载链接为普通http的下载链接
 * 如果找不到 sun.misc.BASE64Decoder 类，用以下步骤可解决 
 * 右键项目-》属性-》java eq path-》jre System Library-》access rules-》resolution选择accessible，下面填上** 点击确定即可！！！ 
 */
public class TurnToHttpUtil {

    public static final String qqUrl = "qqdl://aHR0cDovL2Rvd24ucXEuY29tL3NnL2Z1bGwvc2dfRnVsbFZlcnNpb25fMS4xLjU4LmV4ZQ==";
    public static final String thunderUrl_1 = "Thunder://QUFodHRwOi8vaW0uYmFpZHUuY29tL2luc3RhbGwvQmFpZHVIaS5leGVaWg==";
    public static final String thunderUrl_2 = "thunder://QUFmdHA6Ly9obHRtLmNjQHd0Ny5obHRtLmNjOjE2MzI3L1slRTklQkUlOTklRTclOEYlQTAlRTglQjYlODVdWzM2XVslRTclQkElQTIlRTYlOTclODUlRTklQTYlOTYlRTUlOEYlOTF3d3cuaGx0bS5jY11bR0JdWzcyMFBdW01QNF1bJUU1JUJDJTgyJUU1JTlGJTlGJUU1JUFEJTk3JUU1JUI5JTk1JUU3JUJCJTg0XS5tcDRaWg==";
    public static final String flashGetUrl = "Flashget://W0ZMQVNIR0VUXWh0dHA6Ly9pbS5iYWlkdS5jb20vaW5zdGFsbC9CYWlkdUhpLmV4ZVtGTEFTSEdFVF0=&yinbing1986";

    public static final String divider = "//";

    public static String turn(String url) {
        String head = url.substring(0, url.indexOf(divider));
        String content = url.substring(url.indexOf(divider) + divider.length());
        String result = "";
        if ("qqdl:".equalsIgnoreCase(head)) {//为qq旋风链接    
            result = qq2http(content);
        } else if ("thunder:".equalsIgnoreCase(head)) {//为迅雷链接
            result = thunder2http(content);
        } else if ("flashget:".equalsIgnoreCase(head)) {//为快车链接    
            result = flashget2http(content);
        }

        return result;
    }

    private static String qq2http(String url) {
        try {
            byte[] bytes = new BASE64Decoder().decodeBuffer(url);
            String result = new String(bytes);
            return result;
        } catch (IOException e) {
            return null;
        }
    }

    private static String thunder2http(String url) {
        try {
            byte[] bytes = new BASE64Decoder().decodeBuffer(url);
            String result = new String(bytes);
            result = result.substring(2, result.length() - 2);
            return result;
        } catch (IOException e) {
            return null;
        }
    }

    private static String flashget2http(String url) {
        String flag = "[FLASHGET]";
        try {
            byte[] bytes = new BASE64Decoder().decodeBuffer(url);
            String result = new String(bytes);
            result = result.substring(flag.length(), result.lastIndexOf(flag));
            return result;
        } catch (IOException e) {
            return null;
        }
    }

}
