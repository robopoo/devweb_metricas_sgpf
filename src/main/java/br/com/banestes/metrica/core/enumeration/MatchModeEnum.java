package br.com.sgpf.metrica.core.enumeration;

import org.hibernate.criterion.MatchMode;

public enum MatchModeEnum {
	START(MatchMode.START),
	END(MatchMode.END),
	ANYWHERE(MatchMode.ANYWHERE);

	private MatchMode matchMode;

	private MatchModeEnum(MatchMode matchMode) {
		this.matchMode = matchMode;
	}

	public MatchMode getMatchMode() {
		return matchMode;
	}

}
