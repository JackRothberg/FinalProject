package projectview;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.Observable;

import project.CodeAccessException;
import project.DivideByZeroException;
import project.MachineModel;
import project.Memory;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
public class ViewMediator extends Observable {
	private MachineModel model;
	private CodeViewPanel codeViewPanel;
	private MemoryViewPanel memoryViewPanel1;
	private MemoryViewPanel memoryViewPanel2;
	private MemoryViewPanel memoryViewPanel3;
	private ControlPanel controlPanel;
	private ProcessorViewPanel processorPanel;
	private MenuBarBuilder menuBuilder;
	private JFrame frame;
	private FilesManager filesManager;
	private Animator animator;
	
	public MachineModel getModel() {
		return model;
	}

	public void setModel(MachineModel model) {
		this.model = model;
	}

	public JFrame getFrame() {
		return frame;
	}

	@SuppressWarnings("deprecation")
	public void step() {
		if (model.getCurrentState() != States.PROGRAM_HALTED && model.getCurrentState() != States.NOTHING_LOADED) {
			try {
				model.step();
			} catch (CodeAccessException e) { // import project.CodeAccessException at the start of the class
				JOptionPane.showMessageDialog(
					frame, 
					"Illegal access to code from line " + model.getInstructionPointer() + "\n"
					+ "Exception message: " + e.getMessage(),
					"Run time error",
					JOptionPane.OK_OPTION);
			} catch (ArrayIndexOutOfBoundsException e) { 
				JOptionPane.showMessageDialog(
						frame, 
						"Illegal access to data " + "\n"
						+ "Exception message: " + e.getMessage(),
						"Run time error",
						JOptionPane.OK_OPTION);
			} catch (NullPointerException e) { 
				JOptionPane.showMessageDialog(
						frame, 
						"NullPointerException from line " + model.getInstructionPointer() + "\n"
						+ "Exception message: " + e.getMessage(),
						"Run time error",
						JOptionPane.OK_OPTION);
			} catch (IllegalArgumentException e) { 
				JOptionPane.showMessageDialog(
						frame, 
						"Program error from line " + model.getInstructionPointer() + "\n"
						+ "Exception message: " + e.getMessage(),
						"Run time error",
						JOptionPane.OK_OPTION);
			} catch (DivideByZeroException e) { 
				JOptionPane.showMessageDialog(
						frame, 
						"Divide by zero from line " + model.getInstructionPointer() + "\n"
						+ "Exception message: " + e.getMessage(),
						"Run time error",
						JOptionPane.OK_OPTION);
			}
			setChanged();
			notifyObservers();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void execute() {
		while (model.getCurrentState() != States.PROGRAM_HALTED && model.getCurrentState() != States.NOTHING_LOADED) {
			try {
				model.step();
			} catch (CodeAccessException e) { // import project.CodeAccessException at the start of the class
				JOptionPane.showMessageDialog(
					frame, 
					"Illegal access to code from line " + model.getInstructionPointer() + "\n"
					+ "Exception message: " + e.getMessage(),
					"Run time error",
					JOptionPane.OK_OPTION);
			} catch (ArrayIndexOutOfBoundsException e) { 
				JOptionPane.showMessageDialog(
						frame, 
						"Illegal access to data " + "\n"
						+ "Exception message: " + e.getMessage(),
						"Run time error",
						JOptionPane.OK_OPTION);
			} catch (NullPointerException e) { 
				JOptionPane.showMessageDialog(
						frame, 
						"NullPointerException from line " + model.getInstructionPointer() + "\n"
						+ "Exception message: " + e.getMessage(),
						"Run time error",
						JOptionPane.OK_OPTION);
			} catch (IllegalArgumentException e) { 
				JOptionPane.showMessageDialog(
						frame, 
						"Program error from line " + model.getInstructionPointer() + "\n"
						+ "Exception message: " + e.getMessage(),
						"Run time error",
						JOptionPane.OK_OPTION);
			} catch (DivideByZeroException e) { 
				JOptionPane.showMessageDialog(
						frame, 
						"Divide by zero from line " + model.getInstructionPointer() + "\n"
						+ "Exception message: " + e.getMessage(),
						"Run time error",
						JOptionPane.OK_OPTION);
			}
		}
		setChanged();
		notifyObservers();
	}
	
	@SuppressWarnings("deprecation")
	public void clearJob() {
		model.clearJob();
		model.setCurrentState(States.NOTHING_LOADED);
		model.getCurrentState().enter();
		setChanged();
		notifyObservers("Clear");
	}
	
	@SuppressWarnings("deprecation")
	public void makeReady(String s) {
		animator.setAutoStepOn(false);
		model.setCurrentState(States.PROGRAM_LOADED_NOT_AUTOSTEPPING);
		model.getCurrentState().enter();
		setChanged();
		notifyObservers(s);
	}
	
	@SuppressWarnings("deprecation")
	public void toggleAutoStep() {
		animator.toggleAutoStep();
		if (animator.isAutoStepOn()) {
			model.setCurrentState(States.AUTO_STEPPING);
		} else {
			model.setCurrentState(States.PROGRAM_LOADED_NOT_AUTOSTEPPING);
			model.getCurrentState().enter();
			setChanged();
			notifyObservers();
		}
	}
	
	public void reload() {
		animator.setAutoStepOn(false);
		clearJob();
		filesManager.loadFile(model.getCurrentJob());
	}
	
	public void loadFile() {
		filesManager.loadFile(model.getCurrentJob());;
	}
	
	public void assembleFile() {
		filesManager.assembleFile();
	}
	public void setPeriod(int value) {
		animator.setPeriod(value);
	}
	
	@SuppressWarnings("deprecation")
	public void setJob(int i) {
		model.setJob(i);
		if(model.getCurrentState() != null) {
			model.getCurrentState().enter();
			setChanged();
			notifyObservers();
		}
	}
	
	private void createAndShowGUI() {
		animator = new Animator(this);
		filesManager = new FilesManager(this);
		filesManager.initialize();
		codeViewPanel = new CodeViewPanel(this, model);
		memoryViewPanel1 = new MemoryViewPanel(this, model, 0, 240);
		memoryViewPanel2 = new MemoryViewPanel(this, model, 240, Memory.DATA_SIZE/2);
		memoryViewPanel3 = new MemoryViewPanel(this, model, Memory.DATA_SIZE/2, Memory.DATA_SIZE);
		controlPanel = new ControlPanel(this);
		processorPanel = new ProcessorViewPanel(this, model);
		menuBuilder = new MenuBarBuilder(this);
		frame = new JFrame("Simulator");
		
		Container content = frame.getContentPane();
		content.setLayout(new BorderLayout(1,1));
		content.setBackground(Color.BLACK);
		
		frame.setSize(1200, 600);
		JPanel center = new JPanel(new GridLayout(1, 3));
		frame.add(codeViewPanel.createCodeDisplay(), BorderLayout.LINE_START);
		center.add(memoryViewPanel1.createMemoryDisplay()); 
		center.add(memoryViewPanel2.createMemoryDisplay()); 
		center.add(memoryViewPanel3.createMemoryDisplay());
		frame.add(center);
		
		frame.add(controlPanel.createControlDisplay(),BorderLayout.PAGE_END);
		
		frame.add(processorPanel.createProcessorDisplay(),BorderLayout.PAGE_START);
		
		JMenuBar bar = new JMenuBar();
		frame.setJMenuBar(bar);
		bar.add(menuBuilder.createFileMenu());
		bar.add(menuBuilder.createExecuteMenu());
		bar.add(menuBuilder.createJobsMenu());
		/** return here for other GUI components*/
		
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(WindowListenerFactory.
				windowClosingFactory(e -> exit()));
		model.setCurrentState(States.NOTHING_LOADED);
		animator.start();
		model.getCurrentState().enter();
		setChanged();
		notifyObservers();
		// return HERE for other setup details
		frame.setVisible(true);
	}
	
	public States getCurrentState() {
		return model.getCurrentState();
	}
	
	@SuppressWarnings("deprecation")
	public void setCurrentState(States currentState) {
		if (currentState == States.PROGRAM_HALTED) {
			animator.setAutoStepOn(false);
		}
		model.setCurrentState(currentState);
		model.getCurrentState().enter();
		setChanged();
		notifyObservers();
		
	}
	
	public void exit() { // method executed when user exits the program
		int decision = JOptionPane.showConfirmDialog(
				frame, "Do you really wish to exit?",
				"Confirmation", JOptionPane.YES_NO_OPTION);
		if (decision == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}
	
	
	
	// CORRECTED LATER IN THE EVENING
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ViewMediator mediator = new ViewMediator();
				MachineModel model = new MachineModel(
						true,  
						() -> mediator.setCurrentState(States.PROGRAM_HALTED)
				);
				mediator.setModel(model);
				mediator.createAndShowGUI();;
			}
		});
	}
	
}