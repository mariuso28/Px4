package org.dx4.agent.excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dx4.bet.Dx4BetRollup;
import org.dx4.external.parser.FxRate;
import org.dx4.external.parser.ParseFxRateEURMYR;
import org.dx4.game.Dx4Game;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.home.Dx4Home;
import org.dx4.json.message.Dx4ProviderJson;
import org.dx4.services.Dx4Services;
import org.dx4.services.Dx4ServicesException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Dx4BetRollupSS {
	
	private static Logger log = Logger.getLogger(Dx4BetRollupSS.class);
	private Dx4MetaGame metaGame;
	private XSSFWorkbook wb;
	private Map<String, CellStyle> styles;
	private Dx4Home dx4Home;
	private List<Dx4Game> games = new ArrayList<Dx4Game>();
	private Sheet sheet;
	
	private FxRate fxRate;
	
	public Dx4BetRollupSS(Dx4MetaGame metaGame,Dx4Home dx4Home,Dx4ProviderJson provider,FxRate fxRate) throws IOException
	{
		setMetaGame(metaGame);
		setDx4Home(dx4Home);
		setFxRate(fxRate);
		
		wb = new XSSFWorkbook();
		styles = ExcelStyles.createStyles(wb);
		
	//	Dx4PlayGame playGame = metaGame.getNextGameAvailableForBet();
		
		Dx4PlayGame playGame = metaGame.getPlayGameById(2);
		if (playGame == null)
		{
			log.info("No coming play date - exiting");
			System.exit(0);
		}
		
		String date = playGame.getPlayDate().toString().substring(0, playGame.getPlayDate().toString().indexOf(' '));
	      
		createSS(playGame,date,4,provider,false);
		createSS(playGame,date,3,provider,false);
		createSS(playGame,date,2,provider,false);
/*		
		createSS(playGame,date,4,provider,true);
		createSS(playGame,date,3,provider,true);
		createSS(playGame,date,2,provider,true);
*/		
		String name = "Totals";
		if (provider!=null)
			name = provider.getName();
		
		XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
		
		String file = "/home/pmk/workspace/Dx4/xls/" +  date + " " + name + ".xlsx";
       
        FileOutputStream out = new FileOutputStream(file);
        wb.write(out);
        out.close();
        
        log.info("workbook : " + file + " created..");
	}
	
	public Dx4BetRollupSS(Dx4MetaGame metaGame,Dx4Home dx4Home,FxRate fxRate) throws IOException
	{
		this(metaGame, dx4Home,null,fxRate);
	}
	
	private void createSS(Dx4PlayGame playGame,String date,int digits, Dx4ProviderJson provider,boolean win)
	{
		String name = metaGame.getName() + " #" +digits + " " + date;
		if (win)
			name += " - WIN";
		String[] titles = buildTitles(digits);
		sheet = wb.createSheet(name);
        initializeSheet(sheet,styles,titles);
        createSheet(playGame,digits,provider,win);
	}

	private void createSheet(Dx4PlayGame playGame,int digits, Dx4ProviderJson provider,boolean win) {
		
		List<Dx4BetRollup> rollups;
		if (provider == null)
			rollups = dx4Home.getTotalBetRollups(playGame.getId(), digits);
		else
			rollups = dx4Home.getTotalBetRollups(playGame.getId(), digits, provider.getCode());
		
		List<Dx4BetRollup> sameNumberRollups = new ArrayList<Dx4BetRollup>();
		 int rownum = 2;
		 for (Dx4BetRollup rollup : rollups)
		 {
			 if (sameNumberRollups.isEmpty())
				 sameNumberRollups.add(rollup);
			 if (sameNumberRollups.get(sameNumberRollups.size()-1).getNumber().equals(rollup.getNumber()))
				 sameNumberRollups.add(rollup);
			 else
			 {
				 createSSRow(sameNumberRollups,rownum++,win);
				 sameNumberRollups.clear();
				 sameNumberRollups.add(rollup);
			 }
		 }
		 if (!sameNumberRollups.isEmpty())
			 createSSRow(sameNumberRollups,rownum++,win);
		
	}

	
	
	private void createSSRow(List<Dx4BetRollup> sameNumberRollups,int rownum,boolean win) {
		
		Row row;
	    Cell cell;
		row = sheet.createRow(rownum);
		int col = 0;
		
		cell = row.createCell(col);
        cell.setCellValue(sameNumberRollups.get(0).getNumber());
        cell.setCellStyle(styles.get("cell_normal"));
        sheet.setColumnWidth(col++, 256*8);
        
        double total = 0.0;
       
        for (Dx4Game game : games)
        {
        	double amount = getRollupForGame(sameNumberRollups,game,win);
        	cell = row.createCell(col);
        	cell.setCellValue(amount);
	        cell.setCellStyle(styles.get("cell_score"));
	        sheet.setColumnWidth(col++, 256*16);
        	total += amount;
        	setCellRM(row,col);
        	col++;
        }
        
        cell = row.createCell(col);
        cell.setCellValue(total);
        cell.setCellStyle(styles.get("cell_score"));
        sheet.setColumnWidth(col, 256*16);
        setCellRM(row,col);
    	col++;
	}

	private void setCellRM(Row row,int col) {
		Cell cell = row.createCell(col);
		char colChar = (char) ('A' + (col-1));
        String strFormula= String.valueOf(colChar)+(row.getRowNum()+1) + "*C1";
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        cell.setCellStyle(styles.get("cell_score"));
        cell.setCellFormula(strFormula);
        sheet.setColumnWidth(col, 256*8);
	}

	private double getRollupForGame(List<Dx4BetRollup> sameNumberRollups, Dx4Game game,boolean win) {
		
		for (Dx4BetRollup r : sameNumberRollups)
			if (r.getGameId()==game.getId())
				if (!win)
					return r.getStake();
				else
					return r.getWin();
		return 0.0;
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
	        
	        Row headerRow = sheet.createRow(0);
	        headerRow.setHeightInPoints(14.75f);
	        Cell cell = headerRow.createCell(0);
            cell.setCellValue("Exch");
            cell.setCellStyle(styles.get("header"));
            sheet.setColumnWidth(0, 256*8);
            cell = headerRow.createCell(1);
            cell.setCellValue("EUR/MYR");
            cell.setCellStyle(styles.get("header"));
            sheet.setColumnWidth(0, 256*16);
            
            cell = headerRow.createCell(2);
            cell.setCellValue(fxRate.getRate());
            cell.setCellStyle(styles.get("score"));
            sheet.setColumnWidth(0, 256*8);
                   
            cell = headerRow.createCell(3);
            cell.setCellValue(fxRate.getTimestamp());
            cell.setCellStyle(styles.get("cell_time_date"));
            sheet.setColumnWidth(0, 256*16);
            
	        headerRow = sheet.createRow(1);
	        headerRow.setHeightInPoints(12.75f);
	        int col = 0;
	        for (int i = 0; i < titles.length; i++) {
	            cell = headerRow.createCell(i);
	            cell.setCellValue(titles[i]);
	            cell.setCellStyle(styles.get("header"));
	            if (i % 2 != 0)
	            	sheet.setColumnWidth(col++, 256*16);
	            else
	            	sheet.setColumnWidth(col++, 256*8);
	        }
	       
	        //freeze the first 2 rows
	        sheet.createFreezePane(0, 2);
	 }
	
	private String[] buildTitles(int digits)
	{
		List<String> ts = new ArrayList<String>();
		ts.add("Choice");
		games = new ArrayList<Dx4Game>();
		for (Dx4Game game : metaGame.getGames())
		{
			if (game.getGtype().getDigits()==digits)
			{
				ts.add(game.getGtype().name());
				ts.add("MYR");
				games.add(game);
			}
		}
		
		ts.add("Total $");
		ts.add("MYR");
		String [] titles = new String [ts.size()];
		return ts.toArray(titles);
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

	public static void createExposureWorkBooks(Dx4Services dx4Services) throws Dx4ServicesException
	{
		Dx4Home dx4Home = dx4Services.getDx4Home();
		
		try {
			ParseFxRateEURMYR fxRate = new ParseFxRateEURMYR();
			FxRate rate = fxRate.getFxRate();
			
	//		 Dx4ProviderJson prv = dx4Home.getProviderByCode('K');
	//		 new Dx4BetRollupSS(dx4Home.getMetaGame("4D With ABC"),dx4Home,prv,rate);
			
			for (Dx4ProviderJson prov : dx4Home.getProviders())
			{
				new Dx4BetRollupSS(dx4Home.getMetaGame("4D With ABC"),dx4Home,prov,rate);
			}
			new Dx4BetRollupSS(dx4Home.getMetaGame("4D With ABC"),dx4Home,rate);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Dx4ServicesException("Could not create exposure rollups : " + e.getMessage());
		}
	}
	
	public FxRate getFxRate() {
		return fxRate;
	}

	public void setFxRate(FxRate fxRate) {
		this.fxRate = fxRate;
	}

	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("Dx4-service.xml");
		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		
		try {
			createExposureWorkBooks(dx4Services);
		} catch (Dx4ServicesException e) {
			e.printStackTrace();
		}
	}


}
