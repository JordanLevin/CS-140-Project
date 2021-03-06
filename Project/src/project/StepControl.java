package project;

import javax.swing.Timer;

public class StepControl {
	private static final int TICK = 500;
	private boolean autoStepOn = false;
	Timer timer;
	GUIMediator gui;
	
	public StepControl(GUIMediator gui){
		this.gui = gui;
	}

	public boolean isAutoStepOn() {
		return autoStepOn;
	}

	public void setAutoStepOn(boolean autoStepOn) {
		this.autoStepOn = autoStepOn;
	}
	
	public void toggleAutoStep(){
		autoStepOn = !autoStepOn;
	}
	
	public void setPeriod(int period){
		timer.setDelay(period);
	}
	
	public void start() {
		timer = new Timer(TICK, e -> {if(autoStepOn) gui.step();});
		timer.start();
	}
	
	

}
