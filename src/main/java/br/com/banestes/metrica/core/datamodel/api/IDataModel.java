package br.com.sgpf.metrica.core.datamodel.api;

import java.io.Serializable;

import org.hibernate.criterion.Order;

import br.com.sgpf.metrica.core.entity.EntityModel;

/**
 * Interface para exposicao de metodos de servico para modelo de dados.
 * 
 * @author Alexandre Cardoso
 * @since Junho 02, 2009 Copyright notice (C) Banco da Amazania S.A
 */
public interface IDataModel<T extends EntityModel<PK>, PK extends Serializable, Filtro extends T> extends Serializable {

	/**
	 * Retorna o objeto a ser utilizado como criterio de filtragem.
	 * 
	 * @return O objeto de filtragem.
	 */
	public T getFiltro();

	/**
	 * Retorna o objeto selecionado pelo usuario.
	 * 
	 * @return A selecao do modelo de dados.
	 */
	public T getSelecao();

	/**
	 * Obtem o tamanho da colecao interna de resultados
	 * 
	 * @return O tamanho da colecao.
	 */
	public int getTamanho();

	/**
	 * Prepara a instancia do modelo de dados para realizar nova consulta paginavel.
	 * 
	 * @param filtro
	 *            - O objeto a ser utilizado como criterio de filtragem, quando necessario.
	 */
	public void iniciar(Filtro filtro);

	public void limpar();

	public Order[] getOrders();

}
