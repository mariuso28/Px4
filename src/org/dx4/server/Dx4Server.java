package org.dx4.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.dx4.event.Dx4BetEvent;
import org.dx4.event.Dx4BetEventException;
import org.dx4.nofiable.Notifiable;
import org.dx4.services.Dx4Services;

public class Dx4Server extends Thread implements Notifiable<Dx4BetEvent>{
	private static final Logger log = Logger.getLogger(Dx4Server.class);
	private Dx4Services dx4Services;
	private ServerSocket providerSocket;
	private Socket connection = null;
	protected DataOutputStream out;
	protected DataInputStream in;
	private HashMap<Long,SocketClientProxy> clientProxies;
	private long nextProxyId;
	
	public Dx4Server(int port)
	{	
		setClientProxies(new HashMap<Long,SocketClientProxy>());
		try
		{
//			providerSocket = new ServerSocket(port);
//			start();
		}
		catch (Exception e)
		{
			log.error( "Creating ServerSocket(port)" + e );
			System.exit(-1);
		}
	}

	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				acceptConnection();
			}
			catch (Exception e)
			{
				log.fatal( "acceptConnection()", e );
				break;
			}
		}
		try{
			if (in!=null)
				in.close();
			if (out!=null)
				out.close();
		}
		catch(IOException ioException){
			log.error("Connection close problem on acceptConnection() :",ioException);
		}
	}

	private void acceptConnection() throws Exception
	{
		log.info("Waiting for connection on : " + providerSocket.getLocalPort());
		connection = providerSocket.accept();
		connection.setSoTimeout(1000*60*12);					// 12 minute timeout
		log.info("Connection received from host :" + connection.getInetAddress().getHostName() + " port:" + connection.getPort() + " timeout:" + connection.getSoTimeout() );
		out = new DataOutputStream(connection.getOutputStream());
		out.flush();
		in = new DataInputStream(connection.getInputStream());
		SocketClientProxy clientProxy = new SocketClientProxy(this, out, in);
		clientProxies.put(clientProxy.getId(),clientProxy);
		try
		{
			clientProxy.start();
		}
		catch (Exception e)							// tidy up on player if still about
		{
			log.error( " Unrecoverable error on SocketClientProxy : " + e.getMessage());
			performFailedSocketClientProxy(clientProxy);
		}
	}

	private void performFailedSocketClientProxy(SocketClientProxy clientProxy)
	{
		log.error( "FailedSocketClientProxy For " + clientProxy);
	}

	protected void removeClientProxy(long id)
	{
		log.info("Removing client proxy for : " + id );
		SocketClientProxy clientProxy = getClientProxies().remove(id);
		if (clientProxy==null)
			log.warn("removeClientProxy - proxy : " + id + "  -  not found");
	}
	
	public Dx4Services getDx4Services() {
		return dx4Services;
	}

	public void setDx4Services(Dx4Services dx4Services) {
		this.dx4Services = dx4Services;
	}

	public long getNextProxyId() {
		return nextProxyId++;
	}

	public void setClientProxies(HashMap<Long,SocketClientProxy> clientProxies) {
		this.clientProxies = clientProxies;
	}

	public HashMap<Long,SocketClientProxy> getClientProxies() {
		return clientProxies;
	}

	@Override
	public void notify(Dx4BetEvent event) throws Dx4BetEventException {
		// dx4Services.processEvent(event);
	}

}
