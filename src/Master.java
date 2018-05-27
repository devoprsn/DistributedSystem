
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
	   
	   Thread clientServerThread = new Thread(new ClientThreadedServer(40121, jobs));  //clients
	   clientServerThread.start();
	   clientServerThread.join();
	  
	   System.out.println("ClientThreadedServer set up!");
	   
	   Thread slaveServerthread = new Thread(new SlaveThreadedServer(jobs,iPAddresses, portNumbers));  //slaves
	   slaveServerthread.start();
	   slaveServerthread.join();
	   
	   
	   
   }
}
