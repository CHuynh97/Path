package hma.path;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;

/**
 * Hello world!
 *
 */
public class ShortestPath
{
    private static long minTime = 0;
    private static List<Location> Locations;
    private static List<Location> skippedLocations = new ArrayList<Location>();
    private static List<Location> finalPath;

    public static long getMinimumTime() {
        return minTime;
    }

    public static ArrayList<Location> getFinalPath() {
        return (ArrayList<Location>)finalPath;
    }

    public static void getShortestPath(Location start, List<Location> midNodes, Location endLocation) {
        long currentTimeInMiliSeconds = System.currentTimeMillis();
        Locations = new ArrayList<Location>();
        Locations.addAll(midNodes);
        Locations.add(start);
        Locations.add(endLocation);
        DistanceMatrix distanceMatrix = MapManager.getLocationsFrom(start, midNodes,
                currentTimeInMiliSeconds);

        List<DistanceElement> distanceElements = new ArrayList<DistanceElement>();
        for(int i = 0; i < distanceMatrix.rows[0].elements.length; i++) {
            DistanceMatrixElement temp = distanceMatrix.rows[0].elements[i];
            distanceElements.add(new DistanceElement(temp.duration.inSeconds*1000));
        }
        for(int i = 0; i < distanceElements.size(); i++) {
            distanceElements.get(i).setTimeSpent(Locations.get(i).getTimeSpentAtLocation());
            distanceElements.get(i).setPostalCode(Locations.get(i).getPostalCode());
        }
        List<Location> finalPath = new ArrayList<Location>();
        finalPath.add(start);

        for (int i = 0; i < midNodes.size(); i++) {
            List<Location> finalPathCopy = new ArrayList<>(finalPath);
            String postalCode = midNodes.get(i).getPostalCode();
            Location currentLocation = null;
            for(Location location : Locations) {
                if(location.getPostalCode().equals(postalCode)) {
                    currentLocation = location;
                    break;
                }
            }
            currentLocation.setTimeLeft(start.getTimeLeft() + currentLocation.getTimeSpentAtLocation());
            finalPathCopy.add(currentLocation);

            List<Location> nodesLeft = new ArrayList<Location>(midNodes);
            nodesLeft.remove(currentLocation);
            recursive(System.currentTimeMillis() + distanceElements.get(i).getTripTime() + distanceElements.get(i).getTimeSpent(),
                    nodesLeft, endLocation, currentLocation, false, finalPath);
        }

    }
    private static void recursive(long currentTime,
                                  List<Location> nodesLeft, Location endLocation, Location currentLocation, boolean solved,
                                  List<Location> finalLocation) {

        if (minTime == 0 && currentTime != 0 && solved && (finalLocation.size() == Locations.size())) {
            minTime = currentTime;
            System.out.println(minTime);
            finalPath = finalLocation;
        } else if (minTime != 0 && currentTime < minTime && solved  && (finalLocation.size() == Locations.size())) {
            minTime = currentTime;
            System.out.println(minTime);

            finalPath = finalLocation;
            return;
        } else if (minTime != 0 && currentTime > minTime && solved  && (finalLocation.size() == Locations.size())) {
            System.out.println("SMALLEST" + " " + currentTime);
        }

        //no items left so figure out the time from here to end location;
        if (nodesLeft.size() == 0 && !solved) {
            long timeSpent = MapManager.getLocationsFrom(currentLocation, Arrays.asList(endLocation),
                    currentTime + currentLocation.getTimeSpentAtLocation()).rows[0].elements[0].duration.inSeconds*1000;

            finalLocation.add(endLocation);
            List<Location> finalLocationCopy = new ArrayList<Location>(finalLocation);
            recursive(currentTime + timeSpent, nodesLeft, null, currentLocation, true, finalLocationCopy);

            //items left
        } else if (!solved){

            //find time from my loc to every other loc
            DistanceMatrix distanceMatrix = MapManager.getLocationsFrom(currentLocation, nodesLeft,
                    currentTime);
            int originalSize = nodesLeft.size();

            for(int i = 0; i < originalSize; i++) {
                String postalCode = nodesLeft.get(i).getPostalCode();
                List<Location> nodesLeftNew = new ArrayList<>(nodesLeft);
                nodesLeftNew.remove(nodesLeft.get(i));

                Location currentLocationNew = null;
                for(Location location: Locations) {
                    if(location.getPostalCode().equals(postalCode)) {
                        currentLocationNew = location;
                        break;
                    }
                }
                currentLocationNew.setTimeLeft(finalLocation.get(finalLocation.size()-1).getTimeLeft() + currentLocation.getTimeSpentAtLocation());
                if(distanceMatrix.rows[0].elements[i].duration == null) {
                    skippedLocations.add(currentLocationNew);
                } else {
                    finalLocation.add(currentLocationNew);
                    List<Location> finalLocationCopy = new ArrayList<Location>(finalLocation);
                    currentLocation.setTimeLeft(finalLocation.get(finalLocation.size()-1).getTimeLeft());
                    recursive(
                            currentTime + nodesLeft.get(i).getTimeSpentAtLocation()
                                    + distanceMatrix.rows[0].elements[i].duration.inSeconds * 1000,
                            nodesLeftNew, endLocation, currentLocationNew, false, finalLocationCopy);
                }
            }
        }

    }


    public static void getBusTimes() {

    }
}