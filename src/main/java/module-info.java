module red_black_tree.user_interface.proiect_sda {
    requires javafx.controls;
    requires javafx.fxml;


    opens red_black_tree.user_interface to javafx.fxml;
    exports red_black_tree.user_interface;
    opens binomial_heaps to javafx.fxml;
    exports binomial_heaps;
}