/**
 * 
 */
package controller;

import javafx.scene.input.KeyCode;

/**
 * @author johnmcculloch
 *
 */
public interface KeySelectionListener {

	/**
	 * Key And Mouse Control Listener 
	 * @param a0Key
	 * @param a1Key
	 * @param a01Key
	 * @param mouseControlXAxisSelected
	 * @param mouseControlYAxisSelected
	 * @param mouseControlXAndYAxisSelected
	 * @param button
	 */
	public void setKeySelection(KeyCode a0Key, KeyCode a1Key, KeyCode a01Key, boolean mouseControlXAxisSelected, boolean mouseControlYAxisSelected, boolean mouseControlXAndYAxisSelected, String button);
	
}
