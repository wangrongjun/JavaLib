package com.wangrg.java_lib.math.matrix;

import com.wangrg.java_lib.math.IOperation;

import java.util.ArrayList;
import java.util.List;

/**
 * by 王荣俊 on 2016/10/27.
 */
public class Matrix<T> {

    private List<T> matrix;
    private int row;
    private int column;
    private IOperation<T> iOperation;

    public Matrix(int row, int column, IOperation<T> iOperation) {
        matrix = new ArrayList<>();
        for (int i = 0; i < row * column; i++) {
            matrix.add(null);
        }
        this.row = row;
        this.column = column;
        this.iOperation = iOperation;
    }

    public Matrix(List<T> matrix, int row, int column, IOperation<T> iOperation) {
        this.matrix = matrix;
        this.row = row;
        this.column = column;
        this.iOperation = iOperation;
    }

    public T get(int i, int j) {
        return matrix.get(i * column + j);
    }

    public T get(int index) {
        return matrix.get(index);
    }

    public void set(int i, int j, T element) {
        matrix.set(i * column + j, element);
    }

    public void set(int index, T element) {
        matrix.set(index, element);
    }

    public void swapRow(int row1, int row2) {
        for (int i = 0; i < column; i++) {
//            示例代码：temp = a[row1][i]; a[row1][i] = a[row2][i]; a[row2][i] = temp;
            T temp = get(row1, i);
            set(row1, i, get(row2, i));
            set(row2, i, temp);
        }
    }

    /**
     * @param complete 是否深度克隆。若是。则会克隆矩阵里的元素对象。
     */
    public Matrix<T> clone(boolean complete) {
        List<T> newMatrix = new ArrayList<>();
        for (T element : matrix) {
            if (complete) {
                newMatrix.add(iOperation.clone(element));
            } else {
                newMatrix.add(element);
            }
        }
        return new Matrix<>(newMatrix, row, column, iOperation);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public IOperation<T> getIOperation() {
        return iOperation;
    }

    /**
     * 高斯前向消去法
     *
     * @throws MatrixException
     */
    public int forwardElimination() throws MatrixException {

        int swapRowCount = 0;//高斯前向消去过程中交换行的次数

        int row = getRow();
        int column = getColumn();
        int len = row < column ? row : column;

        //以第i行为主元行，对i+1,i+2...len-1行进行消元操作
        for (int i = 0; i < len; i++) {

            //使用部分选主元法，每次都去找第i列系数的绝对值最大的行，避免scale过大或者分母为0
            int maxRow = i;
            for (int p = maxRow + 1; p < len; p++) {
//                代码：if(a[maxRow][i] < a[j][i]) maxRow = j;
                if (iOperation.compare(get(maxRow, i), get(p, i)) == -1) {
                    maxRow = p;
                }
            }
            if (maxRow != i) {
                swapRow(maxRow, i);//把当前主元行与选出的maxRow整行交换
                swapRowCount++;
            }

            //从第j列开始，参照主元行从第i个元素开始（因为前i-1个已为0）进行操作,会把第i个元素变为0
            for (int j = i + 1; j < row; j++) {

                T scale = iOperation.divide(get(j, i), get(i, i));

                //第i行为主元行，第j行为需要操作的行，对第j行从i开始的每个元素进行操作
                for (int k = i; k < column; k++) {
                    /*
                        // scale = 操作行需要变为0的元素 / 主元行与主对角线相交的元素
                        int scale = a[j][i] / a[i][i];
                        // 操作行 = 操作行 - 主元行 * scale
                        a[j][k] = a[j][k] - a[i][k] * scale;
                    */
                    T result = iOperation.multiply(get(i, k), scale);
                    result = iOperation.subtract(get(j, k), result);
                    set(j, k, result);
                }

//                set(j, i, scale);//如果有这行，则为LU分解矩阵

            }
        }

        return swapRowCount;
    }

    /**
     * 计算矩阵的行列式
     * <p/>
     * 第一类初等变换（换行换列）使行列式变号，第二类初等变换（某行或某列乘k倍）使行列式变k倍，
     * 第三类初等变换（某行（列）乘k倍加到另一行（列））使行列式不变。
     */
    public T determinant()
            throws MatrixException {

        if (getRow() != getColumn()) {
            throw new MatrixException("Error : row != column");
        }

        Matrix<T> cloneMatrix = clone(false);
        int swapRowCount = cloneMatrix.forwardElimination();
        T result = cloneMatrix.get(0, 0);
        for (int i = 1; i < cloneMatrix.getRow(); i++) {
            result = iOperation.multiply(result, cloneMatrix.get(i, i));
        }
        if (swapRowCount % 2 == 1) {
            iOperation.opposite(result);
        }
        return result;
    }

    public interface Iterator<T> {
        void next(int i, int j, int index, T element);
    }

    public void iterator(Iterator<T> iterator) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                iterator.next(i, j, i * column + j, get(i, j));
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                builder.append(get(i, j)).append("\t\t");
                if (j == column - 1) {
                    builder.append("\n\n");
                }
            }
        }
        return builder.toString();
    }

    public void show() {
        System.out.println(toString());
    }

}
