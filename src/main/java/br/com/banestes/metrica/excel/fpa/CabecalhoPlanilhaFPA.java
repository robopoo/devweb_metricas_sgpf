package br.com.sgpf.metrica.excel.fpa;

import java.util.Date;


public interface CabecalhoPlanilhaFPA {

	public Date getDtCriacao();

	public Date getDataRevisao();

	public String getFornecedor();

	public String getTituloPlanilha();

	public String getSistema();

	public String getAnalistaFornecedor();

	public String getAnalistaCliente();

	enum CabecalhoPlanilha{
		FORNECEDOR(5, 3),
		TITULO(5, 4),
		SISTEMA(5, 5),
		ANALISTA_FORNECEDOR(5, 6),
		ANALISTA_CLIENTE(5, 7),
		DATA_CRIACAO(17, 6),
		DATA_REVISAO(17, 7);
		
		private Integer column;
		
		private Integer row;
		
		private CabecalhoPlanilha(Integer column, Integer row){
			this.column = column;
			this.row = row;
		}

		public Integer getColumn() {
			return column;
		}

		public Integer getRow() {
			return row;
		}
		
		
	}
	
}
