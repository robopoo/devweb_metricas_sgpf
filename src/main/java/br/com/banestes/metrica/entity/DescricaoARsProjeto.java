package br.com.sgpf.metrica.entity;

import java.util.ArrayList;
import java.util.List;

public class DescricaoARsProjeto {

	
	private String nome;
	
	public DescricaoARsProjeto(String nomeFuncaoDadosProjeto, String nomeFuncaoDadosBaseline){
		if(nomeFuncaoDadosProjeto != null){
			this.nome = nomeFuncaoDadosProjeto;
		}else{
			this.nome = nomeFuncaoDadosBaseline;
		}
	}
	
	public String getNome() {
		return nome;
	}
	
	public static List<String> toList(List<DescricaoARsProjeto> list){
		List<String> result = new ArrayList<String>(list.size());
		
		for (DescricaoARsProjeto desc : list) {
			result.add(desc.getNome());
		}
		
		return result;
	}
	
	
}
