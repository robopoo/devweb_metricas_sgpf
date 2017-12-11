package br.com.sgpf.metrica.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.sgpf.metrica.core.entity.EntityModel;

@Entity
@Table(name = "TB_PAS_PARAMETRO_SISTEMA")
@SequenceGenerator(name = "SQ_PAS_PARAMETRO_SISTEMA", sequenceName = "SQ_PAS_PARAMETRO_SISTEMA")
public class ParametroSistemaTO extends EntityModel<Long> {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PAS_ID_PARAMETRO_SISTEMA")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_PAS_PARAMETRO_SISTEMA")
	private Long idParametroSistema;

	@Deprecated
	@Column(name = "PAS_DS_DIR_PLANILHA_FPA_EXPORT")
	private String dsDiretorioPlanilhaFPAExportacao;

	@OneToOne
	@JoinColumn(name = "ELC_ID_ELEMENTO_CONTAGEM")
	private ElementoContagemTO elementoContagemTO;

	@Column(name = "PAS_DS_EMAIL_QA")
	private String dsEmailQA;

	public Long getIdParametroSistema() {
		return idParametroSistema;
	}

	public ElementoContagemTO getElementoContagemTO() {
		return elementoContagemTO;
	}

	public void setElementoContagemTO(ElementoContagemTO elementoContagemTO) {
		this.elementoContagemTO = elementoContagemTO;
	}

	public void setIdParametroSistema(Long idParametroSistema) {
		this.idParametroSistema = idParametroSistema;
	}

	public String getDsDiretorioPlanilhaFPAExportacao() {
		return dsDiretorioPlanilhaFPAExportacao;
	}

	public void setDsDiretorioPlanilhaFPAExportacao(String dsDiretorioPlanilhaFPAExportacao) {
		this.dsDiretorioPlanilhaFPAExportacao = dsDiretorioPlanilhaFPAExportacao;
	}

	@Override
	public Long getEntityId() {
		return this.idParametroSistema;
	}

	public String getDsEmailQA() {
		return dsEmailQA;
	}

	public void setDsEmailQA(String dsEmailQA) {
		this.dsEmailQA = dsEmailQA;
	}

}
