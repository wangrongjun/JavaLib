package com.wangrj.java_lib.math.combination;

import com.wangrj.java_lib.java_util.ComparatorUtil;
import com.wangrj.java_lib.java_util.ListUtil;
import com.wangrj.java_lib.java_util.ComparatorUtil;
import com.wangrj.java_lib.java_util.ListUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * by wangrongjun on 2017/9/2.
 * <p>
 * 排列 http://blog.csdn.net/joylnwang/article/details/7064115
 */
public class ArrangementUtil {

    @Test
    public void testRecursive() {
        List<List<Integer>> list = arrangementRecursive(ListUtil.build(
                1, 2, 3
        ));
        print(list);
    }

    @Test
    public void testDictionarySequence() {
        List<List<Integer>> list = arrangementDictionarySequence(
                ListUtil.build(4, 1, 2, 3, 8, 7, 6, 5),
                ComparatorUtil.IntegerComparator);
        print(list);
    }

    public static <T> void print(List<List<T>> list) {
        for (int i = 0; i < list.size(); i++) {
            List<T> strings = list.get(i);
            StringBuilder builder = new StringBuilder((i + 1) + ":\t");
            for (T string : strings) {
                builder.append(string).append(" ");
            }
            System.out.println(builder.toString());
        }
    }

    /**
     * 递归算法实现排列
     * <p>
     * 以abc为例：
     * 1.以a开头后面跟着(b,c)的排列，(b,c)的排列可以用相同的方法获得。
     * 2.以b开头后面跟着(a,c)的排列，(a,c)的排列可以用相同的方法获得。
     * 3.以c开头后面跟着(a,b)的排列，(a,b)的排列可以用相同的方法获得。
     * 4.把以上三步的排列汇总，就得到abc的排列。
     */
    public static <T> List<List<T>> arrangementRecursive(List<T> list) {
        /*
         * 41238765 ->
         * 1. 41(23)8765  k=2
         * 2. 41(23)8765
         */

        List<List<T>> resultList = new ArrayList<>();

        // 如果长度为1或0，就不必进行递归，可以直接返回结果
        if (list.size() == 0) {
            return resultList;
        }
        if (list.size() == 1) {
            resultList.add(list);
            return resultList;
        }

        for (int i = 0; i < list.size(); i++) {// 遍历abc，接下来将以a开头为例进行讲解
            T first = list.get(i);// 元素a

            List<T> remainList = ListUtil.clone(list);// 余下的元素列表bc
            remainList.remove(i);
            List<List<T>> remainCombList = arrangementRecursive(remainList);// bc的排列

            for (List<T> combList : remainCombList) {// 遍历bc,cb
                combList.add(0, first);// combList变为abc,acb(循环第一次为abc，第二次为acb)
                resultList.add(combList);// 把abc,acb加进结果集
            }
        }

        return resultList;
    }

    /**
     * 字典序算法实现排列 // TODO 未完成，有Bug
     * <p>
     * 思路：比abcde大一级的是abced，比abced大一级的是abdce，比abdce大一级的是abdec，以此类推。
     * <p>
     * 以13876542为例生成大一级的14235678：
     * 1.找到所有满足a[k]<a[k+1](0<=k<n-1)的k的最大值。如果k不存在，则说明当前排列已经是最大者，程序结束。这里k=1。
     * 2.在k位置之后的元素中，找到比a[k]小的最大值a[j]，并与a[k]互换。这里a[k]=4，a[j]=3，两者互换，变为14876532。
     * 3.把k位置之后的元素倒序，此时的排列就是比之前大一级的排列。这里从a[k]后面倒序，变为14235678。
     * <p>
     * 优点：
     * 1.不受重复元素的影响。例如1224，交换中间的两个2，实际上得到的还是同一个排列。
     * 2.避免了递归，避免了创建多个数组，降低空间和时间的复杂度。
     * <p>
     * 缺点：
     * 1.元素必须是可比较的。
     * 2.必须先排序，否则得不到部排列。
     */
    public static <T> List<List<T>> arrangementDictionarySequence(List<T> list, Comparator<T> comparator) {
        List<List<T>> resultList = new ArrayList<>();
        list = ListUtil.clone(list);// 避免修改了原数组
        resultList.add(ListUtil.clone(list));
        int k;
        while ((k = findK(list, comparator)) != -1) {
            int j = k;
            for (int i = k + 1; i < list.size(); i++) {
                if (comparator.compare(list.get(k), list.get(i)) == 1) {// a[k]>a[i]
                    // 如果a[i]>a[j]，就把j更新，保证a[j]是比a[k]小的最大值
                    if (comparator.compare(list.get(i), list.get(j)) == 1) {
                        j = i;
                    }
                }
            }
            if (j != k) {// 如果找到比a[k]小的最大值a[j]，就互换
                ListUtil.swap(list, j, k);
            } else {// 如果找不到，a[k]和a[k+1]互换
                ListUtil.swap(list, k, k + 1);
            }
            /*
            0123456789  size=10
            1.k=4  i=5,6    k+1到4+(10-4-1)/2  swap(5,9) swap(6,8)
            2.k=3  i=4,5,6  k+1到3+(10-3-1)/2  swap(4,9) swap(5,8) swap(6,7)
            通式：k+1到k+(size-k-1)/2  swap(i,size-(i-k))
             */
            for (int i = k + 1; i <= k + (list.size() - k - 1) / 2; i++) {// 把k位置之后的元素倒序
                ListUtil.swap(list, i, list.size() - (i - k));
            }
            resultList.add(ListUtil.clone(list));
        }

        return resultList;
    }

    private static <T> int findK(List<T> list, Comparator<T> comparator) {
        int result = -1;
        for (int i = 0; i < list.size() - 1; i++) {
            if (comparator.compare(list.get(i), list.get(i + 1)) == -1) {// a[i]<a[i+1]
                result = i;
            }
        }
        return result;
    }

}
