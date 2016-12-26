package hma.path.activities;


import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hma.path.fragments.GoogleMapsFragment;
import hma.path.mapcontrol.Location;
import hma.path.mapcontrol.MapManager;
import hma.path.R;


public class ResultsActivity extends AppCompatActivity {
    GoogleMapsFragment mapsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        long timeTaken = getIntent().getExtras().getLong("minTime");
        TextView locationHeader = new TextView(getApplicationContext());
        locationHeader.setTextColor(Color.BLACK);
        LinearLayout LLayout = (LinearLayout) findViewById(R.id.results);
        LLayout.setOrientation(LinearLayout.VERTICAL);
        locationHeader.setText("Time: \n" + new Date(timeTaken).toString() + "\n");
        LLayout.addView(locationHeader);

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


        DirectionsRoute route = MapManager.getRoute(List2.get(0), List2.get(1), System.currentTimeMillis());
        mapsFragment = GoogleMapsFragment.newInstance(List2.get(0), List2.get(1), getPath(route));

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(LLayout.getId(), mapsFragment);
        fragmentTransaction.commit();

    }

    public ArrayList<LatLng> getPath(DirectionsRoute route) {
        try {
            DirectionsLeg[] legs = route.legs;
            DirectionsStep[] legSteps = legs[0].steps;
            //StringBuilder text = new StringBuilder();
            List<com.google.maps.model.LatLng> path = new ArrayList<>();
            for (DirectionsStep step : legSteps) {
                path.addAll(step.polyline.decodePath());
            }
            ArrayList<LatLng> result = new ArrayList<>();
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
    }

    @Override
    public void onPause() {
        super.onPause();
        mapsFragment.onPause();
    }


}
