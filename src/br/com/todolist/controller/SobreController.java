package br.com.todolist.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SobreController {
	@FXML
    private Button btOk;

    @FXML
    public void btOkClick(ActionEvent event) {
    	//caminho para chegar no stage(janela) atraves do botao ok
    	Stage stage =(Stage) btOk.getScene().getWindow();
    	//fechando a janela
    	stage.close();
    }
}
