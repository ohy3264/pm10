package model;

/**
 * Created by oh on 2015-02-27.
 */
public class StationAddressModel {
    String sido;
    String stationName;
    String address;
    String items;

    public StationAddressModel(String sido, String stationName, String address, String items) {
        this.sido = sido;
        this.stationName = stationName;
        this.address = address;
        this.items = items;
    }

    public String getSido() {
        return sido;
    }

    public void setSido(String sido) {
        this.sido = sido;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }
}

