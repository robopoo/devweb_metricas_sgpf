package br.com.sgpf.metrica.core.interceptor;

import javax.faces.application.ViewExpiredException;

import org.apache.log4j.Logger;
import org.jboss.seam.Component;
import org.jboss.seam.RequiredException;
import org.jboss.seam.annotations.intercept.AroundInvoke;
import org.jboss.seam.intercept.InvocationContext;
import org.jboss.seam.transaction.FacesTransactionEvents;
import org.jboss.seam.transaction.Transaction;

import br.com.sgpf.metrica.core.exception.BusinessException;
import br.com.sgpf.metrica.core.exception.DaoException;
import br.com.sgpf.metrica.core.util.JSFUtil;

public class TransactionExceptionInterceptor {

	private static Logger logger = Logger
			.getLogger(TransactionExceptionInterceptor.class);

	@AroundInvoke
	public Object checkException(InvocationContext invocation) throws Exception {
		if (invocation
				.getMethod()
				.isAnnotationPresent(
						br.com.sgpf.metrica.core.annotation.TransactionExceptionInterceptor.class)) {
			try {
				setTransactionFailedMessageEnabled(false);
				logger.error(invocation.toString());
				return invocation.proceed();
			} catch (DaoException e) {
				setRollbackOnly();
				JSFUtil.addErrorMessageFromResource("org.jboss.seam.TransactionFailed");
				return null;
			} catch (BusinessException e) {
				setRollbackOnly();
				JSFUtil.addErrorMessageFromResource(e);
				return null;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				setRollbackOnly();
				JSFUtil.addErrorMessageFromResource("org.jboss.seam.TransactionFailed");
				return null;
			}
		} else {
			try {
				setTransactionFailedMessageEnabled(true);
				return invocation.proceed();
			} catch (RequiredException e) {
				setTransactionFailedMessageEnabled(false);
				throw e;
			} catch (ViewExpiredException e) {
				setTransactionFailedMessageEnabled(false);
				throw e;
			} catch (Exception e) {
				throw e;
			}
		}
	}

	private void setTransactionFailedMessageEnabled(
			boolean transactionFailedMessageEnabled) {
		((FacesTransactionEvents) Component
				.getInstance("org.jboss.seam.transaction.facesTransactionEvents"))
				.setTransactionFailedMessageEnabled(transactionFailedMessageEnabled);
	}

	private void setRollbackOnly() {
		try {
			if (!Transaction.instance().isMarkedRollback()) {
				Transaction.instance().setRollbackOnly();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
