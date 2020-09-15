/**
 * 
 */
package view;

import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;

/**
 * @author johnmcculloch
 * Line Graph GUI
 * Class sets up components to create the line graph GUI
 */
public class LineGraphGUI extends StackPane {

	//Instance Vars

	/**
	 * xAxis NumberAxis for use in lineChart
	 */
	private NumberAxis xAxis;
	
	/**
	 * yAxis NumberAxis for use in lineChart
	 */
	private NumberAxis yAxis;
	
	/**
	 * lineChart<Number, Number> 
	 */
	private LineChart<Number, Number> lineChart;
	
	/**
	 * First Series, places data into lineChart 
	 */
	private XYChart.Series<Number, Number> series;
	
	/**
	 * Second Series, places data into lineChart
	 */
	private XYChart.Series<Number, Number> series1;

	/**
	 * Sets upper limit of series to refresh
	 * Used to refresh line graph to stop scaling
	 */
	private final int seriesUpperLimit = 60;
	
	/**
	 * Series Name
	 */
	private final String seriesName = "A0 analog input";
	
	/**
	 * Series One Name
	 */
	private final String seriesOneName = "A1 analog input";
	
	/**
	 * X Axis Label
	 */
	private final String xAxisLabel = "Time (Ms)";
	
	/**
	 * Y Axis Label
	 */
	private final String yAxisLabel = "Voltage (0-5V)";
	
	/**
	 * Y Axis Lower Bound
	 */
	private final double yAxisLowerBound = 0.0;
	
	/**
	 * Y Axis Upper Bound
	 */
	private final double yAxisUpperBound = 6.0;
	
	/**
	 * Y Axis Unit Increment Measure
	 */
	private final double yAxisUnitMeasure = 1.0;
	
	
	
	//Constructors
	
	/**
	 * Default Constructor
	 */
	@SuppressWarnings("unchecked") //Unable to change generic Number to (Integer, Double)
	public LineGraphGUI() {
		
		//Instantiate Chart Object
		xAxis = new NumberAxis();
		//always display from 0 range in XAxis (Keep line graph from starting at 0)
		xAxis.setForceZeroInRange(false);
		xAxis.setLabel(xAxisLabel);
		yAxis = new NumberAxis();
		//Set lower and Upper bounds
		yAxis.setLowerBound(yAxisLowerBound);
		yAxis.setUpperBound(yAxisUpperBound);
		yAxis.setTickUnit(yAxisUnitMeasure);
		yAxis.setLabel(yAxisLabel);
		//Set AutoRanging false (don't allow graph to grow and grow)
		yAxis.setAutoRanging(false);
		lineChart = new LineChart<>(xAxis, yAxis);
		
		//Instantiate series and setName of Arduino analog pin (Reading in Data)
		series = new XYChart.Series<Number, Number>();
		series.setName(seriesName);
		series1 = new XYChart.Series<Number, Number>();
		series1.setName(seriesOneName);
		
		//add series to chart
		lineChart.getData().addAll(series, series1);
		
		lineChart.setAnimated(false);
		lineChart.setCreateSymbols(false); //hide dots
		
		//Add lineChart to StackPane Layout
		getChildren().add(lineChart);
		
	}
	
	//Methods
	
	/**
	 * Updates lineChat values
	 * Runs Platform.runLater to reduce application UI freezing,
	 * Takes the data from the Arduino Thread and passes to the Application Thread
	 * @param sensorOne XYChart.Data<Number, Number>
	 * @param sensorTwo XYChart.Data<Number, Number>
	 */
	public void addSeriesData(XYChart.Data<Number, Number> sensorOne, XYChart.Data<Number, Number>sensorTwo) {
		
		int sensorOneXAxisCounter = sensorOne.getXValue().intValue();
		Double passVoltOne = sensorOne.getYValue().doubleValue();
		
		int sensorTwoXAxisCounter = sensorTwo.getXValue().intValue();
		Double passVoltTwo = sensorTwo.getYValue().doubleValue();
		
        Platform.runLater(() -> {
        	series.getData().add(new XYChart.Data<Number, Number>(sensorOneXAxisCounter, passVoltOne));
        	series1.getData().add(new XYChart.Data<Number, Number>(sensorTwoXAxisCounter, passVoltTwo));
        	
        	//if series gets longer than value remove 
            if (series.getData().size()>seriesUpperLimit) {
                series.getData().remove(0);
            }
            
            if (series1.getData().size()>seriesUpperLimit) {
                series1.getData().remove(0);
            }
        });
        
        

	}
}
