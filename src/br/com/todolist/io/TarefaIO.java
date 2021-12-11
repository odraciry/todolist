package br.com.todolist.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import br.com.todolist.model.GrauImportancia;
import br.com.todolist.model.StatusTarefa;
import br.com.todolist.model.Tarefa;
import sun.invoke.empty.Empty;
import sun.misc.ObjectInputFilter.Status;

public class TarefaIO {
	private static final String FOLDER = System.getProperty("user.home") + "/todolist";
	private static final String FILE_ID = FOLDER + "/id.csv";
	private static final String FILE_TAREFA = FOLDER + "/tarefas.csv";

	// cria o arquivo das tarefas e do ID
	public static void creatFiles() {
		try {
			File folder = new File(FOLDER);
			File fileId = new File(FILE_ID);
			File fileTarefa = new File(FILE_TAREFA);

			if (!folder.exists()) {
				folder.mkdir();
				fileTarefa.createNewFile();
				fileId.createNewFile();
				FileWriter writer = new FileWriter(fileId);
				writer.write("1");
				writer.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void resetFile() {
		File folder = new File(FOLDER);
		File fileId = new File(FILE_ID);
		File fileTarefa = new File(FILE_TAREFA);
		System.out.println(fileId.delete());
		System.out.println(fileTarefa.delete());
		System.out.println(folder.delete());
		creatFiles();
	}

	// insere no banco de dados
	public static void insert(Tarefa tarefa) throws IOException {
		File arqTarefa = new File(FILE_TAREFA);
		File arqId = new File(FILE_ID);
		Scanner sc = new Scanner(arqId);
		tarefa.setId(sc.nextLong());
		sc.close();
		FileWriter writer = new FileWriter(arqTarefa, true);
		writer.append(tarefa.formatToSave());
		writer.close();
		// gravar o novo id no arquivo de id
		writer = new FileWriter(arqId);
		writer.write((tarefa.getId() + 1) + "");
		writer.close();
	}

	// le o arquivo e devolve a lista dos arquivos
	public static List<Tarefa> readTarefa() throws IOException {
		File arqTarefa = new File(FILE_TAREFA);
		List<Tarefa> tarefas = new ArrayList<>();
		FileReader reader = new FileReader(arqTarefa);
		BufferedReader buff = new BufferedReader(reader);
		String linha;
		while ((linha = buff.readLine()) != null) {
			String[] vetor = linha.split(";");
			Tarefa t = new Tarefa();
			t.setId(Long.parseLong(vetor[0]));
			DateTimeFormatter padraoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			t.setDataCriacao(LocalDate.parse(vetor[1], padraoData));
			t.setDataLimite(LocalDate.parse(vetor[2], padraoData));
			if (!vetor[3].isEmpty()) {
				t.setDataConcluida(LocalDate.parse(vetor[3], padraoData));
			}
			int indImportancia = Integer.parseInt(vetor[4]);
			t.setImportancia(GrauImportancia.values()[indImportancia]);
			t.setDescricao(vetor[5]);
			t.setComentarios(vetor[6]);
			int indStatus = Integer.parseInt(vetor[7]);
			t.setName(vetor[8]);
			t.setStatus(StatusTarefa.values()[indStatus]);

			tarefas.add(t);
		}

		// fecha reader
		reader.close();
		// fecha buff
		buff.close();

		Collections.sort(tarefas);
		return tarefas;
	}

	// Reescreve o arquivo
	public static void saveTarefas(List<Tarefa> tarefas) throws IOException {
		File arqTarefas = new File(FILE_TAREFA);
		FileWriter writer = new FileWriter(arqTarefas);
		for (Tarefa t : tarefas) {
			writer.write(t.formatToSave());
		}
		writer.close();
	}

	public static long proximoId() throws IOException {
		Scanner sc = new Scanner(new File(FILE_ID));
		long proxId = sc.nextLong();
		sc.close();
		return proxId;
	}

	public static void exportHtml(List<Tarefa> tarefas, File arquivo) throws IOException {
		FileWriter writer = new FileWriter(arquivo);
		writer.write("<html>\n");
		writer.write("<head>\n");
		writer.write("<style>\n" + "*{" + "background-color:7f89df;" + "font-family: arial;" + "}" + "ul, h1, li{"
				+ "text-align:center;" + "}" + "" + "</style>\n");
		writer.write("</head>\n");
		writer.write("<body>\n");
		writer.write("<h1>Lista de Tarefas</h1>\n");
		writer.write("<ul>\n");
		for (Tarefa tarefa : tarefas) {
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MMMM/yyyy");
			writer.write("<li>" + tarefa.getDataLimite().format(fmt) + " - " + tarefa.getName() + "</li>\n");
		}

		writer.write("</ul>\n");
		writer.write("</body>\n");
		writer.write("</html>");
		writer.close();
	}
}
