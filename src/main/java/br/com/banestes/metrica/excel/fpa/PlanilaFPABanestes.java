package br.com.sgpf.metrica.excel.fpa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.util.IOUtils;

import br.com.sgpf.metrica.excel.fpa.CabecalhoPlanilhaFPA.CabecalhoPlanilha;
import br.com.sgpf.metrica.excel.fpa.FuncaoPlanilhaFPA.ColunaFuncao;
import br.com.sgpf.metrica.excel.fpa.ServicoNaoMensuravelPlanilhaFPA.QuantidadeIten;

public class PlanilaFPAsgpf {

	public static final Integer INDEX_RESUMO_CONTAGEM = 0;

	public static final Integer INDEX_FUNCOES = 1;

	public static final Integer INDEX_MANUTENCOES_NAO_MENSURAVEIS = 2;

	public static final Integer INDEX_SUMARIO = 3;

	private List<FuncaoPlanilhaFPA> dadosFuncoes;

	private List<ServicoNaoMensuravelPlanilhaFPA> servicosNaoMensuraveis;

	private CabecalhoPlanilhaFPA cabecalhoPlanilhaFPA;

	private boolean baseline;

	public PlanilaFPAsgpf(CabecalhoPlanilhaFPA cabecalhoPlanilhaFPA, List<FuncaoPlanilhaFPA> funcoes) {
		this.cabecalhoPlanilhaFPA = cabecalhoPlanilhaFPA;
		this.dadosFuncoes = funcoes;
		this.baseline = true;
	}

	public PlanilaFPAsgpf(CabecalhoPlanilhaFPA cabecalhoPlanilhaFPA, List<FuncaoPlanilhaFPA> funcoes,
			List<ServicoNaoMensuravelPlanilhaFPA> servicosNaoMensuraveis) {
		this(cabecalhoPlanilhaFPA, funcoes);
		this.servicosNaoMensuraveis = servicosNaoMensuraveis;
		this.baseline = false;
	}

	public byte[] export(String filePath) {

		File file = new File(filePath);

		try {
			FileInputStream fis = new FileInputStream(file);

			return export(fis);

		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Arquivo parametrizado não existe!");
		}
	}

	public byte[] export(InputStream inputStream) {

		HSSFWorkbook workbook;
		FileInputStream fileResult = null;
		try {

			workbook = new HSSFWorkbook(inputStream);

			setCabecalho(workbook.getSheetAt(INDEX_RESUMO_CONTAGEM));

			setFuncoes(workbook.getSheetAt(INDEX_FUNCOES));

			if (this.baseline) {
				workbook.removeSheetAt(INDEX_MANUTENCOES_NAO_MENSURAVEIS);
			} else {
				setServicosNaoMensuraveis(workbook.getSheetAt(INDEX_MANUTENCOES_NAO_MENSURAVEIS));
			}

			File tmp = File.createTempFile("prefix", "xls");

			workbook.write(new FileOutputStream(tmp));

			fileResult = new FileInputStream(tmp);

			byte[] bytes = IOUtils.toByteArray(fileResult);

			return bytes;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (fileResult != null) {
				try {
					fileResult.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	private void setServicosNaoMensuraveis(HSSFSheet funcoes) {

		QuantidadeIten quantidadeIten;
		for (ServicoNaoMensuravelPlanilhaFPA snm : this.servicosNaoMensuraveis) {
			quantidadeIten = QuantidadeIten.parse(snm.getCdElementoContagem());

			if (quantidadeIten == null) {
				throw new IllegalStateException("Código do Elemento de Contagem Inválido!");
			}

			setCell(funcoes, quantidadeIten, snm.getQuantidadeItensPlanilha());
		}
	}

	private void setFuncoes(HSSFSheet funcoes) {

		int i = FuncaoPlanilhaFPA.INDEX_ROW_START;
		for (FuncaoPlanilhaFPA func : this.dadosFuncoes) {
			setCell(funcoes, ColunaFuncao.NOME_FUNCAO, i, func.getNome());
			setCell(funcoes, ColunaFuncao.TIPO_FUNCAO, i, func.getTipoFuncaoDescricao());
			setCell(funcoes, ColunaFuncao.ELEMENTO_CONTAGEM, i, func.getCodElementoContagem());
			setCell(funcoes, ColunaFuncao.TIPO_DADO, i, func.getQtTipoDados());
			setCell(funcoes, ColunaFuncao.AR_TR, i, func.getARTR());
			i++;
		}
	}

	private void setCabecalho(HSSFSheet resumoContagem) {

		setCell(resumoContagem, CabecalhoPlanilha.FORNECEDOR, cabecalhoPlanilhaFPA.getFornecedor());

		setCell(resumoContagem, CabecalhoPlanilha.TITULO, cabecalhoPlanilhaFPA.getTituloPlanilha());

		setCell(resumoContagem, CabecalhoPlanilha.SISTEMA, cabecalhoPlanilhaFPA.getSistema());

		setCell(resumoContagem, CabecalhoPlanilha.ANALISTA_FORNECEDOR, cabecalhoPlanilhaFPA.getAnalistaFornecedor());

		setCell(resumoContagem, CabecalhoPlanilha.ANALISTA_CLIENTE, cabecalhoPlanilhaFPA.getAnalistaCliente());

		setCell(resumoContagem, CabecalhoPlanilha.DATA_CRIACAO, new Date());

		//setCell(resumoContagem, CabecalhoPlanilha.DATA_REVISAO, null);
	}

	private void setCell(HSSFSheet sheet, CabecalhoPlanilha cabecalhoPlanilha, String value) {
		setCell(sheet, cabecalhoPlanilha.getRow(), cabecalhoPlanilha.getColumn(), value);
	}

	private void setCell(HSSFSheet sheet, CabecalhoPlanilha cabecalhoPlanilha, Date value) {
		setCell(sheet, cabecalhoPlanilha.getRow(), cabecalhoPlanilha.getColumn(), value);
	}

	private void setCell(HSSFSheet sheet, QuantidadeIten quantidadeIten, String value) {
		setCell(sheet, quantidadeIten.getRow(), quantidadeIten.getColumn(), value);
	}

	private void setCell(HSSFSheet sheet, ColunaFuncao colunaFuncao, Integer row, Object value) {

		if (value instanceof Integer) {
			sheet.getRow(row).getCell(colunaFuncao.getColumn()).setCellValue((Integer) value);
		} else if (value instanceof Double) {
			sheet.getRow(row).getCell(colunaFuncao.getColumn()).setCellValue((Double) value);
		} else {
			sheet.getRow(row).getCell(colunaFuncao.getColumn()).setCellValue(value.toString());
		}
	}

	private void setCell(HSSFSheet sheet, Integer row, Integer column, String value) {
		if (value != null) {
			sheet.getRow(row).getCell(column).setCellValue(value);
		}
	}

	private void setCell(HSSFSheet sheet, Integer row, Integer column, Date value) {
		if (value != null) {
			sheet.getRow(row).getCell(column).setCellValue(value);
		}
	}

}
