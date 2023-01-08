package binary_search_tree.classes;

public class BSTree {
    private Node root;

    public BSTree() {
        this.root = Node.Nil;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }
}
