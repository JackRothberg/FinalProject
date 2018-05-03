package projectview;

import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class ControlPanel implements Observer {
	private ViewMediator view;
	private JButton stepButton = new JButton("Step");
	private JButton clearButton = new JButton("Clear");
	private JButton runButton = new JButton("Run");
	private JButton reloadButton = new JButton("Reload");
	public ControlPanel(ViewMediator gui) { 
	     view = gui; gui.addObserver(this); 
	}
	public JComponent createControlDisplay() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,4));
		panel.add(stepButton);
		panel.add(clearButton);
		panel.add(runButton);
		panel.add(reloadButton);
		stepButton.addActionListener(e -> view.step());
		clearButton.addActionListener(e -> view.clearJob());
		runButton.addActionListener(e -> view.toggleAutoStep());
		reloadButton.addActionListener(e -> view.reload());
		JSlider slider = new JSlider(5,1000);
		slider.addChangeListener(e -> view.setPeriod(slider.getValue()));
		panel.add(slider);
		return panel;
	}
	public void update(Observable arg0, Object arg1) {
		stepButton.setEnabled(view.getCurrentState().getStepActive());
		clearButton.setEnabled(view.getCurrentState().getClearActive());
		runButton.setEnabled(view.getCurrentState().getRunPauseActive());
		reloadButton.setEnabled(view.getCurrentState().getReloadActive());
	}
}
