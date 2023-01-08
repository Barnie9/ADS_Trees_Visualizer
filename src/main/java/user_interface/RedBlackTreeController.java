package user_interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.Shadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import red_black_tree.classes.Color;
import red_black_tree.classes.RBNode;
import red_black_tree.classes.RBTree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private AnchorPane rbFxml;

    public void back() throws IOException {
        try {
            rbFxml.getScene().getWindow().hide();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Index.fxml")));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    void buttonPressed(ActionEvent event) {
        if(event.getSource() == insertButton) {
            restoreSteps();

            int value = Integer.parseInt(insertValue.getText());
            RBNode rbNode = new RBNode(value);
            insert(rbNode);

            // System.out.println(depth(tree.getRoot()));
            // System.out.println(sumPower(depth(tree.getRoot())));

            // display(tree.getRoot(), 1);
            insertValue.setText("");
        } else if(event.getSource() == deleteButton) {
            restoreSteps();

            int value = Integer.parseInt(deleteValue.getText());
            RBNode rbNode = search(tree.getRoot(), value);

            if(rbNode != RBNode.Nil) {
                delete(rbNode);
            }

            // display(tree.getRoot(), 1);
            deleteValue.setText("");
        } else if(event.getSource() == searchButton) {
            restoreSteps();

            int value = Integer.parseInt(searchValue.getText());
            RBNode rbNode = search(tree.getRoot(), value);
            searchValue.setText("");

            // System.out.println("Found: " + RBNode.getKey());
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

    public void restoreSteps() {
        if(steps.size() != 0) {
            steps = new ArrayList<>();
            currentStep = -1;
            createStep();
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

    public void createStep(RBNode... highlights) {
        generateLevels(tree.getRoot(), 1);

        int rows = depth(tree.getRoot());
        int columns = sumPower(depth(tree.getRoot()));

        ScrollPane scrollPane = initializeScrollPane();

        AnchorPane anchorPane = initializeAnchorPane();

        GridPane gridPane = initializeGridPane(rows, columns);

        // gridPane.setGridLinesVisible(true);

        generateGrid(anchorPane, gridPane, tree.getRoot(), 0, columns - 1, highlights);
        anchorPane.getChildren().add(gridPane);
        scrollPane.setContent(anchorPane);
        content.getChildren().add(scrollPane);

        steps.add(scrollPane);
        currentStep = steps.size() - 1;
    }

    public void generateGrid(AnchorPane pane, GridPane gridPane, RBNode RBNode, int left, int right, RBNode... highlights) {
        Circle circle = new Circle();
        circle.setRadius(20);
        circle.setCenterX(30);
        circle.setCenterY(30);
        circle.setStrokeWidth(2);
        circle.setStrokeType(StrokeType.INSIDE);
        if(RBNode.getColor() == Color.BLACK) {
            circle.setStroke(javafx.scene.paint.Color.BLACK);
            circle.setFill(javafx.scene.paint.Color.GREY);
        } else {
            circle.setStroke(javafx.scene.paint.Color.DARKRED);
            circle.setFill(javafx.scene.paint.Color.RED);
        }

        Label label = new Label(RBNode.getKey() + "");
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

        for(RBNode highlight : highlights) {
            if (RBNode == highlight) {
                Circle highlighter = new Circle();
                highlighter.setRadius(25);
                // highlighter.setFill(javafx.scene.paint.Color.GREEN);
                Shadow shadow = new Shadow();
                shadow.setBlurType(BlurType.GAUSSIAN);
                shadow.setColor(javafx.scene.paint.Color.DARKGREEN);
                shadow.setHeight(20);
                shadow.setRadius(12);
                shadow.setWidth(20);
                highlighter.setEffect(shadow);
                highlighter.setCenterX(30);
                highlighter.setCenterY(30);
                if(anchorPane.getChildren().size() == 2) {
                    anchorPane.getChildren().add(0, highlighter);
                }
            }
        }

        int column = left + ((right - left) / 2);
        int row = RBNode.getLevel() - 1;

        gridPane.add(anchorPane, column, row);

        if(RBNode.getLeftChild() != RBNode.Nil) {
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

            generateGrid(pane, gridPane, RBNode.getLeftChild(), left, column - 1, highlights);
        }
        if(RBNode.getRightChild() != RBNode.Nil) {
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

            generateGrid(pane, gridPane, RBNode.getRightChild(), column + 1, right, highlights);
        }
    }

    public void generateLevels(RBNode x, int level) {
        if(x != RBNode.Nil) {
            generateLevels(x.getLeftChild(), level + 1);
            generateLevels(x.getRightChild(), level + 1);
            x.setLevel(level);
        }
    }

    public void leftRotate(RBNode x) {
        RBNode y = x.getRightChild();
        createStep(x, y);
        x.setRightChild(y.getLeftChild());
        if(y.getLeftChild() != RBNode.Nil) {
            y.getLeftChild().setParent(x);
        }
        y.setParent(x.getParent());
        if(x.getParent() == RBNode.Nil) {
            tree.setRoot(y);
        } else if(x == x.getParent().getLeftChild()) {
            x.getParent().setLeftChild(y);
        } else {
            x.getParent().setRightChild(y);
        }
        y.setLeftChild(x);
        x.setParent(y);
        createStep(x, y);
        createStep();
    }

    public void rightRotate(RBNode x) {
        RBNode y = x.getLeftChild();
        createStep(x, y);
        x.setLeftChild(y.getRightChild());
        if(y.getRightChild() != RBNode.Nil) {
            y.getRightChild().setParent(x);
        }
        y.setParent(x.getParent());
        if(x.getParent() == RBNode.Nil) {
            tree.setRoot(y);
        } else if(x == x.getParent().getLeftChild()) {
            x.getParent().setLeftChild(y);
        } else {
            x.getParent().setRightChild(y);
        }
        y.setRightChild(x);
        x.setParent(y);
        createStep(x, y);
        createStep();
    }
    
    public RBNode maximum(RBNode w) {
        RBNode x = w;
        createStep(x);
        while(x.getRightChild() != RBNode.Nil) {
            createStep(x.getRightChild());
            x = x.getRightChild();
        }
        createStep();
        return x;
    }

    public RBNode minimum(RBNode w) {
        RBNode x = w;
        while(x.getLeftChild() != RBNode.Nil) {
            x = x.getLeftChild();
        }
        return x;
    }
    
    RBNode successor(RBNode w) {
        if(w == RBNode.Nil) {
            return w;
        }
        RBNode x = w;
        if(x.getRightChild() != RBNode.Nil)
            return minimum(x.getRightChild());
        RBNode y = x.getParent();
        while (y != RBNode.Nil && x == y.getRightChild()) {
            x = y;
            y = x.getParent();
        }
        return y;
    }

    RBNode predecessor(RBNode w) {
        if(w == RBNode.Nil) {
            return w;
        }
        RBNode x = w;
        if(x.getLeftChild() != RBNode.Nil) {
            return maximum(x.getLeftChild());
        }
        RBNode y = x.getParent();
        createStep(y);
        while (y != RBNode.Nil && x == y.getLeftChild()) {
            x = y;
            y = x.getParent();
            createStep(y);
        }
        createStep();
        return y;
    }

    public void insert(RBNode z) {
        RBNode y = RBNode.Nil;
        RBNode x = tree.getRoot();
        while(x != RBNode.Nil) {
            y = x;
            createStep(y);
            x = (z.getKey() < x.getKey()) ? x.getLeftChild() : x.getRightChild();
        }
        z.setParent(y);
        if(y == RBNode.Nil) {
            tree.setRoot(z);
        } else if(z.getKey() < y.getKey()) {
            y.setLeftChild(z);
        } else {
            y.setRightChild(z);
        }
        z.setLeftChild(RBNode.Nil);
        z.setRightChild(RBNode.Nil);
        z.setColor(Color.RED);
        createStep(z);
        createStep();
        insertFixup(z);
    }

    public void insertFixup(RBNode z) {
        while(z.getParent().getColor() == Color.RED) {
            if(z.getParent() == z.getParent().getParent().getLeftChild()) {
                RBNode y = z.getParent().getParent().getRightChild();
                if (y.getColor() == Color.RED) {
                    createStep(z.getParent(), y, z.getParent().getParent());
                    z.getParent().setColor(Color.BLACK);
                    y.setColor(Color.BLACK);
                    z.getParent().getParent().setColor(Color.RED);
                    createStep(z.getParent(), y, z.getParent().getParent());
                    createStep();
                    z = z.getParent().getParent();
                } else {
                    if (z == z.getParent().getRightChild()) {
                        z = z.getParent();
                        leftRotate(z);
                    }
                    createStep(z.getParent(), z.getParent().getParent());
                    z.getParent().setColor(Color.BLACK);
                    z.getParent().getParent().setColor(Color.RED);
                    createStep(z.getParent(), z.getParent().getParent());
                    createStep();
                    rightRotate(z.getParent().getParent());
                }
            } else {
                RBNode y = z.getParent().getParent().getLeftChild();
                if(y.getColor() == Color.RED) {
                    createStep(z.getParent(), y, z.getParent().getParent());
                    z.getParent().setColor(Color.BLACK);
                    y.setColor(Color.BLACK);
                    z.getParent().getParent().setColor(Color.RED);
                    createStep(z.getParent(), y, z.getParent().getParent());
                    createStep();
                    z = z.getParent().getParent();
                } else {
                    if (z == z.getParent().getLeftChild()) {
                        z = z.getParent();
                        rightRotate(z);
                    }
                    createStep(z.getParent(), z.getParent().getParent());
                    z.getParent().setColor(Color.BLACK);
                    z.getParent().getParent().setColor(Color.RED);
                    createStep(z.getParent(), z.getParent().getParent());
                    createStep();
                    leftRotate(z.getParent().getParent());
                }
            }
        }
        if(tree.getRoot().getColor() == Color.RED) {
            createStep(tree.getRoot());
            tree.getRoot().setColor(Color.BLACK);
            createStep(tree.getRoot());
            createStep();
        }
    }

    public void delete(RBNode z) {
        RBNode y = (z.getLeftChild() == RBNode.Nil || z.getRightChild() == RBNode.Nil) ? z : predecessor(z);
        RBNode x = (y.getLeftChild() != RBNode.Nil) ? y.getLeftChild() : y.getRightChild();
        createStep(x, y, z);
        x.setParent(y.getParent());
        if(y.getParent() == RBNode.Nil) {
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
        createStep(x, y, z);
        createStep();
        if(y.getColor() == Color.BLACK) {
            deleteFixup(x);
        }
    }

    public void deleteFixup(RBNode x) {
        RBNode w;
        while(x != tree.getRoot() && x.getColor() == Color.BLACK) {
            if(x == x.getParent().getLeftChild()) {
                w = x.getParent().getRightChild();
                if(w.getColor() == Color.RED) {
                    createStep(w, x.getParent());
                    w.setColor(Color.BLACK);
                    x.getParent().setColor(Color.RED);
                    createStep(w, x.getParent());
                    createStep();
                    leftRotate(x.getParent());
                    w = x.getParent().getRightChild();
                }
                if(w.getLeftChild().getColor() == Color.BLACK && w.getRightChild().getColor() == Color.BLACK) {
                    createStep(w);
                    w.setColor(Color.RED);
                    createStep(w);
                    createStep();
                    x = x.getParent();
                } else {
                    if(w.getRightChild().getColor() == Color.BLACK) {
                        createStep(w.getRightChild(), w);
                        w.getRightChild().setColor(Color.BLACK);
                        w.setColor(Color.RED);
                        createStep(w.getRightChild(), w);
                        createStep();
                        rightRotate(w);
                        w = x.getParent().getRightChild();
                    }
                    createStep(w, x.getParent(), w.getRightChild());
                    w.setColor(x.getParent().getColor());
                    x.getParent().setColor(Color.BLACK);
                    w.getRightChild().setColor(Color.BLACK);
                    createStep(w, x.getParent(), w.getRightChild());
                    createStep();
                    leftRotate(x.getParent());
                    x = tree.getRoot();
                }
            } else {
                w = x.getParent().getLeftChild();
                if(w.getColor() == Color.RED) {
                    createStep(w, x.getParent());
                    w.setColor(Color.BLACK);
                    x.getParent().setColor(Color.RED);
                    createStep(w, x.getParent());
                    createStep();
                    rightRotate(x.getParent());
                    w = x.getParent().getLeftChild();
                }
                if(w.getLeftChild().getColor() == Color.BLACK && w.getRightChild().getColor() == Color.BLACK) {
                    createStep(w);
                    w.setColor(Color.RED);
                    createStep(w);
                    createStep();
                    x = x.getParent();
                } else {
                    if(w.getLeftChild().getColor() == Color.BLACK) {
                        createStep(w.getRightChild(), w);
                        w.getRightChild().setColor(Color.BLACK);
                        w.setColor(Color.RED);
                        createStep(w.getRightChild(), w);
                        createStep();
                        leftRotate(w);
                        w = x.getParent().getLeftChild();
                    }
                    createStep(w, x.getParent(), w.getLeftChild());
                    w.setColor(x.getParent().getColor());
                    x.getParent().setColor(Color.BLACK);
                    w.getLeftChild().setColor(Color.BLACK);
                    createStep(w, x.getParent(), w.getLeftChild());
                    createStep();
                    rightRotate(x.getParent());
                    x = tree.getRoot();
                }
            }
        }
        if(x.getColor() == Color.RED) {
            createStep(x);
            x.setColor(Color.BLACK);
            createStep(x);
            createStep();
        }
    }

    public int depth(RBNode RBNode) {
        if(RBNode == RBNode.Nil) {
            return 0;
        } else {
            int lDepth = depth(RBNode.getLeftChild());
            int rDepth = depth(RBNode.getRightChild());
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

    public RBNode search(RBNode w, int key) {
        createStep(w);
        if(w == RBNode.Nil || w.getKey() == key) {
            return w;
        }
        return search((key < w.getKey()) ? w.getLeftChild() : w.getRightChild(), key);
    }

    public void display(RBNode w, int indent) {
        if(w != RBNode.Nil) {
            display(w.getRightChild(), indent + 5);
            for(int i = 0; i < indent; i++) {
                System.out.print(" ");
            }
            System.out.println(w.getLevel() + " : " + w.getKey() + " " + w.getColor().toString());
            display(w.getLeftChild(), indent + 5);
        }
    }
}