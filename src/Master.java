package DistributedSystemProject;

import java.util.LinkedList;

public class Master 
{
   public static void main(String [] args)
   {
	   LinkedList<Job> jobs = new LinkedList<Job>();
	   
	   //master has a thread to communicate with the clients who give in jobs and a thread to communicate with its slaves who do the work
	   
	   Thread clientServerThread = new Thread(new ClientThreadedServer(40121, jobs));  //clients
	   Thread slaveServerthread = new Thread(new SlaveThreadedServer());  //slaves
   }
}
