/**
 * 
 */
package model;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import javafx.scene.input.KeyCode;

/**
 * @author johnmcculloch
 *
 */
public class KeyboardControlModel implements Runnable, model.ArduinoDataContentServer.Consumer {

	// Instance Variables
	/**
	 * Values to pass to keyController.
	 */
	private KeyCode a0Key, a1Key, a01Key;

	/**
	 * Slider Values
	 */
	private double a0SliderThresholdValue, a1SliderThresholdValue;

	/**
	 * Signal from arduino (from subscription)
	 */
	private double a0Signal, a1Signal;

	/**
	 * Robot Creates key presses
	 */
	private Robot robot;

	/**
	 * Keyboard Control Model Thread (THIS)
	 */
	private Thread keyboardControlModelThread;

	/**
	 * Application Running
	 */
	private boolean running = false;

	/**
	 * Mouse Control X Axis Selected True - Selected False - Not Selected
	 */
	private boolean mouseControlXAxisSelected;

	/**
	 * Mouse Control Y Axis Selected True - Selected False - Not Selected
	 */
	private boolean mouseControlYAxisSelected;

	/**
	 * Mouse Control X And Y Axis Selected True - Selected False - Not Selected
	 */
	private boolean mouseControlXAndYAxisSelected;

	/**
	 * Pointer Info (Mouse Info)
	 */
	private PointerInfo pointerInfo;

	/**
	 * Location of mouse
	 */
	private Point point;

	/**
	 * Width and Height of Screen used by program
	 */
	private int widthOfScreenX, heightOfScreenY;

	// Constructors

	/**
	 * Default Constructor
	 */
	public KeyboardControlModel() {

		// Register to incoming data (signals)
		ArduinoDataContentServer.getInstance().registerConsumer(this);
		
		// Initialise mouse pointer info
		pointerInfo = MouseInfo.getPointerInfo();
		point = pointerInfo.getLocation();

		//Get width and height of users screen and store to local variables
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		heightOfScreenY = (int) screen.getHeight();
		widthOfScreenX = (int) screen.getWidth();

		// set initial A0 and A1 Threshold Values
		a0SliderThresholdValue = 2.5;
		a1SliderThresholdValue = 2.5;

		// Instantiation of Robot
		try {
			robot = new Robot();
		} catch (AWTException e) {
			System.err.println("Error creating Robot");
		}

	}

	// Getters and Setters

	/**
	 * @return the mouseControlXAxisSelected
	 */
	public boolean isMouseControlXAxisSelected() {
		return mouseControlXAxisSelected;
	}

	/**
	 * @param mouseControlXAxisSelected the mouseControlXAxisSelected to set
	 */
	public void setMouseControlXAxisSelected(boolean mouseControlXAxisSelected) {
		this.mouseControlXAxisSelected = mouseControlXAxisSelected;
	}

	/**
	 * @return the mouseControlYAxisSelected
	 */
	public boolean isMouseControlYAxisSelected() {
		return mouseControlYAxisSelected;
	}

	/**
	 * @param mouseControlYAxisSelected the mouseControlYAxisSelected to set
	 */
	public void setMouseControlYAxisSelected(boolean mouseControlYAxisSelected) {
		this.mouseControlYAxisSelected = mouseControlYAxisSelected;
	}

	/**
	 * @return the mouseControlXAndYAxisSelected
	 */
	public boolean isMouseControlXAndYAxisSelected() {
		return mouseControlXAndYAxisSelected;
	}

	/**
	 * @param mouseControlXAndYAxisSelected the mouseControlXAndYAxisSelected to set
	 */
	public void setMouseControlXAndYAxisSelected(boolean mouseControlXAndYAxisSelected) {
		this.mouseControlXAndYAxisSelected = mouseControlXAndYAxisSelected;
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * @param running the running to set
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}

	/**
	 * @return the a0SliderThresholdValue
	 */
	public double getA0SliderThresholdValue() {
		return a0SliderThresholdValue;
	}

	/**
	 * @return the a0Signal
	 */
	public double getA0Signal() {
		return a0Signal;
	}

	/**
	 * @param a0Signal the a0Signal to set
	 */
	public void setA0Signal(double a0Signal) {
		this.a0Signal = a0Signal;
	}

	/**
	 * @return the a1Signal
	 */
	public double getA1Signal() {
		return a1Signal;
	}

	/**
	 * @param a1Signal the a1Signal to set
	 */
	public void setA1Signal(double a1Signal) {
		this.a1Signal = a1Signal;
	}

	/**
	 * @param a0SliderThresholdValue the a0SliderThresholdValue to set
	 */
	public void setA0SliderThresholdValue(double a0SliderThresholdValue) {
		this.a0SliderThresholdValue = a0SliderThresholdValue;
	}

	/**
	 * @return the a1SliderThresholdValue
	 */
	public double getA1SliderThresholdValue() {
		return a1SliderThresholdValue;
	}

	/**
	 * @param a1SliderThresholdValue the a1SliderThresholdValue to set
	 */
	public void setA1SliderThresholdValue(double a1SliderThresholdValue) {
		this.a1SliderThresholdValue = a1SliderThresholdValue;
	}

	/**
	 * @return the a0Key
	 */
	public KeyCode getA0Key() {
		return a0Key;
	}

	/**
	 * @param a0Key the a0Key to set
	 */
	public void setA0Key(KeyCode a0Key) {
		this.a0Key = a0Key;
	}

	/**
	 * @return the a1Key
	 */
	public KeyCode getA1Key() {
		return a1Key;
	}

	/**
	 * @param a1Key the a1Key to set
	 */
	public void setA1Key(KeyCode a1Key) {
		this.a1Key = a1Key;
	}

	/**
	 * @return the a01Key
	 */
	public KeyCode getA01Key() {
		return a01Key;
	}

	/**
	 * @param a01Key the a01Key to set
	 */
	public void setA01Key(KeyCode a01Key) {
		this.a01Key = a01Key;
	}

	// Methods
	/**
	 * Start Keyboard Control Model Thread If boolean running true return; If
	 * boolean false, start running this thread
	 */
	public synchronized void start() {
		// if running already true return
		if (running) {
			return;
		} else {
			running = true;
			// create thread and start
			keyboardControlModelThread = new Thread(this);
			keyboardControlModelThread.start();
		}
	}

	/**
	 * Stop thread from running If thread is not running return If thread is running
	 * interrupt and join thread
	 */
	public synchronized void stop() {
		if (!running) {
			return;
		} else {
			try {
				running = false;
				// interrupt thread (stop any such like sleep) and then join to halt
				keyboardControlModelThread.interrupt();
				keyboardControlModelThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.exit(0);
				e.printStackTrace();
			}
		}
	}

	/**
	 * While running this method will continue to loop (run) Controls action
	 * selected by user of mouse or keyboard control
	 */
	public void run() {

		try {

			while (this.running) {

				// check if mouse Controls have been selected
				// if so key should not be activated or selected but mouse control
				// if false go to else to activate the key controls
				if (this.mouseControlXAxisSelected || this.mouseControlYAxisSelected
						|| this.mouseControlXAndYAxisSelected) {

					// If mouse controls are selected mousecontrol method called
					mouseControl();
				} else {

					// Call KeyControl
					keyControl();

				} // end of mouse control or key selected if statement

				// Thread sleep (to not overwhelm thread)
				Thread.sleep(200);
			} // end of while loop

		} catch (Exception e) {
			System.err.println("KeyboardControlModel run() Handeled");
		}

	}

	/**
	 * Control Mouse Movement
	 * 
	 * @param xaxis x or y mouse movement (True XAxis Selected, False YAxis
	 *              Selected)
	 * @throws AWTException
	 */
	private void mouseMovement(boolean xaxis) throws AWTException {
		
		// Get pointer information to move mouse position
		pointerInfo = MouseInfo.getPointerInfo();
		point = pointerInfo.getLocation();
		int x = (int) point.getX();
		int y = (int) point.getY();

		// If Signal A0 is greater than threshold
		if (this.a0Signal > a0SliderThresholdValue) {
			// if true x-axis control else y axis
			if (xaxis) {
				robot.mouseMove(x + 50, y);
			} else {
				robot.mouseMove(x, y + 50);
			}
		}

		// If Signal A1 is greater than threshold
		if (this.a1Signal > a1SliderThresholdValue) {
			// if true x-axis control else y axis
			if (xaxis) {
				robot.mouseMove(x - 50, y);
			} else {
				robot.mouseMove(x, y - 50);
			}
		}

	}

	/**
	 * Control X and Y Movement Proportional to signal and screen size
	 */
	private void mouseXYMovement() {

		// Get Relative Placement Against Screen Size
		// Divide width of screen by max arduino input (5V) to get one point
		// Multiple by current signal to get placement
		int x = (int) ((this.widthOfScreenX / 5) * this.a0Signal);
		int y = (int) ((this.heightOfScreenY / 5) * this.a1Signal);
		// Robot move mouse
		robot.mouseMove(x, y);

	}

	/**
	 * Mouse Control
	 * 
	 * @throws AWTException
	 */
	private void mouseControl() throws AWTException {

		if (this.mouseControlXAndYAxisSelected) {
			// does not control left click like x or y, as will be multiple cross over of signals
			// allows for freedom of both x and y movement of mouse.
			mouseXYMovement();
		} else {
			// if co-contraction click left mouse key
			if (this.a0Signal > a0SliderThresholdValue && this.a1Signal > a1SliderThresholdValue) {
				robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
				robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			}
			if (this.mouseControlXAxisSelected) {
				// call mouse movement (true) control X-Axis Movement
				mouseMovement(true);
			} else {
				// call mouse movement (false) control Y-Axis
				mouseMovement(false);
			}
		} // end of if statement to check if XAndY Selected
	}

	/**
	 * KeyControl If user selects to control key this will perform the function
	 */
	private void keyControl() {
		// Check if A01 Key is not null
		// If the key is not null and A0 + A1 Signals are greater than threshold
		// (Co-Contraction)
		// Press A01 (Co-Contraction Key) if not go to else
		if (a01Key != null && (this.a0Signal > a0SliderThresholdValue && this.a1Signal > a1SliderThresholdValue)) {
			// Press mapped A01 Key
			robot.keyPress(a01Key.getCode());
		} else {

			// if A0Key is not null and signal is over threshold press key
			if (a0Key != null) {
				if (this.a0Signal > a0SliderThresholdValue) {
					robot.keyPress(a0Key.getCode());
				} else {
					robot.keyRelease(a0Key.getCode());
				}
			}

			// if A1Key is not null and signal is over threshold press key
			if (a1Key != null) {
				if (this.a1Signal > a1SliderThresholdValue) {
					robot.keyPress(a1Key.getCode());
				} else {
					robot.keyRelease(a1Key.getCode());
				}
			}
		} // end of if else key statement
		//check if not null if not release key
		if(a01Key != null) {
		robot.keyRelease(a01Key.getCode());
		}
	}

	/**
	 * Method to subscribe to arduino signals Convert analog signal to Voltage
	 * (matches Threshold)
	 */
	@Override
	public void data(double A0, double A1) {

		// Process ArduinoData Convert analog reading (0 and 1023) to 0V to 5v
		this.a0Signal = ((A0) * (5.0 / 1023.0));
		this.a1Signal = ((A1) * (5.0 / 1023.0));

		// Adjust reading if above or below 0V or 5V
		// Arduino can only read 0V to 5V clean reading to error handle and smooth data further
		// Arduino can send faulty signal at times (Clean Data)
		if (a0Signal > 5.0) {
			a0Signal = 5.0;
		} else if (a0Signal < 0.0) {
			a0Signal = 0.0;
		}

		if (a1Signal > 5.0) {
			a1Signal = 5.0;
		} else if (a1Signal < 0.0) {
			a1Signal = 0.0;
		}

	}

}
