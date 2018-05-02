package projectview;

import javax.swing.Timer;

public class Animator {
	private static final int TICK = 500;
	private boolean autoStepOn = false;
	Timer timer;
	ViewMediator view;
	
	public Animator(ViewMediator view) {
		this.view = view;
	}

	public boolean isAutoStepOn() {
		return autoStepOn;
	}

	public void setAutoStepOn(boolean autoStepOn) {
		this.autoStepOn = autoStepOn;
	}
	
	void toggleAutoStep() {
		if (autoStepOn == false) {
			autoStepOn = true;
		} else {
			autoStepOn = false;
		}
	}
	
	void setPeriod(int period) {
		timer.setDelay(period);
	}
	
	public void start() {
		timer = new Timer(TICK, e -> {if(autoStepOn) view.step();});
		timer.start();
	}
	
}
