package br.com.sgpf.metrica.core.datamodel;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;

import br.com.sgpf.metrica.core.datamodel.api.IDataModel;
import br.com.sgpf.metrica.core.entity.EntityModel;

/**
 * Implementacao base para modelos de dados JSF com suporte a paginacao no banco de dados.
 * 
 * @author Alexandre Cardoso
 * @since Junho 02, 2009
 * @param <T>
 *            O tipo da entidade exibida no modelo de dados.
 * @param <PK>
 *            O tipo da chave da entidade.
 */
@SuppressWarnings("serial")
public abstract class PaginatedDataModelBase<T extends EntityModel<PK>, PK extends Serializable, Filtro extends T> extends ExtendedDataModel implements
		IDataModel<T, PK, Filtro> {

	/**
	 * Refer�ncia para o identificador do registro atual.
	 */
	protected PK identificadorAtual;

	/**
	 * Referencia para o indice da primeira linha da pagina.
	 */
	protected Integer primeiraLinha = -1;

	/**
	 * Indice da linha selecionada.
	 */
	protected int indiceLinhaSelecionada;

	/**
	 * Referencia para os registros exibidos para a pagina atual.
	 */
	protected Map<PK, T> cacheRegistrosPagina = new LinkedHashMap<PK, T>();

	/**
	 * Referencia local para o total de registros.
	 */
	protected Integer totalRegistros;

	/**
	 * Atributo indicativo de que o modelo de dados esta pronto para realizar a consulta ou foi explicitamente autorizado a realizar a consulta, atraves da chamada do metodo IModeloDados.iniciar
	 */
	protected boolean prontoParaConsultar = false;

	public abstract Integer getTotalRegistros();

	public abstract List<T> getListaPagina(Integer iniciarDe, Integer registrosPorPagina);

	public abstract T buscar(PK chave);

	public PK getChave(T row) {
		return row.getEntityId();
	}

	public void wrap(FacesContext context, DataVisitor visitor, Range range, Object argument, List<T> list) throws IOException {
		primeiraLinha = ((SequenceRange) range).getFirstRow();
		cacheRegistrosPagina = new LinkedHashMap<PK, T>();
		for (T row : list) {
			PK id = getChave(row);
			cacheRegistrosPagina.put(id, row);
			visitor.process(context, id, argument);
		}
	}

	public boolean hasById(PK id) {
		return cacheRegistrosPagina.get(id) != null;
	}

	/**
	 * Metodo principal para execucao do processo de paginacao. E responsavel por realizar a "virada" de pagina, realizando a consulta no banco de dados para retornar os registros correspondentes a p�gina solicitada.
	 */
	@Override
	public void walk(FacesContext context, DataVisitor visitor, Range range, Object argument) throws IOException {
		Integer firstRow = ((SequenceRange) range).getFirstRow();
		Integer maxResults = ((SequenceRange) range).getRows();
		if (!primeiraLinha.equals(firstRow))
			wrap(context, visitor, range, argument, getListaPagina(firstRow, maxResults));
		else {
			for (PK id : cacheRegistrosPagina.keySet())
				visitor.process(context, id, argument);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setRowKey(Object key) {
		this.identificadorAtual = (PK) key;
	}

	@Override
	public int getRowCount() {
		if (totalRegistros == null)
			return totalRegistros = this.getTotalRegistros();
		else
			return totalRegistros.intValue();
	}

	@Override
	public boolean isRowAvailable() {
		if (identificadorAtual == null) {
			return false;
		} else {
			return hasById(identificadorAtual);
		}
	}

	@Override
	public Object getRowData() {
		if (identificadorAtual == null) {
			return null;
		} else {
			T ret = cacheRegistrosPagina.get(identificadorAtual);
			if (ret == null) {
				ret = this.buscar(identificadorAtual);
				cacheRegistrosPagina.put(identificadorAtual, ret);
				return ret;
			} else {
				return ret;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public T getSelecao() {
		return (T) getRowData();
	}

	public int getTamanho() {
		return getRowCount();
	}

	// Unused rudiment from old JSF staff.
	@Override
	public int getRowIndex() {
		return indiceLinhaSelecionada;
	}

	@Override
	public void setRowIndex(int rowIndex) {
		this.indiceLinhaSelecionada = rowIndex;
	}

	@Override
	public Object getWrappedData() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setWrappedData(Object data) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getRowKey() {
		return identificadorAtual;
	}

}