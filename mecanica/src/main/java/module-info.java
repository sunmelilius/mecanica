module com.senai.mecanica {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.senai.mecanica to javafx.fxml;
    exports com.senai.mecanica;
}