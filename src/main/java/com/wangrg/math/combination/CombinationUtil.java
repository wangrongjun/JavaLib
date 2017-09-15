package com.wangrg.math.combination;

import com.wangrg.java_util.ComparatorUtil;
import com.wangrg.java_util.ListUtil;
import com.wangrg.math.sort.SortHelper;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;

/**
 * by wangrongjun on 2017/9/3.
 */
public class CombinationUtil {

    public static class Group<T> {
        private List<T> list;
        private int count;

        public Group(List<T> list, int count) {
            this.list = list;
            this.count = count;
        }

        public List<T> getList() {
            return list;
        }

        public Group setList(List<T> list) {
            this.list = list;
            return this;
        }

        public int getCount() {
            return count;
        }

        public Group setCount(int count) {
            this.count = count;
            return this;
        }
    }

    @Test
    public void test() {
        List<List<Integer>> combination = combination(
                ListUtil.build(1, 2, 3),
                2,
                ComparatorUtil.IntegerComparator
        );
        ArrangementUtil.print(combination);
    }

    /**
     * 组合
     * <p>
     * 思路：排列筛选。例如元素1,2,3，限定取两个。先进行排列，得到123,132,213,231,312,321。
     * 然后取前两列，得到12,13,21,23,31,32。最后去重(最好先排序)，得到12,13,23。
     */
    public static <T> List<List<T>> combination(List<T> list, int count, Comparator<T> elementComparator) {
        // TODO 为了提高效率，使用字典序排列来代替递归排列
        List<List<T>> arrangementList = ArrangementUtil.arrangementRecursive(list);// 1,2,3的排列
        // 遍历所有排列结果，例如第一次遍历到排列123，第二次132
        for (List<T> arrangement : arrangementList) {
            while (arrangement.size() > count) {// 截取前2列。arrangement=12
                arrangement.remove(arrangement.size() - 1);
            }
        }
        Comparator<List<T>> listComparator = getListComparator(elementComparator);
        // 排序
        SortHelper.sortMerge(arrangementList, listComparator);
        // 去重 12,13,21,23,31,32 变为 12,13,23
        int i = 0;// 指向前移时的位置
        for (int j = 1; j < arrangementList.size(); j++) {
            // 遇到相同的跳过，不同的则加到前面
            if (listComparator.compare(arrangementList.get(j), arrangementList.get(i)) != 0) {// a[j]!=a[i]
                arrangementList.set(++i, arrangementList.get(j));// a[++i] = a[j]
            }
        }
        while (arrangementList.size() > i + 1) {
            arrangementList.remove(arrangementList.size() - 1);
        }
        return arrangementList;
    }

    private static <T> Comparator<List<T>> getListComparator(final Comparator<T> comparator) {
        return new Comparator<List<T>>() {
            @Override
            public int compare(List<T> listA, List<T> listB) {
                // 先对listA和listB进行排序。因为12和21是相等的
                SortHelper.sortMerge(listA, comparator);
                SortHelper.sortMerge(listB, comparator);
                // 如果某一位的内容不相同，那么就一定会执行循环中的return
                for (int i = 0; i < listA.size() && i < listB.size(); i++) {
                    int k;
                    if ((k = comparator.compare(listA.get(i), listB.get(i))) != 0) {// a[i]!=b[i]
                        return k;
                    }
                }
                // 来到这里说明一个list是另一个的前缀，需要通过长度来比较大小
                if (listA.size() < listB.size()) {
                    return -1;
                } else if (listA.size() > listB.size()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };
    }

    /**
     * 分组组合
     * <p>
     * 例如(1,2|1),(3,4,5|2),(6,7|2)的组合：13467,13476,13567,13576,14567,14576,23467,23476...
     */
    public static <T> List<List<T>> combinationByGroup(List<Group<T>> groupList) {
        // TODO 分组组合
        return null;
    }

}
