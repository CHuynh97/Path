package hma.path;

/**
 * Created by Vtkachuk on 16-11-19.
 */
public class DistanceElement {

    public DistanceElement(long penis) {
        this.tripTime = penis;
    }
    public boolean isVisited() {
        return visited;
    }

    private long timeOfTrip;
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }

    public long getTripTime() {
        return tripTime;
    }

    public void setTripTime(long tripTime) {
        this.tripTime = tripTime;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    public void setDurationTimeOfTransport(long time) {
        this.timeOfTrip = time;
    }

    public long getDurationTimeOfTransport() {return this.timeOfTrip;}
    private boolean visited = false;
    private long timeSpent, tripTime;
    private String postalCode;


}
