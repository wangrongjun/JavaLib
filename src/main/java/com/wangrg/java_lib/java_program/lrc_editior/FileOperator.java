package com.wangrg.java_lib.java_program.lrc_editior;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileOperator {

    /**
     * 注意：使用bufferedReader.readLine得到的每一句话会在后面人为地加上 "\r\n"
     */
    public static String readFile(File file, String charsetName)
            throws Exception {
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, charsetName);
            BufferedReader br = new BufferedReader(isr);
            String line = "";
            StringBuilder builder = new StringBuilder();

            while ((line = br.readLine()) != null) {
                builder.append(line + "\r\n");
            }

            br.close();
            isr.close();
            fis.close();

            return builder.toString();

        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

    public static void saveFile(File file, String charsetName, String text)
            throws Exception {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osr = new OutputStreamWriter(fos, charsetName);
            BufferedWriter bw = new BufferedWriter(osr);
            bw.write(text);

            bw.close();
            osr.close();
            fos.close();

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
