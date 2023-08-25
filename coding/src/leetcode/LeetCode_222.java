package leetcode;

import leetcode.dependency.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 222. 完全二叉树的节点个数
 * https://leetcode.cn/problems/count-complete-tree-nodes/description/
 * <p>
 * 给你一棵 完全二叉树 的根节点 root ，求出该树的节点个数。
 * 完全二叉树 的定义如下：在完全二叉树中，除了最底层节点可能没填满外，其余每层节点数都达到最大值，并且最下面一层的节点都集中在该层最左边的若干位置。
 * 若最底层为第 h 层，则该层包含 1~ 2h 个节点。
 */

public class LeetCode_222 {
    public int countNodes(TreeNode root) {
        if (root == null) {
            return 0;
        }

        return traverse(root);
    }

    public int traverse(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int left = 0;
        TreeNode leftNode = root.left;
        // 求左子树深度
        while (leftNode != null) {
            left++;
            leftNode = leftNode.left;
        }

        int right = 0;
        TreeNode rightNode = root.right;
        // 求右子树深度
        while (rightNode != null) {
            right++;
            rightNode = rightNode.right;
        }
        // 左右深度相等，说明是完全满二叉树
        if (left == right) {
            // 使用幂运算，2^n - 1 来计算节点个数
            return (int) Math.pow(2, left + 1) - 1;
        }

        // 如果不是满的，则继续遍历左右节点。
        return traverse(root.left) + traverse(root.right) + 1;
    }
}
