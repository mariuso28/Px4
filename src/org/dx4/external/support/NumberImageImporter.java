package org.dx4.external.support;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

public class NumberImageImporter {

	private static final Logger log = Logger.getLogger(NumberImageImporter.class);

	private URL url;
	private String sourcePath;
	private String destPath;

	
	public NumberImageImporter( String sourcePath, String destPath ) throws Exception
	{
		setSourcePath( sourcePath );
		setDestPath( destPath );
		log.info("Reading URL [" + sourcePath + "]" );
		url = new URL( sourcePath );
		connectUrl();
	}

	private void connectUrl() throws Exception
	{
		while (true)
		{
			int backoff = 1000;
			try
			{
				URLConnection connection  = url.openConnection();
				connection.setRequestProperty("User-Agent", "Mozilla/5.0");
				FileOutputStream output = new FileOutputStream(destPath);
				xferData(connection.getInputStream(),output);
				break;
			}
			catch (IOException e)
			{
				log.warn( "Couldn't connect - backing off: " + backoff );
				Thread.sleep( backoff );
				backoff+=backoff;
			}
		}
	}
	
	private void xferData(InputStream ins,FileOutputStream output) throws Exception
	{
		int expectedDataLength = 128; 
	    byte[] chunk = new byte[expectedDataLength];
	    int numBytesJustRead;
	    while((numBytesJustRead = ins.read(chunk)) != -1) {
	            log.trace("Read : " + numBytesJustRead);
	            output.write(chunk, 0, numBytesJustRead);
	            log.trace("Wrote : " + numBytesJustRead);
	      }
	    output.close();
	}
	
	
	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public String getDestPath() {
		return destPath;
	}

	public void setDestPath(String destPath) {
		this.destPath = destPath;
	}

	public static void main(String args[])
	{
		try
		{
			for (int i=1000; i<10000; i++)
			{
				String num = Integer.toString(i);
				while (num.length()<4)						// 4 digits for modern
					num = "0" + num;
				log.info("Extracting : " + num);
				
				//http://www.4dmanager.com/repo/qzt/tpk/001.png?tpk
				// http://www.4dmanager.com/repo/qzt/gym/002.png?gym  traditional
				// http://www.4dmanager.com/repo/wzt/0005.jpg?wzt modern
				new NumberImageImporter("http://www.4dmanager.com/repo/wzt/" + num + ".jpg",
					"/home/pmk/4DX/number-images-mod/" + num + ".jpeg");	
			}
		}
		catch (Exception e)
		{
			log.fatal(e.getClass().getSimpleName() + " - " + e.getMessage());
		}
	}
	
}
