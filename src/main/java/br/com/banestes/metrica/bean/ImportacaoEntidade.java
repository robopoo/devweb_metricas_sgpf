package br.com.sgpf.metrica.bean;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.sgpf.metrica.core.enumeration.SimNaoEnum;
import br.com.sgpf.metrica.core.enumeration.StatusEnum;
import br.com.sgpf.metrica.entity.AtributoTO;
import br.com.sgpf.metrica.entity.EntidadeTO;
import br.com.sgpf.metrica.enumeration.CategoriaEnum;
import br.com.sgpf.metrica.enumeration.FormatoEnum;
import br.com.sgpf.metrica.jdbc.Coluna;
import br.com.sgpf.metrica.jdbc.MetadadosBD;
import br.com.sgpf.metrica.jdbc.Tabela;

public class ImportacaoEntidade {

	private MetadadosBD metadadosBD;
	
	public ImportacaoEntidade(MetadadosBD metadadosBD){
		this.metadadosBD = metadadosBD;
	}

	public List<EntidadeTO> findAll() throws SQLException{

		List<Tabela> tabelas = this.metadadosBD.findAll();
		
		List<EntidadeTO> entidades = new ArrayList<EntidadeTO>();
		
		EntidadeTO entidadeTO;
		
		for (Tabela tabela : tabelas) {
			entidadeTO = new EntidadeTO();
			
			entidadeTO.setNmEntidade(tabela.getNome());
			entidadeTO.setDsEntidade(tabela.getDescricao());
			entidadeTO.setAtributoTOlist(findAllByTabela(tabela, entidadeTO));
			entidadeTO.setCategoria(CategoriaEnum.NEGOCIO);
			entidadeTO.setTpEntidade(null);
			entidadeTO.setStatus(StatusEnum.ATIVADO);
			entidadeTO.setQtTd(entidadeTO.getAtributoTOlist().size());
			entidadeTO.setVersaoRegistro(0);
			entidades.add(entidadeTO);
		}
		
		
		return entidades;
	}
	
	private List<AtributoTO> findAllByTabela(Tabela tabela, EntidadeTO entidadeTO) throws SQLException{

		
		List<AtributoTO> atribList = new ArrayList<AtributoTO>();
		
		AtributoTO atributoTO;
		
		for(Coluna coluna : tabela.getColunas()){
			atributoTO = new AtributoTO();
			atributoTO.setNmAtributo(coluna.getNome());
			atributoTO.setDsAtributo(coluna.getDescricao());
			atributoTO.setEntidadeTO(entidadeTO);
			
			FormatoEnum formatoEnum = FormatoEnum.parseDataTypes(coluna.getDataType()); 
			if(FormatoEnum.parseDataTypes(coluna.getDataType()) == null && coluna.getTypeName().indexOf("TIMESTAMP") > -1){//Flexibilizador de Solução para o Oracle
				formatoEnum = FormatoEnum.DATA;
			}
			atributoTO.setTpFormato(formatoEnum);
			
			int tamanho = 0;
			int precisao = 0;
			if(!FormatoEnum.DATA.equals(atributoTO.getTpFormato())){
				tamanho = coluna.getTamanho();
				precisao = coluna.getPrecisao();
			}
			
			atributoTO.setNrTamanho(tamanho);
			atributoTO.setPrecisao(precisao);
			atributoTO.setDsValidade(null);
			atributoTO.setFlReconhecido(SimNaoEnum.SIM);
			atributoTO.setStatus(StatusEnum.ATIVADO);
			
			atribList.add(atributoTO);
			
		}
		
		return atribList;
	}


}
