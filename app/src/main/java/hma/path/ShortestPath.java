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
            start.setTimeArrivedToNextLoc(start.getTimeLeft() + distanceMatrix.rows[0].elements[0].duration.inSeconds*1000);
            List<Location> finalPathCopy = new ArrayList<>(finalPath);
            String postalCode = midNodes.get(i).getPostalCode();
            Location currentLocation = null;
            for(Location location : Locations) {
                if(location.getPostalCode().equals(postalCode)) {
                    currentLocation = location;
                    break;
                }
            }
            currentLocation.setTimeLeft(start.getTimeArrivedToNextLoc() + currentLocation.getTimeSpentAtLocation());
            currentLocation.setTimeArrivedToNextLoc(currentLocation.getTimeLeft() +
                distanceMatrix.rows[0].elements[i].duration.inSeconds*1000);
            finalPathCopy.add(currentLocation);

            List<Location> nodesLeft = new ArrayList<Location>(midNodes);
            nodesLeft.remove(currentLocation);
            recursive(currentLocation.getTimeLeft(),
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
                    currentLocation.getTimeArrivedToNextLoc()).rows[0].elements[0].duration.inSeconds*1000;

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

                if(distanceMatrix.rows[0].elements[i].duration == null) {
                    skippedLocations.add(currentLocationNew);
                } else {
                    currentLocationNew.setTimeLeft(
                           currentLocation.getTimeArrivedToNextLoc()
                                    +currentLocationNew.getTimeSpentAtLocation());
                    currentLocationNew.setTimeArrivedToNextLoc(currentLocationNew.getTimeLeft() +
                            distanceMatrix.rows[0].elements[i].duration.inSeconds*1000
                    );
                    finalLocation.add(currentLocationNew);
                    List<Location> finalLocationCopy = new ArrayList<Location>(finalLocation);
                    currentLocation.setTimeLeft(finalLocation.get(finalLocation.size()-1).getTimeLeft());
                    recursive(
                            currentLocationNew.getTimeArrivedToNextLoc(),
                            nodesLeftNew, endLocation, currentLocationNew, false, finalLocationCopy);
                }
            }
        }

    }


    public static void getBusTimes() {

    }


    private static List<int[]> getPermutations(int[] arr) {
        List<int[]> results = new ArrayList<>();
        calcPermutations(arr, 0, arr.length-1, results);
        return  results;
    }


    private static void calcPermutations(int[] arr, int first, int last, List<int[]> results) {
        if (first < last) {
            for (int i = first; i <= last; i++) {
                swap(arr, i, first);
                calcPermutations(arr, first+1, last, results);
                swap(arr, i, first);
            }
        }
        else if (first == last) {
            if (results != null) {
                results.add(arr.clone());
            }
        }
    }

    private static void swap(int[] arr, int p, int q) {
        int temp = arr[p];
        arr[p] = arr[q];
        arr[q] = temp;
    }

    public static int[] shortestPath(Location start, List<Location> midNodes, Location end, long currentTimeInMillis) {
        if (start == null || midNodes == null || end == null) {
            return null;
        }
        long minTime = -1;
        int size = midNodes.size();
        int[] indexes = new int[size];
        for (int i = 0; i < size; i++) {
            indexes[i] = i;
        }
        List<int[]> perms = getPermutations(indexes);
        int[] result = null;
        for (int[] arr : perms) {
            long duration = calcPathDuration(start, midNodes, end, arr, currentTimeInMillis);
            if (minTime == -1 || minTime > duration) {
                result = arr;
                minTime = duration;
            }
        }
        return result;
    }

    public static long calcPathDuration(Location start, List<Location> locations, Location end, int[] indexes, long currentTimeInMillis) {
        if (locations.size() == indexes.length) {
            int size = indexes.length;
            long duration = start.getTimeSpentAtLocation();
            long leaveStart = currentTimeInMillis + duration;
            Location nextLoc = locations.get(indexes[0]);
            duration += MapManager.getTravelTimeInMillis(start, nextLoc, leaveStart);

            for (int i = 1; i < size; i++) {
                duration += nextLoc.getTimeSpentAtLocation();
                long departureTime = currentTimeInMillis + duration;
                long travelTime = MapManager.getTravelTimeInMillis(nextLoc, locations.get(indexes[i]), departureTime);
                duration += travelTime;
                nextLoc = locations.get(indexes[i]);
            }
            return duration;
        }
        return -1;
    }

}