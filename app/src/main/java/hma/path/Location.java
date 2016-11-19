package hma.path;

import android.location.Address;
import android.text.TextUtils;

import org.joda.time.DateTime;

/**
 * Created by Chris on 2016-11-19.
 */
public class Location {
    private String mAddressName;
    private String mCity;
    private String mProvince;
    private String mPostalCode;
    private long mDuration;
    private DateTime mTimeArrived;

    public static String[] parseAdress(String address) {
        String[] result = address.split(", ");
        return result;
    }

    public Location(String[] address, int duration) {
        mAddressName = address[0];
        mCity = address[1];
        mProvince = address[2];
        mPostalCode = address[3];
        this.mDuration = duration;
    }

    public String getAddressName() {
        return mAddressName;
    }

    public String getCity() {
        return mCity;
    }

    public String getProvince() {
        return mProvince;
    }

    public String getPostalCode() {
        return mPostalCode;
    }

    public long getDuration() {
        return mDuration;
    }

    public DateTime getTimeArrived() {
        return this.mTimeArrived;
    }

    public void setTimeArrived(DateTime time) {
        mTimeArrived = time;
    }
}
