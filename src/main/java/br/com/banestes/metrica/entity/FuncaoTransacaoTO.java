package br.com.sgpf.metrica.entity;

import java.math.BigDecimal;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import br.com.sgpf.metrica.enumeration.TipoComplexidadeEnum;
import br.com.sgpf.metrica.enumeration.TipoElementoContagemEnum;
import br.com.sgpf.metrica.enumeration.TipoFuncaoTransacaoEnum;

@MappedSuperclass
public abstract class FuncaoTransacaoTO extends FuncaoTO {

	private static final long serialVersionUID = 1L;

	@Enumerated(value = EnumType.ORDINAL)
	private TipoFuncaoTransacaoEnum funcaoTransacaoTP;

	private Integer qtArquivoReferenciado;

	@Override
	public Integer getARTR() {
		return this.qtArquivoReferenciado;
	}

	public TipoComplexidadeEnum obterComplexidade() {

		if (this.getElementoContagemTO().getTpElementoContagem() == TipoElementoContagemEnum.QUANTIDADE)
			return null;

		Integer quantidadeTD = getQtTipoDados();
		Integer quantidadeAR = getQtArquivoReferenciado();

		if (quantidadeTD == null || quantidadeAR == null)
			throw new IllegalStateException(
					"Não é possível obter complexidade sem a quantidade de Tipo de dados ou de Arquivos Referenciados!");

		if (TipoFuncaoTransacaoEnum.EE.equals(this.funcaoTransacaoTP)) {

			if ((quantidadeAR < 2 && quantidadeTD < 5) || (quantidadeAR < 2 && quantidadeTD >= 5 && quantidadeTD <= 15)
					|| (quantidadeAR == 2 && quantidadeTD < 5)) {

				return TipoComplexidadeEnum.BAIXA;

			} else if ((quantidadeAR < 2 && quantidadeTD > 15)
					|| (quantidadeAR == 2 && quantidadeTD >= 5 && quantidadeTD <= 15)
					|| (quantidadeAR > 2 && quantidadeTD < 5)) {

				return TipoComplexidadeEnum.MEDIA;

			} else if ((quantidadeAR == 2 && quantidadeTD > 15) || (quantidadeAR > 2 && quantidadeTD >= 5)) {

				return TipoComplexidadeEnum.ALTA;
			}

		} else {
			if ((quantidadeAR <= 1 && quantidadeTD >= 1 && quantidadeTD <= 19)
					|| ((quantidadeAR == 2 || quantidadeAR == 3) && quantidadeTD >= 1 && quantidadeTD <= 5)) {
				return TipoComplexidadeEnum.BAIXA;
			} else if ((quantidadeAR > 3 && quantidadeTD >= 6) || (quantidadeAR >= 2 && quantidadeTD > 19)) {
				return TipoComplexidadeEnum.ALTA;
			} else if (quantidadeTD > 0) {
				return TipoComplexidadeEnum.MEDIA;
			}
		}

		throw new IllegalStateException("Não foi possível obter complexidade!");
	}

	public BigDecimal obterValorPontoFuncao() {
		int valorPF = 0;

		if (getComplexidadeTP() != null) {

			switch (getComplexidadeTP()) {
			case BAIXA: {

				if (TipoFuncaoTransacaoEnum.EE.equals(getFuncaoTransacaoTP())
						|| TipoFuncaoTransacaoEnum.CE.equals(getFuncaoTransacaoTP())) {
					valorPF = 3;
				} else if (TipoFuncaoTransacaoEnum.SE.equals(getFuncaoTransacaoTP())) {
					valorPF = 4;
				}
				break;
			}
			case MEDIA: {
				if (TipoFuncaoTransacaoEnum.EE.equals(getFuncaoTransacaoTP())
						|| TipoFuncaoTransacaoEnum.CE.equals(getFuncaoTransacaoTP())) {
					valorPF = 4;
				} else if (TipoFuncaoTransacaoEnum.SE.equals(getFuncaoTransacaoTP())) {
					valorPF = 5;
				}
				break;
			}
			case ALTA: {
				if (TipoFuncaoTransacaoEnum.EE.equals(getFuncaoTransacaoTP())
						|| TipoFuncaoTransacaoEnum.CE.equals(getFuncaoTransacaoTP())) {
					valorPF = 6;
				} else if (TipoFuncaoTransacaoEnum.SE.equals(getFuncaoTransacaoTP())) {
					valorPF = 7;
				}
				break;
			}
			default:
				throw new IllegalStateException("Complexidade inexistente!");
			}
		}

		return new BigDecimal(valorPF);
	}

	@Override
	public String getTipoFuncaoDescricao() {
		return this.funcaoTransacaoTP != null ? this.funcaoTransacaoTP.toString() : "";
	}

	public TipoFuncaoTransacaoEnum getFuncaoTransacaoTP() {
		return funcaoTransacaoTP;
	}

	public void setFuncaoTransacaoTP(TipoFuncaoTransacaoEnum funcaoTransacaoTP) {
		this.funcaoTransacaoTP = funcaoTransacaoTP;
	}

	public Integer getQtArquivoReferenciado() {
		return qtArquivoReferenciado;
	}

	public void setQtArquivoReferenciado(Integer qtArquivoReferenciado) {
		this.qtArquivoReferenciado = qtArquivoReferenciado;
	}
	


}
