package br.com.todolist.model;

public enum GrauImportancia {
	POUCO("Pouco importante"), MUITO("Muito importante"), SUPER("Super Importante");
	
	private GrauImportancia(String descricao) {
		this.descricao = descricao;
	}
	
	String descricao;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return descricao;
	}
}
