package br.com.sgpf.metrica.core.bean;

import java.io.Serializable;
import java.util.TimeZone;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;
import org.jboss.seam.transaction.Transaction;

import br.com.sgpf.metrica.core.annotation.TransactionExceptionInterceptor;
import br.com.sgpf.metrica.core.enumeration.ModoOperacaoEnum;
import br.com.sgpf.metrica.core.enumeration.OperacoesEnum;
import br.com.sgpf.metrica.core.enumeration.TipoRelatorioEnum;
import br.com.sgpf.metrica.core.exception.BusinessException;
import br.com.sgpf.metrica.core.exception.DaoException;
import br.com.sgpf.metrica.core.util.JSFUtil;

@TransactionExceptionInterceptor
public abstract class AbstractWebBean implements Serializable {

	private static final long serialVersionUID = 6412551448942720973L;

	protected String OUTCOME_FORM = "form";

	protected String OUTCOME_LISTA = "list";

	@Logger
	protected Log logger;

	/* Nao remover. Variavel usada para garatir o mesmo EntityManager nos DAO(s) */
	@In("entityManager")
	protected EntityManager em;

	private OperacoesEnum operacao = OperacoesEnum.NENHUM;

	private ModoOperacaoEnum modoOperacao = ModoOperacaoEnum.NORMAL;
	
	@In
	private Identity identity;

	public void preRender() {
	}

	public void setRollbackOnly() {
		try {
			if (!Transaction.instance().isMarkedRollback()) {
				Transaction.instance().setRollbackOnly();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void tratarException(Exception exception) {
		setRollbackOnly();
		if (exception instanceof BusinessException) {
			System.out.println(exception.getMessage());
			logger.error(exception.getMessage(), exception);
			addErrorMessageFromResource((BusinessException) exception);
		} else if (exception instanceof DaoException) {
			System.out.println(exception.getMessage());
			logger.error(exception.getMessage(), exception);
			addErrorMessageFromResource("org.jboss.seam.TransactionFailed");
		} else {
			System.out.println(exception.getMessage());
			logger.error(exception.getMessage(), exception);
			addErrorMessageFromResource("org.jboss.seam.TransactionFailed");
		}
	}

	public void addInfoMessageFromResource(String key) {
		JSFUtil.addInfoMessageFromResource(key);
	}

	public void addErrorMessageFromResource(String key) {
		JSFUtil.addErrorMessageFromResource(key);
	}

	public void addErrorMessageFromResource(String key, String... params) {
		JSFUtil.addErrorMessageFromResource(key, params);
	}

	public void addErrorMessageFromResource(BusinessException businessException) {
		JSFUtil.addErrorMessageFromResource(businessException);
	}

	public String getMessageFromResource(String key) {
		return JSFUtil.getMessageFromResource(key);
	}

	public FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	public ExternalContext getExternalContext() {
		return FacesContext.getCurrentInstance().getExternalContext();
	}

	public ServletContext getServletContext() {
		return (ServletContext) getExternalContext().getContext();
	}

	public HttpServletRequest getRequest() {
		return (HttpServletRequest) getExternalContext().getRequest();
	}

	public HttpServletResponse getResponse() {
		return (HttpServletResponse) getExternalContext().getResponse();
	}

	public Object getSessionAttribute(String name) {
		HttpSession session = (HttpSession) getExternalContext().getSession(false);
		return session.getAttribute(name);
	}

	public void setSessionAttribute(String name, Object value) {
		HttpSession session = (HttpSession) getExternalContext().getSession(false);
		synchronized (session) {
			session.setAttribute(name, value);
		}
	}

	public OperacoesEnum getOperacao() {
		return operacao;
	}

	public void setOperacao(OperacoesEnum operacao) {
		this.operacao = operacao;
	}

	public ModoOperacaoEnum getModoOperacao() {
		return modoOperacao;
	}

	public void setModoOperacao(ModoOperacaoEnum modoOperacao) {
		this.modoOperacao = modoOperacao;
	}

	@RequestParameter
	public void setModo(ModoOperacaoEnum modo) {
		if (modo != null && modo.equals(ModoOperacaoEnum.LOV))
			modoOperacao = modo;
	}

	public void downloadReport(String name, byte[] file, TipoRelatorioEnum reportType) {
		HttpServletResponse response = getResponse();
		try {
			String result = null;
			switch (reportType) {
			case PDF:
				result = "attachment;filename=" + name + ".pdf";
				response.setContentType("application/pdf");
				break;

			case XLS:
				result = "attachment;filename=" + name + ".xls";
				response.setContentType("application/vnd.ms-excel");
				break;

			case DOC:
				result = "attachment;filename=" + name + ".doc";
				//				response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
				response.setContentType("application/vnd.ms-word");
				break;

			default:
				break;
			}
			response.addHeader("Content-Disposition", result);
			response.getOutputStream().write(file);
			response.getOutputStream().flush();
			getFacesContext().responseComplete();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			logger.error(ex.getMessage());
		}
	}

	public String getTimezone() {
		return TimeZone.getDefault().getID();
	}

	public String getUsuarioEmissaoRelatorio() {
		return identity.getPrincipal().getName();
	}

	public String telaInicial() {
		return "home";
	}

}