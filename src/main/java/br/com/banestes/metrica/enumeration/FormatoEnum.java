package br.com.sgpf.metrica.enumeration;

import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public enum FormatoEnum {
	NUMERO(0, "Número"),
	TEXTO(1, "Texto"),
	DATA(2,"Data");

	private Integer value;

	private String descricao;
	
	public static final Map<Integer, FormatoEnum> MAP_DATA_TYPES;
	
	static{
		
		
		Map<Integer, FormatoEnum> mapDataTypes = new HashMap<Integer, FormatoEnum>();
		
		mapDataTypes.put(Types.BIGINT, FormatoEnum.NUMERO);
		mapDataTypes.put(Types.BINARY, FormatoEnum.NUMERO);
		mapDataTypes.put(Types.CHAR, FormatoEnum.TEXTO);
		mapDataTypes.put(Types.DATE, FormatoEnum.DATA);
		mapDataTypes.put(Types.DECIMAL, FormatoEnum.NUMERO);
		mapDataTypes.put(Types.DOUBLE, FormatoEnum.NUMERO);
		mapDataTypes.put(Types.FLOAT, FormatoEnum.NUMERO);
		mapDataTypes.put(Types.INTEGER, FormatoEnum.NUMERO);
		mapDataTypes.put(Types.NUMERIC, FormatoEnum.NUMERO);
		mapDataTypes.put(Types.NCHAR, FormatoEnum.TEXTO);
		mapDataTypes.put(Types.NVARCHAR, FormatoEnum.TEXTO);
		mapDataTypes.put(Types.TIME, FormatoEnum.DATA);
		mapDataTypes.put(Types.TIMESTAMP, FormatoEnum.DATA);
		mapDataTypes.put(Types.VARCHAR, FormatoEnum.TEXTO);
		
		MAP_DATA_TYPES = Collections.unmodifiableMap(mapDataTypes);
	}
	
	public static FormatoEnum parseDataTypes(int dataTypes){
		return MAP_DATA_TYPES.get(dataTypes);
	}

	private FormatoEnum(Integer value, String descricao) {
		this.value = value;
		this.descricao = descricao;
	}

	public Integer getValue() {
		return value;
	}

	public String getDescricao() {
		return descricao;
	}
	
	
	
}