package red_black_tree.classes;

public class Node {
    private int key;
    private Node rightChild;
    private Node leftChild;
    private Node parent;
    private int level;
    private Color color;

    public static Node Nil = new Node(0);

    public Node(int key) {
        this.key = key;
        this.rightChild = Nil;
        this.leftChild = Nil;
        this.parent = Nil;
        this.level = 0;
        this.color = Color.BLACK;
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String toString() {
        return "" + this.key;
    }
}
