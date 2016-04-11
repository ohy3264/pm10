package model;

import java.io.Serializable;

/**
 * <pre>
 * @author        : oh
 * @Day           : 2014. 11. 20.   
 * @Time          : 오전 11:00:13
 * @Explanation   : 시도 시별, 측정소별 DataModel 
 * </pre>
 *
 */
public class StationModel implements Serializable{
    int _id;


    String stationName;
    String sidoName;
	String dataTime;
	String so2Value;
	String coValue;
	String o3Value;
	String no2Value;
	String pm10Value;
    String pm2_5Value;
	String khaiValue;
	String khaiGrade;
	String so2Grade;
	String coGrade;
	String o3Grade;
	String no2Grade;
	String pm2_5Grade;
    String pm10Grade;

    public String getPm2_5Value() {
        return pm2_5Value;
    }

    public void setPm2_5Value(String pm2_5Value) {
        this.pm2_5Value = pm2_5Value;
    }

    public String getPm2_5Grade() {
        return pm2_5Grade;
    }

    public void setPm2_5Grade(String pm2_5Grade) {
        this.pm2_5Grade = pm2_5Grade;
    }



    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }
    public String getSidoName() {
        return sidoName;
    }

    public void setSidoName(String sidoName) {
        this.sidoName = sidoName;
    }
	/**
	 * @return the stationName
	 */
	public String getStationName() {
		return stationName;
	}

	/**
	 * @param stationName
	 *            the stationName to set
	 */
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	/**
	 * @return the dataTime
	 */
	public String getDataTime() {
		return dataTime;
	}

	/**
	 * @param dataTime
	 *            the dataTime to set
	 */
	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}

	/**
	 * @return the so2Value
	 */
	public String getSo2Value() {
		return so2Value;
	}

	/**
	 * @param so2Value
	 *            the so2Value to set
	 */
	public void setSo2Value(String so2Value) {
		this.so2Value = so2Value;
	}

	/**
	 * @return the coValue
	 */
	public String getCoValue() {
		return coValue;
	}

	/**
	 * @param coValue
	 *            the coValue to set
	 */
	public void setCoValue(String coValue) {
		this.coValue = coValue;
	}

	/**
	 * @return the o3Value
	 */
	public String getO3Value() {
		return o3Value;
	}

	/**
	 * @param o3Value
	 *            the o3Value to set
	 */
	public void setO3Value(String o3Value) {
		this.o3Value = o3Value;
	}

	/**
	 * @return the no2Value
	 */
	public String getNo2Value() {
		return no2Value;
	}

	/**
	 * @param no2Value
	 *            the no2Value to set
	 */
	public void setNo2Value(String no2Value) {
		this.no2Value = no2Value;
	}

	/**
	 * @return the pm10Value
	 */
	public String getPm10Value() {
		return pm10Value;
	}

	/**
	 * @param pm10Value
	 *            the pm10Value to set
	 */
	public void setPm10Value(String pm10Value) {
		this.pm10Value = pm10Value;
	}

	/**
	 * @return the khaiGrade
	 */
	public String getKhaiGrade() {
		return khaiGrade;
	}

	/**
	 * @param khaiGrade
	 *            the khaiGrade to set
	 */
	public void setKhaiGrade(String khaiGrade) {
		this.khaiGrade = khaiGrade;
	}

	/**
	 * @return the so2Grade
	 */
	public String getSo2Grade() {
		return so2Grade;
	}

	/**
	 * @param so2Grade
	 *            the so2Grade to set
	 */
	public void setSo2Grade(String so2Grade) {
		this.so2Grade = so2Grade;
	}

	/**
	 * @return the coGrade
	 */
	public String getCoGrade() {
		return coGrade;
	}

	/**
	 * @param coGrade
	 *            the coGrade to set
	 */
	public void setCoGrade(String coGrade) {
		this.coGrade = coGrade;
	}

	/**
	 * @return the o3Grade
	 */
	public String getO3Grade() {
		return o3Grade;
	}

	/**
	 * @param o3Grade
	 *            the o3Grade to set
	 */
	public void setO3Grade(String o3Grade) {
		this.o3Grade = o3Grade;
	}

	/**
	 * @return the no2Grade
	 */
	public String getNo2Grade() {
		return no2Grade;
	}

	/**
	 * @param no2Grade
	 *            the no2Grade to set
	 */
	public void setNo2Grade(String no2Grade) {
		this.no2Grade = no2Grade;
	}

	/**
	 * @return the pm10Grade
	 */
	public String getPm10Grade() {
		return pm10Grade;
	}

	/**
	 * @param pm10Grade
	 *            the pm10Grade to set
	 */
	public void setPm10Grade(String pm10Grade) {
		this.pm10Grade = pm10Grade;
	}

	/**
	 * @return the khaiValue
	 */
	public String getKhaiValue() {
		return khaiValue;
	}

	/**
	 * @param khaiValue
	 *            the khaiValue to set
	 */
	public void setKhaiValue(String khaiValue) {
		this.khaiValue = khaiValue;
	}
}
