package br.com.sgpf.metrica.core.enumeration;

public enum OperacoesEnum {

	ACESSO("Acesso"),
	INSERCAO("Inclusão"),
	ALTERACAO("Alteração"),
	EXCLUSAO("Exclusão"),
	FILTRAGEM("Filtragem"),
	VISUALIZACAO("Visualização"),
	LISTAGEM("Listagem"),
	DESATIVACAO("Desativação"),
	ATIVACAO("Ativação"),
	PUBLICAR("Publicar"),
	NENHUM("Nenhum");

	private String rotulo;
	
	private OperacoesEnum(String rotulo) {
		this.rotulo = rotulo;
	}

	public String getRotulo() {
		return rotulo;
	}
}
