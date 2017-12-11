package br.com.sgpf.metrica.core.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Order;

import br.com.sgpf.metrica.core.dao.AbstractDao;
import br.com.sgpf.metrica.core.entity.EntityModel;

@SuppressWarnings("serial")
public abstract class PaginatedDataModel<T extends EntityModel<PK>, PK extends Serializable, C extends AbstractDao<T, PK, Filtro>, Filtro extends T> extends
		PaginatedDataModelBase<T, PK, Filtro> {

	public abstract Filtro getFiltro();

	public abstract void setFiltro(Filtro filtro);

	public abstract C getControlador();

	public abstract void setControlador(C controlador);

	public PaginatedDataModel() {
	}

	public PaginatedDataModel(C controlador, Filtro filtro) {
		super();
		setFiltro(filtro);
		setControlador(controlador);
	}

	@Override
	public Integer getTotalRegistros() {
		if (prontoParaConsultar) {
			if (getControlador() != null) {
				totalRegistros = getControlador().getCount(getFiltro());
				return totalRegistros;
			} else
				throw new IllegalStateException("Controlador de negocio nao disponivel para realiza a paginacao.");
		} else
			return 0;
	}

	@Override
	public T buscar(PK id) {
		if (prontoParaConsultar) {
			if (getControlador() != null) {
				return getControlador().findById(id);
			} else
				throw new IllegalStateException("Controlador de negocio nao disponivel para realiza a paginacao.");
		} else
			return null;
	}

	@Override
	public List<T> getListaPagina(Integer iniciarDe, Integer registrosPorPagina) {
		if (prontoParaConsultar) {
			if (getControlador() != null) {
				if (getRowCount() > 0) {
					return getControlador().findByCriteria(getFiltro(), iniciarDe, registrosPorPagina, getOrders());
				} else
					return new ArrayList<T>();
			} else
				throw new IllegalStateException("Controlador de negocio nao disponivel para realiza a paginacao.");
		} else
			return new ArrayList<T>();
	}

	@SuppressWarnings("unchecked")
	public void iniciar(Filtro filtro) {
		totalRegistros = null;
		primeiraLinha = -1;
		indiceLinhaSelecionada = -1;
		cacheRegistrosPagina.clear();
		setFiltro(null);
		if (filtro != null) {
			setFiltro((Filtro) filtro.clone());
		}
		prontoParaConsultar = true;
	}

	public void limpar() {
		totalRegistros = null;
		primeiraLinha = -1;
		indiceLinhaSelecionada = -1;
		cacheRegistrosPagina.clear();
		prontoParaConsultar = false;
	}

	@Override
	public Order[] getOrders() {
		return new Order[] {};
	}

}