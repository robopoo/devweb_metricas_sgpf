package br.com.sgpf.metrica.core.enumeration;

import br.com.sgpf.metrica.enumeration.TipoPerfilEnum;

public enum OperacaoMenuEnum {
	GERENCIAR_BANCO_DADOS(new TipoPerfilEnum[] { TipoPerfilEnum.ADMINISTRADOR }, "/view/gbd", true),
	GERENCIAR_SISTEMAS(new TipoPerfilEnum[] { TipoPerfilEnum.ADMINISTRADOR }, "/view/sistema", true),
	GERENCIAR_USUARIOS(new TipoPerfilEnum[] { TipoPerfilEnum.ADMINISTRADOR }, "/view/usuario", true),
	GERENCIAR_CLIENTES(new TipoPerfilEnum[] { TipoPerfilEnum.ADMINISTRADOR, TipoPerfilEnum.OPERACIONAL }, "/view/pessoa", true),
	GERENCIAR_CONTESTACAO(new TipoPerfilEnum[] { TipoPerfilEnum.ADMINISTRADOR }, "/view/contestacao", true),
	GERENCIAR_OPERACOES(new TipoPerfilEnum[] { TipoPerfilEnum.ADMINISTRADOR, TipoPerfilEnum.OPERACIONAL }, "/view/operacao", true),
	GERENCIAR_RELATORIOS(new TipoPerfilEnum[] { TipoPerfilEnum.ADMINISTRADOR, TipoPerfilEnum.OPERACIONAL }, "/view/relatorios", true),
	CONSULTAR_AUTORIZACOES_GBD(new TipoPerfilEnum[] { TipoPerfilEnum.ADMINISTRADOR, TipoPerfilEnum.OPERACIONAL }, "/view/autorizacaoGbd", true),
	CONSULTAR_ARQUIVOS(new TipoPerfilEnum[] { TipoPerfilEnum.ADMINISTRADOR, TipoPerfilEnum.OPERACIONAL }, "/view/arquivo", true),
	GERENCIAR_MODALIDADE_CREDITO(new TipoPerfilEnum[] { TipoPerfilEnum.ADMINISTRADOR, TipoPerfilEnum.OPERACIONAL }, "/view/modalidadeCredito", true),
	GERENCIAR_PARAMETRO_SISTEMA(new TipoPerfilEnum[] { TipoPerfilEnum.ADMINISTRADOR }, "/view/parametroSistema", true),
	CONSULTAR_REMESSA(new TipoPerfilEnum[] { TipoPerfilEnum.ADMINISTRADOR }, "/view/remessa", true);

	private TipoPerfilEnum[] tipoPerfils;

	private String diretorio;

	private boolean liberado;

	private OperacaoMenuEnum(TipoPerfilEnum[] tipoPerfils, String diretorio, boolean liberado) {
		this.tipoPerfils = tipoPerfils;
		this.diretorio = diretorio;
		this.liberado = liberado;
	}

	public TipoPerfilEnum[] getTipoPerfils() {
		return tipoPerfils;
	}

	public String getDiretorio() {
		return diretorio;
	}

	public boolean isLiberado() {
		return liberado;
	}

}
