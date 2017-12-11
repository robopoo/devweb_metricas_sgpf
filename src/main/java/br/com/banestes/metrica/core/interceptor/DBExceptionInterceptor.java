package br.com.sgpf.metrica.core.interceptor;

import org.apache.log4j.Logger;
import org.jboss.seam.annotations.intercept.AroundInvoke;
import org.jboss.seam.intercept.InvocationContext;
import org.jboss.seam.transaction.Transaction;

import br.com.sgpf.metrica.core.exception.DaoException;

public class DBExceptionInterceptor {

	private static Logger logger = Logger
			.getLogger(DBExceptionInterceptor.class);

	@AroundInvoke
	public Object checkException(InvocationContext invocation) throws Exception {
		try {
			return invocation.proceed();
		} catch (DaoException e) {
			logger.error(e.getMessage(), e);
			setRollbackOnly();
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setRollbackOnly();
			throw new DaoException(e);
		}
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
