package hma.path;


import com.google.android.gms.maps.model.LatLng;
import com.google.maps.DirectionsApi;
import com.google.maps.DistanceMatrixApi;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.LocationType;
import com.google.maps.model.TravelMode;
import com.google.maps.DirectionsApiRequest;
import java.util.List;
import org.joda.time.DateTime;


public class MapManager {

    private static final String API_KEY = "AIzaSyCDmikUWztdbXQBt3Xbn1TZZYDXqhMgXeY";

    private static GeoApiContext context;
    private static DirectionsRoute directions;

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

    public static long getTravelTimeFromDirectionRoute(Location a, Location b, long departureTimeInMillis) {
        try {
            DirectionsRoute route = DirectionsApi.newRequest(context).mode(TravelMode.TRANSIT).origin(formatLocation(a))
                    .destination(formatLocation(b)).departureTime(new DateTime(departureTimeInMillis))
                    .await().routes[0];
            DirectionsLeg[] legs = route.legs;

            long duration = 0;
            for (DirectionsLeg leg : legs) {
                duration += leg.duration.inSeconds*1000;
            }
            return duration;
        }
        catch (Exception e) {
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


    public static DirectionsRoute getRoute(Location a, Location b, long departureTimeInMillis) {
        DirectionsRoute route = null;
        try {
            route = DirectionsApi.newRequest(context).mode(TravelMode.TRANSIT).origin(formatLocation(a))
                    .destination(formatLocation(b)).departureTime(new DateTime(departureTimeInMillis))
                    .await().routes[0];
        }
        catch (Exception e) {

        }
        return route;

    }

    public static LatLng convertToLatLng(Location location) {
        LatLng latlng = null;
        try {
            com.google.maps.model.LatLng result =
                    GeocodingApi.newRequest(context).address(formatLocation(location)).await()[0].geometry.location;
            latlng = new LatLng(result.lat, result.lng);
        }
        catch (Exception e) {

        }
        return latlng;
    }

}

