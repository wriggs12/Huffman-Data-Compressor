/**
 * @aurthor Winston Rigs
 * @version 9/4/20
 *
 * Node class with a value, frequency, left/right child, visited, and a parent
 */

public class Node {
    private String value;
    private int freq;
    private Node left;
    private Node right;
    private Node parent;
    private boolean visited;

    Node(String v, int f) {
        this.value = v;
        this.freq = f;
        left = right = null;
        parent = null;
        visited = false;
    }

    public void setLeft(Node l) {
        left = l;
    }

    public void setRight(Node r) {
        right = r;
    }

    public void setParent(Node p) {
        parent = p;
    }

    public void visit() {
        visited = true;
    }

    public int getFreq() {
        return freq;
    }

    public String getVal() {
        return value;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public Node getParent() {
        return parent;
    }

    public boolean hasBeenVisited() {
        return visited;
    }
}
