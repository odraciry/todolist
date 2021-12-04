package br.com.todolist.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class Tarefa implements Comparable<Tarefa> {
	private long id;
	private LocalDate dataCriacao;
	private LocalDate dataLimite;
	private LocalDate dataConcluida;
	private String descricao;
	private String comentarios;
	private String name;
	private StatusTarefa status;
	private GrauImportancia importancia;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the importancia
	 */
	public GrauImportancia getImportancia() {
		return importancia;
	}

	/**
	 * @param object the importancia to set
	 */
	public void setImportancia(GrauImportancia object) {
		this.importancia = object;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the dataCriacao
	 */
	public LocalDate getDataCriacao() {
		return dataCriacao;
	}

	/**
	 * @param dataCriacao the dataCriacao to set
	 */
	public void setDataCriacao(LocalDate dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	/**
	 * @return the dataLimite
	 */
	public LocalDate getDataLimite() {
		return dataLimite;
	}

	/**
	 * @param dataLimite the dataLimite to set
	 */
	public void setDataLimite(LocalDate dataLimite) {
		this.dataLimite = dataLimite;
	}

	/**
	 * @return the dataConcluida
	 */
	public LocalDate getDataConcluida() {
		return dataConcluida;
	}

	/**
	 * @param dataConcluida the dataConcluida to set
	 */
	public void setDataConcluida(LocalDate dataConcluida) {
		this.dataConcluida = dataConcluida;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return the comentarios
	 */
	public String getComentarios() {
		return comentarios;
	}

	/**
	 * @param comentarios the comentarios to set
	 */
	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	/**
	 * @return the status
	 */
	public StatusTarefa getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(StatusTarefa status) {
		this.status = status;
	}

	// transforma a tarefa em uma String para salvar
	public String formatToSave() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getId() + ";");
		DateTimeFormatter padraoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		builder.append(this.getDataCriacao().format(padraoData) + ";");
		builder.append(this.getDataLimite().format(padraoData) + ";");
		if (this.getDataConcluida() != null) {
			builder.append(this.getDataConcluida().format(padraoData));
		}
		builder.append(";");
		builder.append(this.getImportancia().ordinal());
		builder.append(";");
		builder.append(this.getDescricao() + ";");
		builder.append(this.getComentarios() + ";");
		builder.append(this.getStatus().ordinal() + ";");
		builder.append(this.getName() + ";\n");
		return builder.toString();
	}

	// compara objetos
	@Override
	public int compareTo(Tarefa o) {
		if (this.getDataLimite().isBefore(o.getDataLimite())) {
			//caso data do obj for antes do obj comparado
			return -1;
		} else if (this.getDataLimite().isAfter(o.getDataLimite())) {
			//caso data do obj for depois do obj comparado
			return 1;
		} else {
			//caso data for igual
			return this.getName().compareTo(o.getName());
		}
	}
}
