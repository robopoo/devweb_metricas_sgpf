package br.com.sgpf.metrica.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InspecaoFuncao implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String> dadosIncluidos;
	
	private List<String> dadosRemovidos;
	
	private List<String> dadosMantidos;
	

	public InspecaoFuncao(List<String> dadosBaseline, List<String> dadosProjeto) {
		
		if(dadosBaseline == null){
			dadosBaseline = new ArrayList<String>();
		}
		
		if(dadosProjeto == null){
			dadosProjeto = new ArrayList<String>();
		}
		
		this.dadosIncluidos = new ArrayList<String>();
		this.dadosRemovidos = new ArrayList<String>();
		this.dadosMantidos = new ArrayList<String>();
		
		this.separarDados(dadosBaseline, dadosProjeto);
	}
	
	private void separarDados(List<String> dadosBaseline, List<String> dadosProjeto){
		
		this.dadosIncluidos.addAll(dadosProjeto);
		
	 	dadosProjeto.retainAll(dadosBaseline);
	 	
	 	this.dadosMantidos = dadosProjeto;

	 	this.dadosIncluidos.removeAll(dadosMantidos);
	 	
	 	dadosBaseline.removeAll(dadosMantidos);
	 	
	 	this.dadosRemovidos = dadosBaseline;
	}
	
	public List<String> getDadosIncluidos() {
		return dadosIncluidos;
	}

	public List<String> getDadosRemovidos() {
		return dadosRemovidos;
	}
	
	public List<String> getDadosMantidos(){
		return this.dadosMantidos;
	}
	
}
