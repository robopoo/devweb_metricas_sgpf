package br.com.sgpf.metrica.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="TB_TRB_TIPO_REGISTRO_BASELINE")
@SequenceGenerator(name = "SQ_TRB_TIPO_REGISTRO_BASELINE", sequenceName = "SQ_TRB_TIPO_REGISTRO_BASELINE")
public class TipoRegistroBaseLineTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TRB_ID_TIPO_REGISTRO_BASELINE")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TRB_TIPO_REGISTRO_BASELINE")
	private Long idTipoRegistroFuncaoDadosBaseline;

	@ManyToOne
	@JoinColumn(name="FDB_ID_FUNCAO_DADOS_BASELINE")
	private FuncaoDadosBaselineTO funcaoDadosBaselineTO;
	
	@ManyToOne
	@JoinColumn(name="ENT_ID_ENTIDADE")
	private EntidadeTO entidadeTO;

	@OneToMany(mappedBy="tipoRegistroBaseLineTO", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.MERGE})
	private List<TipoDadosFuncaoDadosBaselineTO> tipoDadosBaselineTOs;

	public static TipoRegistroBaseLineTO valueOf(TipoRegistroProjetoTO tipoRegistroProjetoTO){
		
		TipoRegistroBaseLineTO tipoRegistroBaseLineTO = new TipoRegistroBaseLineTO();
		
		tipoRegistroBaseLineTO.setEntidadeTO(tipoRegistroProjetoTO.getEntidadeTO());
		
		List<TipoDadosFuncaoDadosBaselineTO> tds = new ArrayList<TipoDadosFuncaoDadosBaselineTO>(tipoRegistroProjetoTO.getTipoDadosFuncaoDadosProjetoTOs().size());
		TipoDadosFuncaoDadosBaselineTO td;
		for (TipoDadosFuncaoDadosProjetoTO funcao : tipoRegistroProjetoTO.getTipoDadosFuncaoDadosProjetoTOs()) {
			td = TipoDadosFuncaoDadosBaselineTO.valueOf(funcao);
			td.setTipoRegistroBaseLineTO(tipoRegistroBaseLineTO);
			tds.add(td);
		}
		
		tipoRegistroBaseLineTO.setTipoDadosBaselineTOs(tds);
		
		return tipoRegistroBaseLineTO;
	}
	
	public Long getIdTipoRegistroFuncaoDadosBaseline() {
		return idTipoRegistroFuncaoDadosBaseline;
	}

	public void setIdTipoRegistroFuncaoDadosBaseline(
			Long idTipoRegistroFuncaoDadosBaseline) {
		this.idTipoRegistroFuncaoDadosBaseline = idTipoRegistroFuncaoDadosBaseline;
	}

	public FuncaoDadosBaselineTO getFuncaoDadosBaselineTO() {
		return funcaoDadosBaselineTO;
	}

	public void setFuncaoDadosBaselineTO(FuncaoDadosBaselineTO funcaoDadosBaselineTO) {
		this.funcaoDadosBaselineTO = funcaoDadosBaselineTO;
	}

	public EntidadeTO getEntidadeTO() {
		return entidadeTO;
	}

	public void setEntidadeTO(EntidadeTO entidadeTO) {
		this.entidadeTO = entidadeTO;
	}

	public List<TipoDadosFuncaoDadosBaselineTO> getTipoDadosBaselineTOs() {
		return tipoDadosBaselineTOs;
	}

	public void setTipoDadosBaselineTOs(
			List<TipoDadosFuncaoDadosBaselineTO> tipoDadosBaselineTOs) {
		this.tipoDadosBaselineTOs = tipoDadosBaselineTOs;
	}



}