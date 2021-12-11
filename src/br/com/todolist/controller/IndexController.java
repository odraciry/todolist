package br.com.todolist.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import br.com.todolist.io.TarefaIO;
import br.com.todolist.model.GrauImportancia;
import br.com.todolist.model.StatusTarefa;
import br.com.todolist.model.Tarefa;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sun.awt.RequestFocusController;

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
	private TextField tfCodigo;

	@FXML
	private Label lbFinalizacao;

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
	void exportar(ActionEvent event) {
		FileFilter filter = new FileNameExtensionFilter("Arquivos HTML", "html", "htm");
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(filter);
		if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			File arqSelecionado = chooser.getSelectedFile();
			if (!arqSelecionado.getAbsolutePath().endsWith(".html")) {
				arqSelecionado = new File(arqSelecionado + ".html");
			}
			try {
				TarefaIO.exportHtml(tarefas, arqSelecionado);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@FXML
	void sair(ActionEvent event) {
		// fechando o programa
		int decisao = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja sair?", "Fechar o programa",
				JOptionPane.YES_NO_OPTION);
		// sim = 0 (entao fecha o programa), nao = 1 (nao fecha)
		if (decisao == 0)
			System.exit(0);

	}

	@FXML
	void sobre(ActionEvent event) {
		AnchorPane root;
		try {
			root = (AnchorPane) FXMLLoader.load(getClass().getResource("/br/com/todolist/view/Sobre.fxml"));
			Scene scene = new Scene(root, 400, 400);
			scene.getStylesheets()
					.add(getClass().getResource("/br/com/todolist/view/application.css").toExternalForm());
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 @FXML
	 void resetar(ActionEvent event) {
		// resetar tarefas e IDs
		TarefaIO.resetFile();
	 }

	@FXML
	void ajuda(ActionEvent event) {

	}

	@FXML
	void arquivo(ActionEvent event) {

	}

	@FXML
	void btAdiarClick(ActionEvent event) {
		if (tarefa != null) {
			int dias;
			do {
				dias = Integer.parseInt(JOptionPane.showInputDialog(null, "Quantos dias você deseja adiar?",
						"Informe quantos dias", JOptionPane.QUESTION_MESSAGE));

				if (dias < 1)
					JOptionPane.showMessageDialog(null, "Informe um número válido", "Informe",
							JOptionPane.ERROR_MESSAGE);

			} while (dias < 1);

			// acrescenta dias escolhido na data limite e cria uma nova data
			LocalDate novaData = tarefa.getDataLimite().plusDays(dias);
			// substitui a data limite
			tarefa.setDataLimite(novaData);
			// Muda o status da tarefa para adiado
			tarefa.setStatus(StatusTarefa.ADIADA);
			try {
				// salva a tarefa
				TarefaIO.saveTarefas(tarefas);

				// formata o padrao da data
				DateTimeFormatter padraoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

				JOptionPane.showMessageDialog(null, "A nova data da tarefa é: " + novaData.format(padraoData),
						"Tarefa adiada", JOptionPane.INFORMATION_MESSAGE);
				carregarTarefas();
				limpar();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Erro ao salvar as tarefas: " + e.getMessage(), "Erro",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}

		}
	}

	@FXML
	void btApagarClick(ActionEvent event) {
		limpar();
	}

	@FXML
	void btConcluirClick(ActionEvent event) {
		if (tarefa != null) {

			// acrescenta dias escolhido na data limite e cria uma nova data
			LocalDate novaData = LocalDate.now();
			// substitui a data limite
			tarefa.setDataLimite(novaData);
			// Muda o status da tarefa para adiado
			tarefa.setStatus(StatusTarefa.CONCLUIDA);
			try {
				// salva a tarefa
				TarefaIO.saveTarefas(tarefas);

				// formata o padrao da data
				DateTimeFormatter padraoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

				JOptionPane.showMessageDialog(null, "A tarefa foi concluida: " + novaData.format(padraoData),
						"Tarefa concluida", JOptionPane.INFORMATION_MESSAGE);
				carregarTarefas();
				limpar();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Erro ao salvar as tarefas: " + e.getMessage(), "Erro",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
	}

	@FXML
	void btExcluirClick(ActionEvent event) {

		if (tarefa != null) {
			int resposta = JOptionPane.showConfirmDialog(null, "Deseja excluir a tarefa '" + tarefa.getName() + "' ?",
					"Confirmar exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (resposta == 0) {
				tarefas.remove(tarefa);
				try {
					TarefaIO.saveTarefas(tarefas);
					carregarTarefas();
					limpar();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Erro ao excluir tarefa: " + e.getMessage(), "Erro",
							JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "Tarefa excluida com sucesso!", "Excluisão bem sucedida",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
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
			// verifica se a tarefa é nula para instancia-la
			if (tarefa == null) {
				// istanciando a tarefa
				tarefa = new Tarefa();
				tarefa.setDataCriacao(LocalDate.now());
				tarefa.setStatus(StatusTarefa.ABERTA);
			}
			// popular a tarefa
			tarefa.setDataLimite(dpData.getValue());
			tarefa.setDescricao(lbDescricao.getText());
			tarefa.setImportancia(cbImportancia.getValue());
			tarefa.setName(tfName.getText());

			System.out.println(tarefa.formatToSave());
			// TODO inserir no banco de dados
			try {
				if (tarefa.getId() == 0) {
					TarefaIO.insert(tarefa);
				} else {
					TarefaIO.saveTarefas(tarefas);
				}
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

	// limpa a parte de edição da tarefa
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
		tfCodigo.clear();
		btSalvar.setDisable(false);
		lbDescricao.setDisable(false);
		try {
			tfCodigo.setText(TarefaIO.proximoId() + "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// inicializa a tarefa na tabela
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cbImportancia.setItems(FXCollections.observableArrayList(GrauImportancia.values()));
		// mostra o ID da proxima tarefa
		try {
			tfCodigo.setText(TarefaIO.proximoId() + "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// definir os parametros para as colunas do tableview
		tcData.setCellValueFactory(new PropertyValueFactory<>("dataLimite"));
		tcTarefa.setCellValueFactory(new PropertyValueFactory<>("status"));
		tcName.setCellValueFactory(new PropertyValueFactory<>("name"));

		// adiciona uma celula para a coluna data
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

		// define as cores das linhas com base no Status da tarefa
		tvTarefa.setRowFactory(call -> new TableRow<Tarefa>() {
			protected void updateItem(Tarefa item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null) {
					setStyle("");
				} else if (item.getStatus() == StatusTarefa.CONCLUIDA) {
					setStyle("-fx-background-color:#03bb85");
				} else if (item.getDataLimite().isBefore(LocalDate.now())) {
					setStyle("-fx-background-color: tomato");
				} else if (item.getStatus() == StatusTarefa.ADIADA) {
					setStyle("-fx-background-color:#ffdb58");
				} else {
					setStyle("-fx-background-color:#4169E1");
				}
			};
		});
		// Evento de seleção de item na tabela
		tvTarefa.getSelectionModel().selectedItemProperty().addListener(this);
		carregarTarefas();
	}

	// carrega as tarefas para serem lidas na parte de editar
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

		tarefa = newValue; // <--aponta para a tarefa
		if (tarefa != null) {
			tfName.setText(tarefa.getName());
			lbDescricao.setText(tarefa.getDescricao());
			tfStatus.setText(tarefa.getStatus() + "");
			dpData.setValue(tarefa.getDataLimite());
			cbImportancia.setValue(tarefa.getImportancia());
			dpData.setDisable(true);
			tfCodigo.setText(tarefa.getId() + "");
			btAdiar.setDisable(true);
			lbFinalizacao.setText("Data para finalização:");
			// desabilita os botoes caso status da tarefa for concluida
			if (tarefa.getStatus() == StatusTarefa.CONCLUIDA) {
				btAdiar.setDisable(true);
				btExcluir.setDisable(false);
				btConcluido.setDisable(true);
				btSalvar.setDisable(true);
				cbImportancia.setEditable(false);
				lbDescricao.setEditable(false);
				dpData.setEditable(false);
				lbFinalizacao.setText("Data de conclusão:");

				// desabilita o botao adiar caso tarefa ja foi adiada
			} else if (tarefa.getStatus() == StatusTarefa.ADIADA) {
				btAdiar.setDisable(true);
				btConcluido.setDisable(false);
				btExcluir.setDisable(false);
				// habilita todos os botoes
			} else {
				btAdiar.setDisable(false);
				btConcluido.setDisable(false);
				btExcluir.setDisable(false);
			}
		}
	}
}
