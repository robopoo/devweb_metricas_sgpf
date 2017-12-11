package br.com.sgpf.metrica.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import br.com.sgpf.metrica.core.annotation.LikeCriteria;
import br.com.sgpf.metrica.core.annotation.LowerCase;
import br.com.sgpf.metrica.core.entity.EntityModel;
import br.com.sgpf.metrica.core.enumeration.MatchModeEnum;
import br.com.sgpf.metrica.core.enumeration.StatusEnum;
import br.com.sgpf.metrica.enumeration.TipoPerfilEnum;

@Entity
@Table(name = "TB_USR_USUARIO")
@SequenceGenerator(name = "SQ_USR_ID_USUARIO", sequenceName = "SQ_USR_ID_USUARIO")
@Deprecated
public class UsuarioTO extends EntityModel<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8528713606147130541L;

	@Id
	@Column(name = "USR_ID_USUARIO", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_USR_ID_USUARIO")
	private Long id;

	@LowerCase
	@Column(name = "USR_CD_LOGIN", nullable = false, unique = true)
	private String login;

	@LikeCriteria(caseSensitive = false, matchMode = MatchModeEnum.ANYWHERE)
	@Column(name = "USR_NM_USUARIO", nullable = false)
	private String nome;

	@LikeCriteria(caseSensitive = false, matchMode = MatchModeEnum.ANYWHERE)
	@Column(name = "USR_CD_MATRICULA", nullable = false)
	private String matricula;

	@Column(name = "USR_CD_SENHA", nullable = false)
	private String senha;

	 @Type(type = "tipoPerfilEnumUserType")
	@Column(name = "USR_TP_PERFIL", nullable = false)
	private TipoPerfilEnum tipoPerfil;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "USR_DT_CADASTRO", nullable = false)
	private Date dataCadastro;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "USR_DT_ULT_ACESSO", nullable = true)
	private Date dataUltimoAcesso;

	@Type(type = "statusEnumUserType")
	@Column(name = "USR_ST_STATUS", nullable = false)
	private StatusEnum status;

	@Transient
	private Date dataAcessoAnterior;

	@Override
	public Long getEntityId() {
		return this.id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public TipoPerfilEnum getTipoPerfil() {
		return tipoPerfil;
	}

	public void setTipoPerfil(TipoPerfilEnum tipoPerfil) {
		this.tipoPerfil = tipoPerfil;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Date getDataUltimoAcesso() {
		return dataUltimoAcesso;
	}

	public void setDataUltimoAcesso(Date dataUltimoAcesso) {
		this.dataUltimoAcesso = dataUltimoAcesso;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public Date getDataAcessoAnterior() {
		return dataAcessoAnterior;
	}

	public void setDataAcessoAnterior(Date dataAcessoAnterior) {
		this.dataAcessoAnterior = dataAcessoAnterior;
	}

}
