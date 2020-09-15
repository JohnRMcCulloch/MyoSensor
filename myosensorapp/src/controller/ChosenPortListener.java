package controller;


public interface ChosenPortListener {

	/**
	 * Pass button text and portName
	 * @param portName
	 * @param buttonText
	 */
	public void chosenPortName(String portName, String buttonText, boolean savePortName);
}
