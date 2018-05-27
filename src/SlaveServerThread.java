import java.io.*;
import java.net.*;
import java.util.LinkedList;

public class SlaveServerThread extends Thread{

	private int id;
	private String IPAddress;
	private int portNumber;
	private LinkedList<String> jobs;
	private SlaveThreadedServer myThreadedServerBoss;
	private PrintWriter outputStream;
	
	public SlaveServerThread(String IPAddress, int portNumber, int id, SlaveThreadedServer parent)
	{
		this.id = id;
		this.IPAddress = IPAddress;
		this.portNumber = portNumber;	
		this.myThreadedServerBoss = parent;
		this.jobs = new LinkedList<String>();
	}
	

	@Override
	public void run() 
	{					
		//set up connection
		try(Socket slaveSocket = new Socket(IPAddress, portNumber);
			//PrintWriter outputStream = new  PrintWriter(slaveSocket.getOutputStream(), true);
			BufferedReader requestReader = new BufferedReader(new InputStreamReader(slaveSocket.getInputStream()));
				)				
		{	
			outputStream = new  PrintWriter(slaveSocket.getOutputStream(), true);
			System.out.println("SlaveServerThread initialized"); //println for testing
			
			String done;
			while((done = requestReader.readLine()) != null)
			{
				myThreadedServerBoss.slaveDoneMessage(id);
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
		jobs.add(job);
		outputStream.println(job);
		System.out.println("Slave Thread "+id+" added job");
	}
}
