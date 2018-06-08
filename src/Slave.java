import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

//here the slave has a serverSocket that communicates with its SlaveServerThread's clientSocket

public class Slave {
	
	private static LinkedList<Job> tasks;
	
	public static void main(String[] args)
	{
		if (args.length != 1) {
			System.err.println("Usage: java EchoServer <port number>");
			System.exit(1);
		}	

		int portNumber = Integer.parseInt(args[0]);
		ServerSocket serverSocket = null;
		try{
			serverSocket = new ServerSocket(portNumber);
		}
		catch(IOException e)
		{
		        System.out.println(e.getMessage());
		}

		System.out.println("Slave initialized"); //println for testing	
		tasks = new LinkedList<Job>();	
		
		try (Socket clientSocket = serverSocket.accept();
					PrintWriter responseWriter = new PrintWriter(clientSocket.getOutputStream(), true);
					BufferedReader requestReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) 
		{
			String msg;
			
			SlaveTaskThread taskThread = new SlaveTaskThread(tasks,responseWriter);           
			taskThread.start();
			
			//slave can receive different messages:
			while ((msg = requestReader.readLine()) != null)
			{			
				if(msg.equals("Job")) {   //new job sent
					Job job = new Job();
					System.out.println("Slave: jobRequest- " + msg);
					
	                synchronized(tasks)
	                {
	                	tasks.add(job);	
	                }	
				}				
				else if(msg.length() >= 6 && msg.substring(0,6).equals("SetJob"))  //job with set duration has been sent
				{
					Job job = new Job(Integer.parseInt(msg.substring(6)));
					System.out.println("Slave: jobRequest- " + msg);
					
					 synchronized(tasks)
		             {
		                tasks.add(job);	
		             }	
				}				
				else if(msg.equals("Duration"))	{  //return the duration
					System.out.println("Slave: jobRequest- " + msg);
					responseWriter.println("dur" + durationOfAll());					
				}			
				else if (msg.equals("Count")) //return how many task there are:
				{
					System.out.println("Slave: jobRequest- " + msg);
					
					Integer numTasks = 0;
					
					synchronized(tasks)
					{
						numTasks = tasks.size();
					}
					
					responseWriter.println("cou"+numTasks);
				}
				
				else if(msg.equals("Remove")) //remove last job from list of tasks and return the duration so can be redistributed
				{
					System.out.println("Slave: jobRequest- " + msg);
					
					int duration = 0;
					synchronized(tasks)
					{
						duration = tasks.removeLast().getDuration();
					}
					
					responseWriter.println("rem" + duration);
				}
			}
	
			taskThread.join();
		} 
		catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + serverSocket.getLocalPort() + " or listening for a connection");
			System.out.println(e.getMessage());
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}		
		
	}
	
	public static int durationOfAll()
	{
		int milliseconds = 0;
		synchronized(tasks)
		{
			for(Job job : tasks)
			{
				milliseconds += job.getDuration();
			}
		}		
		
		return milliseconds;
	}
}
