
import java.util.LinkedList;

public class Master 
{
   public static void main(String [] args) throws InterruptedException
   {
	   LinkedList<String> jobs = new LinkedList<String>();
	   
	   //maybe give each slaves ipaddress and port # (all localhost for now but diff. port - so we can test on our machines)
	   String[] iPAddresses = {"127.0.0.1", "127.0.0.1","127.0.0.1","127.0.0.1"};
	   int [] portNumbers = {40121,40123,40124,40125};
	   
	   System.out.println("Master is running!");
	   
	   //master has a thread to communicate with the clients who give in jobs and a thread to communicate with its slaves who do the work

	   Thread clientThreadedServer = new ClientThreadedServer(30121, jobs);  //clients
	   clientThreadedServer.start();	
		
	   SlaveThreadedServer slaveThreadedServer = new SlaveThreadedServer(jobs, iPAddresses, portNumbers);
	   slaveThreadedServer.start();
	  	
	   try {
	 	clientThreadedServer.join();
		slaveThreadedServer.join();
	   } 
	   catch (InterruptedException e)
	   {
		   System.out.println("Error: ThreadedServers coudn't join.");
	   	e.printStackTrace();
	   }		
	   //program ends	   

   }
}
