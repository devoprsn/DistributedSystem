import java.io.IOException;
import java.net.ServerSocket;

public class ThreadedServer implements Runnable{
	
	int portNumber;
	public ThreadedServer(int portNumber)
	{
		this.portNumber = portNumber;
	}
	
	@Override
	public void run() 
	{
		try (ServerSocket serverSocket = new ServerSocket(portNumber);) {
			
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
