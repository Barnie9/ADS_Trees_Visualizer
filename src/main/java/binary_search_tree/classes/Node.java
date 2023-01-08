package binary_search_tree.classes;

public class Node {
    private int key;
    private Node rightChild;
    private Node leftChild;
    private Node parent;
    private int level;

    public static Node Nil = new Node(0);

    public Node(int key) {
        this.key = key;
        this.rightChild = Nil;
        this.leftChild = Nil;
        this.parent = Nil;
        this.level = 0;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String toString() {
        return "" + this.key;
    }
}
