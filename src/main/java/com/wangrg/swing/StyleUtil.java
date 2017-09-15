package com.wangrg.swing;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

/**
 * by wangrongjun on 2017/9/15.
 */
public class StyleUtil {

    /**
     * 统一设置字体，父界面设置之后，所有由父界面进入的子界面都不需要再次设置字体
     * 注意：InitGlobalFont这个方法需要在创建JFrame对象之前调用。
     * <p>
     * http://blog.csdn.net/chenxuejiakaren/article/details/7637731 -  java swing 界面统一设置字体样式
     *
     * @param font new Font("Dialog",1,15)dialog：Dialog代表字体，1代表样式(1是粗体，0是平常的），15是字号
     *             new Font("alias", Font.PLAIN, 12)
     */
    public static void InitGlobalFont(Font font) {
        FontUIResource fontRes = new FontUIResource(font);
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }

}
