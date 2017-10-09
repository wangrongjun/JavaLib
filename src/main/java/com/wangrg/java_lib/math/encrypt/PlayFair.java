package com.wangrg.java_lib.math.encrypt;

import com.wangrg.java_lib.java_util.MathUtil;
import com.wangrg.java_lib.java_util.TextUtil;
import com.wangrg.java_lib.math.matrix.Matrix;

/**
 * by 王荣俊 on 2016/10/27.
 */
public class PlayFair {

    private Matrix<String> matrix;

    /**
     * @param key 密钥，会自动把key转化为大写，删去所有非字母的符号（比如空格和标点符号）,
     *            并删除第二次出现的字母和I,J
     */
    public PlayFair(String key) throws Exception {

        if (TextUtil.isEmpty(key)) {
            throw new Exception("key is null");
        }

        matrix = new Matrix<>(5, 5, null);

        key = key.toUpperCase();
        key = key.replaceAll("[^A-Z]+", "");
        key = key.replace("I", "");
        key = key.replace("J", "");
        key = deleteRepeat(key);

        if (TextUtil.isEmpty(key)) {
            throw new Exception("key is illegal, must be letter and not contains i/j");
        }

        createMatrix(key);
    }

    /**
     * 对明文加密，会自动把明文转化为大写，删去所有非字母的符号（比如空格和标点符号）
     * 并且在个相同的字母之间插入K，插完后如果字符串长度为奇数，在尾部添加一个K。
     *
     * @param clearText 明文
     */
    public String encode(String clearText) {

        clearText = clearText.toUpperCase();
        clearText = clearText.replaceAll("[^A-Z]+", "");
        clearText = insertKToRepeatLetters(clearText);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < clearText.length(); i = i + 2) {
            String unit = encodeOrDecodeUnit(clearText.charAt(i), clearText.charAt(i + 1), true);
            builder.append(unit + " ");
        }

        return builder.toString();
    }

    /**
     * 对密文解密
     */
    public String decode(String cipher) throws Exception {

        cipher = cipher.toUpperCase();
        cipher = cipher.replace("I/J", "I");
        cipher = cipher.replace(" ", "");
        if (!cipher.matches("[A-Z]+")) {
            throw new Exception("cipher is error, has illegal char");
        }
        if (cipher.length() % 2 != 0) {
            throw new Exception("cipher is error, length is odd number");
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < cipher.length(); i = i + 2) {
            String unit = encodeOrDecodeUnit(cipher.charAt(i), cipher.charAt(i + 1), false);
            builder.append(unit);
        }

        return builder.toString();
    }

    /**
     * 对一个分组进行加密/解密，c1与c2可以相同
     */
    private String encodeOrDecodeUnit(char c1, char c2, boolean isEncode) {

        int row1 = -1;
        int column1 = -1;
        int row2 = -1;
        int column2 = -1;

        int flag = isEncode ? 1 : -1;

        //获取c1与c2在矩阵的位置
        for (int i = 0; i < matrix.getRow(); i++) {
            for (int j = 0; j < matrix.getColumn(); j++) {
                if (matrix.get(i, j).contains(c1 + "")) {
                    row1 = i;
                    column1 = j;
                }
                if (matrix.get(i, j).contains(c2 + "")) {
                    row2 = i;
                    column2 = j;
                }
            }
        }

        int n = matrix.getRow();
        if (row1 == row2) {//若分组在矩阵中同行，则循环取其右（左）边字母为密文（明文）
            column1 = (column1 + flag + n) % n;//这里加n是避免对负数求余。
            column2 = (column2 + flag + n) % n;
        } else if (column1 == column2) {//若分组在矩阵中同列，则循环取其下（上）边字母为密文（明文）
            row1 = (row1 + flag + n) % n;
            row2 = (row2 + flag + n) % n;
        } else {//若不同行不同列，则取其同行且与下（上）一个字母同列的字母为密文（明文）
            int temp = column1;
            column1 = column2;
            column2 = temp;
        }

        String s1 = matrix.get(row1, column1);
        String s2 = matrix.get(row2, column2);
        return s1 + s2;
    }

    /**
     * 创建加密辅助数组
     *
     * @param key 不能包含I或J，必须为大写，没有重复字母
     */
    private void createMatrix(String key) {

        int n = matrix.getRow();
        char c = 'A';
        for (int index = 0; index < n * n; index++) {
            if (index < key.length()) {
                matrix.set(index, key.charAt(index) + "");
            } else {
                while (key.contains(c + "")) {
                    c++;
                }
                if (c == 'I') {
                    matrix.set(index, c + "/J");
                    c++;
                } else if (c != 'J') {
                    matrix.set(index, c + "");
                }
                c++;
            }
        }
    }

    /**
     * 两个相同的字母之间插入K，插完后如果字符串长度为奇数，在尾部添加一个K。
     *
     * @param clearText 大写的明文，不能有多个连续的K
     */
    private String insertKToRepeatLetters(String clearText) {

        char previousChar = '\0';
        String result = "";
        for (int i = 0; i < clearText.length(); i++) {
            char currentChar = clearText.charAt(i);
            if (previousChar == currentChar) {
                result += "K";
            }
            result += currentChar;
            previousChar = currentChar;
        }
        if (result.length() % 2 == 1) {
            result += "K";
        }
        return result;
    }

    /**
     * 删除第二次及以上出现的字符（如aasssfca变为asfc）
     */
    private String deleteRepeat(String text) {
        if (TextUtil.isEmpty(text)) {
            return "";
        }
        String result = "";
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (!result.contains(c + "")) {
                result += c;
            }
        }
        return result;
    }

    public Matrix<String> getMatrix() {
        return matrix;
    }

    //测试PlayFair
    public static void test(String[] args) throws Exception {

        PlayFair playFair = new PlayFair("monchary");
        playFair.getMatrix().show();

        String clearText = "kk";
        System.out.println("clearText: " + clearText);

        String cipher = playFair.encode(clearText);
        System.out.println("cipher: " + cipher);

        clearText = playFair.decode(cipher);
        System.out.println("newClearText: " + clearText);

        System.out.println("\n");

        for (int i = 0; i < 50; i++) {

            int letterNumber = MathUtil.random(1, 30);
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < letterNumber; j++) {
                char c = (char) MathUtil.random('a', 'z');
                String blank = MathUtil.random(0, 4) == 0 ? " " : "";
                builder.append(c + blank);
            }

            if (i % 10 == 0) {
                playFair = new PlayFair(builder.toString());
                playFair.getMatrix().show();
                continue;
            }

            clearText = builder.toString();
            System.out.println("clearText: " + clearText);

            cipher = playFair.encode(clearText);
            System.out.println("cipher: " + cipher);

            clearText = playFair.decode(cipher);
            System.out.println("newClearText: " + clearText);

            System.out.println("\n");
        }
    }

}
