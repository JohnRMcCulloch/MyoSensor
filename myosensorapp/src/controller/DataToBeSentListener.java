/**
 * 
 */
package controller;

/**
 * @author johnmcculloch
 *
 */
public interface DataToBeSentListener {

	/**
	 * Pass Arduino Data when received.
	 * @param data
	 */
	public void passData(String data);
	
}
