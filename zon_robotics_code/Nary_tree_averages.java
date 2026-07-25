import java.util.ArrayList;
import java.util.List;

public class Nary_tree_averages {
// https://leetcode.com/discuss/post/808433/n-ary-tree-averages-by-anonymous_user-0v5z/
//  You are given a n-tree, you need to calculate the averages of all the children and itself. 
// You need to get the parent node average including its children that is the highest.

// In the tree above the highest average is 11 (12+10/2) for a branch in middle. 
// For node 10 at the root the average is 7.2 so it is not highest. 
// This is easy part,do dfs and run averages at each level.

// What is the efficient way to store this tree and its averages if there are frequent changes to the node values and addition of more nodes? And you will be requested for the highest average child frequently after the change to the tree.
// Another follow up, how do you efficiently know the highest average child at a particular level and below it. (not considering the whole tree).

// You can imagine that the tree is very large.
    static class Node {
        int value;
        List<Node> children;

        Node(int value) {
            this.value = value;
            this.children = new ArrayList<>();
        }

        void addChild(Node child) {
            children.add(child);
        }
    }

    static class SubtreeInfo {
        long sum;
        int count;

        SubtreeInfo(long sum, int count) {
            this.sum = sum;
            this.count = count;
        }
    }

    private Node maxAverageNode;
    private double maxAverage;

    public Node findMaxAverageNode(Node root) {
        if (root == null) {
            return null;
        }

        maxAverageNode = null;
        maxAverage = Double.NEGATIVE_INFINITY;

        dfs(root);

        return maxAverageNode;
    }

    private SubtreeInfo dfs(Node node) {
        long subtreeSum = node.value;
        int subtreeCount = 1;

        for (Node child : node.children) {
            SubtreeInfo childInfo = dfs(child);

            subtreeSum += childInfo.sum;
            subtreeCount += childInfo.count;
        }

        double average = (double) subtreeSum / subtreeCount;

        // 截图说找 parent node，所以 leaf 不参与比较
        if (!node.children.isEmpty() && average > maxAverage) {
            maxAverage = average;
            maxAverageNode = node;
        }

        return new SubtreeInfo(subtreeSum, subtreeCount);
    }

    public double getMaxAverage() {
        return maxAverage;
    }

    public static void main(String[] args) {
        /*
                         10
                       /  |  \
                      9  12   6
                     / \   \ /|\
                    6   7 10 5 4 3
        */

        Node root = new Node(10);

        Node n9 = new Node(9);
        Node n12 = new Node(12);
        Node n6Right = new Node(6);

        root.addChild(n9);
        root.addChild(n12);
        root.addChild(n6Right);

        n9.addChild(new Node(6));
        n9.addChild(new Node(7));

        n12.addChild(new Node(10));

        n6Right.addChild(new Node(5));
        n6Right.addChild(new Node(4));
        n6Right.addChild(new Node(3));

        Nary_tree_averages solution = new Nary_tree_averages();

        Node result = solution.findMaxAverageNode(root);

        System.out.println("Max average node: " + result.value);
        System.out.println("Max average: " + solution.getMaxAverage());
    }
}
