package red_black_tree.user_interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import red_black_tree.classes.Color;
import red_black_tree.classes.Node;
import red_black_tree.classes.RBTree;

public class RedBlackTreeController {
    private RBTree tree = new RBTree();

    //  Insert
    @FXML
    private TextField insertValue;
    @FXML
    private Button insertButton;

    @FXML
    void buttonPressed(ActionEvent event) {
        if(event.getSource() == insertButton) {
            int value = Integer.parseInt(insertValue.getText());
            Node node = new Node(value);
            insert(node);
            display(tree.getRoot(), 1);
            insertValue.setText("");
            System.out.println();
            System.out.println("-----------------------");
            System.out.println();
        }
    }

    public void insert(Node z) {
        Node y = Node.Nil;
        Node x = tree.getRoot();
        while(x != Node.Nil) {
            y = x;
            x = (z.getKey() < x.getKey()) ? x.getLeftChild() : x.getRightChild();
        }
        z.setParent(y);
        if(y == Node.Nil) {
            tree.setRoot(z);
        } else if(z.getKey() < y.getKey()) {
            y.setLeftChild(z);
        } else {
            y.setRightChild(z);
        }
        z.setLeftChild(Node.Nil);
        z.setRightChild(Node.Nil);
        z.setColor(Color.RED);
    }

    public void display(Node w, int indent) {
        if(w != Node.Nil) {
            display(w.getRightChild(), indent + 2);
            for(int i = 0; i < indent; i++) {
                System.out.print(" ");
            }
            System.out.println(w.getKey());
            display(w.getLeftChild(), indent + 2);
        }
    }
}