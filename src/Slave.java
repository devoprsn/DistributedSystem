import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

//where will we put the code for the slave to actually carry out the work - the slave itself may need a worker thread to work while communicating...

public class Slave {

	private static IRandomValueGenerator rand;

	
	public Slave(int portNumber, IRandomValueGenerator rand)
	{
		this.rand = rand;
	}
	
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
			
		LinkedList<Job> tasks = new LinkedList<Job>();
		try (Socket clientSocket = serverSocket.accept();
					PrintWriter responseWriter = new PrintWriter(clientSocket.getOutputStream(), true);
					BufferedReader requestReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) 
		{
			String jobRequest;
			while ((jobRequest = requestReader.readLine()) != null) //really needs to receive a job
			{				
				Job job = new Job(rand);
				tasks.add(job);
					
				
			}
			} 
			catch (IOException e) {
				System.out.println(
						"Exception caught when trying to listen on port " + serverSocket.getLocalPort() + " or listening for a connection");
				System.out.println(e.getMessage());
			}
	}
}
