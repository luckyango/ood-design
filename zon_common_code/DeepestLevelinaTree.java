// Given a tree (not necessarily binary), return its maximum depth.

import java.util.*;

public class DeepestLevelinaTree {
    public static class TreeNode {
        int val; 
        List<TreeNode> children;
        public TreeNode(int val){
            this.val = val;
            children = new ArrayList<>();
        }
    }
    // depth - the number of edges from root to the farthest leaf node
    public int maxDepth(TreeNode node){
        if(node == null) return -1;
        int maxChildPath = -1;
        for(TreeNode child: node.children){
            maxChildPath = Math.max(maxChildPath, maxDepth(child));
        }
        return maxChildPath+1;
    }
    public TreeNode findLowestCommonAncestor(TreeNode root, TreeNode n1, TreeNode n2){
        if(n1==null || n2==null || root==null) return null;
        if(root == n1 || root == n2) return root;
        // if we can find both n1 & n2 in the children list -> the current node is the common ancestor
        // if we can only find n1 or n2, return the found node
        // if cant find, return null
        TreeNode found = null; int count = 0;
        for(TreeNode child: root.children){
            // if(findN1 && findN2) return root;
            TreeNode cur = findLowestCommonAncestor(child, n1, n2);
            // if(cur == n1) findN1 = true;
            // if(cur == n2) findN2 = true;
            if(cur != null){
                found = cur; count++;
            }
            if(count==2) return root;
        }
        return found;
    }
}
