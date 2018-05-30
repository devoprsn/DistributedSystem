
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.LinkedList;

public class SlaveTaskThread extends Thread{

	private LinkedList<Job> tasks;
	//private PrintWriter responseWriter; //to tell the slaveServerThread that it is done
         private Socket clientSocket;
	
	//public SlaveTaskThread(LinkedList<Job> tasks, PrintWriter responseWriter)
          public SlaveTaskThread(LinkedList<Job> tasks, Socket clienSocket)
	{
		this.tasks = tasks;
		//this.responseWriter = responseWriter;
                  this.clientSocket=clientSocket;
	}
	
	@Override
	public void run() 
	{
		System.out.println("SlaveTaskThread initialized"); //println for testing
           boolean noTasks = true;	
		
		while(true)
		{
			while(tasks.isEmpty());
			
			while(!tasks.isEmpty())
			{	
				try {
					//perform task
					sleep(tasks.getFirst().getDuration());
									
					tasks.removeFirst();
				
				} 
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		
			//no tasks left, notify slaveServerThread
                        System.out.println("SlaveTaskThread: Done!");	
                          
                         try(
					
                               PrintWriter responseWriter = new PrintWriter(clientSocket.getOutputStream(), true);)
                                 {
                               
			
			                responseWriter.println("Done");	
                               
                                  }
                              catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + "__" + " or listening for a connection");
			System.out.println(e.getMessage());
		} 
                 

                
                  	

                        System.out.println("SlaveTaskThread: sent message to slaveserverthread");	
		}
	}
}
