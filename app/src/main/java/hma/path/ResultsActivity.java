package hma.path;

import android.app.Fragment;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ArrayList<Location> paths = (ArrayList<Location>) getIntent().getExtras().get("path");
        long timeTaken = getIntent().getExtras().getLong("minTime");
        TextView locationHeader = new TextView(getApplicationContext());
        locationHeader.setTextColor(Color.BLACK);
        LinearLayout LLayout = (LinearLayout) findViewById(R.id.results);
        LLayout.setOrientation(LinearLayout.VERTICAL);
        locationHeader.setText("Time: \n" + new Date(timeTaken).toString() + "\n");
        LLayout.addView(locationHeader);
        StringBuilder patheronies = new StringBuilder();
        for(Location loc : paths) {
            patheronies.append(loc.getAddressName() + "\n");
        }
        TextView pathTaken = new TextView(getApplicationContext());
        pathTaken.setTextColor(Color.BLACK);
        pathTaken.setText(patheronies.toString());
        LLayout.addView(pathTaken);

        StringBuilder patheronies2 = new StringBuilder();

        ArrayList<Location> List2 = (ArrayList<Location>)getIntent().getExtras().get("sim2");
        for (Location loc : List2) {
          patheronies2.append(loc.getAddressName() + "\n");
        }

        TextView pathTaken2 = new TextView(getApplicationContext());
        pathTaken2.setTextColor(Color.BLUE);
        pathTaken2.setText(patheronies2.toString());
        LLayout.addView(pathTaken2);


        StringBuilder coords = new StringBuilder();
        for (Location loc : List2) {
            LatLng point = MapManager.convertToLatLng(loc);
            if (point != null) {
                coords.append(point.latitude + ", " + point.longitude + "\n");
            }
            else {
                coords.append("Latlng object was null.\n");
            }

        }

        TextView CoordsTV = new TextView(getApplicationContext());
        CoordsTV.setTextColor(Color.RED);
        CoordsTV.setText(coords.toString());
        LLayout.addView(CoordsTV);


    }

    public List<LatLng> getPath(DirectionsRoute route) {
        try {
            DirectionsLeg[] legs = route.legs;
            DirectionsStep[] legSteps = legs[0].steps;
            StringBuilder text = new StringBuilder();
            List<com.google.maps.model.LatLng> path = new ArrayList<>();
            for (DirectionsStep step : legSteps) {
                text.append(step.htmlInstructions);
                path.addAll(step.polyline.decodePath());
            }
            List<LatLng> result = new ArrayList<>();
            for (com.google.maps.model.LatLng latLng : path) {
                result.add(new LatLng(latLng.lat, latLng.lng));
            }
            return result;
        }
        catch (Exception e) {
            return null;
        }
    }

    public String getInstructions(DirectionsRoute route) {

        StringBuilder steps = new StringBuilder();
        DirectionsLeg[] legs = route.legs;
        for (DirectionsLeg leg : legs) {
            DirectionsStep[] dSteps = leg.steps;
            for (DirectionsStep step : dSteps) {
                steps.append(step.htmlInstructions);
            }
        }
        return steps.toString();
        //return route.overviewPolyline.getEncodedPath();
    }
}
