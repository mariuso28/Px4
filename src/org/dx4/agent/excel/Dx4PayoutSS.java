package org.dx4.agent.excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dx4.game.Dx4Game;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.payout.Dx4PayOut;
import org.dx4.home.Dx4Home;
import org.dx4.json.message.Dx4GameTypeJson;
import org.dx4.json.message.Dx4PayOutTypeJson;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Dx4PayoutSS {
	
	private static Logger log = Logger.getLogger(Dx4PayoutSS.class);
	private Dx4MetaGame metaGame;
	private XSSFWorkbook wb;
	private Map<String, CellStyle> styles;
	@SuppressWarnings("unused")
	private Dx4Home dx4Home;
	private Sheet sheet;
	
	public Dx4PayoutSS(Dx4MetaGame metaGame,Dx4Home dx4Home) throws IOException
	{
		setMetaGame(metaGame);
		setDx4Home(dx4Home);
		
		wb = new XSSFWorkbook();
		styles = ExcelStyles.createStyles(wb);
		
		String[] titles = { "Clover 4D Games and Payouts" };
		sheet = wb.createSheet("Clover-4D");
		initializeSheet(sheet,styles,titles);
	
		int rownum = create4D(0);
		rownum = create3D(rownum);
		rownum = create2D(rownum);
		rownum = createIBox(rownum,Dx4GameTypeJson.D4IBoxBig);
		rownum = createIBox(rownum,Dx4GameTypeJson.D4IBoxSmall);
		rownum = createIBox(rownum,Dx4GameTypeJson.D4BoxBig);
		rownum = createIBox(rownum,Dx4GameTypeJson.D4BoxSmall);
		
		String file = "/home/pmk/workspace/Dx4/xls/payout.xlsx";
       
        FileOutputStream out = new FileOutputStream(file);
        wb.write(out);
        out.close();
        
        log.info("workbook : " + file + " created..");
	}
	
	private int createIBox(int rownum,Dx4GameTypeJson type)
	{
		Dx4Game gameD4Big = metaGame.getGameByType(type);
		Row row = sheet.createRow(rownum++);
		addHeader2(row,0,type.name().substring(2));
        row = sheet.createRow(rownum++);
        int col = 0;
        col = addHeader(row,col,"Prize");
        col = addHeader(row,col,"24 Perm");
        col = addHeader(row,col,"12 Perm");
        col = addHeader(row,col,"6 Perm");
        col = addHeader(row,col,"4 Perm");
   
        String[] prizes = {"First","Second","Third","Spec","Cons"};
        String[] ibs = { "IB24", "IB12", "IB6", "IB4" };
        int cnt=0;
		for (String prize : prizes)
		{
		    col = 0;
			row = sheet.createRow(rownum++);
			col = addHeader(row,col,prize);
			for (String ib : ibs)
			{
				Dx4PayOut po = gameD4Big.getPayOutByType(Dx4PayOutTypeJson.valueOf(prize+ib));
				col = addPrize(row,col,po.getPayOut());
			}
			cnt++;
			if (type.equals(Dx4GameTypeJson.D4IBoxSmall) || type.equals(Dx4GameTypeJson.D4BoxSmall))
				if (cnt>2)
					break;
		}
        
        return rownum;
	}
	
	private int create4D(int rownum)
	{
		Dx4Game gameD4Big = metaGame.getGameByType(Dx4GameTypeJson.D4Big);
		Dx4Game gameD4Small = metaGame.getGameByType(Dx4GameTypeJson.D4Small);
		Row row = sheet.createRow(rownum++);
		addHeader2(row,0,"4D");
        row = sheet.createRow(rownum++);
        int col = 0;
        col = addHeader(row,col,"Prize");
        col = addHeader(row,col,gameD4Big.getGtype().name());
        col = addHeader(row,col,gameD4Small.getGtype().name());
   
        int index = 0;
		for (Dx4PayOut po : gameD4Big.getPayOuts())
		{
		    col = 0;
			row = sheet.createRow(rownum++);
			col = addHeader(row,col,po.getType().name());
			col = addPrize(row,col,po.getPayOut());
			if (index<3)
				col = addPrize(row,col,gameD4Small.getPayOuts().get(index).getPayOut());
			else
				col = addNormal(row,col,"-");
			index++;
		}
        
        return rownum;
	}
	
	private int create3D(int rownum)
	{
		Dx4Game gameD3Big = metaGame.getGameByType(Dx4GameTypeJson.ABCC);
		Dx4Game gameD3Small = metaGame.getGameByType(Dx4GameTypeJson.ABCA);
		Row row = sheet.createRow(rownum++);
		addHeader2(row,0,"3D");
        row = sheet.createRow(rownum++);
        int col = 0;
        col = addHeader(row,col,"Prize");
        col = addHeader(row,col,gameD3Big.getGtype().name());
        col = addHeader(row,col,gameD3Small.getGtype().name());
   
        int index = 0;
		for (Dx4PayOut po : gameD3Big.getPayOuts())
		{
		    col = 0;
			row = sheet.createRow(rownum++);
			col = addHeader(row,col,po.getType().name());
			col = addPrize(row,col,po.getPayOut());
			if (index<1)
				col = addPrize(row,col,gameD3Small.getPayOuts().get(index).getPayOut());
			else
				col = addNormal(row,col,"-");
			index++;
		}
        
        return rownum;
	}

	private int create2D(int rownum)
	{
		Dx4Game gameD2 = metaGame.getGameByType(Dx4GameTypeJson.D2);
		Row row = sheet.createRow(rownum++);
		addHeader2(row,0,"2D");
        row = sheet.createRow(rownum++);
        int col = 0;
        col = addHeader(row,col,"Prize");
        col = addHeader(row,col,gameD2.getGtype().name());
 		for (Dx4PayOut po : gameD2.getPayOuts())
		{
		    col = 0;
			row = sheet.createRow(rownum++);
			col = addHeader(row,col,po.getType().name());
			col = addPrize(row,col,po.getPayOut());
		}
        
        return rownum;
	}
	
	private int addHeader2(Row row,int col,String header)
	{
		Cell cell = row.createCell(col);
        cell.setCellValue(header);
        cell.setCellStyle(styles.get("header2"));
        sheet.setColumnWidth(col++, 512*8);
        return col;
	}
	
	private int addHeader(Row row,int col,String header)
	{
		Cell cell = row.createCell(col);
        cell.setCellValue(header);
        cell.setCellStyle(styles.get("header"));
        sheet.setColumnWidth(col++, 512*8);
        return col;
	}

	private int addPrize(Row row,int col,Double prize)
	{
		Cell cell = row.createCell(col);
		cell.setCellValue(prize);
        cell.setCellStyle(styles.get("cell_score"));
        sheet.setColumnWidth(col++, 512*8);
        return col;
	}
	
	private int addNormal(Row row,int col,String data)
	{
		Cell cell = row.createCell(col);
		cell.setCellValue(data);
        cell.setCellStyle(styles.get("cell_normal_centered"));
        sheet.setColumnWidth(col++, 512*8);
        return col;
	}

	void initializeSheet(Sheet sheet, Map<String, CellStyle> styles,String[] titles)
	 {
		//turn off gridlines
	        sheet.setDisplayGridlines(false);
	        sheet.setPrintGridlines(false);
	        sheet.setFitToPage(true);
	        sheet.setHorizontallyCenter(true);
	        PrintSetup printSetup = sheet.getPrintSetup();
	        printSetup.setLandscape(true);

	        //the following three statements are required only for HSSF
	        sheet.setAutobreaks(true);
	        printSetup.setFitHeight((short)1);
	        printSetup.setFitWidth((short)1);

	        //the header row: centered text in 48pt font
/*	        
            
	        Row headerRow = sheet.createRow(0);
	        headerRow.setHeightInPoints(12.75f);
	        int col = 0;
	        for (int i = 0; i < titles.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(titles[i]);
	            cell.setCellStyle(styles.get("header"));
	            sheet.setColumnWidth(col++, 256*16);
	        }
	       
	        //freeze the first row
	        sheet.createFreezePane(0, 1);
	    */
	 }
	

	public Dx4MetaGame getMetaGame() {
		return metaGame;
	}

	public void setMetaGame(Dx4MetaGame metaGame) {
		this.metaGame = metaGame;
	}
	
	public void setDx4Home(Dx4Home dx4Home) {
		this.dx4Home = dx4Home;
	}

	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("Dx4-service.xml");
		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		Dx4Home dx4Home = dx4Services.getDx4Home();
		
		try {
			new Dx4PayoutSS(dx4Home.getMetaGame("4D With ABC"),dx4Home);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
