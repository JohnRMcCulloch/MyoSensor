/**
 * 
 */
package controller;

/**
 * @author johnmcculloch
 *
 */
public interface ChangeSceneDataListener {

	/**
	 * Method changes scene
	 * Pass userDetails to Populate Written File
	 * @param patientID String
	 * @param notes String
	 * @param amputationSide String
	 */
	public void changeScene(String patientID, String notes, String amputationSide);
	
}
