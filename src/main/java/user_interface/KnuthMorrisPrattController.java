package user_interface;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
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
import red_black_tree.classes.Color;
import red_black_tree.classes.RBNode;

import java.io.IOException;
import java.util.Objects;

import java.util.ArrayList;
import java.util.List;

public class KnuthMorrisPrattController {
    //  Header
    @FXML
    private Button kmpRun;
    @FXML
    private TextField textField;
    @FXML
    private TextField patternField;

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

    private String text;
    private String pattern;
    private List<String> textList;
    private List<String> patternList;
    private List<Integer> prefix = new ArrayList<>();

    @FXML
    private AnchorPane kmpFxml;

    private int currentStep = -1;
    private List<ScrollPane> steps = new ArrayList<>();
    
    public void back() throws IOException {
        try {
            kmpFxml.getScene().getWindow().hide();
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
        if(event.getSource() == kmpRun) {
            restoreSteps();

            text = textField.getText();
            pattern = patternField.getText();
            textList = generateList(text);
            patternList = generateList(pattern);

            computePrefix();

            kmpMatcher();
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
            prefix = new ArrayList<>();
        }
    }

    public GridPane initializeGridPane(int rows, int columns) {
        GridPane gridPane = new GridPane();
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                AnchorPane anchorPane = new AnchorPane();
                anchorPane.setPrefHeight(40);
                anchorPane.setPrefWidth(40);

                gridPane.add(anchorPane, j, i);
            }
        }
        gridPane.setLayoutX(20);
        gridPane.setLayoutY(60);
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
        return scrollPane;
    }

    public void createStepPrefix() {
        int rows = 3;
        int columns = patternList.size();

        ScrollPane scrollPane = initializeScrollPane();

        AnchorPane anchorPane = initializeAnchorPane();

        GridPane gridPane = initializeGridPane(rows, columns);

        // gridPane.setGridLinesVisible(true);

        generateGrid(anchorPane, gridPane);

        scrollPane.setContent(anchorPane);
        content.getChildren().add(scrollPane);

        steps.add(scrollPane);
        currentStep = steps.size() - 1;
    }

    public void generateGrid(AnchorPane anchorPane, GridPane gridPane) {
        for(int i = 0; i < patternList.size(); i++) {
            Label label1 = new Label();
            label1.setPrefHeight(40);
            label1.setPrefWidth(40);
            label1.setAlignment(Pos.CENTER);
            label1.setFont(new Font(18));

            if(i == 0) {
                label1.setText("q");
            } else {
                label1.setText(i + "");
            }

            AnchorPane anchorPane1 = new AnchorPane();
            anchorPane1.setPrefHeight(40);
            anchorPane1.setPrefWidth(40);
            anchorPane1.getChildren().addAll(label1);

            gridPane.add(anchorPane1, i, 0);

            Label label2 = new Label();
            label2.setPrefHeight(40);
            label2.setPrefWidth(40);
            label2.setAlignment(Pos.CENTER);
            label2.setFont(new Font(18));

            if(i == 0) {
                label2.setText("P");
            } else {
                label2.setText(patternList.get(i));
            }

            AnchorPane anchorPane2 = new AnchorPane();
            anchorPane2.setPrefHeight(40);
            anchorPane2.setPrefWidth(40);
            anchorPane2.getChildren().addAll(label2);

            gridPane.add(anchorPane2, i, 1);
        }
        for(int i = 0; i < prefix.size(); i++) {
            Label label1 = new Label();
            label1.setPrefHeight(40);
            label1.setPrefWidth(40);
            label1.setAlignment(Pos.CENTER);
            label1.setFont(new Font(18));

            if(i == 0) {
                label1.setText("pre");
            } else {
                label1.setText(prefix.get(i) + "");
            }

            AnchorPane anchorPane1 = new AnchorPane();
            anchorPane1.setPrefHeight(40);
            anchorPane1.setPrefWidth(40);
            anchorPane1.getChildren().addAll(label1);

            gridPane.add(anchorPane1, i, 2);
        }

        Label label = new Label();
        label.setPrefHeight(50);
        label.setPrefWidth(1000);
        label.setLayoutX(50);
        label.setLayoutY(0);
        label.setFont(new Font(22));
        if(prefix.size() == 2) {
            label.setText("Pasul 0:");
        } else if(prefix.size() > 2) {
            label.setText("Pasul " + (prefix.size() - 2) + ": q = " + (prefix.size() - 1) + ", k = " + prefix.get(prefix.size() - 2) + " si prefix[" + (prefix.size() - 1) + "] = " + prefix.get(prefix.size() - 1));
        }
        anchorPane.getChildren().addAll(label, gridPane);
    }

    public List<String> generateList(String string) {
        List<String> list = new ArrayList<>();
        list.add("");
        for(int i = 0; i < string.length(); i++) {
            list.add(string.charAt(i) + "");
        }
        return list;
    }

    public void computePrefix() {
        prefix.add(-1);
        prefix.add(0);
        createStepPrefix();
        int k = 0;
        for(int q = 2; q < patternList.size(); q++) {
            while(k > 0 && !patternList.get(k + 1).equals(patternList.get(q))) {
                k = prefix.get(k);
            }
            if(patternList.get(k + 1).equals(patternList.get(q))) {
                k++;
            }
            prefix.add(k);
            createStepPrefix();
        }
    }

    public void kmpMatcher() {
        int n = textList.size();
        int m = patternList.size();
        int q = 0;
        for(int i = 1; i < n; i++) {
            while(q > 0 && !patternList.get(q + 1).equals(textList.get(i))) {
                q = prefix.get(q);
            }
            if(patternList.get(q + 1).equals(textList.get(i))) {
                q++;
            }
            if(q == m - 1) {
                // System.out.println("Pattern occurs with shift" + (i - m));
                q = prefix.get(q);
            }
        }
    }
}
