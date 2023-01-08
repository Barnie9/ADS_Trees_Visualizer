package red_black_tree.classes;

public class RBTree {
    private RBNode root;

    public RBTree() {
        this.root = RBNode.Nil;
    }

    public RBNode getRoot() {
        return root;
    }

    public void setRoot(RBNode root) {
        this.root = root;
    }
}
