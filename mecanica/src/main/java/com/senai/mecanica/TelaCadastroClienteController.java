package com.senai.mecanica;

import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.*;

public class TelaCadastroClienteController {
    String[] dadosClientes = new String[5];

    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtCPF;
    @FXML
    private TextField txtTelefone;
    @FXML
    private TextField txtCEP;
    @FXML
    private TextField txtEmail;
    @FXML
    private Label lblMensagem;

    @FXML
    private void formatarCPF() {

        txtCPF.textProperty().addListener((observable, antigo, novo) -> {

            String cpf = novo.replaceAll("\\D", "");

            if (cpf.length() > 11) {
                cpf = cpf.substring(0, 11);
            }

            if (cpf.length() > 9) {
                cpf = cpf.substring(0, 3) + "." +
                        cpf.substring(3, 6) + "." +
                        cpf.substring(6, 9) + "-" +
                        cpf.substring(9);
            }

            txtCPF.setText(cpf);
            txtCPF.positionCaret(cpf.length());

        });
    }

    @FXML
    private void formatarTelefone() {

        txtTelefone.textProperty().addListener((observable, antigo, novo) -> {

            String telefone = novo.replaceAll("\\D", "");

            if (telefone.length() > 11) {
                telefone = telefone.substring(0, 11);
            }

            if (telefone.length() > 7) {
                telefone = "(" + telefone.substring(0, 2) + ") " +
                        telefone.substring(2, 7) + "-" +
                        telefone.substring(7);
            } else if (telefone.length() > 2) {
                telefone = "(" + telefone.substring(0, 2) + ") " +
                        telefone.substring(2);
            }

            txtTelefone.setText(telefone);
            txtTelefone.positionCaret(telefone.length());

        });
    }

    @FXML
    public void initialize() {
        formatarCPF();
        formatarTelefone();
    }

    @FXML
    private void salvar() {

        if (txtNome.getText().isEmpty()) {
            lblMensagem.setText("Digite o nome.");
            return;
        }

        if (!txtCPF.getText().replaceAll("\\D", "").matches("\\d{11}")) {
            lblMensagem.setText("CPF inválido.");
            return;
        }

        if (!txtTelefone.getText().replaceAll("\\D", "").matches("\\d{11}")) {
            lblMensagem.setText("Telefone inválido.");
            return;
        }

        if (!txtCEP.getText().replaceAll("\\D", "").matches("\\d{8}")) {
            lblMensagem.setText("CEP inválido.");
            return;
        }

        if (!txtEmail.getText().matches(".+@.+\\..+")) {
            lblMensagem.setText("E-mail inválido.");
            return;
        }

        dadosClientes[0] = txtNome.getText();
        dadosClientes[1] = txtCPF.getText();
        dadosClientes[2] = txtTelefone.getText();
        dadosClientes[3] = txtCEP.getText();
        dadosClientes[4] = txtEmail.getText();

        System.out.println("Cliente cadastrado:");
        System.out.println("Nome: " + dadosClientes[0]);
        System.out.println("CPF: " + dadosClientes[1]);
        System.out.println("Telefone: " + dadosClientes[2]);
        System.out.println("CEP: " + dadosClientes[3]);
        System.out.println("E-mail: " + dadosClientes[4]);

        lblMensagem.setText("Cliente cadastrado com sucesso!");

        txtNome.clear();
        txtCPF.clear();
        txtTelefone.clear();
        txtCEP.clear();
        txtEmail.clear();
    }

    @FXML
    private void voltar() {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource("tela2.fxml")
            );

            Parent tela2 = fxmlLoader.load();

            Stage stage = (Stage) txtNome.getScene().getWindow();

            stage.setScene(new Scene(tela2));

        } catch (Exception e) {

            e.printStackTrace();

        }
    }


}