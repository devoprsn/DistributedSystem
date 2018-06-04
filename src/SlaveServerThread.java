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
	
	
	public void addJobWithDuration(int duration)
	{
		outputStream.println("JobSet" + duration);
		System.out.println("Slave Thread "+id+" added job with duration");
		
	}

	//removes the job but returns its duration
	public int removeJob()
	{
		outputStream.println("Remove");
        String msg;
		
		try {
			while((msg = requestReader.readLine()) != null)  //now parse the slave's response
			{
				//check to see if the right message was received - begins with "rem" for remove
				
				if(msg.substring(0,3).equals("rem"))  
				{
					return Integer.parseInt(msg.substring(3));
				}
				
				
			}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return 0;
	}
	
	
	
	public int getCountOfTasks()
	{
		outputStream.println("Count");     //request slave to send the duration of all its tasks
        String msg;
		
		try {
			while((msg = requestReader.readLine()) != null)  //now parse the slave's response
			{
				//check to see if the right message was received - begins with "cou" for count
				
				if(msg.substring(0,3).equals("cou"))  
				{
					return Integer.parseInt(msg.substring(3));
				}
				
				
			}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return 0;
		
	}
	
	
	
	public int getTotalDurationOfAllTasks()
	{
		
		
		outputStream.println("Duration");  //request slave to send the duration of all its tasks
		String msg;
		
		try {
			while((msg = requestReader.readLine()) != null)  //now parse the slave's response
			{
                //check to see if the right message was received - begins with "dur" for duration
				
				if(msg.substring(0,3).equals("dur"))  
				{
					return Integer.parseInt(msg.substring(3));
				}
				
				
			}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return 0;
	}
	
//method added so redistributing thread can use a slaveserverthread to communicate with threadedserver to move around the slaves
	public SlaveThreadedServer getMyThreadedServerBoss()
	{
		return myThreadedServerBoss;
	}
}
