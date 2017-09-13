package org.dx4.secure.web.pdf;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dx4.account.Dx4Transaction;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.json.message.Dx4NumberPageElementJson;
import org.dx4.secure.domain.Dx4SecureUser;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class PDFCreateTransaction {

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(PDFCreateTransaction.class);

	
	@SuppressWarnings("unchecked")
	public static void buildPdfMetaBetDocument(Map<String, Object> model, Document doc) throws Exception {
		Dx4SecureUser currAccountUser = (Dx4SecureUser) model.get("currAccountUser");
		Dx4MetaBet associatedMetaBet = (Dx4MetaBet) model.get("currMetaBet");	
		HashMap<String,Dx4NumberPageElementJson> npes = (HashMap<String,Dx4NumberPageElementJson>) model.get("currNpes");
		HashMap<String,byte[]> images = (HashMap<String, byte[]>) model.get("currImages");
		
		
		String title =  "Bet details for : " + currAccountUser.getEmail() + "  " + currAccountUser.getContact();
		SimpleDateFormat df1 = new SimpleDateFormat("dd-MMM-yy hh:mm:ss");
		title += " Placed on: " + df1.format(associatedMetaBet.getPlaced());
		if (associatedMetaBet.getPlayed()!=null)
		{
			df1 = new SimpleDateFormat("dd-MMM-yy");
			title += " Played on: " + df1.format(associatedMetaBet.getPlayed());
		}
			
		doc.add(new Paragraph(title));
		
		addAssociatedMetaBet(associatedMetaBet,doc,npes,images);		
	}
	
	@SuppressWarnings("unchecked")
	public static void buildPdfDocument(Map<String, Object> model, Document doc)
				throws Exception {
		{
			Dx4SecureUser currAccountUser = (Dx4SecureUser) model.get("currAccountUser");
			Dx4Transaction trans = (Dx4Transaction) model.get("currTransaction");
			Dx4MetaBet associatedMetaBet = (Dx4MetaBet) model.get("currMetaBet");	
			HashMap<String,Dx4NumberPageElementJson> npes = (HashMap<String,Dx4NumberPageElementJson>) model.get("currNpes");
			HashMap<String,byte[]> images = (HashMap<String, byte[]>) model.get("currImages");
			
			String title =  "Transaction details for : " + currAccountUser.getRole().getDesc() +  " - " 
				+ currAccountUser.getEmail() + "  " + currAccountUser.getContact();
			
			doc.add(new Paragraph(title));
			
			PdfPTable table = new PdfPTable(4);
			table.setWidthPercentage(100.0f);
			table.setWidths(new float[] {2.0f, 2.0f, 2.0f, 2.0f});
			table.setSpacingBefore(10);

			// define font for table header row
			Font font = FontFactory.getFont(FontFactory.HELVETICA);
			font.setColor(BaseColor.WHITE);

			// define table header cell
			PdfPCell cell = new PdfPCell();
			cell.setBackgroundColor(BaseColor.BLUE);
			cell.setPadding(5);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			// write table header
			cell.setPhrase(new Phrase("Transaction#", font));
			table.addCell(cell);
			cell.setPhrase(new Phrase("Date", font));
			table.addCell(cell);
			cell.setPhrase(new Phrase("Type", font));
			table.addCell(cell);
			cell.setPhrase(new Phrase("Amount", font));
			table.addCell(cell);

			DecimalFormat df = new DecimalFormat("#0.00");
			SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yy");
			
			font = FontFactory.getFont(FontFactory.HELVETICA);
			font.setColor(BaseColor.BLACK);
			cell = getRightCell();
			
			cell.setPhrase(new Phrase(String.valueOf(trans.getId()), font));
			table.addCell(cell);
			cell.setPhrase(new Phrase(df1.format(trans.getDate()), font));
			table.addCell(cell);
			cell.setPhrase(new Phrase(String.valueOf(trans.getType().name()), font));
			table.addCell(cell);
			cell.setPhrase(new Phrase(df.format(trans.getAmount()), font));
			table.addCell(cell);
			
			doc.add(table);
			addAssociatedMetaBet(associatedMetaBet,doc,npes,images);			
		}
		
	}

	static PdfPCell getRightCell()
	{
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setPadding(5);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		return cell;
	}
	
	private static void addAssociatedMetaBet(Dx4MetaBet associatedMetaBet,Document doc, 
			HashMap<String,Dx4NumberPageElementJson> npes, HashMap<String, byte[]> images) throws Exception
	{
		doc.add(PDFGenMetaBet.getPdfTableForMetaBet(associatedMetaBet));
		
		doc.add(new Paragraph("Bets:"));
		List<PdfPTable> tables = PDFGenMetaBet.getPdfTablesForMetaBetBets(npes,images,associatedMetaBet);
		for (PdfPTable tab : tables)
			doc.add(tab);
		
		if (associatedMetaBet.getWins().isEmpty())
			return;
		doc.add(new Paragraph("Wins:"));
		tables = PDFGenMetaBet.getPdfTablesForMetaBetWins(npes,images,associatedMetaBet);
		for (PdfPTable tab : tables)
			doc.add(tab);
	}
	
	@SuppressWarnings("unused")
	private static PdfPTable addInvoiceLine(Dx4Transaction trans,int cols) throws Exception
	{
		PdfPTable table = addInvoiceTitle(trans,cols);

		DecimalFormat df = new DecimalFormat("#0.00");
		SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yy");
		
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.BLACK);
		PdfPCell cell = getRightCell();
		
		cell.setPhrase(new Phrase(String.valueOf(trans.getId()), font));
		table.addCell(cell);
		cell.setPhrase(new Phrase(df1.format(trans.getDate()), font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase(df.format(trans.getAmount()), font));
		table.addCell(cell);
		
		return table;
	}
	
	private static PdfPTable addInvoiceTitle(Dx4Transaction trans,int cols) throws Exception
	{
		PdfPTable table = new PdfPTable(cols);
		table.setWidthPercentage(100.0f);
		float[] widths = new float[cols];
		for (int i=0; i<cols; i++)
			widths[i] = 2.0f;
		table.setWidths(widths);
		table.setSpacingBefore(10);

		// define font for table header row
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.WHITE);

		// define table header cell
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.GREEN);
		cell.setPadding(5);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);

		// write table header
		cell.setPhrase(new Phrase("Invoice#", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Issue Date", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Due Date", font));
		table.addCell(cell);
		if (cols>6)
		{
			cell.setPhrase(new Phrase("Paid On", font));
			table.addCell(cell);
		}
		cell.setPhrase(new Phrase("Amount", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Comm", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Due", font));
		table.addCell(cell);
		
		return table;
	}

	
}
