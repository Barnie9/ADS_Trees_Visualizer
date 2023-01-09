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
    private String text;
    private String pattern;
    private List<String> textList;
    private List<String> patternList;
    private List<Integer> prefix;

    @FXML
    private Button kmpRun;
    @FXML
    private TextField textField;
    @FXML
    private TextField patternField;

    //  Content
    @FXML
    private AnchorPane content;

    private String text;
    private String pattern;
    private List<String> textList;
    private List<String> patternList;
    private List<Integer> prefix;

    @FXML
    private AnchorPane kmpFxml;
    
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

    private int currentStep = -1;
    private List<ScrollPane> steps = new ArrayList<>();

    @FXML
    public void buttonPressed(ActionEvent event) {
        if(event.getSource() == kmpRun) {
            // restoreSteps();

            text = textField.getText();
            pattern = patternField.getText();
            textList = generateList(text);
            patternList = generateList(pattern);

            prefix = computePrefix();

            kmpMatcher();

            createStepPrefix();
        }
    }

    public void restoreSteps() {
        if(steps.size() != 0) {
            steps = new ArrayList<>();
            currentStep = -1;
            // createStep();
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
        gridPane.setLayoutX(50);
        gridPane.setLayoutY(50);
        return gridPane;
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

        GridPane gridPane = initializeGridPane(rows, columns);

        gridPane.setGridLinesVisible(true);

        generateGrid(gridPane);

        scrollPane.setContent(gridPane);
        content.getChildren().add(scrollPane);

        steps.add(scrollPane);
        currentStep = steps.size() - 1;
    }

    public void generateGrid(GridPane gridPane) {
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
    }

    public List<String> generateList(String string) {
        List<String> list = new ArrayList<>();
        list.add("");
        for(int i = 0; i < string.length(); i++) {
            list.add(string.charAt(i) + "");
        }
        return list;
    }

    public List<Integer> computePrefix() {
        List<Integer> list = new ArrayList<>();
        list.add(-1);
        list.add(0);
        int k = 0;
        for(int q = 2; q < patternList.size(); q++) {
            while(k > 0 && !patternList.get(k + 1).equals(patternList.get(q))) {
                k = list.get(k);
            }
            if(patternList.get(k + 1).equals(patternList.get(q))) {
                k++;
            }
            list.add(k);
        }
        return list;
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
                System.out.println("Pattern occurs with shift" + (i - m));
                q = prefix.get(q);
            }
        }
    }
}
