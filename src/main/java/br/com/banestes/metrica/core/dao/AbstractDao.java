package br.com.sgpf.metrica.core.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Version;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.log.Log;

import br.com.sgpf.metrica.core.annotation.DBExceptionInterceptor;
import br.com.sgpf.metrica.core.annotation.LikeCriteria;
import br.com.sgpf.metrica.core.annotation.LowerCase;
import br.com.sgpf.metrica.core.annotation.UpperCase;
import br.com.sgpf.metrica.core.entity.EntityModel;
import br.com.sgpf.metrica.core.enumeration.OperacoesEnum;
import br.com.sgpf.metrica.core.exception.DaoException;
import br.com.sgpf.metrica.core.util.ReflectionUtils;

@DBExceptionInterceptor
public abstract class AbstractDao<T extends EntityModel<PK>, PK extends Serializable, Filtro extends T> implements Serializable {

	private static final long serialVersionUID = 3243867872984564821L;

	@Logger
	private Log logger;

	@In(create = true)
	private EntityManager entityManager;

	protected Session getSession() {
		return (Session) entityManager.getDelegate();
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@SuppressWarnings("unchecked")
	protected Class<T> getClasse() {
		return (Class<T>) ((java.lang.reflect.ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	protected void executarPreEventos(T entity, OperacoesEnum operacao) throws DaoException {
	};

	protected void executarPosEventos(T entity, OperacoesEnum operacao) throws DaoException {
	};

	public void insert(T entity) throws DaoException {
		try {
			executarPreEventos(entity, OperacoesEnum.INSERCAO);
			getSession().persist(entity);
			executarPosEventos(entity, OperacoesEnum.INSERCAO);
			flush();
		} catch (HibernateException he) {
			getSession().clear();
			throw new DaoException(he);
		} finally {
			evict(entity);
		}
	};

	public void update(T entity) throws DaoException {
		update(entity, true);
	};

	public void update(T entity, OperacoesEnum operacao) throws DaoException {
		try {
			executarPreEventos(entity, operacao);
			getSession().merge(entity);
			executarPosEventos(entity, operacao);
			flush();
		} catch (HibernateException he) {
			throw new DaoException(he);
		}
	}

	public void update(T entity, boolean merge) throws DaoException {
		try {
			executarPreEventos(entity, OperacoesEnum.ALTERACAO);
			if (merge) {
				getSession().merge(entity);
			} else {
				getSession().update(entity);
			}
			executarPosEventos(entity, OperacoesEnum.ALTERACAO);
			flush();
		} catch (HibernateException he) {
			throw new DaoException(he);
		}
	}

	public void delete(T entity) throws DaoException {
		try {
			executarPreEventos(entity, OperacoesEnum.EXCLUSAO);
			T object = findById(entity.getEntityId());
			getSession().delete(object);
			executarPosEventos(entity, OperacoesEnum.EXCLUSAO);
			flush();
		} catch (HibernateException he) {
			throw new DaoException(he);
		}
	};

	@SuppressWarnings("unchecked")
	public T findById(PK id) {
		DetachedCriteria criterio = DetachedCriteria.forClass(getClasse());
		criterio.add(Restrictions.idEq(id));
		return (T) criterio.getExecutableCriteria(getSession()).uniqueResult();
	}

	public Integer getCount(Filtro filter) {
		DetachedCriteria criterio = DetachedCriteria.forClass(getClasse());
		criterio.setProjection(Projections.rowCount());

		buildCriteria(criterio, filter, null);

		preencherOptionalCriteria(criterio, filter);

		Integer count = (Integer) criterio.getExecutableCriteria(getSession()).uniqueResult();

		return (Integer) count;
	}

	public List<T> findByCriteria(Filtro filter, Integer firstRow, Integer maxResults) {
		return executeSearch(filter, firstRow, maxResults, null);
	}

	public List<T> findByCriteria(Filtro filter, Integer firstRow, Integer maxResults, Order[] orders) {
		return executeSearch(filter, firstRow, maxResults, orders);
	}

	protected void preencherOptionalCriteria(DetachedCriteria criterio, Filtro filter) {
	}

	@SuppressWarnings("unchecked")
	private List<T> executeSearch(Filtro filter, Integer firstRow, Integer maxResults, Order[] orders) {
		DetachedCriteria criterio = DetachedCriteria.forClass(getClasse());

		buildCriteria(criterio, filter, null);

		preencherOptionalCriteria(criterio, filter);

		if (orders != null) {
			for (Order order : orders) {
				if (order != null) {
					criterio.addOrder(order);
				}
			}
		}

		Criteria crit = criterio.getExecutableCriteria(getSession());

		if (firstRow != null && firstRow.intValue() > 0) {
			crit.setFirstResult(firstRow);
		}

		if (maxResults != null && maxResults.intValue() > 0) {
			crit.setMaxResults(maxResults);
		}

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return crit.list();
	}

	private void buildCriteria(DetachedCriteria criterio, Object objeto, String ancestorField) {

		if (objeto != null) {
			Class<?> classe = objeto.getClass();

			while (!classe.equals(Object.class)) {
				for (Field f : classe.getDeclaredFields()) {
					if (f.isAnnotationPresent(Id.class) || (f.isAnnotationPresent(Column.class) && !f.isAnnotationPresent(Version.class))
							|| f.isAnnotationPresent(JoinColumn.class)) {
						Object valor = ReflectionUtils.recuperarValorAtributo(objeto.getClass(), objeto, f.getName());

						if (valor != null) {

							if (f.isAnnotationPresent(JoinColumn.class) && ((EntityModel<?>) valor).getEntityId() == null)
								continue;

							if (f.getType().equals(String.class)) {
								if (!valor.toString().trim().isEmpty()) {
									if (f.isAnnotationPresent(UpperCase.class)) {
										valor = valor.toString().trim().toUpperCase();
									} else if (f.isAnnotationPresent(LowerCase.class)) {
										valor = valor.toString().trim().toLowerCase();
									} else {
										valor = valor.toString().trim();
									}
									if (f.isAnnotationPresent(LikeCriteria.class)) {
										LikeCriteria likeCriteria = f.getAnnotation(LikeCriteria.class);
										if (!likeCriteria.caseSensitive()) {
											criterio.add(Restrictions.ilike((ancestorField != null ? ancestorField + "." : "") + f.getName(), valor.toString()
													.toLowerCase(), likeCriteria.matchMode().getMatchMode()));
										} else {
											criterio.add(Restrictions.like((ancestorField != null ? ancestorField + "." : "") + f.getName(), valor.toString(),
													likeCriteria.matchMode().getMatchMode()));
										}
									} else {
										criterio.add(Restrictions.eq((ancestorField != null ? ancestorField + "." : "") + f.getName(), valor));
									}
								}
							} else {
								criterio.add(Restrictions.eq((ancestorField != null ? ancestorField + "." : "") + f.getName(), valor));
							}
						}
					} else if (f.isAnnotationPresent(EmbeddedId.class)) {
						Object embeddedId = ReflectionUtils.recuperarValorAtributo(objeto.getClass(), objeto, f.getName());
						buildCriteria(criterio, embeddedId, f.getName());
					}
				}

				classe = classe.getSuperclass();
			}
		}
	}

	public ScrollableResults findByCriteria(Filtro filter, Order[] orders) {
		DetachedCriteria criterio = DetachedCriteria.forClass(getClasse());

		buildCriteria(criterio, filter, null);

		preencherOptionalCriteria(criterio, filter);

		if (orders != null) {
			for (Order order : orders) {
				if (order != null) {
					criterio.addOrder(order);
				}
			}
		}

		Criteria crit = criterio.getExecutableCriteria(getSession());

		crit.setCacheMode(CacheMode.IGNORE);

		return crit.scroll(ScrollMode.FORWARD_ONLY);
	}
	
	@SuppressWarnings("unchecked")
	public List<Filtro> findEntitiesByCriteria(Filtro filter, Order[] orders) {
		DetachedCriteria criterio = DetachedCriteria.forClass(getClasse());
		
		buildCriteria(criterio, filter, null);
		
		preencherOptionalCriteria(criterio, filter);
		
		if (orders != null) {
			for (Order order : orders) {
				if (order != null) {
					criterio.addOrder(order);
				}
			}
		}
		
		Criteria crit = criterio.getExecutableCriteria(getSession());
		
		crit.setCacheMode(CacheMode.IGNORE);
		
		return crit.list();
	}

	public void flush() throws DaoException {
		try {
			getSession().flush();
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new DaoException(he);
		}
	}

	public void clear() {
		try {
			getSession().clear();
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		}
	}

	public void evict(T bean) {
		try {
			getSession().evict(bean);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		}
	}

	public void refresh(T bean) {
		getSession().refresh(bean);
	}

	public Date getCurrentDate() {
		SQLQuery query = getSession().createSQLQuery("select sysdate as mydate from dual");
		query.addScalar("mydate", Hibernate.TIMESTAMP);
		return (Date) query.uniqueResult();
	}
}