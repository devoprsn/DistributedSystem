import java.io.*;
import java.net.*;

public class SlaveServerThread implements Runnable{

	String IPAddress;
	int portNumber;
	String currentJob;
	public SlaveServerThread(String IPAddress, int portNumber)
	{
		this.IPAddress = IPAddress;
		this.portNumber = portNumber;
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
}
