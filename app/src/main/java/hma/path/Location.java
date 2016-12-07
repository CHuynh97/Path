package hma.path;

import android.location.Address;
import android.text.TextUtils;

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Created by Chris on 2016-11-19.
 */
public class Location implements Serializable {

    private String address, city, province, postalCode;
    private long timeSpent = -1;
    private long timeLeft, timeOfTrip;
    private long timeArrivedToNextLoc;

    public Location(String address, String city, String province, String postalCode, long timeSpent) {
        this.address = address;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.timeSpent = 60*1000*timeSpent;
    }

    public Location(String address, String city, String province, String postalCode) {
        this.address = address;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
    }

    public static String[] parseAdress(String address) {
        String[] result = address.split(", ");
        return result;
    }

    public Location(String[] address, int duration) {
        this.address = address[0];
        city = address[1];
        province = address[2];
        postalCode = address[3];
        timeSpent = duration;
    }

    public String getAddressName() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public long getTimeSpentAtLocation() {
        return timeSpent;
    }

    public void setTimeLeft(long timeLeft) {
        this.timeLeft = timeLeft;
    }
    public long getTimeLeft() {
        return this.timeLeft;
    }
    public long getTimeArrivedToNextLoc() {
        return timeArrivedToNextLoc;
    }
    public void setTimeArrivedToNextLoc(long timeArrived) {
        this.timeArrivedToNextLoc = timeArrived;
    }
}