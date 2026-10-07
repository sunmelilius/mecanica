package com.senai.mecanica;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HelloController {

    @FXML
    private TextField txtLogin;

    @FXML
    private PasswordField txtSenha;

    @FXML
    private void entrar() {

        String login = txtLogin.getText();
        String senha = txtSenha.getText();

        if (login.equals("kauan-senai") && senha.equals("kauan4321")) {

            try {

                FXMLLoader fxmlLoader = new FXMLLoader(
                        getClass().getResource("tela2.fxml")
                );

                Parent tela2 = fxmlLoader.load();

                Stage stage = (Stage) txtLogin.getScene().getWindow();

                stage.setScene(new Scene(tela2));

            } catch (Exception e) {

                e.printStackTrace();

            }

        } else {

            System.out.println("Incorreto.");

        }
    }
}