package org.dx4.secure.web.pdf;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dx4.secure.domain.Dx4SecureUser;
import org.dx4.secure.web.Dx4ExceptionFatal;
import org.dx4.services.Dx4Config;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFStoreMetaBet {

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(PDFStoreMetaBet.class);
	private String pdfPath;
	private String destEmail;
	
	public PDFStoreMetaBet(Map<String, Object> model, Dx4SecureUser from, Dx4SecureUser to ) throws Exception
	{
		pdfPath = Dx4Config.getProperties().getProperty("dx4.pdfPath");
		if (pdfPath==null)
			throw new Dx4ExceptionFatal("Couldn't format pdf doc - pdf path not set in config");
		
		setDestEmail(to.getEmail());
		
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat df1 = new SimpleDateFormat("yyMMddhhmmss");
		String dStr = df1.format(gc.getTime());
		
		pdfPath = pdfPath.trim() + "MetaBet_"  + from.getCode() + "_" + to.getCode() 
					+ "_" + dStr + ".pdf";
		Document doc=new Document();
		PdfWriter.getInstance(doc,new FileOutputStream(pdfPath));
		doc.open(); 
		PDFCreateTransaction.buildPdfMetaBetDocument(model, doc);
		doc.close();	
	}

	public String getPdfPath() {
		return pdfPath;
	}

	public void setPdfPath(String pdfPath) {
		this.pdfPath = pdfPath;
	}

	public void setDestEmail(String destEmail) {
		this.destEmail = destEmail;
	}

	public String getDestEmail() {
		return destEmail;
	}

	
}