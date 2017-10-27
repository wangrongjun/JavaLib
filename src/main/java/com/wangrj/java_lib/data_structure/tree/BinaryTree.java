package com.wangrj.java_lib.data_structure.tree;

import com.wangrj.java_lib.data_structure.Queue;

import java.util.ArrayList;
import java.util.List;

/**
 * by wangrongjun on 2017/5/9.
 */
public class BinaryTree<T> {

    private T data;
    private BinaryTree<T> leftChild;
    private BinaryTree<T> rightChild;

    public BinaryTree() {
    }

    public BinaryTree(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public BinaryTree<T> getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(BinaryTree<T> leftChild) {
        this.leftChild = leftChild;
    }

    public BinaryTree<T> getRightChild() {
        return rightChild;
    }

    public void setRightChild(BinaryTree<T> rightChild) {
        this.rightChild = rightChild;
    }

    /**
     * 遍历者
     */
    public interface Visitor<T> {
        void visit(BinaryTree<T> node);
    }

    /**
     * 先序遍历
     */
    public void perOrderTraverse(Visitor<T> visitor) {
        visitor.visit(this);
        if (leftChild != null) leftChild.perOrderTraverse(visitor);
        if (rightChild != null) rightChild.perOrderTraverse(visitor);
    }

    /**
     * 中序遍历
     */
    public void inOrderTraverse(Visitor<T> visitor) {
        if (leftChild != null) leftChild.inOrderTraverse(visitor);
        visitor.visit(this);
        if (rightChild != null) rightChild.inOrderTraverse(visitor);
    }

    /**
     * 后序遍历
     */
    public void postOrderTraverse(Visitor<T> visitor) {
        if (leftChild != null) leftChild.postOrderTraverse(visitor);
        if (rightChild != null) rightChild.postOrderTraverse(visitor);
        visitor.visit(this);
    }

    /**
     * 层序遍历
     */
    public void levelOrderTraverse(Visitor<T> visitor) {
        Queue<BinaryTree<T>> queue = new Queue<>();
        queue.add(this);
        while (!queue.isEmpty()) {
            BinaryTree<T> binaryTree = queue.poll();
            visitor.visit(binaryTree);
            if (binaryTree.getLeftChild() != null) {
                queue.add(binaryTree.getLeftChild());
            }
            if (binaryTree.getRightChild() != null) {
                queue.add(binaryTree.getRightChild());
            }
        }
    }

    public BinaryTree<T> lastNode() {
        final BinaryTree<T>[] lastNode = new BinaryTree[1];
        levelOrderTraverse(new Visitor<T>() {
            @Override
            public void visit(BinaryTree<T> node) {
                lastNode[0] = node;
            }
        });
        return lastNode[0];
    }

    /**
     * 得到参照完全二叉树来遍历的列表。对应到完全二叉树位置，如果没有，则在列表中用null表示。
     * 本质就是BinaryTreeBuilder.createByLevelOrderList的逆运算。
     * <p/>
     * 算法：对于队列出来的节点，如果不是最后一个节点，不管左右孩子是否为空，都把左右孩子加入队列中。
     * 如果是最后一个节点，则马上结束循环
     */
    public List<BinaryTree<T>> getCompleteLevelOrderTraverseList() {
        List<BinaryTree<T>> resultList = new ArrayList<>();
        Queue<BinaryTree<T>> queue = new Queue<>();
        queue.add(this);
        BinaryTree<T> lastNode = lastNode();
        while (!queue.isEmpty()) {
            BinaryTree<T> binaryTree = queue.poll();
            resultList.add(binaryTree);
            if (lastNode.equals(binaryTree)) {
                break;
            }
            if (binaryTree != null) {
                BinaryTree<T> leftChild = binaryTree.getLeftChild();
                queue.add(leftChild);
                queue.add(binaryTree.getRightChild());
            }
        }

        // 在nodeList后面补nul，使得size==2^k-1
        int n = 1;
        while (n < resultList.size()) {
            n *= 2;
        }
        while (resultList.size() < n - 1) {// 现在的n就是2^k
            resultList.add(null);
        }

        return resultList;
    }

    /**
     * 获取层数（深度）
     */
    public int depth() {
        return depth(this);
    }

    private static int depth(BinaryTree binaryTree) {
        if (binaryTree == null) {
            return 0;
        }
        int leftChildDepth = depth(binaryTree.getLeftChild());
        int rightChildDepth = depth(binaryTree.getRightChild());
        return Math.max(leftChildDepth, rightChildDepth) + 1;
    }

    /**
     * 获取某个节点所在的层数
     * 算法：先求出参照完全二叉树，该节点的位置p，再根据log2(p)
     */
    public int getFloor(BinaryTree<T> node) {
        List<BinaryTree<T>> list = getCompleteLevelOrderTraverseList();
        for (int i = 0; i < list.size(); i++) {
            if (node.equals(list.get(i))) {
                int floor = 1;
                int temp = 1;// 1,2,4,8...
                int sum = 1;// 1,3,7,15...
                while (sum < i + 1) {
                    floor += 1;
                    temp *= 2;
                    sum += temp;
                }
                return floor;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        String result = "";

        final int[] a = {0};
        perOrderTraverse(new Visitor<T>() {
            @Override
            public void visit(BinaryTree<T> binaryTree) {
                T data = binaryTree.getData();
                if (data != null) {
                    int length = data.toString().length();
                    if (a[0] < length) {
                        a[0] = length;
                    }
                }
            }
        });

        int lengthOfEachNode = a[0];
        System.out.println(print(lengthOfEachNode));

        return result;
    }

    private String print(int lengthOfEachNode) {
        int depth = depth();// 树的层数
        List<String> rowList = new ArrayList<>();
        for (int i = 0; i < depth * 2; i++) {
            rowList.add("");
        }

        print(this, this, rowList, lengthOfEachNode);

        String result = "";
        for (int i = 0; i < rowList.size(); i++) {
            if (i < rowList.size() - 1) {// 不要最后一行
                result += rowList.get(i) + "\r\n";
            }
        }

        return result;
    }

    private static <T> int print(BinaryTree<T> root,
                                 BinaryTree<T> currentNode,
                                 List<String> rowList,
                                 int maxLengthContent) {
        if (currentNode == null) {
            return 0;
        }

        int currentFloor = root.getFloor(currentNode);
        int dataRowPosition = currentFloor * 2 - 1 - 1;
        String dataRow = rowList.get(dataRowPosition);
        int underLineRowPosition = currentFloor * 2 - 1;
        String underLineRow = rowList.get(underLineRowPosition);

        // 画左孩子
        int leftChildWidth = print(root, currentNode.getLeftChild(), rowList, maxLengthContent);
        // 给左孩子一个maxLengthContent宽的纵向空格条作为分割条
        for (int i = underLineRowPosition; i < rowList.size(); i++) {
            rowList.set(i, rowList.get(i) + space(maxLengthContent));
        }
        // 画右孩子
        int rightChildWidth = print(root, currentNode.getRightChild(), rowList, maxLengthContent);

        int childWidth = Math.max(leftChildWidth, rightChildWidth);

        // 画dataRow的空格，数据，空格
        String data = (String) currentNode.getData();
        dataRow = dataRow +
                space(childWidth) +
                data + space(maxLengthContent - data.length()) +
                space(childWidth);// 保证dataRow新增的长度一定等于maxLengthContent

        // 画underLineRow的空格，下划线，纵向分割线，下划线，空格。
        // 其中space=underLine-2，space+underLine=childWidth。
        underLineRow = underLineRow +
                space(childWidth / 2 - 1) +
                (currentNode.getLeftChild() != null ? underLine(childWidth / 2 + 1) : space(childWidth / 2 + 1)) +
                (currentNode.getLeftChild() != null || currentNode.getRightChild() != null ?
                        verticalLine(maxLengthContent) : space(maxLengthContent)) +
                (currentNode.getRightChild() != null ? underLine(childWidth / 2 + 1) : space(childWidth / 2 + 1)) +
                space(childWidth / 2 - 1);

        rowList.set(dataRowPosition, dataRow);
        rowList.set(underLineRowPosition, underLineRow);

        return childWidth + maxLengthContent + childWidth;
    }

    private static String space(int n) {
        String result = "";
        for (int i = 0; i < n; i++) {
            result += " ";
        }
        return result;
    }

    private static String underLine(int n) {
        String result = "";
        for (int i = 0; i < n; i++) {
            result += "_";
        }
        return result;
    }

    private static String verticalLine(int n) {
        String result = "";
        for (int i = 0; i < n; i++) {
            result += "|";
        }
        return result;
    }

}
