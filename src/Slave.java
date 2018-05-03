import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

//where will we put the code for the slave to actually carry out the work - the slave itself may need a worker thread to work while communicating...

public class Slave {

	private ServerSocket serverSocket;
	private LinkedList<Job> tasks = new LinkedList<Job>();
	
	public Slave(int portNumber)
	{
		try{
		serverSocket = new ServerSocket(portNumber);
		}
		catch(IOException e)
		{
		        System.out.println(e.getMessage());
		}
		

		try (Socket clientSocket = serverSocket.accept();
				PrintWriter responseWriter = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader requestReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) 
		{
			String requestString;
			while ((requestString = requestReader.readLine()) != null) //really needs to receive a job
			{				
				
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
