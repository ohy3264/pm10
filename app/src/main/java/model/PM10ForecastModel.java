package model;

/**
 * <pre>
 * @author        : oh
 * @Day           : 2014. 11. 20.   
 * @Time          : 오전 10:56:51
 * @Explanation   : 예보개황 DataModel
 * </pre>
 *
 */
public class PM10ForecastModel {


	String informCode;
	String informOverall;
	String informCause;
	String informGrade;
	String actionKnack;
	String dataTime;
	/**
	 * @return the imageUrl
	 */
	public String getDataTime() {
		return dataTime;
	}
	/**
	 * @param dataTime the dataTime to set
	 */
	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}
	/**
	 * @return the informCode
	 */
	public String getInformCode() {
		return informCode;
	}
	/**
	 * @param informCode the informCode to set
	 */
	public void setInformCode(String informCode) {
		this.informCode = informCode;
	}
	/**
	 * @return the informOverall
	 */
	public String getInformOverall() {
		return informOverall;
	}
	/**
	 * @param informOverall the informOverall to set
	 */
	public void setInformOverall(String informOverall) {
		this.informOverall = informOverall;
	}
	/**
	 * @return the informCause
	 */
	public String getInformCause() {
		return informCause;
	}
	/**
	 * @param informCause the informCause to set
	 */
	public void setInformCause(String informCause) {
		this.informCause = informCause;
	}
	/**
	 * @return the informGrade
	 */
	public String getInformGrade() {
		return informGrade;
	}
	/**
	 * @param informGrade the informGrade to set
	 */
	public void setInformGrade(String informGrade) {
		this.informGrade = informGrade;
	}
	/**
	 * @return the actionKnack
	 */
	public String getActionKnack() {
		return actionKnack;
	}
	/**
	 * @param actionKnack the actionKnack to set
	 */
	public void setActionKnack(String actionKnack) {
		this.actionKnack = actionKnack;
	}
}
