import java.io.*;
import java.net.*;

public class SlaveServerThread extends Thread{

	private String IPAddress;
	private int portNumber;
	private int id;
	private String currentJob;
	private PrintWriter outputStream;
	private BufferedReader requestReader;
	private SlaveThreadedServer myThreadedServerBoss;
	
	public SlaveServerThread(String IPAddress, int portNumber, int id, SlaveThreadedServer myThreadedServerBoss)
	{
		this.IPAddress = IPAddress;
		this.portNumber = portNumber;
		this.id =id;
		this.myThreadedServerBoss=myThreadedServerBoss;
		
		
		//set up connection
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
						
				 catch (IOException f) {
					
					System.out.println(f.getMessage());
				}
		
		
	}
	

	@Override
	public void run() 
	{					
		String slaveDoneMessage;
		
		try			
			{	
			
				//accepts communication from the slaveTaskThread when it completes all its tasks
				while((slaveDoneMessage = requestReader.readLine()) !=null)
				{
					//messages its boss - the threaded server - so it can know that a slave is idle
					myThreadedServerBoss.slaveDoneMessage(getID());
				}
			}
		
		catch(UnknownHostException e)
		{
			e.printStackTrace();
		}
				
		 catch (IOException f) {
			
			System.out.println(f.getMessage());
		}
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
