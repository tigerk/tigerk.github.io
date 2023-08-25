package leetcode;

import leetcode.dependency.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 222. 完全二叉树的节点个数
 * https://leetcode.cn/problems/count-complete-tree-nodes/description/
 *
 * 给你一棵 完全二叉树 的根节点 root ，求出该树的节点个数。
 * 完全二叉树 的定义如下：在完全二叉树中，除了最底层节点可能没填满外，其余每层节点数都达到最大值，并且最下面一层的节点都集中在该层最左边的若干位置。
 * 若最底层为第 h 层，则该层包含 1~ 2h 个节点。
 */

public class LeetCode_222 {
    public int countNodes(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int count = 0;

        Queue<TreeNode> capacity = new LinkedList<>();
        capacity.add(root);
        while (!capacity.isEmpty()) {
            int size = capacity.size();
            count = count + size;
            for (int i = 0; i < size; i++) {
                TreeNode cur = capacity.poll();
                if (cur != null && cur.left != null) {
                    capacity.add(cur.left);
                }
                if (cur != null && cur.right != null) {
                    capacity.add(cur.right);
                }
            }
        }

        return count;
    }
}
