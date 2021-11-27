package br.com.todolist.model;

public enum StatusTarefa {
	ABERTA("aberta"), ADIADA("Adiada"), CONCLUIDA("Conclu�da");
	
	private StatusTarefa(String descricao) {
		this.descricao = descricao;
	}
	
	String descricao;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return descricao;
	}
}
