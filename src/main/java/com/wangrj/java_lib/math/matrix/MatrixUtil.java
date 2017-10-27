package com.wangrj.java_lib.math.matrix;

import com.wangrj.java_lib.math.IOperation;
import com.wangrj.java_lib.math.fraction.Fraction;
import com.wangrj.java_lib.math.IOperation;
import com.wangrj.java_lib.math.fraction.Fraction;

import java.util.ArrayList;
import java.util.List;

/**
 * by 王荣俊 on 2016/10/27.
 */
public class MatrixUtil {

    public static List<Integer> toIntegerList(int[] intList) {
        List<Integer> integerList = new ArrayList<>();
        for (int temp : intList) {
            integerList.add(temp);
        }
        return integerList;
    }

    public static List<Double> toDoubleList(int[] intList) {
        List<Double> doubleList = new ArrayList<>();
        for (int temp : intList) {
            doubleList.add((double) temp);
        }
        return doubleList;
    }

    public static List<Fraction> toFractionList(int[] intList) {
        List<Fraction> fractionList = new ArrayList<>();
        for (int temp : intList) {
            fractionList.add(new Fraction(temp, 1));
        }
        return fractionList;
    }

    /**
     * 矩阵相乘
     *
     * @throws MatrixException 如果左矩阵的列不等于右矩阵的行，抛异常
     */
    public static <T> Matrix<T> multiply(Matrix<T> matrix1, Matrix<T> matrix2) throws MatrixException {

        if (matrix1.getColumn() != matrix2.getRow()) {//如果左矩阵的列不等于右矩阵的行，无法计算。
            throw new MatrixException("Error : matrix1(column) != matrix2(row)");
        }

        int publicLength = matrix1.getColumn();
        IOperation<T> iOperation = matrix1.getIOperation();
        Matrix<T> matrix = new Matrix<>(matrix1.getRow(), matrix2.getColumn(), iOperation);

        for (int i = 0; i < matrix.getRow(); i++) {
            for (int j = 0; j < matrix.getColumn(); j++) {
                T element = iOperation.getZero();
                for (int k = 0; k < publicLength; k++) {
                    T temp = iOperation.multiply(
                            matrix1.get(i, k),
                            matrix2.get(k, j)
                    );
                    element = iOperation.add(element, temp);
                    matrix.set(i, j, element);
                }
            }
        }

        return matrix;
    }

    /**
     * 解方程组，返回x1,x2,x3...xn
     *
     * @param variableMatrix 系数矩阵，要求行数等于列数
     * @param constantList   常数列表，要求行数与列表长度相等
     */
    public static <T> List<T> soluteEquations(Matrix<T> variableMatrix, List<T> constantList)
            throws MatrixException {

        int n = variableMatrix.getRow();
        if (n != variableMatrix.getColumn() || n != constantList.size()) {
            throw new MatrixException("soluteEquations error");
        }

        //合并系数矩阵和常数列表
        List<T> mergeMatrixList = new ArrayList<>();
        for (int i = 0; i < n * n; i++) {
            mergeMatrixList.add(variableMatrix.get(i));
            if (i % n == n - 1) {
                mergeMatrixList.add(constantList.get(i / n));
            }
        }

        //进行高斯消元，获取上三角矩阵（左下三角为0）
        Matrix<T> mergeMatrix = new Matrix<>(mergeMatrixList, n, n + 1, variableMatrix.getIOperation());
        mergeMatrix.forwardElimination();

        //反向替换法求解方程组
        return soluteEliminatedMatrix(mergeMatrix);
    }

    /**
     * 对包含系数和常数列的扩展矩阵求解
     */
    private static <T> List<T> soluteEliminatedMatrix(Matrix<T> matrix) {

        List<T> resultList = new ArrayList<>();
        int n = matrix.getRow();
        IOperation<T> iOperation = matrix.getIOperation();

        for (int i = n - 1; i >= 0; i--) {
            T temp = iOperation.getZero();
            int index = 0;
            for (int j = n - 1; j >= i; j--) {
                if (j == i) {
//                    示例代码：resultList.add((a[i][n] - temp) / a[i][j]);
                    T t = iOperation.subtract(matrix.get(i, n), temp);
                    t = iOperation.divide(t, matrix.get(i, j));
                    resultList.add(t);
                } else {
//                    示例代码：temp += a[i][j] * resultList.get(index++);
                    T t = iOperation.multiply(matrix.get(i, j), resultList.get(index++));
                    temp = iOperation.add(temp, t);
                }
            }
        }

        //把结果数组倒序
        for (int i = 0; i < n / 2; i++) {
            T temp = resultList.get(i);
            resultList.set(i, resultList.get(n - 1 - i));
            resultList.set(n - 1 - i, temp);
        }

        return resultList;
    }

    /**
     * 计算矩阵的逆
     */
    public static <T> Matrix<T> reverse(Matrix<T> matrix) throws MatrixException {

        if (matrix.getRow() != matrix.getColumn()) {
            throw new MatrixException("Error: row != column");
        }

        IOperation<T> iOperation = matrix.getIOperation();

        if (iOperation.compare(matrix.determinant(), iOperation.getZero()) == 0) {
            throw new MatrixException("Error: det = zero");
        }

        int n = matrix.getRow();
        Matrix<T> reverseMatrix = new Matrix<>(n, n, iOperation);

        for (int i = 0; i < n; i++) {
            List<T> constantList = new ArrayList<>();//单位矩阵的每一列，如{1,0,0}T, {0,1,0}T
            for (int j = 0; j < n; j++) {
                constantList.add(i == j ? iOperation.getUnit() : iOperation.getZero());
            }
            List<T> resultList = soluteEquations(matrix, constantList);//组成逆矩阵的每一列

            for (int j = 0; j < n; j++) {
                reverseMatrix.set(j, i, resultList.get(j));
            }
        }

        return reverseMatrix;
    }

}
