package br.com.sgpf.metrica.dao;

import java.util.Date;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jboss.seam.annotations.Name;

import br.com.sgpf.metrica.core.dao.AbstractDao;
import br.com.sgpf.metrica.core.enumeration.OperacoesEnum;
import br.com.sgpf.metrica.core.enumeration.StatusEnum;
import br.com.sgpf.metrica.core.exception.DaoException;
import br.com.sgpf.metrica.core.util.AES;
import br.com.sgpf.metrica.entity.UsuarioTO;

@Deprecated
@Name("usuarioDAO")
public class UsuarioDAO extends AbstractDao<UsuarioTO, Long, UsuarioTO> {

	private static final long serialVersionUID = -6643325674595268696L;

	@Override
	protected void executarPreEventos(UsuarioTO entity, OperacoesEnum operacao) throws DaoException {
		switch (operacao) {
			case INSERCAO :
				entity.setId(null);
				entity.setSenha(AES.encrypt(entity.getSenha()));
				entity.setDataCadastro(new Date());
				entity.setStatus(StatusEnum.ATIVADO);
				break;
			case EXCLUSAO :
				entity.setStatus(StatusEnum.DESATIVADO);
				break;
			default :
				break;
		}
	}

	@Override
	public void delete(UsuarioTO entity) throws DaoException {
		super.update(entity, OperacoesEnum.EXCLUSAO);
	}

	public boolean existeByLogin(Long id, String login) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getClasse());
		criteria.setProjection(Projections.rowCount());

		if (id != null) {
			criteria.add(Restrictions.ne("id", id));
		}
		criteria.add(Restrictions.eq("login", login));

		Integer count = (Integer) criteria.getExecutableCriteria(getSession()).uniqueResult();

		return count != null && count.intValue() > 0 ? true : false;
	}

	public UsuarioTO findByLoginSenha(String login, String senha) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getClasse());

		criteria.add(Restrictions.eq("login", login));
		criteria.add(Restrictions.eq("senha", senha));

		return (UsuarioTO) criteria.getExecutableCriteria(getSession()).uniqueResult();
	}

}