package red_black_tree.classes;

public class RBTree {
    private Node root;

    public RBTree() {
        this.root = Node.Nil;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }
}
