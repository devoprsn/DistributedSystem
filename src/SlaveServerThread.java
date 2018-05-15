import java.io.*;
import java.net.*;

public class SlaveServerThread extends Thread{

	private String IPAddress;
	private int portNumber;
	private int id;
	private String currentJob;
	private PrintWriter outputStream;
	private BufferedReader requestReader;
	
	public SlaveServerThread(String IPAddress, int portNumber, int id)
	{
		this.IPAddress = IPAddress;
		this.portNumber = portNumber;
		this.id =id;
		
		try(Socket slaveSocket = new Socket(IPAddress, portNumber);
				)				
			{	
				outputStream =new  PrintWriter(slaveSocket.getOutputStream(), true);
				requestReader = new BufferedReader(new InputStreamReader(slaveSocket.getInputStream()));
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

	@Override
	public void run() 
	{					
		
	}	
	
	
	public void addJob(String job)
	{
		this.currentJob = job;
		outputStream.write(currentJob);
	}
	
	public int getID()
	{
		return id;
	}
}
