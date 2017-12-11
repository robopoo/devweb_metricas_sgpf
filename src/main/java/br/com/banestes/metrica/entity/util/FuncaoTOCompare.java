package br.com.sgpf.metrica.entity.util;

import java.util.Comparator;

import br.com.sgpf.metrica.entity.FuncaoTO;

public class FuncaoTOCompare implements Comparator<FuncaoTO> {

	@Override
	public int compare(FuncaoTO o1, FuncaoTO o2) {
		return o1.getNome().compareTo(o2.getNome());
	}

}
