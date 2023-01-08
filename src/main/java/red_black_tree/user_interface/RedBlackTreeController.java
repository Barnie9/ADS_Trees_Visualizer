package red_black_tree.user_interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import red_black_tree.classes.Color;
import red_black_tree.classes.Node;
import red_black_tree.classes.RBTree;

import java.util.ArrayList;
import java.util.List;

public class RedBlackTreeController {
    private RBTree tree = new RBTree();
    private int currentStep = -1;
    private List<ScrollPane> steps = new ArrayList<>();

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

    //  Content
    @FXML
    private AnchorPane content;

    //  Footer Buttons
    @FXML
    private Button firstStepButton;
    @FXML
    private Button prevStepButton;
    @FXML
    private Button nextStepButton;
    @FXML
    private Button lastStepButton;

    @FXML
    void buttonPressed(ActionEvent event) {
        if(event.getSource() == insertButton) {
            int value = Integer.parseInt(insertValue.getText());
            Node node = new Node(value);
            insert(node);

            System.out.println(depth(tree.getRoot()));
            System.out.println(sumPower(depth(tree.getRoot())));
            createStep(depth(tree.getRoot()), sumPower(depth(tree.getRoot())));

            display(tree.getRoot(), 1);
            insertValue.setText("");

            System.out.println();
            System.out.println("----------------------------------------------");
            System.out.println();
        } else if(event.getSource() == deleteButton) {
            int value = Integer.parseInt(deleteValue.getText());
            Node node = search(tree.getRoot(), value);
            delete(node);

            generateLevels(tree.getRoot(), 1);

            display(tree.getRoot(), 1);
            deleteValue.setText("");

            System.out.println();
            System.out.println("----------------------------------------------");
            System.out.println();
        } else if(event.getSource() == searchButton) {
            int value = Integer.parseInt(searchValue.getText());
            Node node = search(tree.getRoot(), value);
            searchValue.setText("");

            System.out.println("Found: " + node.getKey());
            System.out.println();
            System.out.println("----------------------------------------------");
            System.out.println();
        } else if(event.getSource() == prevStepButton) {
            if(currentStep > 0) {
                currentStep--;
                for(int i = 0; i < steps.size(); i++) {
                    if(i != currentStep) {
                        steps.get(i).setVisible(false);
                    }
                }
                steps.get(currentStep).setVisible(true);
            }
        } else if(event.getSource() == nextStepButton) {
            if(currentStep < steps.size() - 1) {
                currentStep++;
                for(int i = 0; i < steps.size(); i++) {
                    if(i != currentStep) {
                        steps.get(i).setVisible(false);
                    }
                }
                steps.get(currentStep).setVisible(true);
            }
        } else if(event.getSource() == firstStepButton) {
            if (currentStep != 0) {
                currentStep = 0;
                for(int i = 1; i < steps.size(); i++) {
                        steps.get(i).setVisible(false);
                }
                steps.get(currentStep).setVisible(true);
            }
        } else if(event.getSource() == lastStepButton) {
            if (currentStep != steps.size() - 1) {
                currentStep = steps.size() - 1;
                for(int i = 0; i < steps.size() - 1; i++) {
                    steps.get(i).setVisible(false);
                }
                steps.get(currentStep).setVisible(true);
            }
        }
    }

    public GridPane initializeGridPane(int rows, int columns) {
        GridPane gridPane = new GridPane();
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                AnchorPane anchorPane = new AnchorPane();
                anchorPane.setPrefHeight(60);
                anchorPane.setPrefWidth(60);

                gridPane.add(anchorPane, j, i);
            }
        }
        if((700 - (columns * 30)) > 0) {
            gridPane.setLayoutX(700 - (columns * 30));
        } else {
            gridPane.setLayoutX(0);
        }
        return gridPane;
    }

    public AnchorPane initializeAnchorPane() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefWidth(1390);
        anchorPane.setPrefHeight(590);
        anchorPane.setLayoutX(0);
        anchorPane.setLayoutY(0);
        return anchorPane;
    }

    public ScrollPane initializeScrollPane() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefWidth(1400);
        scrollPane.setPrefHeight(600);
        scrollPane.setLayoutX(0);
        scrollPane.setLayoutY(0);
        scrollPane.setPannable(true);
        return scrollPane;
    }

    public void createStep(int rows, int columns) {
        generateLevels(tree.getRoot(), 1);

        ScrollPane scrollPane = initializeScrollPane();

        AnchorPane anchorPane = initializeAnchorPane();

        GridPane gridPane = initializeGridPane(rows, columns);

        // gridPane.setGridLinesVisible(true);

        generateGrid(anchorPane, gridPane, tree.getRoot(), 0, columns - 1);
        anchorPane.getChildren().add(gridPane);
        scrollPane.setContent(anchorPane);
        content.getChildren().add(scrollPane);

        steps.add(scrollPane);
        currentStep = steps.size() - 1;
    }

    public void generateGrid(AnchorPane pane, GridPane gridPane, Node node, int left, int right) {
        Circle circle = new Circle();
        circle.setRadius(20);
        circle.setCenterX(30);
        circle.setCenterY(30);
        circle.setStrokeWidth(2);
        circle.setStrokeType(StrokeType.INSIDE);
        if(node.getColor() == Color.BLACK) {
            circle.setStroke(javafx.scene.paint.Color.BLACK);
            circle.setFill(javafx.scene.paint.Color.GREY);
        } else {
            circle.setStroke(javafx.scene.paint.Color.DARKRED);
            circle.setFill(javafx.scene.paint.Color.RED);
        }

        Label label = new Label(node.getKey() + "");
        label.setTextFill(javafx.scene.paint.Color.WHITE);
        label.setPrefHeight(40);
        label.setPrefWidth(40);
        label.setLayoutX(10);
        label.setLayoutY(10);
        label.setAlignment(Pos.CENTER);
        label.setFont(new Font(18));

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefHeight(60);
        anchorPane.setPrefWidth(60);
        anchorPane.getChildren().addAll(circle, label);

        int column = left + ((right - left) / 2);
        int row = node.getLevel() - 1;

        gridPane.add(anchorPane, column, row);

        if(node.getLeftChild() != Node.Nil) {
            Line line = new Line();
            line.setStroke(javafx.scene.paint.Color.BLACK);
            line.setStrokeWidth(2);
            line.setLayoutX(gridPane.getLayoutX() + (left + ((column - 1 - left) / 2)) * 60 + 30);
            line.setLayoutY(gridPane.getLayoutY() + (row + 1) * 60 + 30);
            line.setStartX(0);
            line.setStartY(0);
            line.setEndX((column - (left + ((column - left - 1) / 2))) * 60);
            line.setEndY(-60);
            pane.getChildren().add(line);

            generateGrid(pane, gridPane, node.getLeftChild(), left, column - 1);
        }
        if(node.getRightChild() != Node.Nil) {
            Line line = new Line();
            line.setStroke(javafx.scene.paint.Color.BLACK);
            line.setStrokeWidth(2);
            line.setLayoutX(gridPane.getLayoutX() + ((column + 1) + ((right - column - 1) / 2)) * 60 + 30);
            line.setLayoutY(gridPane.getLayoutY() + (row + 1) * 60 + 30);
            line.setStartX(0);
            line.setStartY(0);
            line.setEndX((column - (left + ((column - left - 1) / 2))) * (-60));
            line.setEndY(-60);
            pane.getChildren().add(line);

            generateGrid(pane, gridPane, node.getRightChild(), column + 1, right);
        }
    }

    public void generateLevels(Node x, int level) {
        if(x != Node.Nil) {
            generateLevels(x.getLeftChild(), level + 1);
            generateLevels(x.getRightChild(), level + 1);
            x.setLevel(level);
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

    public void delete(Node z) {
        Node y = (z.getLeftChild() == Node.Nil || z.getRightChild() == Node.Nil) ? z : predecessor(z);
        Node x = (y.getLeftChild() != Node.Nil) ? y.getLeftChild() : y.getRightChild();
        x.setParent(y.getParent());
        if(y.getParent() == Node.Nil) {
            tree.setRoot(x);
        } else {
            if(y == y.getParent().getLeftChild()) {
                y.getParent().setLeftChild(x);
            } else {
                y.getParent().setRightChild(x);
            }
        }
        if(y != z) {
            z.setKey(y.getKey());
        }
        if(y.getColor() == Color.BLACK) {
            deleteFixup(x);
        }
    }

    public void deleteFixup(Node x) {
        Node w;
        while(x != tree.getRoot() && x.getColor() == Color.BLACK) {
            if(x == x.getParent().getLeftChild()) {
                w = x.getParent().getRightChild();
                if(w.getColor() == Color.RED) {
                    w.setColor(Color.BLACK);
                    x.getParent().setColor(Color.RED);
                    leftRotate(x.getParent());
                    w = x.getParent().getRightChild();
                }
                if(w.getLeftChild().getColor() == Color.BLACK && w.getRightChild().getColor() == Color.BLACK) {
                    w.setColor(Color.RED);
                    x = x.getParent();
                } else {
                    if(w.getRightChild().getColor() == Color.BLACK) {
                        w.getRightChild().setColor(Color.BLACK);
                        w.setColor(Color.RED);
                        rightRotate(w);
                        w = x.getParent().getRightChild();
                    }
                    w.setColor(x.getParent().getColor());
                    x.getParent().setColor(Color.BLACK);
                    w.getRightChild().setColor(Color.BLACK);
                    leftRotate(x.getParent());
                    x = tree.getRoot();
                }
            } else {
                w = x.getParent().getLeftChild();
                if(w.getColor() == Color.RED) {
                    w.setColor(Color.BLACK);
                    x.getParent().setColor(Color.RED);
                    rightRotate(x.getParent());
                    w = x.getParent().getLeftChild();
                }
                if(w.getLeftChild().getColor() == Color.BLACK && w.getRightChild().getColor() == Color.BLACK) {
                    w.setColor(Color.RED);
                    x = x.getParent();
                } else {
                    if(w.getLeftChild().getColor() == Color.BLACK) {
                        w.getRightChild().setColor(Color.BLACK);
                        w.setColor(Color.RED);
                        leftRotate(w);
                        w = x.getParent().getLeftChild();
                    }
                    w.setColor(x.getParent().getColor());
                    x.getParent().setColor(Color.BLACK);
                    w.getLeftChild().setColor(Color.BLACK);
                    rightRotate(x.getParent());
                    x = tree.getRoot();
                }
            }
        }
        x.setColor(Color.BLACK);
    }

    public int depth(Node node) {
        if(node == Node.Nil) {
            return 0;
        } else {
            int lDepth = depth(node.getLeftChild());
            int rDepth = depth(node.getRightChild());
            return (lDepth < rDepth ? rDepth : lDepth) + 1;
        }
    }

    public int sumPower(int pow) {
        int sum = 0;
        for(int i = 0; i < pow; i++) {
            sum += Math.pow(2, i);
        }
        return sum;
    }

    public Node search(Node w, int key) {
        if(w == Node.Nil || w.getKey() == key) {
            return w;
        }
        return search((key < w.getKey()) ? w.getLeftChild() : w.getRightChild(), key);
    }

    public void display(Node w, int indent) {
        if(w != Node.Nil) {
            display(w.getRightChild(), indent + 5);
            for(int i = 0; i < indent; i++) {
                System.out.print(" ");
            }
            System.out.println(w.getLevel() + " : " + w.getKey() + " " + w.getColor().toString());
            display(w.getLeftChild(), indent + 5);
        }
    }
}