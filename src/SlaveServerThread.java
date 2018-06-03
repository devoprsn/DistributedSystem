import java.io.*;
import java.net.*;
import java.util.LinkedList;

public class SlaveServerThread extends Thread{

	private int id;
	private String IPAddress;
	private int portNumber;
	private SlaveThreadedServer myThreadedServerBoss;
	private PrintWriter outputStream;
	BufferedReader requestReader;
	
	public SlaveServerThread(String IPAddress, int portNumber, int id, SlaveThreadedServer parent)
	{
		this.id = id;
		this.IPAddress = IPAddress;
		this.portNumber = portNumber;	
		this.myThreadedServerBoss = parent;
	}
	

	@Override
	public void run() 
	{					
		//set up connection
		try(Socket slaveSocket = new Socket(IPAddress, portNumber);
			//PrintWriter outputStream = new  PrintWriter(slaveSocket.getOutputStream(), true);
			//BufferedReader requestReader = new BufferedReader(new InputStreamReader(slaveSocket.getInputStream()));
				)				
		{	
			outputStream = new  PrintWriter(slaveSocket.getOutputStream(), true);
			requestReader = new BufferedReader(new InputStreamReader(slaveSocket.getInputStream()));
			System.out.println("SlaveServerThread initialized"); //println for testing
			
			String msg;
			while((msg = requestReader.readLine()) != null)
			{
				if(msg.equals("Done"))
				{
					myThreadedServerBoss.slaveDoneMessage(id);
					System.out.println("SlaveServerThread" + id + ": notified threadedServer that slave is done!");
				}
			}
		}
		catch(UnknownHostException e)
		{
			e.printStackTrace();
		}
				
		 catch (IOException f) 
		{
			System.out.println(f.getMessage());
		}
	}	
	
	public int getID()
	{
		return id;
	}
	
	public void addJob(String job)
	{
		outputStream.println("Job");
		System.out.println("Slave Thread "+id+" added job");
	}
	
	public int getDuration()
	{
		outputStream.println("Duration");
		String msg;
		try {
			while((msg = requestReader.readLine()) != null)
			{
				return Integer.parseInt(msg);
			}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return 0;
	}
}
