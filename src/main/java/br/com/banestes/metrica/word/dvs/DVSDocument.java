package br.com.sgpf.metrica.word.dvs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.io3.Save;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.Color;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.HpsMeasure;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPr;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Text;

import br.com.sgpf.metrica.entity.ArquivoReferenciadoProjetoTO;
import br.com.sgpf.metrica.entity.AtributoTO;
import br.com.sgpf.metrica.entity.ProjetoTO;
import br.com.sgpf.metrica.entity.TipoDadosDERFuncaoTransacaoProjetoTO;
import br.com.sgpf.metrica.entity.TipoDadosFuncaoTransacaoProjetoTO;
import br.com.sgpf.metrica.enumeration.TipoComplexidadeEnum;
import br.com.sgpf.metrica.enumeration.TipoFuncaoTransacaoEnum;
import br.com.sgpf.metrica.view.FuncaoDadosProjetoVO;
import br.com.sgpf.metrica.view.FuncaoTransacaoProjetoVO;
import br.com.sgpf.metrica.word.dvs.ValueStyle.STYLE;

public class DVSDocument
{

	private static final String NÃO_SE_APLICA = "Não se aplica";

	private String template;

	private OutputStream outputStream;

	private Map<String, String> valores;

	private int indexParagrafo = 9;

	private ProjetoTO projetoTO;

	private List<FuncaoTransacaoProjetoVO> funcoesTransacaoVOListAlteradas;

	private List<FuncaoTransacaoProjetoVO> funcoesTransacaoVOListIncluidas;

	private List<FuncaoTransacaoProjetoVO> funcoesTransacaoVOListExcluidas;

	private List<FuncaoDadosProjetoVO> funcoesDadosVOListIncluidas;

	private List<FuncaoDadosProjetoVO> funcoesDadosVOListAlteradas;

	private List<FuncaoDadosProjetoVO> funcoesDadosVOListExcluidas;

	private List<FuncaoDadosProjetoVO> funcoesDadosVOListNaoMensuraveis;

	private org.docx4j.wml.ObjectFactory factory;

	private DVSDocument()
	{
		factory = Context.getWmlObjectFactory();
	}

	public void gerarDvs() throws Exception
	{
		WordprocessingMLPackage template;
		template = getTemplate(this.template);

		replacePlaceholder(template);

		escreverFuncoesTransacaoIncluidas(template);
		escreverFuncoesTransacaoAlteradas(template);
		escreverFuncoesTransacaoExcluidas(template);

		escreverFuncoesDadosIncluidas(template);
		escreverFuncoesDadosAlteradas(template);
		escreverFuncoesDadosExcluidas(template);

		escreverFuncoesNaoMensuraveis(template);

		escreverObservacoes(template);

		writeDocxToStream(template);

	}

	public static class Builder
	{

		private DVSDocument document = new DVSDocument();

		public Builder( String arquivo, ByteArrayOutputStream baos )
		{
			document.template = arquivo;
			document.outputStream = baos;
		}

		public Builder comTransacoesExcluidas(
				List<FuncaoTransacaoProjetoVO> funcoesTransacaoVOListExcluidas )
		{
			document.funcoesTransacaoVOListExcluidas = funcoesTransacaoVOListExcluidas;
			return this;
		}

		public Builder comTransacoesIncluidas(
				List<FuncaoTransacaoProjetoVO> funcoesTransacaoVOListIncluidas )
		{
			document.funcoesTransacaoVOListIncluidas = funcoesTransacaoVOListIncluidas;
			return this;
		}

		public Builder comTransacoesAlteradas(
				List<FuncaoTransacaoProjetoVO> funcoesTransacaoVOListAlteradas )
		{
			document.funcoesTransacaoVOListAlteradas = funcoesTransacaoVOListAlteradas;
			return this;
		}

		public Builder incluirFuncoesDados(
				List<FuncaoDadosProjetoVO> funcoesDadosVOListIncluidas )
		{
			document.funcoesDadosVOListIncluidas = funcoesDadosVOListIncluidas;
			return this;

		}

		public Builder incluirFuncoesDadosAlteradas(
				List<FuncaoDadosProjetoVO> funcoesDadosVOListAlteradas )
		{
			document.funcoesDadosVOListAlteradas = funcoesDadosVOListAlteradas;
			return this;
		}

		public Builder incluirFuncoesDadosExcluidas(
				List<FuncaoDadosProjetoVO> funcoesDadosVOListExcluidas )
		{
			document.funcoesDadosVOListExcluidas = funcoesDadosVOListExcluidas;
			return this;
		}

		public Builder excluirFuncoesDados(
				List<FuncaoDadosProjetoVO> funcoesDadosVOListExcluidas )
		{
			document.funcoesDadosVOListExcluidas = funcoesDadosVOListExcluidas;
			return this;

		}

		public Builder comFuncaoDadosNaoMensuraveis(
				List<FuncaoDadosProjetoVO> funcoesDadosVOListNaoMensuraveis )
		{
			document.funcoesDadosVOListNaoMensuraveis = funcoesDadosVOListNaoMensuraveis;
			return this;
		}

		public Builder doProjeto( ProjetoTO projetoTO )
		{
			document.projetoTO = projetoTO;

			Map<String, String> valores = new HashMap<String, String>();
			int i = 1;
			valores.put("#" + i++, document.projetoTO.getProjeto() + "/"
					+ document.projetoTO.getSubProjeto() + " - "
					+ document.projetoTO.getTitulo());

			if ( document.projetoTO.getGestorPrincipalTO() == null )
				valores.put("#" + i++, "");
			else
				valores.put("#" + i++, document.projetoTO
						.getGestorPrincipalTO().getNomeAnalista());

			if ( document.projetoTO.getGestorEnvolvidoTO() == null )
				valores.put("#" + i++, "");
			else
				valores.put("#" + i++, document.projetoTO
						.getGestorEnvolvidoTO().getNomeAnalista());

			if ( document.projetoTO.getResponsavelPrincipalClienteTO() == null )
				valores.put("#" + i++, "");
			else
				valores.put("#" + i++, document.projetoTO
						.getResponsavelPrincipalClienteTO().getNomeAnalista());

			if ( document.projetoTO.getResponsavelEnvolvidoClientTO() == null )
				valores.put("#" + i++, "");
			else
				valores.put("#" + i++, document.projetoTO
						.getResponsavelEnvolvidoClientTO().getNomeAnalista());

			if ( document.projetoTO.getSistemaTO() == null )
				valores.put("#" + i++, "");
			else
				valores.put("#" + i++, document.projetoTO.getSistemaTO()
						.getSistema());

			valores.put("#" + i++, projetoTO.getDsSistemasEnvolvidos());

			valores.put("#" + i++, document.projetoTO.getFornecedor());
			valores.put("#" + i++, document.projetoTO.getVisaoGeralSolucao());

			document.valores = valores;

			return this;
		}

		public DVSDocument instanciar()
		{
			return document;
		}

	}

	private void escreverObservacoes( WordprocessingMLPackage template )
	{
		indexParagrafo++;
		indexParagrafo++;

		R run = factory.createR();
		Text text1 = factory.createText();
		text1.setValue(NÃO_SE_APLICA);

		run.getContent().add(text1);

		P paragrafo = factory.createP();
		paragrafo.getContent().add(run);

		template.getMainDocumentPart().getContent()
				.add(indexParagrafo++, paragrafo);

	}

	private void escreverFuncoesNaoMensuraveis( WordprocessingMLPackage template )
	{

		indexParagrafo++;
		indexParagrafo++;

		RPr arialText = factory.createRPr();
		arialText = setCorrectFormat(arialText);
		
		
		R run = factory.createR();
		run.setRPr(arialText);
		Text text1;
		P paragrafo;
		if ( this.funcoesDadosVOListNaoMensuraveis == null
				|| funcoesDadosVOListNaoMensuraveis.isEmpty() )
		{

			text1 = factory.createText();
			text1.setValue(NÃO_SE_APLICA);

			run.getContent().add(text1);

			paragrafo = factory.createP();
			paragrafo.getContent().add(run);

			template.getMainDocumentPart().getContent()
					.add(indexParagrafo++, paragrafo);

			return;
		}
		int i = 0;

		for ( FuncaoDadosProjetoVO item : funcoesDadosVOListNaoMensuraveis )
		{
			run = factory.createR();

			RPr runProperties = factory.createRPr();
			addBoldStyle(runProperties);
			runProperties = setCorrectFormat(runProperties);
			if ( i++ == 0 )
			{
				run.setRPr(arialText);
				text1 = factory.createText();
				text1.setValue("Função de Dados");
				run.getContent().add(text1);

				// Quebra linha
				run.getContent().add(factory.createBr());

				// Quebra linha
				run.getContent().add(factory.createBr());
			}
			
			text1 = factory.createText();
			text1.setValue(item.getNome());
			run.getContent().add(text1);

			run.setRPr(runProperties);

			paragrafo = factory.createP();
			paragrafo.getContent().add(run);

			run = factory.createR();
			
		
			run.setRPr(arialText);
			// Quebra linha
			run.getContent().add(factory.createBr());

			text1 = factory.createText();
			text1.setValue(item.getDescricao());
			run.getContent().add(text1);

			// Quebra linha
			run.getContent().add(factory.createBr());

			text1 = factory.createText();
			text1.setValue(item.getDescricaoManutencao());
			run.getContent().add(text1);

			// Quebra linha
			run.getContent().add(factory.createBr());

			paragrafo.getContent().add(run);

			template.getMainDocumentPart().getContent()
					.add(indexParagrafo++, paragrafo);

			if ( item.getAtributoTOs().size() > 0 )
			{
				run = factory.createR();
			
				run.setRPr(arialText);
				paragrafo = factory.createP();
				
				run.getContent().add(
						getTableAtributos(template, item.getAtributoTOs()));
				paragrafo.getContent().add(run);

				template.getMainDocumentPart().getContent()
						.add(indexParagrafo++, paragrafo);
			}
		}

	}

	private void escreverFuncoesTransacao( WordprocessingMLPackage template,
			FuncaoTransacaoProjetoVO item, boolean doubleBreakLine )
	{

		R run = factory.createR();
		RPr arialText = factory.createRPr();
		arialText = setCorrectFormat(arialText);
		run.setRPr(arialText);
		Text text1;
		P paragrafo;

		run = factory.createR();
		run.setRPr(arialText);
		paragrafo = factory.createP();
		// Quebra linha
		if ( doubleBreakLine )
		{
			run.getContent().add(factory.createBr());
			run.getContent().add(factory.createBr());
		}

		text1 = factory.createText();
		text1.setValue(item.getNome());
		run.getContent().add(text1);
		RPr runProperties = factory.createRPr();
		addTitleText(runProperties);
		run.setRPr(runProperties);
		runProperties = setCorrectFormat(runProperties);
		
		// Quebra linha
		run.getContent().add(factory.createBr());
		paragrafo.getContent().add(run);

		run = factory.createR();
		run.setRPr(arialText);
		text1 = factory.createText();
		text1.setValue(item.getDescricao());
		run.getContent().add(text1);

		// Quebra linha
		run.getContent().add(factory.createBr());

		paragrafo.getContent().add(run);

		run = factory.createR();
		runProperties = factory.createRPr();
		addBoldStyle(runProperties);
		runProperties = setCorrectFormat(runProperties);
		run.setRPr(runProperties);

		text1 = factory.createText();
		text1.setValue("Justificativa: ");
		text1.setSpace("preserve");
		run.getContent().add(text1);

		paragrafo.getContent().add(run);

		run = factory.createR();
		run.setRPr(arialText);
		text1 = factory.createText();
		text1.setValue(item.getJustificativaSe());
		run.getContent().add(text1);

		// Quebra linha
		run.getContent().add(factory.createBr());

		paragrafo.getContent().add(run);

		run = factory.createR();
		runProperties = factory.createRPr();
		addBoldStyle(runProperties);
		runProperties = setCorrectFormat(runProperties);
		run.setRPr(runProperties);

		text1 = factory.createText();
		text1.setValue("Manutenção: ");
		text1.setSpace("preserve");
		run.getContent().add(text1);

		paragrafo.getContent().add(run);

		run = factory.createR();
		run.setRPr(arialText);
		text1 = factory.createText();
		text1.setValue(item.getDsManutencao());
		run.getContent().add(text1);

		if ( item.isElementoQuantidade() )
		{

			// Quebra linha
			run.getContent().add(factory.createBr());
			paragrafo.getContent().add(run);

			run = factory.createR();
			runProperties = factory.createRPr();
			addBoldStyle(runProperties);
			runProperties = setCorrectFormat(runProperties);
			run.setRPr(runProperties);

			text1 = factory.createText();
			text1.setValue("Quantidade: ");
			text1.setSpace("preserve");
			run.getContent().add(text1);

			paragrafo.getContent().add(run);

			run = factory.createR();
			run.setRPr(arialText);
			text1 = factory.createText();
			text1.setValue(String.valueOf(item.getQtItem()));
			run.getContent().add(text1);

		}
		// Quebra linha
		run.getContent().add(factory.createBr());
		paragrafo.getContent().add(run);

		template.getMainDocumentPart().getContent()
				.add(indexParagrafo++, paragrafo);

		if ( !item.isElementoQuantidade() )
		{

			run = factory.createR();
			run.setRPr(arialText);
			paragrafo = factory.createP();

			List<List<ValueStyle>> values = new ArrayList<List<ValueStyle>>();

			List<ValueStyle> temp = new ArrayList<ValueStyle>();
			temp.add(new ValueStyle("Arquivos Referenciados", STYLE.BOLD));
			temp.add(new ValueStyle("Tipos de Dados", STYLE.BOLD));
			values.add(temp);

			boolean temTD;

			int qtTDNecessario = 0;

			qtTDNecessario = calcularQtTipoDadosNecessario(item, qtTDNecessario);

			int qtTD = 0;

			for ( ArquivoReferenciadoProjetoTO arquivo : item.arqReferenciados )
			{

				temTD = false;

				if ( qtTD < qtTDNecessario )
				{
					for ( TipoDadosFuncaoTransacaoProjetoTO dados : arquivo
							.getTipoDadosFuncaoTransacaoProjetos() )
					{

						if ( !dados.getAtravessaFronteira() )
							continue;

						temp = new ArrayList<ValueStyle>();
						temp.add(new ValueStyle(arquivo.getNomeArquivoLogico()));

						if ( !dados.getAtributoTO().isHasNmLogico() )
						{
							temp.add(new ValueStyle(dados.getAtributoTO()
									.getNmAtributo(), STYLE.RED));
						} else
						{
							temp.add(new ValueStyle(dados.getAtributoTO()
									.getDsAtributo()));
						}

						values.add(temp);
						temTD = true;

						qtTD++;

						if ( qtTD >= qtTDNecessario )
							break;
					}
				}
				if ( !temTD )
				{
					temp = new ArrayList<ValueStyle>();
					temp.add(new ValueStyle(arquivo.getNomeArquivoLogico()));
					temp.add(new ValueStyle("-"));
					values.add(temp);
				}

			}

			if ( qtTD < qtTDNecessario )
			{
				List<TipoDadosDERFuncaoTransacaoProjetoTO> derList = item.tipoDadosDERFuncaoTransacaoProjetoTOs;
				for ( TipoDadosDERFuncaoTransacaoProjetoTO tipoDadosDERFuncaoTransacaoProjetoTO : derList )
				{
					temp = new ArrayList<ValueStyle>();
					temp.add(new ValueStyle("-"));
					temp.add(new ValueStyle(
							tipoDadosDERFuncaoTransacaoProjetoTO.getNmCampo()));
					values.add(temp);

					if ( ++qtTD >= qtTDNecessario )
						break;
				}
			}
			
			run.getContent().add(new TableWord(factory, values).getTable());

			paragrafo.getContent().add(run);
			template.getMainDocumentPart().getContent()
					.add(indexParagrafo++, paragrafo);
		}

	}

	private int calcularQtTipoDadosNecessario( FuncaoTransacaoProjetoVO item,
			int qtTDNecessario )
	{
		if ( item.funcaoTransacaoTP == TipoFuncaoTransacaoEnum.EE )
		{
			if ( item.complexidadeTP == TipoComplexidadeEnum.BAIXA )
			{
				if ( item.qtAr == 2 )
				{
					qtTDNecessario = 1;
				} else
				{
					qtTDNecessario = 1;
				}
			} else if ( item.complexidadeTP == TipoComplexidadeEnum.MEDIA )
			{

				if ( item.qtAr < 2 )
				{
					qtTDNecessario = 16;
				} else if ( item.qtAr == 2 )
				{
					qtTDNecessario = 5;
				} else
				{
					qtTDNecessario = 1;
				}

			} else
			{
				if ( item.qtAr == 2 )
				{
					qtTDNecessario = 16;
				} else
				{
					qtTDNecessario = 5;
				}
			}
		} else
		{
			if ( item.complexidadeTP == TipoComplexidadeEnum.BAIXA )
			{
				if ( item.qtAr < 2 )
				{
					qtTDNecessario = 1;
				} else if ( item.qtAr == 2 || item.qtAr == 3 )
				{
					qtTDNecessario = 1;
				}
			} else if ( item.complexidadeTP == TipoComplexidadeEnum.MEDIA )
			{

				if ( item.qtAr < 2 )
				{
					qtTDNecessario = 20;
				} else if ( item.qtAr == 2 || item.qtAr == 3 )
				{
					qtTDNecessario = 6;
				} else
				{
					qtTDNecessario = 1;
				}

			} else
			{
				if ( item.qtAr == 2 || item.qtAr == 3 )
				{
					qtTDNecessario = 20;
				} else
				{
					qtTDNecessario = 6;
				}
			}
		}
		return qtTDNecessario;
	}

	private void escreverFuncoesTransacaoVazia( WordprocessingMLPackage template )
	{
		R run = factory.createR();
		RPr arialText = factory.createRPr();
		arialText = setCorrectFormat(arialText);
		run.setRPr(arialText);
		Text text1 = factory.createText();
		text1.setValue(NÃO_SE_APLICA);

		run.getContent().add(text1);

		P paragrafo = factory.createP();
		paragrafo.getContent().add(run);

		template.getMainDocumentPart().getContent()
				.add(indexParagrafo++, paragrafo);
	}

	private void escreverFuncoesTransacaoIncluidas(
			WordprocessingMLPackage template )
	{

		indexParagrafo++;

		if ( this.funcoesTransacaoVOListIncluidas == null
				|| funcoesTransacaoVOListIncluidas.isEmpty() )
		{

			escreverFuncoesTransacaoVazia(template);

			return;
		}

		int cont = 1;
		for ( FuncaoTransacaoProjetoVO item : this.funcoesTransacaoVOListIncluidas )
		{
			escreverFuncoesTransacao(template, item, cont++ > 1);
		}

	}

	private void escreverFuncoesTransacaoAlteradas(
			WordprocessingMLPackage template )
	{

		indexParagrafo++;
		indexParagrafo++;

		if ( this.funcoesTransacaoVOListIncluidas == null
				|| this.funcoesTransacaoVOListIncluidas.isEmpty() )
			indexParagrafo++;

		if ( this.funcoesTransacaoVOListAlteradas == null
				|| funcoesTransacaoVOListAlteradas.isEmpty() )
		{

			escreverFuncoesTransacaoVazia(template);
			return;
		}

		int cont = 1;

		for ( FuncaoTransacaoProjetoVO item : this.funcoesTransacaoVOListAlteradas )
		{
			escreverFuncoesTransacao(template, item, cont++ > 1);
		}
	}

	private void escreverFuncoesTransacaoExcluidas(
			WordprocessingMLPackage template )
	{

		indexParagrafo++;
		indexParagrafo++;

		if ( this.funcoesTransacaoVOListExcluidas == null
				|| funcoesTransacaoVOListExcluidas.isEmpty() )
		{

			escreverFuncoesTransacaoVazia(template);

			return;
		}

		int cont = 1;

		for ( FuncaoTransacaoProjetoVO item : this.funcoesTransacaoVOListExcluidas )
		{
			escreverFuncoesTransacao(template, item, cont++ > 1);
		}

	}

	private void escreverFuncaoDados( WordprocessingMLPackage template,
			FuncaoDadosProjetoVO item )
	{
		R run = factory.createR();
		Text text1;
		P paragrafo;
		RPr arialText = factory.createRPr();
		arialText = setCorrectFormat(arialText);
		
		run = factory.createR();
		
		RPr runProperties = factory.createRPr();
		addBoldStyle(runProperties);
		runProperties = setCorrectFormat(runProperties);

		text1 = factory.createText();
		text1.setValue(item.getNome());
		run.getContent().add(text1);

		run.setRPr(runProperties);

		paragrafo = factory.createP();
		paragrafo.getContent().add(run);

		run = factory.createR();
		run.setRPr(arialText);
		// Quebra linha
		run.getContent().add(factory.createBr());

		text1 = factory.createText();
		text1.setValue(item.getDescricao());
		run.getContent().add(text1);

		// Quebra linha
		run.getContent().add(factory.createBr());

		text1 = factory.createText();
		text1.setValue(item.getDescricaoManutencao());
		run.getContent().add(text1);

		// Quebra linha
		run.getContent().add(factory.createBr());

		// paragrafo = factory.createP();
		paragrafo.getContent().add(run);

		template.getMainDocumentPart().getContent()
				.add(indexParagrafo++, paragrafo);

		if ( item.getAtributoTOs().size() > 0 )
		{

			run = factory.createR();
			run.setRPr(arialText);
			paragrafo = factory.createP();

			run.getContent().add(
					getTableAtributos(template, item.getAtributoTOs()));

			paragrafo.getContent().add(run);
			template.getMainDocumentPart().getContent()
					.add(indexParagrafo++, paragrafo);

		}

	}

	private void escreverFuncaoDadosVazia( WordprocessingMLPackage template )
	{
		RPr arialText = factory.createRPr();
		arialText = setCorrectFormat(arialText);
		Text text1 = factory.createText();
		text1.setValue(NÃO_SE_APLICA);

		R run = factory.createR();
		run.setRPr(arialText);
		run.getContent().add(text1);

		P paragrafo = factory.createP();
		paragrafo.getContent().add(run);

		template.getMainDocumentPart().getContent()
				.add(indexParagrafo++, paragrafo);
	}

	private void escreverFuncoesDadosIncluidas( WordprocessingMLPackage template )
	{

		indexParagrafo++;
		indexParagrafo++;

		if ( this.funcoesTransacaoVOListExcluidas == null
				|| this.funcoesTransacaoVOListExcluidas.isEmpty() )
			indexParagrafo++;

		if ( this.funcoesDadosVOListIncluidas == null
				|| funcoesDadosVOListIncluidas.isEmpty() )
		{
			escreverFuncaoDadosVazia(template);
			return;
		}

		for ( FuncaoDadosProjetoVO item : funcoesDadosVOListIncluidas )
		{
			escreverFuncaoDados(template, item);
		}

	}

	private void escreverFuncoesDadosAlteradas( WordprocessingMLPackage template )
	{

		indexParagrafo++;
		indexParagrafo++;

		if ( this.funcoesDadosVOListAlteradas == null
				|| funcoesDadosVOListAlteradas.isEmpty() )
		{
			escreverFuncaoDadosVazia(template);

			return;
		}

		for ( FuncaoDadosProjetoVO item : funcoesDadosVOListAlteradas )
		{
			escreverFuncaoDados(template, item);
		}
	}

	private void escreverFuncoesDadosExcluidas( WordprocessingMLPackage template )
	{

		indexParagrafo++;
		indexParagrafo++;

		if ( this.funcoesDadosVOListExcluidas == null
				|| funcoesDadosVOListExcluidas.isEmpty() )
		{

			escreverFuncaoDadosVazia(template);

			return;
		}

		for ( FuncaoDadosProjetoVO item : funcoesDadosVOListExcluidas )
		{
			escreverFuncaoDados(template, item);
		}

	}

	private Tbl getTableAtributos( WordprocessingMLPackage wPMLpackage,
			List<AtributoTO> atributoTOs )
	{
		List<List<ValueStyle>> values = new ArrayList<List<ValueStyle>>();

		List<ValueStyle> temp = new ArrayList<ValueStyle>();
		temp.add(new ValueStyle("TRs - Tipo de Registro", STYLE.BOLD));
		temp.add(new ValueStyle("Nome do Campo", STYLE.BOLD));
		temp.add(new ValueStyle("Formato", STYLE.BOLD));
		temp.add(new ValueStyle("Tamanho", STYLE.BOLD));
		temp.add(new ValueStyle("Decimais", STYLE.BOLD));
		temp.add(new ValueStyle("Validades", STYLE.BOLD));
		values.add(temp);

		for ( AtributoTO item : atributoTOs )
		{

			temp = new ArrayList<ValueStyle>();

			if ( !item.getEntidadeTO().isHasNomeLogico() )
			{
				temp.add(new ValueStyle(item.getEntidadeTO().getNmEntidade(),
						STYLE.RED));
			} else
			{
				temp.add(new ValueStyle(item.getEntidadeTO().getDsEntidade()));
			}

			if ( !item.isHasNmLogico() )
			{
				temp.add(new ValueStyle(item.getNmAtributo(), STYLE.RED));
			} else
			{
				temp.add(new ValueStyle(item.getDsAtributo()));
			}

			if ( item.getTpFormato() != null )
				temp.add(new ValueStyle(item.getTpFormato().getDescricao()));

			temp.add(new ValueStyle(
					String.valueOf(item.getNrTamanho() != null ? item
							.getNrTamanho() : 0)));
			temp.add(new ValueStyle(
					String.valueOf(item.getPrecisao() != null ? item
							.getPrecisao() : 0)));
			temp.add(new ValueStyle(item.getDsValidade()));
			values.add(temp);

		}

		return new TableWord(factory, values).getTable();

	}

	private void addTitleText( RPr runProperties )
	{
		BooleanDefaultTrue b = new BooleanDefaultTrue();
		b.setVal(true);
		runProperties.setB(b);
		Color value = new Color();
		value.setVal("800000");
		runProperties.setColor(value);
	}

	private WordprocessingMLPackage getTemplate( String name )
			throws Docx4JException, FileNotFoundException
	{
		WordprocessingMLPackage template = WordprocessingMLPackage
				.load(new FileInputStream(new File(name)));
		return template;
	}

	private static List<Object> getAllElementFromObject( Object obj,
			Class<?> toSearch )
	{
		List<Object> result = new ArrayList<Object>();
		if ( obj instanceof JAXBElement )
			obj = ((JAXBElement<?>) obj).getValue();

		if ( obj.getClass().equals(toSearch) )
			result.add(obj);
		else if ( obj instanceof ContentAccessor )
		{
			List<?> children = ((ContentAccessor) obj).getContent();
			for ( Object child : children )
			{
				result.addAll(getAllElementFromObject(child, toSearch));
			}
		}
		return result;
	}

	private void replacePlaceholder( WordprocessingMLPackage template )
	{
		List<Object> texts = getAllElementFromObject(
				template.getMainDocumentPart(), Text.class);

		String key;
		String value;
		for ( Object text : texts )
		{
			Text textElement = (Text) text;
			key = textElement.getValue();
			if ( key.startsWith("#") )
			{
				value = this.valores.get(key);
				if ( value != null )
					textElement.setValue(value);
			}
		}
	}

	private void writeDocxToStream( WordprocessingMLPackage template )
			throws IOException, Docx4JException
	{

		Save saver = new Save(template);

		saver.save(this.outputStream);

	}

	private void addBoldStyle( RPr runProperties )
	{
		BooleanDefaultTrue b = new BooleanDefaultTrue();
		b.setVal(true);
		runProperties.setB(b);
	}

	public List<FuncaoTransacaoProjetoVO> getFuncoesTransacaoVOListExcluidas()
	{
		return funcoesTransacaoVOListExcluidas;
	}

	public static RPr changeFontToArial( RPr runProperties )
	{
		RFonts runFont = new RFonts();
		runFont.setAscii("Arial");
		runFont.setHAnsi("Arial");
		runProperties.setRFonts(runFont);
		return runProperties;
	}

	public static RPr changeFontSize( RPr runProperties, int fontSize )
	{
		HpsMeasure size = new HpsMeasure();
		size.setVal(BigInteger.valueOf(fontSize));
		runProperties.setSz(size);
		return runProperties;
	}

	public static RPr setCorrectFormat( RPr runProperties )
	{

		runProperties = changeFontToArial(runProperties);
		runProperties = changeFontSize(runProperties, 20);
		return runProperties;
	}

}