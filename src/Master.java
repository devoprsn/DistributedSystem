
import java.util.LinkedList;

public class Master 
{
   public static void main(String [] args)
   {
	   LinkedList<String> jobs = new LinkedList<String>();
	   
	   //maybe give each slaves ipaddress and port # (all localhost for now but diff. port - so we can test on our machines
	   String[] iPAddresses = {"127.0.0.1", "127.0.0.1","127.0.0.1","127.0.0.1"};
	   int [] portNumbers = {40121,40123,40124,40125};
	   
	   
	   
	   //master has a thread to communicate with the clients who give in jobs and a thread to communicate with its slaves who do the work
	   
	   Thread clientServerThread = new Thread(new ClientThreadedServer(40121, jobs));  //clients
	   Thread slaveServerthread = new Thread(new SlaveThreadedServer(iPAddresses, portNumbers, jobs));  //slaves
   }
}
