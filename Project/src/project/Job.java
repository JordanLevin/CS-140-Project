package project;

public class Job {
	private int id, startcodeIndex, codeSize, startmemoryIndex, currentPC, currentAcc;
	private States currentState = States.NOTHING_LOADED;
	
	void reset(){
		setCodeSize(0);
		currentAcc = 0;
		currentState = States.NOTHING_LOADED;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStartcodeIndex() {
		return startcodeIndex;
	}
	public void setStartcodeIndex(int startcodeIndex) {
		this.startcodeIndex = startcodeIndex;
	}
	public int getStartmemoryIndex() {
		return startmemoryIndex;
	}
	public void setStartmemoryIndex(int startmemoryIndex) {
		this.startmemoryIndex = startmemoryIndex;
	}
	public int getCurrentPC() {
		return currentPC;
	}
	public void setCurrentPC(int currentPC) {
		this.currentPC = currentPC;
	}
	public int getCurrentAcc() {
		return currentAcc;
	}
	public void setCurrentAcc(int currentAcc) {
		this.currentAcc = currentAcc;
	}
	public States getCurrentState() {
		return currentState;
	}
	public void setCurrentState(States currentState) {
		this.currentState = currentState;
	}

	public int getCodeSize() {
		return codeSize;
	}

	public void setCodeSize(int codeSize) {
		this.codeSize = codeSize;
	}
	
	
}
