package red_black_tree.classes;

public class RBNode {
    private int key;
    private RBNode rightChild;
    private RBNode leftChild;
    private RBNode parent;
    private int level;
    private Color color;

    public static RBNode Nil = new RBNode(0);

    public RBNode(int key) {
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

    public RBNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(RBNode rightChild) {
        this.rightChild = rightChild;
    }

    public RBNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(RBNode leftChild) {
        this.leftChild = leftChild;
    }

    public RBNode getParent() {
        return parent;
    }

    public void setParent(RBNode parent) {
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
