package com.senai.mecanica;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Tela2Controller {

    @FXML
    private void cadastrarCliente() {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource("telaCadastroCliente.fxml")
            );

            Parent tela3 = fxmlLoader.load();

            Stage stage = new Stage();

            stage.setScene(new Scene(tela3));

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}