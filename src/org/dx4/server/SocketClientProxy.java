package org.dx4.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;
import org.dx4.event.Dx4BetEvent;
import org.dx4.event.Dx4BetEventException;
import org.dx4.nofiable.Notifiable;


public class SocketClientProxy extends Thread implements Notifiable<Dx4BetEvent>
{
	private static final Logger log = Logger.getLogger(SocketClientProxy.class);

	private BufferedWriter bw;
	private BufferedReader br;
	private Dx4Server server;
	private volatile Thread proxy;
	private long id;

	public SocketClientProxy( Dx4Server server, DataOutputStream out, DataInputStream in )
	{
		this.server = server;
		setId(server.getNextProxyId());
		br = new BufferedReader(new InputStreamReader(in));
		bw = new BufferedWriter(new OutputStreamWriter(out));
	}

	@Override
	public void start() {
		super.start();
	}

	private void stopProxy()
	{
		proxy = null;
	}
	
	@Override
	public synchronized void notify(Dx4BetEvent event) 
	{
		log.debug( "NOTIFIED For " + getId() + " with : " + event );
		write(event);
	}

	@Override
	public void run() {

		Thread thisThread = Thread.currentThread();
		proxy = thisThread;
		char[] cbuf;
		String str;
		int bytes = 0;
		while (proxy == thisThread)
		{
			Dx4BetEvent betEvent = null;
			try
			{
				cbuf = new char[4096];
				bytes = br.read(cbuf);
				log.info("Got : " + bytes + " bytes");
				if (bytes < 0)				// eof
					break;
				str = (new String(cbuf)).trim();
				log.info( "@Event " + str);

				betEvent = Dx4BetEvent.createDx4BetEvent(getId(),str);
				log.debug( "Got from client, Event : " + betEvent );
				
				server.notify(betEvent);
			}
			catch (IOException e)
			{
				log.error( "For " + getId() + " - IOException on : " + betEvent, e );
				break;
			}
			catch (Dx4BetEventException e)
			{
				log.error( "For " + getId() + " - Dx4BetEventException on : " + betEvent, e);
				break;
			}
			catch (NullPointerException e)
			{
				log.error( "For " +getId() + " - NullPointerException on : " + betEvent, e);
				break;
			}

			if (bytes < 0)
				performClosedConnection();
		}
	}

	private void performClosedConnection()
	{
		log.info( "For " + getId() + " - Connection closed");
		try
		{
			br.close();
			bw.close();
		}
		catch (IOException e)
		{
			;
		}
		server.removeClientProxy(getId());
	}

	private void write(Dx4BetEvent event) 
	{
		log.debug( " writing : " + event );
		try
		{
			String str = Dx4BetEvent.getFlatStringBuffer(event).toString();
			bw.write(str.toCharArray());
			bw.flush();
			return;
		}
		catch (IOException e)
		{
			log.error( "For " + getId() + " - Exception writing to client connection", e);
			stopProxy();
		}
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}
}


