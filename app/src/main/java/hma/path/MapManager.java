package hma.path;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.TravelMode;

import java.util.List;

import org.joda.time.DateTime;


public class MapManager {
    private static final String API_KEY = "AIzaSyCDmikUWztdbXQBt3Xbn1TZZYDXqhMgXeY";

    private static GeoApiContext context;

    static {
        context = new GeoApiContext().setApiKey(API_KEY);
    }

    public static DistanceMatrix getLocationsFrom(Location start, List<Location> endLocations, long currentTimeInMillis) {

        DistanceMatrix distanceMatrix = null;
        try {
            distanceMatrix = DistanceMatrixApi.newRequest(context).mode(TravelMode.TRANSIT).origins(formatLocation(start))
                    .destinations(formatLocation(endLocations))
                    .departureTime(new DateTime(currentTimeInMillis)).await();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return distanceMatrix;
    }

    public static long getTravelTimeInMillis(Location a, Location b, long departureTimeInMillis) {
        try {
            DistanceMatrixElement element = DistanceMatrixApi.newRequest(context).mode(TravelMode.TRANSIT).origins(formatLocation(a))
                    .destinations(formatLocation(b)).departureTime(new DateTime(departureTimeInMillis))
                    .await().rows[0].elements[0];
            return element.duration.inSeconds*1000;
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static String formatLocation(Location location) {
        String test = location.getAddressName() + " " + location.getCity() + ", " +
                location.getProvince() + " " + location.getPostalCode();
        return test;
    }

    private static String[] formatLocation(List<Location> locations) {
        String[] returnLoc = new String[locations.size()];
        for (int i = 0; i < returnLoc.length; i++) {
            returnLoc[i] = formatLocation(locations.get(i));
        }
        return returnLoc;
    }


}

