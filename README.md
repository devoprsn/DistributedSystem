# DistributedSystem

Distributed System Project

Project Design: 
Our task was to implement a simulation of a system where jobs are completed by multiple computers and the main thing was to ensure that jobs were distributed efficiently and fairly. 
The master computer has two aspects. The first is  the client aspect –  it receives requests from clients to complete jobs and for each job it sends a message to the client that the job was received. The second aspect is the slave aspect – it sends jobs to the slaves to complete, and then receives a message from the slave when it’s done. 
A job's duration is set by probability using a Random generator. 
The slave aspect of the master keeps track of the working slaves and the idle slaves. All slaves initially start off as idle slaves. Each time a job is sent to the master, if there are any slaves in the idle queue, it sends the job to the first computer in the queue, and transfers that slave from the idle queue to the working queue. Otherwise, it gives the job to the first computer in the working queue and that computer is then moved to the end of the working queue.
When the slave sends a message to the master that it has completed all the tasks in its task queue, the master  then places it on the queue of idle slaves. Now, it will receive the new jobs that arrive.
The master has a thread that is in charge of redistributing the jobs at given intervals to make sure that one slave isn’t working much harder than the other slaves. It checks if there are idle slaves. If there are none, it sleeps for some time and checks again. If there are idle slaves, it checks for working slaves. If there are none, it sleeps for some time and begins looking again for idle slaves.  If there are idle slaves and working slaves: it looks for  the working slave with the longest total duration until completion of all its tasks. If the greatest working slave has a duration left of greater than zero seconds it will now assess if the tasks need to be divided up. It will only divide up the tasks it has left if it has a certain minimum amount of jobs left (we set the minimum to 2 tasks). The master will then take 1/2 of the tasks and give it to the next idle slave. The idle slave will then be moved to the list of working slaves.

Implementation: 
We created a Master with a main() that instantiates 2 Threaded Servers - ClientThreadedServer and SlaveThreadedServer: one to communicate with the clients, and one to communicate with the slaves, respectively. The way they communicate with each other is through a shared jobs list. This shared jobs list needs to be synchronized when reading  from it or writing to it. ClientThreadedServer is a thread that establishes a server socket and passes a reference to it to each ClientServerThread that it instantiates. Each Client has a main() that establishes a client socket and sends jobs as a string of text to its corresponding ClientServerThread. The ClientServerThread adds the jobs to the global jobs list. The SlaveThreadedServer creates a few SlaveServerThreads – each one creates a client socket and communicates with one specific Slave. (We decided to have the SlaveServerThread establish a client socket because it is requesting the slave to complete a job.) The SlaveThreadedServer is in charge of instantiating all the SlaveServerThreads and groups them by working or idle . It keeps checking the global jobs list and if there available jobs it gives them out and moves the slaves around as needed. We also created a Slave that has a main() where it instantiates a server socket that enables it to communicate with the SlaveServerThread that it is assigned to. Each Slave has its own internal tasks list. When  a Slave receives a job from its SlaveServerThread it will instantiate a Job object that has a duration using probability and add it to its internal task list. The Slave has a separate thread called the SlaveTaskThread that simulates the actual work getting done. It also accesses the Slave’s internal task list, which requires all reads and writes to the Slave’s internal task list to be synchronized. The SlaveTaskThread removes each successive job from the Slave’s task list and sleeps for the duration of each job. If all jobs are complete, it sends a message to the Slave’s SlaveServerThread. The SlaveServerThread will call its boss – the SlaveThreadedServer - who will move that slave from the working queue to the idle queue.
 Lastly, we worked on the redistributing aspect. The RedistributingThread is instantiated in Master and accesses SlaveThreadedServer’s list of idle slaves and working slaves via getters. This required all reads and writes to the lists of working and idle SlaveServerThreads to be synchronized. The RedistributingThread sleeps for a few seconds and then begins to check for at least one idle and one working slave. If it finds both, it continues with the algorithm, otherwise it sleeps and then starts to checks all over again. The algorithm requires lots of communication between the RedistributingThread, the SlaveServerThreads, and the Slaves. To ensure that the messages would get through to the right place, we created the RedistributingObject. It has getters and setters for  the values we need to obtain: duration of all tasks for each Slave, number of task left for Slave with longest total duration, and the duration of a removed task so the idle Slave that the task is being redistributed to  can know what to do with it.  There is one RedistributingObject for each slave -  SlaveThreadedServer sends a new instance of RedistributingObject to each SlaveServerThread as it’s created. As the RedistributingThread needs a value, it calls a method of the SlaveServerThread. Then, the SlaveServerThread send the command to the Slave. When it receives the Slave’s reply, it updates its RedistributingObject. The RedistributingThread  reads the values needed from the particular RedistributingObject, then decides if it needs to redistribute or not. Since there are different messages being sent over the socket between the SlaveServerThread and its corresponding Slave, we used the first few characters as a “code” to know what value is being requested or sent. ex. Message starting with “cou” for count means that the slave is sending the count of all tasks left. 
