package red_black_tree.user_interface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RedBlackTreeMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RedBlackTreeMain.class.getResource("RedBlackTree.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Red-Black Tree");
        stage.setScene(scene);
        stage.show();
        // Test
    }

    public static void main(String[] args) {
        launch();
    }
}