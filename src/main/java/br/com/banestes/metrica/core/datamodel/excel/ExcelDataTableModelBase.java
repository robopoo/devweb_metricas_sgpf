package br.com.sgpf.metrica.core.datamodel.excel;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.hibernate.ScrollableResults;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.log.Log;

import br.com.sgpf.metrica.core.entity.EntityModel;

public abstract class ExcelDataTableModelBase {

	@Logger
	protected Log log;

	private Map<String, ScrollableResults> resultSets;

	private EntityModel<Serializable> currentObject;


	public ExcelDataTableModelBase(Map<String, ScrollableResults> resultSets) {
		this.resultSets = resultSets;
	}

	public ExcelDataTableModelBase(String sheetName, ScrollableResults resultSet) {
		this.resultSets = new HashMap<String, ScrollableResults>();
		resultSets.put(sheetName, resultSet);
	}

	public abstract int getColumnCount(String sheetName);

	public abstract String getColumnName(String sheetName, int column);

	public abstract Object getValueAt(String sheetName, int rowIndex, int columnIndex);

	@SuppressWarnings("rawtypes")
	public byte[] export() {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			WritableWorkbook workBook = Workbook.createWorkbook(outputStream);

			Set<String> sheetNames = resultSets.keySet();
			int indexSheetName = 0;
			for (String sheetName : sheetNames) {
				WritableSheet sheet = workBook.createSheet(sheetName, indexSheetName);
				WritableFont pretoNegritoFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
				pretoNegritoFont.setColour(Colour.BLACK);
				WritableCellFormat negritoFundoBorda = new WritableCellFormat(pretoNegritoFont);
				negritoFundoBorda.setBorder(Border.ALL, BorderLineStyle.THIN);
				negritoFundoBorda.setBackground(Colour.GRAY_25);

				for (int i = 0; i < getColumnCount(sheetName); i++) {
					Label column = new Label(i, 0, getColumnName(sheetName, i));
					column.setCellFormat(negritoFundoBorda);
					sheet.addCell(column);

				}
				int j = 0;
				WritableCellFormat somenteBorda = new WritableCellFormat();
				somenteBorda.setWrap(true);
				somenteBorda.setBorder(Border.ALL, BorderLineStyle.THIN);
				int selectedRow = 0;
				while (resultSets.get(sheetName).next()) {
					setCurrentObject((EntityModel) resultSets.get(sheetName).get(0));
					for (j = 0; j < getColumnCount(sheetName); j++) {
						Label row = new Label(j, selectedRow + 1, (getValueAt(sheetName, selectedRow, j) == null ? "" : getValueAt(sheetName, selectedRow, j)
								.toString()));
						sheet.addCell(row);
						row.setCellFormat(somenteBorda);
						CellView cell = sheet.getColumnView(j);
						cell.setAutosize(true);
						sheet.setColumnView(j, cell);
					}
					++selectedRow;
				}
				++indexSheetName;
			}
			workBook.write();
			workBook.close();
			return outputStream.toByteArray();
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex.getMessage(), ex);
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	public EntityModel getCurrentObject() {
		return this.currentObject;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setCurrentObject(EntityModel currentObject) {
		this.currentObject = currentObject;
	}
}
