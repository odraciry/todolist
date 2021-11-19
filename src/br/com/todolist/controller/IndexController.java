package br.com.todolist.controller;

import java.time.LocalDate;

import javax.swing.JOptionPane;

import br.com.todolist.model.StatusTarefa;
import br.com.todolist.model.Tarefa;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class IndexController {
	@FXML
	private ImageView btSave;

	@FXML
	private TextField tfName;

	@FXML
	private DatePicker dpData;

	@FXML
	private TextArea lbDescricao;

	@FXML
	private ComboBox<?> cbImportancia;

	private Tarefa tarefa;

	@FXML
	void btAdiarClick(ActionEvent event) {

	}

	@FXML
	void btApagarClick(ActionEvent event) {

	}

	@FXML
	void btConcluirClick(ActionEvent event) {

	}

	@FXML
	void btExcluirClick(ActionEvent event) {

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

			// TODO inserir no banco d dados

			// Limpar os Campos
		}
	}
	
	private void limpar(){
		tarefa = null;
		dpData.setValue(null);
		tfName.clear();
		cbImportancia.setValue(null);
		lbDescricao.clear();
		dpData.setValue(null);
	}
}
