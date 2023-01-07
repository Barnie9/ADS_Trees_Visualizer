package binomial_heaps;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class BinomialHeapsController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application! We are testing 2 files!");
    }
}
