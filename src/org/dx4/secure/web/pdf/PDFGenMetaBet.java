package org.dx4.secure.web.pdf;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.dx4.bet.Dx4Bet;
import org.dx4.bet.Dx4MetaBet;
import org.dx4.json.message.Dx4BetJson;
import org.dx4.json.message.Dx4BetStakeCombo;
import org.dx4.json.message.Dx4NumberPageElementJson;
import org.dx4.json.message.Dx4WinJson;
import org.dx4.json.server.JsonServerServices;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class PDFGenMetaBet {

	private static final Logger log = Logger.getLogger(PDFGenMetaBet.class);

	public PDFGenMetaBet()
	{}
	
	public static PdfPTable getPdfTableForMetaBet(Dx4MetaBet currMetaBet) throws DocumentException
	{
		return getTable1(currMetaBet);
	}

	public static List<PdfPTable> getPdfTablesForMetaBetBets(HashMap<String,Dx4NumberPageElementJson> npes,
			HashMap<String, byte[]> images, Dx4MetaBet currMetaBet) throws DocumentException
	{
		return getTables3(currMetaBet,images);
	}

	public static List<PdfPTable> getPdfTablesForMetaBetWins(HashMap<String,Dx4NumberPageElementJson> npes,
			HashMap<String, byte[]> images, Dx4MetaBet currMetaBet) throws DocumentException
	{
		return getTableWins(currMetaBet,images,npes);
	}

	private static PdfPTable getTable1(Dx4MetaBet currMetaBet) throws DocumentException
	{
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy hh:mm");
		String placed = df.format(currMetaBet.getPlaced());
		SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yy");
		String playdate = df1.format(currMetaBet.getPlayGame().getPlayDate());

		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100.0f);
		table.setWidths(new float[] {3.0f, 3.0f, 2.0f, 2.0f, 2.0f });
		table.setSpacingBefore(10);

		// define font for table header row
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.WHITE);

		// define table header cell
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		cell.setPadding(5);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		cell.setPhrase(new Phrase("Game", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Placed", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Draw", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Total Stake", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Total Win", font));
		table.addCell(cell);

		font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.BLACK);
		cell = PDFCreateTransaction.getRightCell();
		cell.setPhrase(new Phrase(currMetaBet.getMetaGame().getName(), font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase(placed, font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase(playdate, font));
		table.addCell(cell);
				
		DecimalFormat df2 = new DecimalFormat("#0.00");
		
		cell.setPhrase(new Phrase(df2.format(currMetaBet.getTotalStake()), font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase(df2.format(currMetaBet.getTotalWin()), font));
		table.addCell(cell);
		
		return table;
	}
	
	private static List<PdfPTable> getTables3(Dx4MetaBet currMetaBet,HashMap<String, byte[]> images) throws DocumentException
	{
		List<Dx4BetJson> betjs = createJsonBets(currMetaBet);
		List<PdfPTable> tabs = new ArrayList<PdfPTable>();
		BetJsonGroups bjgs = new BetJsonGroups(betjs);
		PdfPTable table = null;
		if (!bjgs.getD4().isEmpty())
			table = createD4Table(bjgs.getD4(),images);
		if (!bjgs.getD3().isEmpty())
			table = createD3Table(bjgs.getD3(),images);
		if (!bjgs.getD2().isEmpty())
			table = createD2Table(bjgs.getD2(),images);
		tabs.add(table);
		
		return tabs;
	}
		
	private static PdfPTable createD2Table( List<Dx4BetJson> d2,HashMap<String,byte[]> images) throws DocumentException {
		PdfPTable table = new PdfPTable(9);
		table.setWidthPercentage(100.0f);
		float w2 = 0.75f;
		table.setWidths(new float[] { 2.25f, 2.5f, w2, w2, w2, w2, w2, w2, w2 });
		table.setSpacingBefore(10);
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.WHITE);

		// define table header cell
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		cell.setPadding(5);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		cell.setPhrase(new Phrase("Choice", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("ABC(A)", font));
		table.addCell(cell);
	
		font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.BLACK);

		for (int i=0; i<7; i++)
		{
			cell.setPhrase(new Phrase("", font));
			table.addCell(cell);
		}
		
		
		for (Dx4BetJson bj : d2)
		{	
			cell = PDFCreateTransaction.getRightCell();
			cell.setPhrase(new Phrase(bj.getChoice(), font));
			table.addCell(cell);
			
			DecimalFormat df = new DecimalFormat("#0.00");
			
			cell.setPhrase(new Phrase(df.format(bj.getSmall()), font));
			table.addCell(cell);
		
			addProviders(table,cell,font,bj,images);
		}
		
		return table;
	}
	
	private static PdfPTable createD3Table(List<Dx4BetJson> d3,HashMap<String,byte[]> images) throws DocumentException {
		// define font for table header row
		PdfPTable table = new PdfPTable(10);
		table.setWidthPercentage(100.0f);
		float w2 = 0.75f;
		float w1 = 1.58f;
		table.setWidths(new float[] { w1, w1, w1, w2, w2, w2, w2, w2, w2, w2 });
		table.setSpacingBefore(10);
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.WHITE);

		// define table header cell
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		cell.setPadding(5);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		cell.setPhrase(new Phrase("Choice", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("ABC(C)", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("ABC(A)", font));
		table.addCell(cell);
		
		for (int i=0; i<7; i++)
		{
			cell.setPhrase(new Phrase("", font));
			table.addCell(cell);
		}
		
		font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.BLACK);

		for (Dx4BetJson bj : d3)
		{	
			cell = PDFCreateTransaction.getRightCell();
			cell.setPhrase(new Phrase(bj.getChoice(), font));
			table.addCell(cell);
			
			DecimalFormat df = new DecimalFormat("#0.00");
			cell.setPhrase(new Phrase(df.format(bj.getBig()), font));
			table.addCell(cell);
			
			cell.setPhrase(new Phrase(df.format(bj.getSmall()), font));
			table.addCell(cell);
		
			addProviders(table,cell,font,bj,images);
			
		}
		
		return table;
	}

	private static PdfPTable createD4Table(List<Dx4BetJson> d4,HashMap<String, byte[]> images) throws DocumentException
	{
		PdfPTable table = new PdfPTable(11);
		table.setWidthPercentage(100.0f);
		float w2 = 0.75f;
		float w1 = 1.25f;
		float w0 = 1.0f;
		table.setWidths(new float[] { w1, w1, w1, w0, w2, w2, w2, w2, w2, w2, w2 });
		table.setSpacingBefore(10);
		
		// define font for table header row
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.WHITE);

		// define table header cell
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		cell.setPadding(5);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		cell.setPhrase(new Phrase("Choice", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Box/iBox", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Big", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Small", font));
		table.addCell(cell);
		
		for (int i=0; i<7; i++)
		{
			cell.setPhrase(new Phrase("", font));
			table.addCell(cell);
		}
		
		font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.BLACK);

		for (Dx4BetJson bj : d4)
		{	
			cell = PDFCreateTransaction.getRightCell();
			cell.setPhrase(new Phrase(bj.getChoice(), font));
			table.addCell(cell);
			
			if (bj.getStakeCombo().equals(Dx4BetStakeCombo.BOX))
				cell.setPhrase(new Phrase("Box", font));
			else
			if (bj.getStakeCombo().equals(Dx4BetStakeCombo.IBOX))
				cell.setPhrase(new Phrase("iBox", font));
			else
				cell.setPhrase(new Phrase("Straight", font));
			table.addCell(cell);
			
			DecimalFormat df = new DecimalFormat("#0.00");
			cell.setPhrase(new Phrase(df.format(bj.getBig()), font));
			table.addCell(cell);
			
			cell.setPhrase(new Phrase(df.format(bj.getSmall()), font));
			table.addCell(cell);
		
			addProviders(table,cell,font,bj,images);
		}
		
		return table;
	}

	private static void addProviders(PdfPTable table,PdfPCell cell,Font font,Dx4BetJson bj,HashMap<String, byte[]> images)
	{
		for (String provider : bj.getProviders())
		{
			addProviderImage(table,font,provider,images);
		}
		for (int i=bj.getProviders().size(); i<7; i++)
		{
			cell.setPhrase(new Phrase(" ", font));
			table.addCell(cell);
		}
	}
	
	private static void addProviderImage(PdfPTable table,Font font,String provider,HashMap<String, byte[]> images)
	{
		Image image;
		PdfPCell cell = new PdfPCell();
		try {
			image = Image.getInstance(images.get(provider));
		} catch (BadElementException | IOException e) {
			e.printStackTrace();
			log.error("Couldn't create image for : " + provider);
			cell.setPhrase(new Phrase(provider.substring(0,5), font));
			table.addCell(cell);
			return ;
		}
		cell = new PdfPCell();
		cell.addElement(image);
		table.addCell(cell);
	}
	
	private static List<Dx4BetJson> createJsonBets(Dx4MetaBet currMetaBet)
	{
		JsonServerServices js = new JsonServerServices();
		List<Dx4BetJson> betjs = new ArrayList<Dx4BetJson>();
		for (Dx4Bet bet : currMetaBet.getBets())
		{
			Dx4BetJson bj = js.createDx4BetJson(bet, currMetaBet);
			betjs.add(bj);
		}
		return betjs;
	}
	
	private static List<PdfPTable> getTableWins(Dx4MetaBet currMetaBet,HashMap<String, byte[]> images,HashMap<String,Dx4NumberPageElementJson> npes) throws DocumentException
	{
		JsonServerServices js = new JsonServerServices();
		List<Dx4WinJson> wins = js.createWinJsons(currMetaBet);
		List<PdfPTable> tabs = new ArrayList<PdfPTable>();
		for (Dx4WinJson win : wins)
		{
			List<Dx4BetJson> bets = new ArrayList<Dx4BetJson>();
			bets.add(win.getBet());
			PdfPTable table = null;
			if (win.getBet().getChoice().length()==4)
				table = createD4Table(bets,images);
			else
			if (win.getBet().getChoice().length()==3)
				table = createD3Table(bets,images);
			else
				table = createD2Table(bets,images);
			
			tabs.add(table);
			PdfPTable winTab = getTableWin(win,images,npes);
			tabs.add(winTab);
		}
		return tabs;
	}
	
	private static PdfPTable getTableWin(Dx4WinJson win,HashMap<String, byte[]> images,HashMap<String,Dx4NumberPageElementJson> npes) throws DocumentException
	{
		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100.0f);
		float width = 8.0f/3.0f;
		float w0 = 1.0f;
		table.setWidths(new float[] { width, width, width, w0, w0 });
		table.setSpacingBefore(10);

		// define font for table header row
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.WHITE);

		// define table header cell
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		cell.setPadding(5);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		cell.setPhrase(new Phrase("Result", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Place", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Win", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("", font));
		table.addCell(cell);
		
		font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.BLACK);
		cell = PDFCreateTransaction.getRightCell();
		
		cell.setPhrase(new Phrase(win.getResult(), font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase(win.getPlace(), font));
		table.addCell(cell);
		
		DecimalFormat df = new DecimalFormat("#0.00");
		cell.setPhrase(new Phrase(df.format(win.getWin()), font));
		table.addCell(cell);
		
		addProviderImage(table,font,win.getProvider(),images);
	    addNumberImage(table,font,win.getResult(),images,npes);
	    
		return table;
	}
	
	private static void addNumberImage(PdfPTable table, Font font, String result, HashMap<String, byte[]> images,
			HashMap<String, Dx4NumberPageElementJson> npes) {
		 
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setPadding(5);

		Dx4NumberPageElementJson npe = npes.get(result);
		Image image;
		try {
			image = Image.getInstance(images.get(result));
			cell = new PdfPCell();
			cell.addElement(image);
			cell.addElement(new Paragraph(result));
			cell.addElement(new Paragraph(npe.getDescription()));
			table.addCell(cell);

		} catch (IOException | BadElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 cell = new PdfPCell();
			 cell.addElement(new Paragraph(" "));
			 table.addCell(cell);	
			 cell = new PdfPCell();
			 cell.addElement(new Paragraph(" "));
			 table.addCell(cell);	
		}
	}
}
