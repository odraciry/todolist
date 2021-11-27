package br.com.todolist.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import br.com.todolist.io.TarefaIO;
import br.com.todolist.model.GrauImportancia;
import br.com.todolist.model.StatusTarefa;
import br.com.todolist.model.Tarefa;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class IndexController implements Initializable, ChangeListener<Tarefa> {

	 @FXML
	 private Button btConcluido;

	 @FXML
	 private Button btAdiar;

	 @FXML
	 private Button btLimpar;

	 @FXML
	 private Button btExcluir;

	 @FXML
	 private Button btSalvar;

	@FXML
	private TextField tfStatus;
	
	 @FXML
	 private TextField tfName;

	@FXML
	private TableColumn<Tarefa, LocalDate> tcData;

	@FXML
	private TableColumn<Tarefa, String> tcTarefa;
	
	@FXML
	private TableColumn<Tarefa, String> tcName;

	@FXML
	private TableColumn<Tarefa, GrauImportancia> tcImportancia;

	@FXML
	private TableView<Tarefa> tvTarefa;

	@FXML
	private DatePicker dpData;

	@FXML
	private TextArea lbDescricao;

	@FXML
	private ComboBox<GrauImportancia> cbImportancia;

	private List<Tarefa> tarefas;
	private Tarefa tarefa;

	@FXML
	void btAdiarClick(ActionEvent event) {
		
	}

	@FXML
	void btApagarClick(ActionEvent event) {
		limpar();
	}

	@FXML
	void btConcluirClick(ActionEvent event) {

	}

	@FXML
	void btExcluirClick(ActionEvent event) {
		limpar();
	}

	@FXML
	void btSaveClick(ActionEvent event) {
		// Validando os campos
		 if (dpData.getValue() == null) {
			JOptionPane.showMessageDialog(null, "Informe uma data", "Informe", JOptionPane.ERROR_MESSAGE);
			dpData.requestFocus();
		} else if (dpData.getValue().isBefore(LocalDate.now())) {
			JOptionPane.showMessageDialog(null, "Informe uma data valida", "Data invalida", JOptionPane.ERROR_MESSAGE);
			dpData.requestFocus();
		} else if (cbImportancia.getValue() == null) {
			JOptionPane.showMessageDialog(null, "Informe a importâcia da tarefa", "Informe", JOptionPane.ERROR_MESSAGE);
			cbImportancia.requestFocus();
		} else {
			// istanciando a tarefa
			tarefa = new Tarefa();

			// popular a tarefa
			tarefa.setDataCriacao(LocalDate.now());
			tarefa.setStatus(StatusTarefa.ABERTA);
			tarefa.setDataLimite(dpData.getValue());
			tarefa.setDescricao(tfStatus.getText());
			tarefa.setComentarios(lbDescricao.getText());
			tarefa.setImportancia(cbImportancia.getValue());
			tarefa.setName(tfName.getText());

			System.out.println(tarefa.formatToSave());
			// TODO inserir no banco de dados
			try {
				TarefaIO.insert(tarefa);
				// Limpar os Campos
				limpar();
				carregarTarefas();
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Arquivo não encontrado: " + e.getMessage(), "Erro",
						JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Arquivo não encontrado: " + e.getMessage(), "Erro",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void limpar() {
		tarefa = null;
		dpData.setValue(null);
		tfStatus.clear();
		tfName.clear();
		cbImportancia.setValue(null);
		lbDescricao.clear();
		dpData.setValue(null);
		dpData.requestFocus();
		dpData.setDisable(false);
		btAdiar.setDisable(true);
		btConcluido.setDisable(true);
		btExcluir.setDisable(true);
		tvTarefa.getSelectionModel().clearSelection();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cbImportancia.setItems(FXCollections.observableArrayList(GrauImportancia.values()));
		
		// definir os parametros para as colunas do tableview
		tcData.setCellValueFactory(new PropertyValueFactory<>("dataLimite"));
		tcTarefa.setCellValueFactory(new PropertyValueFactory<>("status"));
		tcImportancia.setCellValueFactory(new PropertyValueFactory<>("importancia"));
		tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tcData.setCellFactory(call -> {
			return new TableCell<Tarefa, LocalDate>() {
				@Override
				protected void updateItem(LocalDate item, boolean empty) {
					DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
					if (!empty) {
						setText(item.format(fmt));
					} else {
						setText("");
					}
				}
			};
		});
		// Evento de seleção de item na tabela
		tvTarefa.getSelectionModel().selectedItemProperty().addListener(this);
		carregarTarefas();
	}

	public void carregarTarefas() {
		try {
			tarefas = TarefaIO.readTarefa();
			tvTarefa.setItems(FXCollections.observableArrayList(tarefas));
			tvTarefa.refresh();
		} catch (IOException e) {
			JOptionPane.showConfirmDialog(null, "Erro ao carregar as tarefas" + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void changed(ObservableValue<? extends Tarefa> observable, Tarefa oldValue, Tarefa newValue) {
		// passo a referencia para a variavel global
		tarefa = newValue;
		if (tarefa != null) {
			tfName.setText(tarefa.getName());
			lbDescricao.setText(tarefa.getDescricao());
			tfStatus.setText(tarefa.getStatus()+"");
			dpData.setValue(tarefa.getDataLimite());
			cbImportancia.setValue(tarefa.getImportancia());
			btAdiar.setDisable(false);
			btConcluido.setDisable(false);
			btExcluir.setDisable(false);
			dpData.setDisable(true);
		}
	}
}
