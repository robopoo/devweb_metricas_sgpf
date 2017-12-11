package br.com.sgpf.metrica.dao;

import org.jboss.seam.annotations.Name;

import br.com.sgpf.metrica.core.dao.AbstractDao;
import br.com.sgpf.metrica.core.enumeration.OperacoesEnum;
import br.com.sgpf.metrica.core.exception.DaoException;
import br.com.sgpf.metrica.entity.BaselineTO;

@Name( "baselineDAO" )
public class BaselineDAO extends AbstractDao<BaselineTO, Long, BaselineTO>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9134754867100751850L;

	@Override
	protected void executarPreEventos( BaselineTO entity, OperacoesEnum operacao )
			throws DaoException
	{
		switch( operacao )
		{
			case INSERCAO:
				entity.setNrVersao(0);

				break;
			case ALTERACAO:
				entity.setNrVersao(entity.getNrVersao() + 1);
				break;

			default:
				break;
		}
	}
}
