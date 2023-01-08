module red_black_tree.user_interface.proiect_sda {
    requires javafx.controls;
    requires javafx.fxml;


    opens user_interface to javafx.fxml;
    exports user_interface;
}