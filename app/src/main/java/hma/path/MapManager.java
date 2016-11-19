package hma.path;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.TravelMode;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Chris on 2016-11-19.
 */
public class MapManager {
    private static final String API_KEY = "AIzaSyCDmikUWztdbXQBt3Xbn1TZZYDXqhMgXeY";

    private static GeoApiContext context;

    static {
        context = new GeoApiContext().setApiKey(API_KEY);
    }

    public static DistanceMatrixElement[] getLocationsFrom(Location start, List<Location> endLocations) {

        DistanceMatrix distanceMatrix = null;
        try {
            distanceMatrix = DistanceMatrixApi.newRequest(context).mode(TravelMode.TRANSIT).origins(formatLocation(start))
                    .destinations(formatLocation(endLocations)).departureTime(
                        new DateTime(start.getDuration() + start.getTimeArrived().getMillis()))
                       .await();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return distanceMatrix.rows[0].elements;
    }

    private static String formatLocation(Location location) {
        String test =  location.getAddressName() + " " + location.getCity() + ", " +
                location.getProvince() + " " + location.getPostalCode();
        return test;
    }

    private static String[] formatLocation(List<Location> locations) {
        String[] returnLoc = new String[locations.size()];
        for(int i = 0; i < returnLoc.length; i++) {
            returnLoc[i] =  formatLocation(locations.get(i));
        }
        return returnLoc;
    }
}
