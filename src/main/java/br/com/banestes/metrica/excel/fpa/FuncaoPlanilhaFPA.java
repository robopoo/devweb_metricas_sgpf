package br.com.sgpf.metrica.excel.fpa;

public interface FuncaoPlanilhaFPA {
	
	public static final Integer INDEX_ROW_START = 9;

	public String getNome();
	
	public String getTipoFuncaoDescricao();
	
	public String getCodElementoContagem();
	
	public Integer getQtTipoDados();
	
	public Integer getARTR();

	
	enum ColunaFuncao{
		
		NOME_FUNCAO(0),
		TIPO_FUNCAO(6),
		ELEMENTO_CONTAGEM(7),
		TIPO_DADO(8),
		AR_TR(9);

		private Integer column;
		
		private ColunaFuncao(Integer column){
			this.column = column;
		}

		public Integer getColumn() {
			return column;
		}

	}
}
