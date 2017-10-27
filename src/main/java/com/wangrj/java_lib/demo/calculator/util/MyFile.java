package com.wangrj.java_lib.demo.calculator.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.wangrj.java_lib.demo.calculator.bean.Sign;
import com.wangrj.java_lib.demo.calculator.constant.C;
import com.wangrj.java_lib.java_util.FileUtil;
import com.wangrj.java_lib.java_util.GsonUtil;
import com.wangrj.java_lib.demo.calculator.bean.Sign;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;


public class MyFile {

    public static String readHelp() {

        File file = new File(C.helpFilePath);
        if (!file.exists()) {
            return "";
        }

        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(
                    file));
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            isr.close();
            return builder.toString();
        } catch (IOException e) {
            return "";
        }

    }

    // 保存从Sign的code域读取的代码到cache/Fun.java下
    public static void saveCode(String code) {
        try {
            File file = new File("cache");
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File("cache/Fun.java");
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(code);
            fileWriter.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "把代码保存到.java时出错", "错误",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // 程序退出时保存ArrayList<Sign>对象到signs.data文件中
    public static void saveSigns(List<Sign> signs) {
        if (signs == null) {
            return;
        }
        try {
            FileUtil.write(GsonUtil.formatJson(signs).replace("\n", "\r\n"), C.dataFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 程序启动时读取文件signs.data的ArrayList<Sign>对象，文件不存在则返回new ArrayList<Sign>()
    public static List<Sign> readSigns() {
        String json = FileUtil.read(C.dataFileName);
        if (json == null) {
            return new ArrayList<>();
        }
        List<Sign> signList;
        try {
            signList = new Gson().fromJson(json, new TypeToken<List<Sign>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            signList = new ArrayList<>();
        }
        return signList;
    }

}
