package br.com.todolist.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import br.com.todolist.io.TarefaIO;
import br.com.todolist.model.GrauImportancia;
import br.com.todolist.model.StatusTarefa;
import br.com.todolist.model.Tarefa;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class IndexController implements Initializable{

	@FXML
	private TextField tfName;
	
	@FXML
	private TableColumn<Tarefa, LocalDate> tcData;
	
	@FXML
	private TableColumn<Tarefa, String> tcTarefa;
	
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
		if (tfName.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe o nome da tarefa", "Informe", JOptionPane.ERROR_MESSAGE);
			tfName.requestFocus();
		} else if (dpData.getValue() == null) {
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
			tarefa.setDescricao(tfName.getText());
			tarefa.setComentarios(lbDescricao.getText());
			tarefa.setImportancia(cbImportancia.getValue());

			System.out.println(tarefa.formatToSave());
			// TODO inserir no banco de dados
			try {
				TarefaIO.insert(tarefa);
				// Limpar os Campos
				limpar();
			}catch(FileNotFoundException e){
			JOptionPane.showMessageDialog(null, "Arquivo não encontrado: "+e.getMessage(),"Erro", JOptionPane.ERROR_MESSAGE);	
			}catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Arquivo não encontrado: "+e.getMessage(),"Erro", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void limpar() {
		tarefa = null;
		dpData.setValue(null);
		tfName.clear();
		cbImportancia.setValue(null);
		lbDescricao.clear();
		dpData.setValue(null);
		dpData.requestFocus();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cbImportancia.setItems(FXCollections.observableArrayList(GrauImportancia.values()));
		 
		//definir os parametros para as colunas do tableview
		tcData.setCellValueFactory(new PropertyValueFactory<>("dataLimite"));
		tcTarefa.setCellValueFactory(new PropertyValueFactory<>("descricao"));
		tcImportancia.setCellValueFactory(new PropertyValueFactory<>("importancia"));
		carregarTarefas();
	}
	
	public void carregarTarefas() {
		try {
			tarefas = TarefaIO.readTarefa();
			tvTarefa.setItems(FXCollections.observableArrayList(tarefas));
			tvTarefa.refresh();
		} catch (IOException e) {
			JOptionPane.showConfirmDialog(null, "Erro ao carregar as tarefas"+e.getMessage(),"Erro", JOptionPane.ERROR_MESSAGE);
		}
	}
}
