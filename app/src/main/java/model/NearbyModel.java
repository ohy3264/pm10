package model;

/**
 * <pre>
 * @author        : oh
 * @Day           : 2014. 11. 20.   
 * @Time          : 오전 10:57:15
 * @Explanation   : 가까운 측정소 DataModel
 * </pre>
 *
 */
public class NearbyModel {
	String stationName;
	String addr;
	String tm;
	/**
	 * @return the stationName
	 */
	public String getStationName() {
		return stationName;
	}
	/**
	 * @param stationName the stationName to set
	 */
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	/**
	 * @return the addr
	 */
	public String getAddr() {
		return addr;
	}
	/**
	 * @param addr the addr to set
	 */
	public void setAddr(String addr) {
		this.addr = addr;
	}
	/**
	 * @return the tm
	 */
	public String getTm() {
		return tm;
	}
	/**
	 * @param tm the tm to set
	 */
	public void setTm(String tm) {
		this.tm = tm;
	}
	

}
