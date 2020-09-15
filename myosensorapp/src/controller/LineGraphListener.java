/**
 * 
 */
package controller;

import javafx.scene.chart.XYChart;

/**
 * @author johnmcculloch
 *
 */
public interface LineGraphListener {

	/**
	 * Pass XYChart.Data (Used to connect Toolbar and LineGraph
	 * @param sensorOne <Number>
	 * @param sensorTwo <Number>
	 */
	public void dataProduced(XYChart.Data<Number, Number> sensorOne,  XYChart.Data<Number, Number> sensorTwo);
	
}
