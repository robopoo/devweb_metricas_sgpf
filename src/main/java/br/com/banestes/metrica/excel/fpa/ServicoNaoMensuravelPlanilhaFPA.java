package br.com.sgpf.metrica.excel.fpa;


public interface ServicoNaoMensuravelPlanilhaFPA {
	
	public String getQuantidadeItensPlanilha();
	
	public String getCdElementoContagem();
	
	public enum QuantidadeIten{
		
		MDM(5, 9),
		MMA(5, 10),
		MME(5, 11),
		TAI(5, 12),
		TAA(5, 13),
		TAE(5, 14),
		DHC(5, 15),
		CDT(5, 16),
		CTI(5, 17),
		CTA(5, 18),
		CTE(5, 19),
		CFI(5, 20),
		CFA(5, 21),
		CFE(5, 22),
		CTD(5, 23);
		
		private Integer column;
		
		private Integer row;
		
		private QuantidadeIten(Integer column, Integer row){
			this.column = column;
			this.row = row;
		}

		public Integer getColumn() {
			return column;
		}

		public Integer getRow() {
			return row;
		}
		
		public static QuantidadeIten parse(String cdElementoContagem){
			
			for (QuantidadeIten iten : QuantidadeIten.values()) {
				if(cdElementoContagem.indexOf(iten.toString()) > -1){
					return iten;
				}
			}
			
			return null;
			
		}

	}
}
