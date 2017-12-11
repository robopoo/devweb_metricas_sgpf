package br.com.sgpf.metrica.word.dvs;

import java.math.BigInteger;
import java.util.List;

import org.docx4j.jaxb.Context;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.CTBorder;
import org.docx4j.wml.CTShd;
import org.docx4j.wml.CTTblPrBase.TblStyle;
import org.docx4j.wml.Color;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.PPrBase;
import org.docx4j.wml.R;
import org.docx4j.wml.RPr;
import org.docx4j.wml.STBorder;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblBorders;
import org.docx4j.wml.TblPr;
import org.docx4j.wml.TblWidth;
import org.docx4j.wml.Tc;
import org.docx4j.wml.TcPr;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;

import br.com.sgpf.metrica.word.dvs.ValueStyle.STYLE;

public class TableWord {

	private static final TblWidth _TAM_PEQ;

	private static final TblWidth _TAM_MEDIO;

	private static final TblWidth _TAM_GRANDE;

	static {
		_TAM_PEQ = new TblWidth();
		_TAM_PEQ.setType(TblWidth.TYPE_DXA);
		_TAM_PEQ.setW(new BigInteger("1000"));

		_TAM_MEDIO = new TblWidth();
		_TAM_MEDIO.setType(TblWidth.TYPE_DXA);
		_TAM_MEDIO.setW(new BigInteger("2500"));

		_TAM_GRANDE = new TblWidth();
		_TAM_GRANDE.setType(TblWidth.TYPE_DXA);
		_TAM_GRANDE.setW(new BigInteger("4000"));
	}

	private ObjectFactory factory;

	private List<List<ValueStyle>> values;

	public TableWord(ObjectFactory factory, List<List<ValueStyle>> values2) {
		this.factory = factory;
		this.values = values2;
	}

	public Tbl getTable() {

		ObjectFactory wmlObjectFactory = Context.getWmlObjectFactory();

		CTBorder border = new CTBorder();
		border.setColor("auto");
		border.setSz(new BigInteger("4"));
		border.setSpace(new BigInteger("0"));
		border.setVal(STBorder.SINGLE);

		TblBorders borders = new TblBorders();
		borders.setBottom(border);
		borders.setLeft(border);
		borders.setRight(border);
		borders.setTop(border);
		borders.setInsideH(border);
		borders.setInsideV(border);

		Tbl table = wmlObjectFactory.createTbl();
		table.setTblPr(new TblPr());
		table.getTblPr().setTblBorders(borders);

		Tr tableRow;

		Tc tableCell;
		TcPr createTcPr;
		int col;
		RPr arialText = factory.createRPr();
		arialText = DVSDocument.setCorrectFormat(arialText);
		
		PPrBase.Spacing pprbasespacing = wmlObjectFactory.createPPrBaseSpacing();
		pprbasespacing.setAfter(BigInteger.valueOf(0));
		pprbasespacing.setBefore(BigInteger.valueOf(0));

		int linha = 0;
		for (List<ValueStyle> vet : values) {

			tableRow = factory.createTr();

			col = 0;
			for (ValueStyle value : vet) {
				tableCell = factory.createTc();
				tableCell.getContent().add(factory.createP());

				TcPr tableCellProperties = factory.createTcPr();
				tableCellProperties.setTcW(_TAM_GRANDE);
				if (linha == 0) {
					tableCell.setTcPr(tableCellProperties);

					CTShd cellShd = factory.createCTShd();
					tableCell.getTcPr().setShd(cellShd);

					CTShd shd = new CTShd();
					shd.setFill("C0C0C0");

					tableCellProperties.setShd(shd);
				}

				tableRow.getContent().add(tableCell);

				Tc column = (Tc) tableRow.getContent().get(col++);
				P columnPara = (P) column.getContent().get(0);
				Text tx = factory.createText();
				R run = factory.createR();
				run.setRPr(arialText);
				P paragrafo = factory.createP();
				PPr pPr = factory.createPPr();
				pPr.setSpacing(pprbasespacing);
				paragrafo.setPPr(pPr);

				if (value.style != STYLE.NORMAL) {
					RPr runProperties = factory.createRPr();
					switch (value.style) {
					case RED:
						addTextRed(runProperties);
						break;
					case BOLD:
						addBoldStyle(runProperties);
						break;
					default:
						break;
					}
					runProperties = DVSDocument.setCorrectFormat(runProperties);
					run.setRPr(runProperties);
				}

				tx.setValue(value.value);
				paragrafo.getContent().add(tx);
				run.getContent().add(tx);
				columnPara.getContent().add(run);
			}
			linha++;
			table.getContent().add(tableRow);
		}

		return table;
	}

	private void addTextRed(RPr runProperties) {
		BooleanDefaultTrue b = new BooleanDefaultTrue();
		b.setVal(true);
		runProperties.setB(b);
		Color value = new Color();
		value.setVal("red");
		runProperties.setColor(value);
	}

	private void addBoldStyle(RPr runProperties) {
		BooleanDefaultTrue b = new BooleanDefaultTrue();
		b.setVal(true);
		runProperties.setB(b);
	}

}