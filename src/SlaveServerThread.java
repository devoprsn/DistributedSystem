import java.io.*;
import java.net.*;

public class SlaveServerThread extends Thread{

	private String IPAddress;
	private int portNumber;
	private int id;
	
	private String currentJob;
	
	public SlaveServerThread(String IPAddress, int portNumber, int id)
	{
		this.IPAddress = IPAddress;
		this.portNumber = portNumber;
		this.id =id;
	}

	@Override
	public void run() 
	{
		try(Socket slaveSocket = new Socket(IPAddress, portNumber);
			PrintWriter outputStream =new  PrintWriter(slaveSocket.getOutputStream(), true);
			BufferedReader requestReader = new BufferedReader(new InputStreamReader(slaveSocket.getInputStream())))				
		{	
			while(currentJob != null)
			{
				outputStream.write(currentJob);
			}
		}
		catch(UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{			
			e.printStackTrace();
		}
		
	}
	
	
	
	
	public void addJob(String job)
	{
		this.currentJob = job;
		
	}
	
	public int getID()
	{
		return id;
	}
}
