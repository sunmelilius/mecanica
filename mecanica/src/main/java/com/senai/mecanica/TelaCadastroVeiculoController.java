package com.senai.mecanica;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TelaCadastroVeiculoController {

    @FXML
    private TextField placaFx;

    @FXML
    private TextField anoFx;

    @FXML
    private TextField corFx;

    @FXML
    private TextField modeloFx;

    @FXML
    private TextField quilometragemFx;

    @FXML
    private TextField historicoDemanutencaoFx;
    @FXML
    public void salvarVeiculo(){
        String placa = placaFx.getText();
        String ano = anoFx.getText();
        String cor = corFx.getText();
        String modelo = modeloFx.getText();
        String quilometragem = quilometragemFx.getText();
        String historicoC = historicoDemanutencaoFx.getText();


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText("Veículo cadastrado com sucesso!");
        alert.showAndWait();
// Aguarda o usuário clicar em "OK"
    }

    @FXML
    private void voltarTela2(ActionEvent event) {
        try {
            Parent tela2 = FXMLLoader.load(
                    getClass().getResource("tela2.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(tela2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
