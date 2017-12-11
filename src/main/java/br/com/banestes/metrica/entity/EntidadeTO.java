package br.com.sgpf.metrica.entity;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import br.com.sgpf.metrica.core.annotation.LikeCriteria;
import br.com.sgpf.metrica.core.entity.EntityModel;
import br.com.sgpf.metrica.core.enumeration.MatchModeEnum;
import br.com.sgpf.metrica.core.enumeration.StatusEnum;
import br.com.sgpf.metrica.enumeration.CategoriaEnum;
import br.com.sgpf.metrica.enumeration.TipoEntidadeEnum;

@Entity
@Table(name = "TB_ENT_ENTIDADE")
@SequenceGenerator(name = "SQ_ENT_ID_ENTIDADE", sequenceName = "SQ_ENT_ID_ENTIDADE")
@NamedQueries({ @NamedQuery(name = "EntidadeTO.findByEmpresa", query = "select obj from EntidadeTO obj where obj.sistemaTO.empresaTO = :empresaTO and obj.status = :status and obj.categoria in (:negocio,:referencia)") })
public class EntidadeTO extends EntityModel<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4143056724322473865L;

	@Id
	@Column(name = "ENT_ID_ENTIDADE")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_ENT_ID_ENTIDADE")
	private Long idEntidade;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SIS_ID_SISTEMA", nullable = false)
	private SistemaTO sistemaTO;

	@LikeCriteria(caseSensitive = false, matchMode = MatchModeEnum.ANYWHERE)
	@Column(name = "ENT_NM_ENTIDADE", nullable = false)
	private String nmEntidade;

	@Column(name = "ENT_DS_ENTIDADE")
	private String dsEntidade;

	@Type(type = "categoriaEnumUserType")
	@Column(name = "ENT_TP_CATEGORIA", nullable = true)
	private CategoriaEnum categoria;

	@Type(type = "tipoEntidadeEnumUserType")
	@Column(name = "ENT_TP_ENTIDADE", nullable = true)
	private TipoEntidadeEnum tpEntidade;

	@Type(type = "statusEnumUserType")
	@Column(name = "ENT_ST_ENTIDADE", nullable = true)
	private StatusEnum status;

	@Column(name = "ENT_QT_TD", nullable = true)
	private Integer qtTd;

	@Column(name = "ENT_NR_VERSAO_REGISTRO", nullable = true)
	private Integer versaoRegistro;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidadeTO", cascade = { CascadeType.ALL })
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<AtributoTO> atributoTOlist;


	@Transient
	private boolean importar;

	@Override
	public Long getEntityId() {

		return idEntidade;
	}

	public Long getIdEntidade() {
		return idEntidade;
	}

	public void setIdEntidade(Long idEntidade) {
		this.idEntidade = idEntidade;
	}

	public SistemaTO getSistemaTO() {
		return sistemaTO;
	}

	public void setSistemaTO(SistemaTO sistemaTO) {
		this.sistemaTO = sistemaTO;
	}

	public String getNmEntidade() {
		return nmEntidade;
	}

	public void setNmEntidade(String nmEntidade) {
		this.nmEntidade = nmEntidade;
	}

	public String getDsEntidade() {
		return dsEntidade;
	}

	public void setDsEntidade(String dsEntidade) {
		this.dsEntidade = dsEntidade;
	}

	public CategoriaEnum getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaEnum categoria) {
		this.categoria = categoria;
	}

	public TipoEntidadeEnum getTpEntidade() {
		return tpEntidade;
	}

	public void setTpEntidade(TipoEntidadeEnum tpEntidade) {
		this.tpEntidade = tpEntidade;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public List<AtributoTO> getAtributoTOlist() {
		return atributoTOlist;
	}

	public void setAtributoTOlist(List<AtributoTO> atributoTOlist) {
		this.atributoTOlist = atributoTOlist;
	}

	public Integer getQtTd() {
		return qtTd;
	}

	public void setQtTd(Integer qtTd) {
		this.qtTd = qtTd;
	}

	public void addQtTd() {
		if (this.qtTd == null) {
			this.qtTd = 0;
		}
		this.qtTd = this.qtTd + 1;
	}

	public Integer getVersaoRegistro() {
		return versaoRegistro;
	}

	public void setVersaoRegistro(Integer versaoRegistro) {
		this.versaoRegistro = versaoRegistro;
	}

	public boolean isImportar() {
		return importar;
	}

	public void setImportar(boolean importar) {
		this.importar = importar;
	}

	public String getDescricaoVisual() {
		return this.nmEntidade;
	}

	public boolean isHasDescricao() {
		return this.dsEntidade != null && this.dsEntidade.length() > 0;
	}

	public boolean isHasNomeLogico() {
		return this.dsEntidade != null && this.dsEntidade.length() > 0;
	}

}