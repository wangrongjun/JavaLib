package com.wangrg.java_lib.demo.calculator.constant;

import java.io.File;

public class C {

    public static String path = new File("").getAbsolutePath();
    public static String dataFileName = "signList.txt";
    public static String binDir = C.path + "/cache/";
    public static String javaFilePath = C.path + "/cache/" + "Fun.java";
    public static String classFilePath = C.path + "/cache/" + "Fun.class";
    public static String helpFilePath = C.path + "/help.txt";

    public static String classFileName = "Fun";

    public static int UNKNOW_SIGN = -3;
    public static int UNKNOW_ERROR = -4;
    public static int ZERO = -2;

    public static int OK = 3;
    public static int ERROR = -1;

    public static int SQSTACK_MAXSIZE = 100;

    public static String preCode = "public class Fun implements com.wangrg.java_lib.demo.calculator.util.Compiler.FunInterface{"
            + " @Override " + "public double fun(double n1, double n2) {";

    public static String postCode = "}}";

    public static String TIP_PRIOR_ERROR = "��������ȷ�����ȼ�";
    public static String TIP_SIGN_ERROR = "�����ֻ����һ���ַ����Ҳ��������֣�С���㣬���ź�Сд��ĸ";
    public static String TIP_SIGN_EXISTED_ERROR = "������Ѵ���";
    public static String TIP_COMPILE_ERROR = "�������,�޷�����";
    public static String TIP_ERROR = "��������ȷ����Ϣ";
    public static String TIP_NEW_SUCCESS = "�½��ɹ�";
    public static String TIP_UPDATE_SUCCESS = "�޸ĳɹ�";

    public static String MSG_ERROR = "���ʽ��ֵ����";


    public static String welcomeWord = "��ӭʹ��Ӣ������� v1.0";
    public static String programmerInfo = "���ߣ�С���μ�\nQQ:915249493";

    public static String tip1_1 = "ע��1.�����ֻ����һ���ַ����Ҳ���";
    public static String tip1_2 = "           �����֣�С���㣬���ź�Сд��ĸ";
    public static String tip2_1 = "        2.ѡ��Ŀʱ��ֻ��n1��Ч�������ȼ�";
    public static String tip2_2 = "           ����������Ѷ����˫Ŀ�������";

    public static int maxPrior = 20;
    public static int minPrior = 1;

}
