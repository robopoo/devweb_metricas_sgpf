/**
 * 
 */
package br.com.sgpf.metrica.enumeration;


/**
 * PD Case Informática Ltda. www.pdcase.com.br
 * 
 * @author Glauber Monteiro <mailto:glauber.monteiro@pdcase.com.br>
 * @version 1.0.0
 * @since 19/03/2014
 * @time 18:16:44
 */

public enum TipoContagemEnum {
	BASELINE("Baseline"), ARQUIVO_LOGICO("Arquivo Lógico");

	private String descricao;

	private TipoContagemEnum(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
