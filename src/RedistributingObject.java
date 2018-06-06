
public class RedistributingObject {

	private int totalDuration;
	private int numTasksLeft;
	private int durationOfRemovedTask;
	
	public RedistributingObject()
	{
		totalDuration = -1;
		numTasksLeft = -1;
		durationOfRemovedTask = -1;
	}

	public int getTotalDuration() {
		return totalDuration;
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
