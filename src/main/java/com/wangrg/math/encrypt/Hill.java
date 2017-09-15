package com.wangrg.math.encrypt;

import com.wangrg.java_util.MathUtil;
import com.wangrg.java_util.TextUtil;
import com.wangrg.math.IOperation;
import com.wangrg.math.matrix.Matrix;
import com.wangrg.math.matrix.MatrixException;
import com.wangrg.math.matrix.MatrixUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * by 王荣俊 on 2016/10/27.
 */
public class Hill {

    private int m;//连续的明文字母，也是加密密钥矩阵的维度。
    private Matrix<Integer> keyMatrix;
    private Matrix<Integer> reverseKeyMatrix;

    /**
     * @param keyMatrix 加密密钥矩阵，必须行等于列，det必须为1
     */
    public Hill(Matrix<Integer> keyMatrix) throws Exception {
        if (keyMatrix == null || keyMatrix.getRow() != keyMatrix.getColumn()) {
            throw new Exception("Error: row != column");
        }
        this.keyMatrix = keyMatrix;
        if (keyMatrix.determinant() == 1) {
            calculateReverseKeyMatrix();
        } else {
            throw new Exception("Error: det != 1");
        }
        m = keyMatrix.getRow();
    }

    public String encode(String text) throws Exception {

        text = text.replace(" ", "");//先去掉空格
        if (TextUtil.isEmpty(text)) {
            throw new Exception("Error: text is null or illegal");
        }

        while (text.length() % m != 0) {//如果分组时不能分成m个一组，补充字母x直到可以分组
            text += "x";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length() / m; i++) {
            String textUnit = "";
            for (int j = 0; j < m; j++) {
                textUnit += text.charAt(i * m + j);
            }
            String s = encodeOrDecodeUnit(textUnit, true);
            builder.append(s);
        }
        return builder.toString();
    }

    public String decode(String text) throws Exception {
        text = text.replace(" ", "");//先去掉空格
        if (TextUtil.isEmpty(text) || text.length() % m != 0) {
            throw new Exception("Error: text is null or length%m != 0");
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length() / m; i++) {
            String textUnit = "";
            for (int j = 0; j < m; j++) {
                textUnit += text.charAt(i * m + j);
            }
            String s = encodeOrDecodeUnit(textUnit, false);
            System.out.println(textUnit + "  " + s);
            builder.append(s);
        }
        return builder.toString();
    }

    /**
     * 对长度为m的单位字符串进行加密/解密
     */
    public String encodeOrDecodeUnit(String unitText, boolean isEncode) throws Exception {

        if (unitText == null || unitText.length() != m) {
            throw new Exception("Error: length != m");
        }

        //如果unitText="pay" 那么textMatrix=(15, 0, 24)T
        List<Integer> textMatrixList = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            textMatrixList.add(toNumber(unitText.charAt(i)) % 26);
        }
        Matrix<Integer> textMatrix = new Matrix<>(textMatrixList, m, 1, IOperation.integerIOperation);

        //如果unitText="pay" 那么resultMatrix=(11, 13, 18)，这几个数由keyMatrix和textMatrix决定。
        Matrix<Integer> resultMatrix =
                MatrixUtil.multiply(isEncode ? keyMatrix : reverseKeyMatrix, textMatrix);

        String s = "";
        for (int i = 0; i < m; i++) {
//            s += toLetter(resultMatrix.get(i, 0) % 26);
            s += toLetter(resultMatrix.get(i, 0) % 26);
        }
        return s;
    }

    /**
     * 计算解密用的逆矩阵
     */
    private void calculateReverseKeyMatrix() throws MatrixException {

        reverseKeyMatrix = MatrixUtil.reverse(keyMatrix);
        reverseKeyMatrix.iterator(new Matrix.Iterator<Integer>() {
            @Override
            public void next(int i, int j, int index, Integer element) {
                while (element < 0) {
                    element += 26;
                }
                element = element % 26;
                reverseKeyMatrix.set(i, j, element);
            }
        });
    }

    private char toLetter(int number) {
        return (char) (number + 'a');
    }

    private int toNumber(char letter) {
        return letter - 'a';
    }

    public Matrix<Integer> getReverseKeyMatrix() {
        return reverseKeyMatrix;
    }


    public static void test(String[] args) throws Exception {

        Matrix<Integer> keyMatrix = new Matrix<>(MatrixUtil.toIntegerList(new int[]{
                1, 7, 3, 2,
                0, 1, 4, 3,
                0, 0, 1, 9,
                0, 0, 0, 1
        }), 4, 4, IOperation.integerIOperation);

        keyMatrix.show();
        System.out.println();

        Hill hill = new Hill(keyMatrix);

        hill.getReverseKeyMatrix().show();
        System.out.println();

//        String text = "we will attack tomorrow six";
        for (int i = 0; i < 30; i++) {
            String text = "";
            for (int j = 0; j < MathUtil.random(1, 30); j++) {
                text += (char) MathUtil.random('a', 'z');
            }

            String encode = hill.encode(text);
            String decode = hill.decode(encode);

            System.out.println("  text: " + text);
            System.out.println("encode: " + encode);
            System.out.println("decode: " + decode);
            System.out.println();
        }
    }
}
