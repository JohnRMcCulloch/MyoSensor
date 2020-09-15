/**
 * 
 */
package model;

import com.google.gson.JsonArray;

/**
 * @author johnmcculloch
 *
 */
public class UserDetails {

	// Instance Vars

	/**
	 * Patient ID (String incase of letters)
	 */
	private String patientID;

	/**
	 * String to store date and time of current session
	 */
	private String dateTime;

	/**
	 * Amputated side (Left or Right)
	 */
	private String amputationSide;

	/**
	 * Notes to take from current session
	 */
	private String notes;
	
	/**
	 * Data collected 
	 * Used to store JSON Array of Collected Data
	 */
	private JsonArray dataCollected;

	//transient = exclude fields from serialization (when converting UserDetails Object TO JSON)
	
	/**
	 * Patient ID Length Upper Limit
	 */
	transient private final int PATIENT_ID_UPPEER_LIMIT = 20;

	/**
	 * Patient ID Length Lower Limit
	 */
	transient private final int PATIENT_ID_LOWER_LIMIT = 2;

	/**
	 * Amputation Side Left
	 * 
	 */
	transient private final String AMPUTATION_SIDE_LEFT = "Left";

	/**
	 * Amputation Side Right
	 * 
	 */
	transient private final String AMPUTATION_SIDE_Right = "Right";

	/**
	 * Notes lower limit
	 */
	transient private final int NOTES_LOWER_LIMIT = 0;

	// Constructors

	/**
	 * Default Constructor
	 */
	public UserDetails() {

	}

	/**
	 * Constructor with Args
	 * 
	 * @param patientID        must be set 2-20
	 * @param dateTime
	 * @param amputationSideID must be set left or right
	 * @param notes            must be set 0
	 * @param dataCollected initialised
	 * @throws IllegalArgumentException
	 */
	public UserDetails(String patientID, String dateTime, String amputationSide, String notes) {
		super();
		this.setPatientID(patientID);
		this.dateTime = dateTime;
		this.setAmputationSide(amputationSide);
		this.setNotes(notes);
	}

	// Getters and Setters

	/**
	 * @return the patientID
	 */
	public String getPatientID() {
		return patientID;
	} 

	/**
	 * Set PatientID
	 * @param patientID must be between 2 - 20 characters
	 * @throws IllegalArgumentException
	 */
	public void setPatientID(String patientID) throws IllegalArgumentException {

		if (patientID.trim().length() >= PATIENT_ID_LOWER_LIMIT
				&& patientID.trim().length() <= PATIENT_ID_UPPEER_LIMIT) {
			this.patientID = patientID;
		} else {
			throw new IllegalArgumentException("Patient ID Length out of scope");
		}
	}

	/**
	 * @return the dateTime
	 */
	public String getDateTime() {
		return dateTime;
	}

	/**
	 * @param dateTime the dateTime to set
	 */
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * @return the amputationSide
	 */
	public String getAmputationSide() {
		return amputationSide;
	}

	/**
	 * @param String amputationSide Must be set Left or Right (caps ignored)
	 * @throws IllegalArgumentException
	 * @throws NullPointerException
	 */
	public void setAmputationSide(String amputationSide) throws IllegalArgumentException, NullPointerException {

		if(amputationSide.trim() == null) {
			throw new NullPointerException();
		} else if (amputationSide.trim().equalsIgnoreCase(AMPUTATION_SIDE_LEFT) || amputationSide.trim().equalsIgnoreCase(AMPUTATION_SIDE_Right)) {
			this.amputationSide = amputationSide;
		} else {
			throw new IllegalArgumentException("Invalid Input");
		}
		

	}

	/**
	 * @return the dataCollected
	 */
	public JsonArray getDataCollected() {
		return dataCollected;
	}

	/**
	 * @param dataCollected the dataCollected to set
	 */
	public void setDataCollected(JsonArray dataCollected) {
		this.dataCollected = dataCollected;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes length must be greater than 0
	 * @throws IllegalArgumentException
	 */
	public void setNotes(String notes) throws IllegalArgumentException {
		if (notes.trim().length() >= NOTES_LOWER_LIMIT) {
			this.notes = notes;
		} else {
			throw new IllegalArgumentException("Invalid input must greater than 0");
		}
	}

	// Methods

	/**
	 * Return String of UserDetails
	 * 
	 * @return String
	 */
	public String printUserDetails() {

		String stringToReturn;

		stringToReturn = "User Details \nPatient ID: " + patientID + "\nAmputation side: " + amputationSide
				+ "\nNotes: " + notes + "\nDate and time of record: " + dateTime;

		return stringToReturn;
	}

}
