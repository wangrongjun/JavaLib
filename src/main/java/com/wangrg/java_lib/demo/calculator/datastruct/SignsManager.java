package com.wangrg.java_lib.demo.calculator.datastruct;

import com.wangrg.java_lib.demo.calculator.bean.Sign;
import com.wangrg.java_lib.demo.calculator.util.MyFile;

import java.util.ArrayList;
import java.util.List;


public class SignsManager {

    private static List<Sign> signs = MyFile.readSigns();
    private static SignsManager signsManager = new SignsManager();

    private SignsManager() {

    }

    public static SignsManager create(ArrayList<Sign> s) {
        signs = s;
        return signsManager;
    }

    public static SignsManager create() {
        return signsManager;
    }

    public static List<Sign> getSigns() {
        return signs;
    }

    // public static void setSigns(ArrayList<Sign> s) {
    // signs = s;
    // }

    public static boolean isEmpty() {
        return signs.isEmpty();
    }

    public static void add(String strSign, String name, int operateNum,
                           int prior, String code) {
        Sign sign = new Sign();
        sign.setSign(strSign);
        sign.setName(name);
        sign.setOperateNum(operateNum);
        sign.setPrior(prior);
        sign.setCode(code);
        signs.add(sign);
    }

    public static void update(String sign, String name, int operateNum,
                              int prior, String code) {
        int i = search(sign);
        if (i != -1) {
            Sign s = new Sign();
            s.setName(name);
            s.setSign(sign);
            s.setOperateNum(operateNum);
            s.setPrior(prior);
            s.setCode(code);
            signs.set(i, s);
        }

    }

    public static Sign getSignByStrSign(String sign) {

        int i;
        for (i = 0; i < signs.size(); i++) {
            if (signs.get(i).getSign().equals(sign)) {
                return signs.get(i);
            }
        }

        return null;
    }

    public static int getPriorBySign(String sign) {
        Sign s = getSignByStrSign(sign);
        if (s == null) {
            return -1;
        }
        return s.getPrior();
    }

    public static int getOperateNumBySign(String sign) {
        Sign s = getSignByStrSign(sign);
        if (s == null) {
            return 0;
        }
        return s.getOperateNum();
    }

    /**
     * 括号默认是符号
     */
    public static boolean isSign(String sign) {
        if (sign == null) {
            return false;
        }
        if (sign.equals("(") || sign.equals(")")) {
            return true;
        }

        return search(sign) != -1;
    }

    public static void delete(String sign) {
        int i = search(sign);
        if (i != -1) {
            signs.remove(i);
        }
    }

    public static boolean exist(String sign) {
        int i = search(sign);
        return i != -1 ? true : false;
    }

    private static int search(String sign) {
        for (int i = 0; i < signs.size(); i++) {
            if (signs.get(i).getSign().equals(sign)) {
                return i;
            }
        }
        return -1;
    }
}
