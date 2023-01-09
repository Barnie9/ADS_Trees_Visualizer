package user_interface;

import binary_search_tree.classes.Node;
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
import binary_search_tree.classes.BSTree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BinarySearchTreeController {
    private BSTree tree = new BSTree();
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

    //  BST Page
    @FXML
    private AnchorPane bstFxml;

    public void back() throws IOException {
        try {
            bstFxml.getScene().getWindow().hide();
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
    public void buttonPressed(ActionEvent event) {
        if(event.getSource() == insertButton) {
            restoreSteps();

            int value = Integer.parseInt(insertValue.getText());
            Node Node = new Node(value);
            insert(Node);

            // System.out.println(depth(tree.getRoot()));
            // System.out.println(sumPower(depth(tree.getRoot())));

            // display(tree.getRoot(), 1);
            insertValue.setText("");
        } else if(event.getSource() == deleteButton) {
            restoreSteps();

            int value = Integer.parseInt(deleteValue.getText());
            Node Node = search(tree.getRoot(), value);

            if(Node != Node.Nil) {
                delete(Node);
            }

            // display(tree.getRoot(), 1);
            deleteValue.setText("");
        } else if(event.getSource() == searchButton) {
            restoreSteps();

            int value = Integer.parseInt(searchValue.getText());
            Node Node = search(tree.getRoot(), value);
            searchValue.setText("");

            // System.out.println("Found: " + Node.getKey());
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

    public void createStep(Node... highlights) {
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

    public void generateGrid(AnchorPane pane, GridPane gridPane, Node Node, int left, int right, Node... highlights) {
        Circle circle = new Circle();
        circle.setRadius(20);
        circle.setCenterX(30);
        circle.setCenterY(30);
        circle.setStrokeWidth(2);
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setStroke(javafx.scene.paint.Color.BLACK);
        circle.setFill(javafx.scene.paint.Color.GREY);

        Label label = new Label(Node.getKey() + "");
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

        for(Node highlight : highlights) {
            if (Node == highlight) {
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
                //anchorPane.getChildren().add(0, highlighter);
                if(anchorPane.getChildren().size() == 2) {
                    anchorPane.getChildren().add(0, highlighter);
                }
            }
        }

        int column = left + ((right - left) / 2);
        int row = Node.getLevel() - 1;

        gridPane.add(anchorPane, column, row);

        if(Node.getLeftChild() != Node.Nil) {
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

            generateGrid(pane, gridPane, Node.getLeftChild(), left, column - 1, highlights);
        }
        if(Node.getRightChild() != Node.Nil) {
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

            generateGrid(pane, gridPane, Node.getRightChild(), column + 1, right, highlights);
        }
    }

    public void generateLevels(Node x, int level) {
        if(x != Node.Nil) {
            generateLevels(x.getLeftChild(), level + 1);
            generateLevels(x.getRightChild(), level + 1);
            x.setLevel(level);
        }
    }

    public Node maximum(Node w) {
        Node x = w;
        createStep(x);
        while(x.getRightChild() != Node.Nil) {
            x = x.getRightChild();
        }
        createStep(x);
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
        createStep(x);
        while (y != Node.Nil && x == y.getLeftChild()) {
            x = y;
            y = x.getParent();
            createStep(y);
        }
        createStep();
        return y;
    }

    public void insert(Node z) {
        Node y = Node.Nil;
        Node x = tree.getRoot();
        while(x != Node.Nil) {
            y = x;
            createStep(y);
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
        createStep(z);
        createStep();
    }

    public void delete(Node z) {
        Node y = (z.getLeftChild() == Node.Nil || z.getRightChild() == Node.Nil) ? z : predecessor(z);
        Node x = (y.getLeftChild() != Node.Nil) ? y.getLeftChild() : y.getRightChild();
        createStep(x, y, z);
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
        createStep(x, y, z);
        createStep();
    }

    public int depth(Node Node) {
        if(Node == Node.Nil) {
            return 0;
        } else {
            int lDepth = depth(Node.getLeftChild());
            int rDepth = depth(Node.getRightChild());
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
        createStep(w);
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
            System.out.println(w.getLevel() + " : " + w.getKey());
            display(w.getLeftChild(), indent + 5);
        }
    }
}
