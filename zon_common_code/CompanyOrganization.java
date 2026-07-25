// As part of managing the organizational structure at Amazon, 
// you have been tasked with designing an algorithm to serialize and deserialize 
// the company’s hierarchical organization. 
// Your objective is to create a solution that can efficiently 
// convert the organizational structure into a string representation and vice versa, 
// allowing you to print the hierarchy in a desired order.

// A hierarchical representation was given as an example Abc —- def —- ghjgv

// Abc could have more then 1 child node similarly for all other nodes.
import java.util.ArrayList;
import java.util.List;

public class CompanyOrganization {

    static class Node {
        String val;
        List<Node> children;

        public Node(String val) {
            this.val = val;
            this.children = new ArrayList<>();
        }

        public void addChild(Node child) {
            children.add(child);
        }
    }

    // Preorder:
    // current node -> all children -> "#"
    public String serialize(Node root) {
        if (root == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        sb.append(root.val).append(",");

        for (Node child : root.children) {
            sb.append(serialize(child));
        }

        // 当前节点的所有 children 结束
        sb.append("#").append(",");

        return sb.toString();
    }

    private int idx;

    public Node deserialize(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }

        String[] nodes = str.split(",");
        idx = 0;

        return buildTree(nodes);
    }

    private Node buildTree(String[] nodes) {
        if (idx >= nodes.length) {
            return null;
        }

        // 当前 token 是节点值
        Node node = new Node(nodes[idx]);
        idx++;

        // 直到遇到当前节点对应的 "#"
        while (idx < nodes.length && !nodes[idx].equals("#")) {
            node.children.add(buildTree(nodes));
        }

        // 跳过当前节点的 "#"
        idx++;

        return node;
    }

    public void printHierarchy(Node root) {
        printHierarchy(root, 0);
    }

    private void printHierarchy(Node node, int depth) {
        if (node == null) {
            return;
        }

        for (int i = 0; i < depth; i++) {
            System.out.print("    ");
        }

        System.out.println(node.val);

        for (Node child : node.children) {
            printHierarchy(child, depth + 1);
        }
    }

    public static void main(String[] args) {
        CompanyOrganization solution = new CompanyOrganization();

        Node abc = new Node("Abc");
        Node def = new Node("Def");
        Node xyz = new Node("Xyz");
        Node ghjgv = new Node("Ghjgv");
        Node john = new Node("John");

        abc.addChild(def);
        abc.addChild(xyz);

        def.addChild(ghjgv);
        def.addChild(john);

        String serialized = solution.serialize(abc);

        System.out.println("Serialized:");
        System.out.println(serialized);

        Node restored = solution.deserialize(serialized);

        System.out.println("\nRestored hierarchy:");
        solution.printHierarchy(restored);
    }
}