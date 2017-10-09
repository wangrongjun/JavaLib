package com.wangrg.java_lib.math;


import java.util.Comparator;
import java.util.List;

/**
 * by 王荣俊 on 2016/10/15.
 */
public class Heap<T> {

    public static int basicOperationCount = 0;

    private List<T> list;
    private Comparator<T> comparator;
    private int begin;//数组中堆的起始位置（即堆的范围从begin到heapList.heapSize-1）

    public Heap(List<T> list, Comparator<T> comparator) {
        this.list = list;
        this.comparator = comparator;
        begin = 0;
        bottomUp();
        basicOperationCount = 0;
    }

    public int heapSize() {
        return list.size() - begin;
    }

    public T popTop() {
        if (heapSize() == 0) {
            return null;
        }
        T t = list.get(begin);
        begin++;
        bottomUp();
        return t;
    }

    /**
     * 自底向上建堆
     */
    private void bottomUp() {
        for (int parent = heapSize() / 2 - 1; parent >= 0; parent--) {
            upBottom(parent);
        }
    }

    /**
     * 以parent为顶点进行自顶向下的优先级调整。
     */
    private void upBottom(int parent) {
        while (true) {
            basicOperationCount++;
            int leftChild = parent * 2 + 1;
            int rightChild = parent * 2 + 2;
            int max = parent;
            if (leftChild < heapSize() && comparator.compare(
                    getHeapItem(leftChild), getHeapItem(max)) == 1) {
                //如果左孩子存在且左孩子比max（这里是parent）大
                max = leftChild;
            }
            if (rightChild < heapSize() && comparator.compare(
                    getHeapItem(rightChild), getHeapItem(max)) == 1) {
                //如果右孩子存在且右孩子比max（parent或leftChild）大
                max = rightChild;
            }
            if (max != parent) {//如果左孩子或右孩子比双亲大，需要把双亲A与较大的孩子B交换，
                // 交换后需要继续对以A为顶点的子堆作调整（即不结束循环）
                swap(parent, max);
                parent = max;
            } else {
                break;
            }
        }
    }

    /**
     * 获取某个堆元素
     *
     * @param index 0到heapSize()-1
     */
    private T getHeapItem(int index) {
        return list.get(begin + index);
    }

    /**
     * 设置某个堆元素
     *
     * @param index 0到heapSize()-1
     */
    private void setHeapItem(int index, T t) {
        list.set(begin + index, t);
    }

    private void swap(int index1, int index2) {
        T t1 = getHeapItem(index1);
        T t2 = getHeapItem(index2);
        setHeapItem(index1, t2);
        setHeapItem(index2, t1);
    }

}
