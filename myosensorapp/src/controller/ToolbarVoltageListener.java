/**
 * 
 */
package controller;

/**
 * @author johnmcculloch
 *
 */
public interface ToolbarVoltageListener {

	/**
	 * Passes String of voltage to update live label readings
	 * @param voltage1
	 * @param voltage2
	 */
	public void toolbarVoltageLabelUpdate(String voltage1, String voltage2);
	
}
