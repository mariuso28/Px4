package org.dx4.agent.excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import org.dx4.bet.persistence.WinNumber;
import org.dx4.external.parser.FxRate;
import org.dx4.game.Dx4Game;
import org.dx4.game.Dx4MetaGame;
import org.dx4.game.Dx4PlayGame;
import org.dx4.home.Dx4Home;
import org.dx4.json.message.Dx4ProviderJson;
import org.dx4.services.Dx4Config;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Dx4WinRollupUSDMYR {
	
	private static Logger log = Logger.getLogger(Dx4WinRollupUSDMYR.class);
	private Dx4MetaGame metaGame;
	private XSSFWorkbook wb;
	private Map<String, CellStyle> styles;
	private Dx4Home dx4Home;
	private List<Dx4Game> games = new ArrayList<Dx4Game>();
	private Sheet sheet;
	private FxRate fxRate;
	private String workbookPath;
	private Map<String,WinNumber> winNumberMap = new HashMap<String,WinNumber>();
	private boolean win;

	public Dx4WinRollupUSDMYR(Dx4MetaGame metaGame,Dx4PlayGame playGame, Dx4Home dx4Home,Dx4ProviderJson provider,FxRate fxRate,boolean win) throws IOException
	{
		setMetaGame(metaGame);
		setDx4Home(dx4Home);
		setFxRate(fxRate);
		setWin(win);
		
		wb = new XSSFWorkbook();
		styles = ExcelStyles.createStyles(wb);
		
		String date = playGame.getPlayDate().toString().substring(0, playGame.getPlayDate().toString().indexOf(' '));
	      
		createSS(playGame,date,4,provider,win);
		createSS(playGame,date,3,provider,win);
		createSS(playGame,date,2,provider,win);

		String name = "Totals";
		if (provider!=null)
			name = provider.getName();
		
		XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MMM-dd");
		
		String xlsFolder = Dx4Config.getProperties().getProperty("dx4.xlsFolder","/home/pmk/dx4/xls/");
		
		workbookPath = xlsFolder +  df.format(playGame.getPlayDate()) + "-" + name;
		if (win)
			workbookPath += " - wins";
		workbookPath += ".xls";
       
        FileOutputStream out = new FileOutputStream(workbookPath);
        wb.write(out);
        out.close();
        
        log.info("workbook : " + workbookPath + " created..");
	}
	
	public Dx4WinRollupUSDMYR(Dx4MetaGame metaGame,Dx4PlayGame playGame,Dx4Home dx4Home,FxRate fxRate,boolean win) throws IOException
	{
		this(metaGame, playGame, dx4Home,null,fxRate,win);
	}
	
	private void createWinNumberMap(Dx4PlayGame playGame,Dx4ProviderJson provider)
	{
		List<WinNumber> wnl = dx4Home.getWinNumber(provider, playGame.getId());
		winNumberMap.clear();
		for (WinNumber wn : wnl)
			winNumberMap.put(wn.createKey(), wn);
	}
	
	private void createSS(Dx4PlayGame playGame,String date,int digits, Dx4ProviderJson provider,boolean win)
	{
		String name = metaGame.getName() + " #" +digits + " " + date;
		if (win)
			name += " - WIN";
		else
			name += " - BET";
		String[] titles = buildTitles(digits);
		sheet = wb.createSheet(name);
        initializeSheet(sheet,styles,titles,provider,date);
        createSheet(playGame,digits,provider);
	}

	private void createSheet(Dx4PlayGame playGame,int digits, Dx4ProviderJson provider) {
		
		List<Dx4BetRollup> rollups;
		if (provider == null)
		{
			rollups = getRollupTotals(playGame, digits);
		}
		else
		{
			rollups = getRollupTotalsForProvider(playGame,digits,provider);
		}
		
		List<Dx4BetRollup> sameNumberRollups = new ArrayList<Dx4BetRollup>();
		 int rownum = 2;
		 for (Dx4BetRollup rollup : rollups)
		 {
//			 if (rollup.getNumber().equals("1392") && provider != null && provider.getCode()=='S')
//				 log.info("found");
			 
			 if (sameNumberRollups.isEmpty())
				 sameNumberRollups.add(rollup);
			 if (sameNumberRollups.get(sameNumberRollups.size()-1).getNumber().equals(rollup.getNumber()))
				 sameNumberRollups.add(rollup);
			 else
			 {
				 createSSRow(sameNumberRollups,rownum++);
				 sameNumberRollups.clear();
				 sameNumberRollups.add(rollup);
			 }
		 }
		 if (!sameNumberRollups.isEmpty())
			 createSSRow(sameNumberRollups,rownum++);
		 
		 if (!rollups.isEmpty())
			 writeTotals(rownum);
		 
		 if (provider == null && digits == 4)
			 writeTotalRollups(rownum);
	}

	private void writeTotalRollups(int rownum) {
		Map<Date,StakeWin>  tws = dx4Home.getTotalWinStakes();
		
		rownum += 2;
		Cell cell;
		Row row = sheet.createRow(rownum++);
		int col = 6;
		cell = row.createCell(col);
        cell.setCellValue("USD/MYR");
        cell.setCellStyle(styles.get("header"));
        sheet.setColumnWidth(col++, 256*16);
        cell = row.createCell(col);
        cell.setCellValue(fxRate.getTimestamp());
        cell.setCellStyle(styles.get("cell_time_date"));
        sheet.setColumnWidth(col++, 256*16);
        cell = row.createCell(col);
        cell.setCellValue(fxRate.getRate());
        cell.setCellStyle(styles.get("cell_score"));
        sheet.setColumnWidth(col++, 256*16);
		
        row = sheet.createRow(rownum++);
		col = 6;
		
		cell = row.createCell(col);
        cell.setCellValue("Draw");
        cell.setCellStyle(styles.get("header"));
        sheet.setColumnWidth(col++, 256*16);
		
		cell = row.createCell(col);
        cell.setCellValue("Bet");
        cell.setCellStyle(styles.get("header"));
        sheet.setColumnWidth(col++, 256*16);
        
        cell = row.createCell(col);
        cell.setCellValue("Win");
        cell.setCellStyle(styles.get("header"));
        sheet.setColumnWidth(col++, 256*16);
        
		int firstRom = rownum;
		for (Date date : tws.keySet())
		{
			StakeWin sq = tws.get(date);
			row = sheet.createRow(rownum++);
			col = 6;
			
			cell = row.createCell(col);
	        cell.setCellValue(date);
	        cell.setCellStyle(styles.get("cell_normal_date"));
	        sheet.setColumnWidth(col++, 256*16);
			
			cell = row.createCell(col);
	        cell.setCellValue(sq.getStake());
	        cell.setCellStyle(styles.get("cell_score"));
	        sheet.setColumnWidth(col++, 256*16);
	        
	        cell = row.createCell(col);
	        cell.setCellValue(sq.getWin());
	        cell.setCellStyle(styles.get("cell_score"));
	        sheet.setColumnWidth(col++, 256*16);
		}

		col = 7;
		row = sheet.createRow(rownum);
		for (char colchar = 'H'; colchar<='I'; colchar++)
		{
    		cell = row.createCell(col);
    		String strFormula= "sum("+colchar+""+firstRom+":"+colchar+""+row.getRowNum()+")";
            cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            cell.setCellStyle(styles.get("cell_score"));
            cell.setCellFormula(strFormula);
            sheet.setColumnWidth(col++, 256*16);
    	}
	}

	private List<Dx4BetRollup> getRollupTotalsForProvider(Dx4PlayGame playGame,int digits,Dx4ProviderJson prov)
	{
		List<Dx4BetRollup> rollups = dx4Home.getTotalBetRollups(playGame.getId(), digits, prov.getCode());
		createWinNumberMap(playGame,prov);
		applyWinNumbers(rollups);
		applyFxRate(rollups);
		return rollups;
	}

	private void applyWinNumbers(List<Dx4BetRollup> rollups) {
	    
		for (Dx4BetRollup ru : rollups)
		{
			WinNumber wn = winNumberMap.get(WinNumber.createKey(ru.getNumber(), ru.getGameId()));
			if (wn==null)
				ru.setWin(0.0);
			else
				ru.setWin(wn.getWin());
		}
	}

	private List<Dx4BetRollup> getRollupTotals(Dx4PlayGame playGame,int digits)
	{
		Map<String,Dx4BetRollup> rollups = new TreeMap<String,Dx4BetRollup>();
		for (Dx4ProviderJson prov : dx4Home.getProviders())
		{
			if (playGame.getProviderCodes().indexOf(prov.getCode())<0)
				continue;
			
			addInProviderRollups(prov,playGame,digits,rollups);
		}
		List<Dx4BetRollup> rlist = new ArrayList<Dx4BetRollup>();
		for (Dx4BetRollup ru : rollups.values())
			rlist.add(ru);
		return rlist;
	}
	
	private void addInProviderRollups(Dx4ProviderJson prov,Dx4PlayGame playGame,int digits,Map<String,Dx4BetRollup> rollups)
	{
		List<Dx4BetRollup> prollups = getRollupTotalsForProvider(playGame,digits,prov);
		for (Dx4BetRollup ru : prollups)
		{
	//		if (ru.getGameId()==8)
	//			log.info("found");
			
			String key = WinNumber.createKey(ru.getNumber(),ru.getGameId());
			Dx4BetRollup mru = rollups.get(key);
			if (mru==null)
				rollups.put(key,ru);
			else
			{
				mru.setStake(mru.getStake()+ru.getStake());
				mru.setWin(mru.getWin()+ru.getWin());
			}
		}
	}
	
	private void applyFxRate(List<Dx4BetRollup> rollups)
	{
		if (fxRate.getRate()==1.0)
			return;
		
		for (Dx4BetRollup ru : rollups)
		{
			ru.setStake(Math.round((ru.getStake()*fxRate.getRate())+0.5));			// round up
			ru.setWin(Math.round((ru.getWin()*fxRate.getRate())+0.5));
		}
	}
	
	private void writeTotals(int rownum)
	{
		Row row;
	    Cell cell;
		row = sheet.createRow(rownum);
		int col = 0;
		
		cell = row.createCell(col);
        cell.setCellValue("Grand Total");
        cell.setCellStyle(styles.get("cell_normal"));
        sheet.setColumnWidth(col++, 256*16);
        
        int columnNum = games.size();
        if (win)
        	columnNum *= 2;
        for (int i=0; i<columnNum; i++)
        {
    		cell = row.createCell(col);
    		char colChar = (char) ('A' + col);
    		String colStr = String.valueOf(colChar);
            String strFormula= "sum("+colStr+"3:"+colStr+row.getRowNum()+")";
            cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            cell.setCellStyle(styles.get("cell_score"));
            cell.setCellFormula(strFormula);
            sheet.setColumnWidth(col++, 256*16);
    	}
       
        
        for (int i=0; i<2; i++)
        {
	        cell = row.createCell(col);
			char colChar = (char) ('A' + col);
			String colStr = String.valueOf(colChar);
	        String strFormula= "sum("+colStr+"3:"+colStr+row.getRowNum()+")";
	        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
	        cell.setCellStyle(styles.get("cell_score"));
	        cell.setCellFormula(strFormula);
	        sheet.setColumnWidth(col++, 256*16);
	        
	        if (!win)
	        	return;
        }
        
	}

	private void createSSRow(List<Dx4BetRollup> sameNumberRollups,int rownum) {
		
		Row row;
	    Cell cell;
		row = sheet.createRow(rownum);
		int col = 0;
		
		cell = row.createCell(col);
        cell.setCellValue(sameNumberRollups.get(0).getNumber());
        cell.setCellStyle(styles.get("cell_normal"));
        sheet.setColumnWidth(col++, 256*16);
        
        String stakeStr = "";
        String winStr = "";
        int cnt = 0;
        int factor = 1;
        if (win)
        	factor = 2;
        for (Dx4Game game : games)
        {
        	StakeWin sw = getRollupForGame(sameNumberRollups,game);
        	stakeStr += Character.toString((char) ('B' + (cnt*factor)));
        	winStr += Character.toString((char) ('C' + (cnt*factor)));
        	cnt++;
        	setCellRM(row,col,sw);
        	col+=factor;
        }
        
        cell = row.createCell(col);
        String strFormula="";
		for (int i=0; i<stakeStr.length(); i++)
			strFormula += stakeStr.substring(i,i+1) + Integer.toString(row.getRowNum()+1) + "+";
        
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        cell.setCellStyle(styles.get("cell_score"));
        cell.setCellFormula(strFormula.substring(0,strFormula.length()-1));
        sheet.setColumnWidth(col++, 256*16);
        
        if (!win)
        	return;
        
        cell = row.createCell(col);
        strFormula="";
		for (int i=0; i<winStr.length(); i++)
			strFormula += winStr.substring(i,i+1) + Integer.toString(row.getRowNum()+1) + "+";
        
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        cell.setCellStyle(styles.get("cell_score"));
        cell.setCellFormula(strFormula.substring(0,strFormula.length()-1));
        sheet.setColumnWidth(col++, 256*16);
	}

	private void setCellRM(Row row,int col,StakeWin sw) {
		Cell cell = row.createCell(col);
		cell.setCellStyle(styles.get("cell_score"));
        cell.setCellValue(sw.getStake());
        sheet.setColumnWidth(col++, 256*16);
        if (!win)
        	return;
        
        cell = row.createCell(col);
		cell.setCellStyle(styles.get("cell_score"));
        cell.setCellValue(sw.getWin());
        sheet.setColumnWidth(col, 256*16);
	}

	private StakeWin getRollupForGame(List<Dx4BetRollup> sameNumberRollups, Dx4Game game) {
		
		StakeWin sw = new StakeWin(0,0);
		for (Dx4BetRollup rollup : sameNumberRollups)
		{
			if (rollup.getGameId()==game.getId())
			{
				sw.setStake(rollup.getStake());
				sw.setWin(rollup.getWin());
				break;
			}
		}
		return sw;
	}

	void initializeSheet(Sheet sheet, Map<String, CellStyle> styles,String[] titles, Dx4ProviderJson provider, String date)
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
/*	        Cell cell = headerRow.createCell(0);
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
 */
	        Cell cell = headerRow.createCell(0);
	        if (provider == null)
	        	cell.setCellValue("All Providers");
	        else
	        	cell.setCellValue(provider.getName());
            cell.setCellStyle(styles.get("cell_time_date"));
            sheet.setColumnWidth(0, 256*16);
	        
	        cell = headerRow.createCell(1);
            cell.setCellValue(date);
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
	            	sheet.setColumnWidth(col++, 256*16);
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
				if (win)
					ts.add("WIN");
				games.add(game);
			}
		}
		
		ts.add("Total Bet");
		if (win)
			ts.add("Total Win");
		String [] titles = new String [ts.size()];
		return ts.toArray(titles);
	}

	public Dx4MetaGame getMetaGame() {
		return metaGame;
	}

	public void setMetaGame(Dx4MetaGame metaGame) {
		this.metaGame = metaGame;
	}
	
	public FxRate getFxRate() {
		return fxRate;
	}

	public void setFxRate(FxRate fxRate) {
		this.fxRate = fxRate;
	}

	public String getWorkbookPath() {
		return workbookPath;
	}

	public void setWorkbookPath(String workbookPath) {
		this.workbookPath = workbookPath;
	}

	public void setDx4Home(Dx4Home dx4Home) {
		this.dx4Home = dx4Home;
	}

	public boolean isWin() {
		return win;
	}

	public void setWin(boolean win) {
		this.win = win;
	}

	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("Dx4-service.xml");
		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");
		
		Dx4Home dx4Home = dx4Services.getDx4Home();
		Dx4MetaGame metaGame = dx4Home.getMetaGame("4D With ABC");
		Dx4PlayGame playGame = metaGame.getPlayGameById(13);
		
		FxRate fxRate = new FxRate();
		fxRate.setRate(4.29);
		try {
			for (Dx4ProviderJson provider : dx4Home.getProviders())
			{
				new Dx4WinRollupUSDMYR(metaGame,playGame,dx4Home,provider,fxRate,false);
			}
			new Dx4WinRollupUSDMYR(metaGame,playGame,dx4Home,null,fxRate,false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			for (Dx4ProviderJson provider : dx4Home.getProviders())
			{
				new Dx4WinRollupUSDMYR(metaGame,playGame,dx4Home,provider,fxRate,true);
			}
			new Dx4WinRollupUSDMYR(metaGame,playGame,dx4Home,null,fxRate,true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
