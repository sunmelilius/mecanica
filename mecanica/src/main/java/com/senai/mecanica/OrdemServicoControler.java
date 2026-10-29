
package com.senai.mecanica;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class OrdemServicoControler {

    // Dados do cliente
    @FXML
    private ComboBox<String> clienteCombo;

    @FXML
    private TextField cpfField;

    // Dados do veículo
    @FXML
    private ComboBox<String> veiculoCombo;

    @FXML
    private TextField placaField;

    @FXML
    private TextField modeloField;

    @FXML
    private TextField quilometragemField;

    // Dados da ordem de serviço
    @FXML
    private TextField numeroOsField;

    @FXML
    private DatePicker dataField;

    @FXML
    private TextField tipoServicoField;

    @FXML
    private TextArea descricaoField;

    @FXML
    private TextArea produtosField;

    @FXML
    private TextField maoObraField;

    // Cadastro de serviços
    @FXML
    private TextField nomeServicoField;

    @FXML
    private TextField precoServicoField;

    // Tabela de serviços
    @FXML
    private TableView<ServicoItem> servicosTable;

    @FXML
    private TableColumn<ServicoItem, String> nomeColumn;

    @FXML
    private TableColumn<ServicoItem, String> precoColumn;

    // Totais e status
    @FXML
    private Label totalServicosLabel;

    @FXML
    private Label maoObraTotalLabel;

    @FXML
    private Label totalGeralLabel;

    @FXML
    private ComboBox<String> statusCombo;

    // Lista temporária de serviços
    private final ObservableList<ServicoItem> servicos =
            FXCollections.observableArrayList();

    // Armazenamento temporário dos dados da OS
    private String[] dadosOrdemServico = new String[11];

    @FXML
    public void initialize() {

        // Configura as colunas da tabela
        nomeColumn.setCellValueFactory(
                dado -> new SimpleStringProperty(
                        dado.getValue().getNome()
                )
        );

        precoColumn.setCellValueFactory(
                dado -> new SimpleStringProperty(
                        moeda(dado.getValue().getPreco())
                )
        );

        servicosTable.setItems(servicos);

        // Opções de status
        statusCombo.setItems(
                FXCollections.observableArrayList(
                        "Aberta",
                        "Em andamento",
                        "Concluída"
                )
        );

        statusCombo.setValue("Aberta");

        // Preenche a data e o número inicial
        dataField.setValue(LocalDate.now());
        numeroOsField.setText("1");

        // Atualiza os totais quando a mão de obra muda
        maoObraField.textProperty().addListener(
                (observavel, antigo, novo) -> atualizarTotais()
        );

        atualizarTotais();
    }

    // Adiciona um serviço à tabela
    @FXML
    private void adicionarServico() {

        String nome = nomeServicoField.getText().trim();

        if (nome.isEmpty()) {
            mostrarMensagem("Informe o nome do serviço.");
            return;
        }

        BigDecimal preco = lerValor(
                precoServicoField.getText()
        );

        if (preco == null ||
                preco.compareTo(BigDecimal.ZERO) < 0) {

            mostrarMensagem(
                    "Informe um preço válido e não negativo."
            );
            return;
        }

        servicos.add(new ServicoItem(nome, preco));

        nomeServicoField.clear();
        precoServicoField.clear();

        atualizarTotais();
    }

    // Remove o serviço selecionado
    @FXML
    private void removerServico() {

        ServicoItem selecionado =
                servicosTable.getSelectionModel()
                        .getSelectedItem();

        if (selecionado == null) {
            mostrarMensagem(
                    "Selecione um serviço para remover."
            );
            return;
        }

        servicos.remove(selecionado);

        atualizarTotais();
    }

    // Converte o texto informado em valor monetário
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

    // Soma os preços dos serviços
    private BigDecimal totalServicos() {

        return servicos.stream()
                .map(ServicoItem::getPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Obtém o valor da mão de obra
    private BigDecimal valorMaoObra() {

        BigDecimal valor = lerValor(
                maoObraField.getText()
        );

        return valor == null
                ? BigDecimal.ZERO
                : valor;
    }

    // Atualiza os valores exibidos na tela
    private void atualizarTotais() {

        BigDecimal total = totalServicos();
        BigDecimal maoObra = valorMaoObra();

        totalServicosLabel.setText(moeda(total));
        maoObraTotalLabel.setText(moeda(maoObra));

        totalGeralLabel.setText(
                moeda(total.add(maoObra))
        );
    }

    // Formata os valores em reais
    private String moeda(BigDecimal valor) {

        return "R$ " + valor
                .setScale(2, RoundingMode.HALF_UP)
                .toPlainString()
                .replace(".", ",");
    }

    // Valida e abre a ordem de serviço
    @FXML
    private void abrirOrdemServico() {

        if (clienteCombo.getValue() == null ||
                veiculoCombo.getValue() == null ||
                dataField.getValue() == null ||
                tipoServicoField.getText().trim().isEmpty() ||
                descricaoField.getText().trim().isEmpty() ||
                servicos.isEmpty()) {

            mostrarMensagem(
                    "Preencha cliente, veículo, data, tipo, " +
                            "descrição e adicione pelo menos um serviço."
            );
            return;
        }

        BigDecimal maoObra = lerValor(
                maoObraField.getText()
        );

        if (maoObra == null ||
                maoObra.compareTo(BigDecimal.ZERO) < 0) {

            mostrarMensagem(
                    "Informe um valor válido para a mão de obra."
            );
            return;
        }

        // Armazena os dados temporariamente
        dadosOrdemServico[0] = numeroOsField.getText();
        dadosOrdemServico[1] = clienteCombo.getValue();
        dadosOrdemServico[2] = cpfField.getText();
        dadosOrdemServico[3] = veiculoCombo.getValue();
        dadosOrdemServico[4] = placaField.getText();
        dadosOrdemServico[5] = modeloField.getText();
        dadosOrdemServico[6] = quilometragemField.getText();
        dadosOrdemServico[7] = dataField.getValue().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
        );
        dadosOrdemServico[8] = tipoServicoField.getText();
        dadosOrdemServico[9] = descricaoField.getText();
        dadosOrdemServico[10] = statusCombo.getValue();

        // Exibe os dados da ordem
        Alert confirmacao = new Alert(
                Alert.AlertType.INFORMATION
        );

        confirmacao.setTitle("Ordem de Serviço");
        confirmacao.setHeaderText(
                "Ordem de serviço preenchida com sucesso!"
        );

        StringBuilder resumo = new StringBuilder();

        resumo.append("Número da OS: ")
                .append(dadosOrdemServico[0]);

        resumo.append("\nCliente: ")
                .append(dadosOrdemServico[1]);

        resumo.append("\nCPF: ")
                .append(dadosOrdemServico[2]);

        resumo.append("\nVeículo: ")
                .append(dadosOrdemServico[3]);

        resumo.append("\nPlaca: ")
                .append(dadosOrdemServico[4]);

        resumo.append("\nModelo: ")
                .append(dadosOrdemServico[5]);

        resumo.append("\nQuilometragem: ")
                .append(dadosOrdemServico[6]);

        resumo.append("\nData: ")
                .append(dadosOrdemServico[7]);

        resumo.append("\nTipo de serviço: ")
                .append(dadosOrdemServico[8]);

        resumo.append("\nDescrição: ")
                .append(dadosOrdemServico[9]);

        resumo.append("\nProdutos utilizados: ")
                .append(produtosField.getText());

        resumo.append("\nServiços adicionados: ");

        for (ServicoItem servico : servicos) {
            resumo.append("\n- ")
                    .append(servico.getNome())
                    .append(": ")
                    .append(moeda(servico.getPreco()));
        }

        resumo.append("\nTotal dos serviços: ")
                .append(moeda(totalServicos()));

        resumo.append("\nMão de obra: ")
                .append(moeda(maoObra));

        resumo.append("\nValor total: ")
                .append(moeda(totalServicos().add(maoObra)));

        resumo.append("\nStatus: ")
                .append(dadosOrdemServico[10]);

        resumo.append(
                "\n\nAtenção: os dados ainda não são " +
                        "salvos permanentemente no banco de dados."
        );

        confirmacao.setContentText(resumo.toString());
        confirmacao.getDialogPane().setMinWidth(400);
        confirmacao.showAndWait();

        System.out.println(
                "Ordem de serviço preenchida com sucesso!"
        );
    }

    // Limpa os campos do formulário
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

    // Retorna ao menu principal
    @FXML
    private void voltar() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource("tela2.fxml")
            );

            Parent tela = fxmlLoader.load();

            Stage stage = (Stage) servicosTable
                    .getScene().getWindow();

            stage.setScene(new Scene(tela));
            stage.show();

        } catch (Exception e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Não foi possível voltar ao menu. " +
                            "Verifique se o arquivo tela2.fxml existe."
            );
        }
    }

    // Exibe mensagens de aviso
    private void mostrarMensagem(String mensagem) {

        Alert alerta = new Alert(
                Alert.AlertType.WARNING
        );

        alerta.setTitle("Atenção");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    // Classe que representa cada serviço da tabela
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
