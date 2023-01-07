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

    //  Delete
    @FXML
    private TextField deleteValue;
    @FXML
    private Button deleteButton;

    //  Search
    @FXML
    private TextField searchValue;
    @FXML
    private Button searchButton;

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
        } else if(event.getSource() == searchButton) {
            int value = Integer.parseInt(searchValue.getText());
            Node node = search(tree.getRoot(), value);
            System.out.println("Found: " + node.getKey());
            System.out.println();
            System.out.println("-----------------------");
            System.out.println();
        }
    }

    public void leftRotate(Node x) {
        Node y = x.getRightChild();
        x.setRightChild(y.getLeftChild());
        if(y.getLeftChild() != Node.Nil) {
            y.getLeftChild().setParent(x);
        }
        y.setParent(x.getParent());
        if(x.getParent() == Node.Nil) {
            tree.setRoot(y);
        } else if(x == x.getParent().getLeftChild()) {
            x.getParent().setLeftChild(y);
        } else {
            x.getParent().setRightChild(y);
        }
        y.setLeftChild(x);
        x.setParent(y);
    }

    public void rightRotate(Node x) {
        Node y = x.getLeftChild();
        x.setLeftChild(y.getRightChild());
        if(y.getRightChild() != Node.Nil) {
            y.getRightChild().setParent(x);
        }
        y.setParent(x.getParent());
        if(x.getParent() == Node.Nil) {
            tree.setRoot(y);
        } else if(x == x.getParent().getLeftChild()) {
            x.getParent().setLeftChild(y);
        } else {
            x.getParent().setRightChild(y);
        }
        y.setRightChild(x);
        x.setParent(y);
    }
    
    public Node maximum(Node w) {
        Node x = w;
        while(x.getRightChild() != Node.Nil) {
            x = x.getRightChild();
        }
        return x;
    }

    public Node minimum(Node w) {
        Node x = w;
        while(x.getLeftChild() != Node.Nil) {
            x = x.getLeftChild();
        }
        return x;
    }
    
    Node successor(Node w) {
        if(w == Node.Nil) {
            return w;
        }
        Node x = w;
        if(x.getRightChild() != Node.Nil)
            return minimum(x.getRightChild());
        Node y = x.getParent();
        while (y != Node.Nil && x == y.getRightChild()) {
            x = y;
            y = x.getParent();
        }
        return y;
    }

    Node predecessor(Node w) {
        if(w == Node.Nil) {
            return w;
        }
        Node x = w;
        if(x.getLeftChild() != Node.Nil)
            return maximum(x.getLeftChild());
        Node y = x.getParent();
        while (y != Node.Nil && x == y.getLeftChild()) {
            x = y;
            y = x.getParent();
        }
        return y;
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
        insertFixup(z);
    }

    public void insertFixup(Node z) {
        while(z.getParent().getColor() == Color.RED) {
            if(z.getParent() == z.getParent().getParent().getLeftChild()) {
                Node y = z.getParent().getParent().getRightChild();
                if (y.getColor() == Color.RED) {
                    z.getParent().setColor(Color.BLACK);
                    y.setColor(Color.BLACK);
                    z.getParent().getParent().setColor(Color.RED);
                    z = z.getParent().getParent();
                } else {
                    if (z == z.getParent().getRightChild()) {
                        z = z.getParent();
                        leftRotate(z);
                    }
                    z.getParent().setColor(Color.BLACK);
                    z.getParent().getParent().setColor(Color.RED);
                    rightRotate(z.getParent().getParent());
                }
            } else {
                Node y = z.getParent().getParent().getLeftChild();
                if(y.getColor() == Color.RED) {
                    z.getParent().setColor(Color.BLACK);
                    y.setColor(Color.BLACK);
                    z.getParent().getParent().setColor(Color.RED);
                    z = z.getParent().getParent();
                } else {
                    if (z == z.getParent().getLeftChild()) {
                        z = z.getParent();
                        rightRotate(z);
                    }
                    z.getParent().setColor(Color.BLACK);
                    z.getParent().getParent().setColor(Color.RED);
                    leftRotate(z.getParent().getParent());
                }
            }
        }
        tree.getRoot().setColor(Color.BLACK);
    }

    public Node search(Node w, int key) {
        if(w == Node.Nil || w.getKey() == key) {
            return w;
        }
        return search((key < w.getKey()) ? w.getLeftChild() : w.getRightChild(), key);
    }

    public void display(Node w, int indent) {
        if(w != Node.Nil) {
            display(w.getRightChild(), indent + 2);
            for(int i = 0; i < indent; i++) {
                System.out.print(" ");
            }
            System.out.println(w.getKey() + " " + w.getColor().toString());
            display(w.getLeftChild(), indent + 2);
        }
    }
}