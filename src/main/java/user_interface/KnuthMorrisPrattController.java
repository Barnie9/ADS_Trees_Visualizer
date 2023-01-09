package user_interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class KnuthMorrisPrattController {
    @FXML
    private Button kmpRun;
    @FXML
    private TextField textField;
    @FXML
    private TextField patternField;

    private String text;
    private String pattern;
    private List<String> textList;
    private List<String> patternList;
    private List<Integer> prefix;

    @FXML
    public void buttonPressed(ActionEvent event) {
        if(event.getSource() == kmpRun) {
            text = textField.getText();
            pattern = patternField.getText();
            textList = generateList(text);
            patternList = generateList(pattern);

            prefix = computePrefix();
            System.out.println(textList);
            System.out.println(patternList);
            System.out.println(prefix);
        }
    }

    public List<String> generateList(String string) {
        List<String> list = new ArrayList<>();
        for(int i = 0; i < string.length(); i++) {
            list.add(string.charAt(i) + "");
        }
        return list;
    }

    public List<Integer> computePrefix() {
        List<Integer> list = new ArrayList<>();
        list.add(0);
        int k = 0;
        for(int q = 1; q < patternList.size(); q++) {
            while(k > 0 && patternList.get(k + 1) != patternList.get(q)) {
                k = list.get(k);
            }
            if(patternList.get(k + 1) == patternList.get(q)) {
                k++;
            }
            list.add(k);
        }
        return list;
    }


}