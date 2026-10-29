package com.senai.mecanica;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Tela3Controller {

    @FXML
    private void ordenDeServico() {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource("ordemservico.fxml")
            );

            Parent tela2 = fxmlLoader.load();

            Stage stage = new Stage();

            stage.setScene(new Scene(tela2));

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}