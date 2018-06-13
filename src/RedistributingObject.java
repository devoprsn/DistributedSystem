
public class RedistributingObject {

	private int totalDuration; //duration for all tasks in a Slave
	private int numTasksLeft; //number of tasks in a Slave 
	private int durationOfRemovedTask;
	
	public RedistributingObject()
	{
		totalDuration = -1;
		numTasksLeft = -1;
		durationOfRemovedTask = -1;
	}

	public int getTotalDuration() {
		return (totalDuration / 1000);
	}

	public void setTotalDuration(int totalDuration) {
		this.totalDuration = totalDuration;
	}

	public int getNumTasksLeft() {
		return numTasksLeft;
	}

	public void setNumTasksLeft(int numTasksLeft) {
		this.numTasksLeft = numTasksLeft;
	}

	public int getDurationOfRemovedTask() {
		return durationOfRemovedTask;
	}

	public void setDurationOfRemovedTask(int durationOfRemovedTask) {
		this.durationOfRemovedTask = durationOfRemovedTask;
	}
	
	
}
