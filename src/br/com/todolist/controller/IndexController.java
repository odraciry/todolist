package br.com.todolist.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import br.com.todolist.model.GrauImportancia;
import br.com.todolist.model.StatusTarefa;
import br.com.todolist.model.Tarefa;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class IndexController implements Initializable {
	
	
	@FXML
	private ImageView btSave;

	@FXML
	private TextField tfName;

	@FXML
	private DatePicker dpData;

	@FXML
	private TextArea lbDescricao;

	@FXML
	private ComboBox<GrauImportancia> cbImportancia;

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
			JOptionPane.showMessageDialog(null, "Informe a importācia da tarefa", "Informe", JOptionPane.ERROR_MESSAGE);
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
			// TODO inserir no banco d dados

			// Limpar os Campos
			limpar();
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
		
	}
}
