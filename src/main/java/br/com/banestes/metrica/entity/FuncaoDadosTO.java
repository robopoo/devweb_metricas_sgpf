package br.com.sgpf.metrica.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;

import br.com.sgpf.metrica.enumeration.TipoComplexidadeEnum;
import br.com.sgpf.metrica.enumeration.TipoElementoContagemEnum;
import br.com.sgpf.metrica.enumeration.TipoFuncaoDadosProjetoEnum;

@MappedSuperclass
public abstract class FuncaoDadosTO extends FuncaoTO {

	private static final long serialVersionUID = 1L;

	// @Enumerated(value=EnumType.ORDINAL)
	@Type(type = "tipoFuncaoDadosProjetoUserType")
	@Column(name = "FDP_TP_FUNCAO")
	private TipoFuncaoDadosProjetoEnum tipoFuncaoDadosProjeto;

	private Integer qtTipoRegistro;

	@Override
	public Integer getARTR() {
		return this.qtTipoRegistro;
	}

	@Override
	public String getTipoFuncaoDescricao() {
		return this.getTipoFuncaoDadosProjeto() != null ? this.getTipoFuncaoDadosProjeto().toString() : "";
	}

	public Integer getQtTipoRegistro() {
		return qtTipoRegistro;
	}

	public void setQtTipoRegistro(Integer qtTipoRegistro) {
		this.qtTipoRegistro = qtTipoRegistro;
	}

	public TipoFuncaoDadosProjetoEnum getFuncaoDadosTP() {
		return getTipoFuncaoDadosProjeto();
	}

	public void setFuncaoDadosTP(TipoFuncaoDadosProjetoEnum funcaoDadosTP) {
		this.setTipoFuncaoDadosProjeto(funcaoDadosTP);
	}

	/**
	 * @return the tipoFuncaoDadosProjeto
	 */
	public TipoFuncaoDadosProjetoEnum getTipoFuncaoDadosProjeto() {
		return tipoFuncaoDadosProjeto;
	}

	/**
	 * @param tipoFuncaoDadosProjeto
	 *            the tipoFuncaoDadosProjeto to set
	 */
	public void setTipoFuncaoDadosProjeto(TipoFuncaoDadosProjetoEnum tipoFuncaoDadosProjeto) {
		this.tipoFuncaoDadosProjeto = tipoFuncaoDadosProjeto;
	}

	/**
	 * | 1 - 19 | 20 - 50 | > 50 1 | B | B | M 2 - 5 | B | M | A > 5 | M | A | A
	 * 
	 * B -> 1, 1-50 | 2 - 5, 1 - 19 A -> >5, >20 | 2 - 5, >50
	 * 
	 * B -> { [TR == 1 and (TD >= 1 and TD <= 50)] or [(TR >= 2 and TR <= 5) and
	 * (TD >= 1 and TD <= 19)] } A -> { [TR > 5 and TD > 20] or [(TR >= 2 and TR
	 * <=5) and TD > 50] }
	 * 
	 * @param funcao
	 * @return
	 */
	public TipoComplexidadeEnum obterComplexidade() {

		if (this.getElementoContagemTO().getTpElementoContagem() == TipoElementoContagemEnum.QUANTIDADE)
			return null;

		Integer quantidadeTD = getQtTipoDados();
		Integer quantidadeTR = getQtTipoRegistro();

		if (quantidadeTD != null && quantidadeTR != null) {
			if (quantidadeTR == 1 && (quantidadeTD >= 1 && quantidadeTD <= 50)
					|| ((quantidadeTR >= 2 && quantidadeTR <= 5) && (quantidadeTD >= 1 && quantidadeTD <= 19))) {
				return TipoComplexidadeEnum.BAIXA;
			} else if ((quantidadeTR > 5 && quantidadeTD >= 20)
					|| ((quantidadeTR >= 2 && quantidadeTR <= 5) && (quantidadeTD > 50))) {
				return TipoComplexidadeEnum.ALTA;
			} else if (quantidadeTD > 0 && quantidadeTR > 0) {
				return TipoComplexidadeEnum.MEDIA;
			}
		}

		return null;
	}

	public BigDecimal obterValorPontoFuncao() {
		int valorPF = 0;

		if (getComplexidadeTP() != null) {

			switch (getComplexidadeTP()) {
			case BAIXA: {
				if (getTipoFuncaoDadosProjeto() == TipoFuncaoDadosProjetoEnum.ALI) {
					valorPF = 7;
				} else if (getTipoFuncaoDadosProjeto() == TipoFuncaoDadosProjetoEnum.AIE) {
					valorPF = 5;
				}
				break;
			}
			case MEDIA: {
				if (getTipoFuncaoDadosProjeto() == TipoFuncaoDadosProjetoEnum.ALI) {
					valorPF = 10;
				} else if (getTipoFuncaoDadosProjeto() == TipoFuncaoDadosProjetoEnum.AIE) {
					valorPF = 7;
				}
				break;
			}
			case ALTA: {
				if (getTipoFuncaoDadosProjeto() == TipoFuncaoDadosProjetoEnum.ALI) {
					valorPF = 15;
				} else if (getTipoFuncaoDadosProjeto() == TipoFuncaoDadosProjetoEnum.AIE) {
					valorPF = 10;
				}
				break;
			}
			default:
				break;
			}
		}

		return new BigDecimal(valorPF);
	}
}
