package br.com.sgpf.metrica.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MetadadosBD {
	
	private ConexaoBD poolConexao;
	
	private String schema;
	
	public MetadadosBD(ConexaoBD poolConexao, String schema){
		this.poolConexao = poolConexao;
		this.schema = schema;
	}
	

	public List<Tabela> findAll() throws SQLException{
		List<Tabela> tabelas = new ArrayList<Tabela>();
		Connection connection = null;
		
		try{

			connection = this.poolConexao.getConnection();

			DatabaseMetaData metaData = connection.getMetaData();

			ResultSet rsTable = metaData.getTables(null, schema, null, new String [] { "TABLE" });


			Tabela tabela;
			while (rsTable.next()) {
				tabela = new Tabela();
				tabela.setNome(rsTable.getString("TABLE_NAME"));
				tabela.setDescricao(rsTable.getString("REMARKS"));

				tabela.setColunas(findAllByTable(tabela.getNome(), metaData));
				tabelas.add(tabela);
			}

			connection.close();

		}finally{
			if(connection != null){
				connection.close();
			}
		}
		
		return tabelas;
	}
	
	private List<Coluna> findAllByTable(String tableName, DatabaseMetaData metaData) throws SQLException{
		
		ResultSet rs = metaData.getColumns(null, this.schema, tableName, null);
		
		List<Coluna> colunas = new ArrayList<Coluna>();
		Coluna coluna;
		while(rs.next()){
			
			coluna = new Coluna();
			
			coluna.setNome(rs.getString("COLUMN_NAME"));
			coluna.setDescricao(rs.getString("REMARKS"));
			coluna.setDataType(rs.getInt("DATA_TYPE"));
			coluna.setTamanho(rs.getInt("COLUMN_SIZE"));
			coluna.setPrecisao(rs.getInt("DECIMAL_DIGITS"));
			coluna.setTypeName(rs.getString("TYPE_NAME"));
			
			colunas.add(coluna);
		}
		
		return colunas;
	}
	
	
	public String getSchema() {
		return schema;
	}
	
	public void setSchema(String schema) {
		this.schema = schema;
	}
}
