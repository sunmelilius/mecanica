
package com.senai.mecanica;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class OrdemServicoControler {

    @FXML private ComboBox<String> clienteCombo;
    @FXML private TextField cpfField;
    @FXML private ComboBox<String> veiculoCombo;
    @FXML private TextField placaField;
    @FXML private TextField modeloField;
    @FXML private TextField quilometragemField;

    @FXML private TextField numeroOsField;
    @FXML private DatePicker dataField;
    @FXML private TextField tipoServicoField;
    @FXML private TextArea descricaoField;
    @FXML private TextArea produtosField;
    @FXML private TextField maoObraField;

    @FXML private TextField nomeServicoField;
    @FXML private TextField precoServicoField;

    @FXML private TableView<ServicoItem> servicosTable;
    @FXML private TableColumn<ServicoItem, String> nomeColumn;
    @FXML private TableColumn<ServicoItem, String> precoColumn;

    @FXML private Label totalServicosLabel;
    @FXML private Label maoObraTotalLabel;
    @FXML private Label totalGeralLabel;
    @FXML private ComboBox<String> statusCombo;

    private final ObservableList<ServicoItem> servicos =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nomeColumn.setCellValueFactory(
                dado -> new SimpleStringProperty(
                        dado.getValue().getNome()));

        precoColumn.setCellValueFactory(
                dado -> new SimpleStringProperty(
                        moeda(dado.getValue().getPreco())));

        servicosTable.setItems(servicos);

        statusCombo.setItems(FXCollections.observableArrayList(
                "Aberta",
                "Em aprovação/andamento",
                "Concluída"
        ));

        statusCombo.setValue("Aberta");
        dataField.setValue(LocalDate.now());
        numeroOsField.setText("1");

        maoObraField.textProperty().addListener(
                (obs, antigo, novo) -> atualizarTotais());

        atualizarTotais();
    }

    @FXML
    private void adicionarServico() {
        String nome = nomeServicoField.getText().trim();

        if (nome.isEmpty()) {
            alerta("Informe o nome do serviço.");
            return;
        }

        BigDecimal preco = lerValor(precoServicoField.getText());

        if (preco == null
                || preco.compareTo(BigDecimal.ZERO) < 0) {
            alerta("Informe um preço válido e não negativo.");
            return;
        }

        servicos.add(new ServicoItem(nome, preco));

        nomeServicoField.clear();
        precoServicoField.clear();

        atualizarTotais();
    }

    @FXML
    private void removerServico() {
        ServicoItem selecionado =
                servicosTable.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            alerta("Selecione um serviço para remover.");
            return;
        }

        servicos.remove(selecionado);
        atualizarTotais();
    }

    private BigDecimal lerValor(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }

        try {
            String valor = texto.trim()
                    .replace("R$", "")
                    .trim();

            if (valor.contains(",")) {
                valor = valor.replace(".", "")
                        .replace(",", ".");
            }

            return new BigDecimal(valor)
                    .setScale(2, RoundingMode.HALF_UP);

        } catch (NumberFormatException e) {
            return null;
        }
    }

    private BigDecimal totalServicos() {
        return servicos.stream()
                .map(ServicoItem::getPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal valorMaoObra() {
        BigDecimal valor = lerValor(maoObraField.getText());

        return valor == null ? BigDecimal.ZERO : valor;
    }

    private void atualizarTotais() {
        BigDecimal total = totalServicos();
        BigDecimal maoObra = valorMaoObra();

        totalServicosLabel.setText(moeda(total));
        maoObraTotalLabel.setText(moeda(maoObra));
        totalGeralLabel.setText(moeda(total.add(maoObra)));
    }

    private String moeda(BigDecimal valor) {
        return "R$ " + valor
                .setScale(2, RoundingMode.HALF_UP)
                .toPlainString()
                .replace(".", ",");
    }

    @FXML
    private void abrirOrdemServico() {
        if (clienteCombo.getValue() == null
                || veiculoCombo.getValue() == null
                || dataField.getValue() == null
                || tipoServicoField.getText().trim().isEmpty()
                || descricaoField.getText().trim().isEmpty()
                || servicos.isEmpty()) {

            alerta("Preencha cliente, veículo, data, tipo, descrição "
                    + "e adicione pelo menos um serviço.");
            return;
        }

        BigDecimal maoObra = lerValor(maoObraField.getText());

        if (maoObra == null
                || maoObra.compareTo(BigDecimal.ZERO) < 0) {
            alerta("Informe um valor válido para a mão de obra.");
            return;
        }

        Alert confirmacao =
                new Alert(Alert.AlertType.INFORMATION);

        confirmacao.setTitle("Ordem de Serviço");
        confirmacao.setHeaderText("Dados preenchidos com sucesso");

        confirmacao.setContentText(
                "Cliente: " + clienteCombo.getValue()
                        + "\nVeículo: " + veiculoCombo.getValue()
                        + "\nData: " + dataField.getValue().format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        + "\nTotal: " + moeda(totalServicos().add(maoObra))
                        + "\nStatus: " + statusCombo.getValue()
                        + "\n\nAtenção: esta versão ainda não salva no banco."
        );

        confirmacao.showAndWait();
    }

    @FXML
    private void limparFormulario() {
        clienteCombo.getSelectionModel().clearSelection();
        cpfField.clear();

        veiculoCombo.getSelectionModel().clearSelection();
        placaField.clear();
        modeloField.clear();
        quilometragemField.clear();

        numeroOsField.clear();
        dataField.setValue(LocalDate.now());
        tipoServicoField.clear();
        descricaoField.clear();
        produtosField.clear();
        maoObraField.clear();

        nomeServicoField.clear();
        precoServicoField.clear();

        servicos.clear();
        statusCombo.setValue("Aberta");

        atualizarTotais();
    }

    @FXML
    private void voltar() {
        Node node = servicosTable;

        if (node.getScene() != null
                && node.getScene().getWindow() != null) {
            node.getScene().getWindow().hide();
        }
    }

    private void alerta(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);

        alerta.setTitle("Atenção");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    public static class ServicoItem {
        private final String nome;
        private final BigDecimal preco;

        public ServicoItem(String nome, BigDecimal preco) {
            this.nome = nome;
            this.preco = preco;
        }

        public String getNome() {
            return nome;
        }

        public BigDecimal getPreco() {
            return preco;
        }
    }
}
