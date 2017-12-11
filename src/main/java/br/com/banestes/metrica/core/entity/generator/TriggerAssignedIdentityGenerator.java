/**
 * 
 */
package br.com.sgpf.metrica.core.entity.generator;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.AbstractPostInsertGenerator;
import org.hibernate.id.IdentifierGeneratorFactory;
import org.hibernate.id.PostInsertIdentityPersister;
import org.hibernate.id.SequenceIdentityGenerator.NoCommentsInsert;
import org.hibernate.id.insert.AbstractReturningDelegate;
import org.hibernate.id.insert.IdentifierGeneratingInsert;
import org.hibernate.id.insert.InsertGeneratedIdentifierDelegate;

/**
 * PD Case Informática Ltda. www.pdcase.com.br
 * @author Glauber Monteiro <mailto:glauber.monteiro@pdcase.com.br>
 * @version 1.0.0
 * @since 27/02/2014
 * @time 11:22:45
 */

public class TriggerAssignedIdentityGenerator extends
		AbstractPostInsertGenerator {

	/* (non-Javadoc)
	 * @see org.hibernate.id.PostInsertIdentifierGenerator#getInsertGeneratedIdentifierDelegate(org.hibernate.id.PostInsertIdentityPersister, org.hibernate.dialect.Dialect, boolean)
	 */
	@Override
	public InsertGeneratedIdentifierDelegate getInsertGeneratedIdentifierDelegate(PostInsertIdentityPersister persister, Dialect dialect,
			boolean isGetGeneratedKeysEnabled) throws HibernateException {
		return new Delegate(persister, dialect);
	}

	public static class Delegate extends AbstractReturningDelegate {

		private final Dialect dialect;

		private final String[] keyColumns;

		public Delegate(PostInsertIdentityPersister persister, Dialect dialect) {
			super(persister);
			this.dialect = dialect;
			this.keyColumns = getPersister().getRootTableKeyColumnNames();
			if (keyColumns.length > 1) {
				throw new HibernateException("trigger assigned identity generator cannot be used with multi-column keys");
			}
		}

		public IdentifierGeneratingInsert prepareIdentifierGeneratingInsert() {
			NoCommentsInsert insert = new NoCommentsInsert(dialect);
			return insert;
		}

		protected PreparedStatement prepare(String insertSQL, SessionImplementor session) throws SQLException {
			return session.getBatcher().prepareStatement(insertSQL, keyColumns);
		}

		protected Serializable executeAndExtract(PreparedStatement insert) throws SQLException {
			insert.executeUpdate();
			return IdentifierGeneratorFactory.getGeneratedIdentity(insert.getGeneratedKeys(), getPersister().getIdentifierType());
		}
	}

}
