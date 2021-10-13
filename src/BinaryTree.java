/**
 * @author Winston Riggs
 * @version 9/4/20
 *
 * Binary Tree class with a root using the Node class
 */

public class BinaryTree {
    private Node root;

    BinaryTree() {
        root = null;
    }

    BinaryTree(Node n) {
        root = n;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node r) {
        root = r;
    }
}
