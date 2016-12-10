package hma.path;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    LinkedList<EditText[]> tasks = new LinkedList<EditText[]>();
    boolean demo = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final LinearLayout LLayout = (LinearLayout) findViewById(R.id.LinLayout);

        Button addEntry = (Button) findViewById(R.id.addentry_btn);
        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText[] entry = addTask(LLayout, tasks.size()+1);
                tasks.add(entry);
            }
        });


        //String[] parsed = Location.parseAdress;
        //Location location = new Locatrion(pasrsed);
        Button btnSubmit = (Button)findViewById(R.id.submit_entriesbtn);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                ArrayList<Location> locations = null;
                int[] order = null;
                if (demo) {
                    locations = runSimulation();
                    order = runSimulation2();
                }
                else {
                    final Location startLocation = (Location) getIntent().getExtras().getSerializable("base_loc");
                    locations = getData(tasks);
                    Location finalLoc = locations.remove(locations.size()-1);
                    //hard code current time. Late implement requested start time;
                    startLocation.setTimeLeft(System.currentTimeMillis());
                    ShortestPath.getShortestPath(startLocation, locations, finalLoc);
                    locations = ShortestPath.getFinalPath();
                }
                long minTime = ShortestPath.getMinimumTime();

                Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
                intent.putExtra("minTime", minTime);
                intent.putExtra("path", locations);
                intent.putExtra("order", order);
                intent.putExtra("OGList", getData(tasks));
                startActivity(intent);

            }
        });
    }

    public EditText[] addTask(LinearLayout layout, int entryCount) {
        View entry = getLayoutInflater().inflate(R.layout.task_layout, layout);
        if (entry instanceof ViewGroup) {
            TextView numTab = (TextView)entry.findViewById(R.id.task_count);
            numTab.setText(String.valueOf(entryCount));
            numTab.setId(R.id.task_count + entryCount*3);


            EditText[] results = new EditText[2];
            results[0] = (EditText) entry.findViewById(R.id.task_location);
            results[0].setId(R.id.task_location + entryCount * 3);
            results[1] = (EditText) entry.findViewById(R.id.task_duration);
            results[1].setId(R.id.task_duration + entryCount * 3);
            return results;
        }
        return null;
    }

    public ArrayList<Location> getData(List<EditText[]> list) {
        ArrayList<Location> locationList = new ArrayList<>();
        for (EditText[] entry : list) {
            String[] address = Location.parseAddress(entry[0].getText().toString());
            int duration = Integer.parseInt(entry[1].getText().toString());
            if (address.length != 4) {
                return null;
            }
            Location location = new Location(address, duration);
            locationList.add(location);
        }
        return locationList;
    }

    public ArrayList<Location> runSimulation() {
        final Location startLocation = (Location) getIntent().getExtras().getSerializable("base_loc");
        List<Location> locationList = new ArrayList<Location>();
        locationList.add(new Location("1235 Upper Village Dr", "Mississauga", "ON", "L5E3J6", 10));
        locationList.add(new Location("2800 Erin Centre Blvd", "Mississauga", "ON", "L5M6R5", 12));
        locationList.add(new Location("2840 Duncairn Dr", "Mississauga", "ON", "L5M5C6", 13));
        Location finalLoc = locationList.get(locationList.size() - 1);
        locationList.remove(finalLoc);
        startLocation.setTimeLeft(System.currentTimeMillis());
        ShortestPath.getShortestPath(startLocation, locationList, finalLoc);
        ArrayList<Location> locations = ShortestPath.getFinalPath();
        return locations;
    }

    public int[] runSimulation2() {
        final Location startLocation = (Location) getIntent().getExtras().getSerializable("base_loc");
        List<Location> locationList = new ArrayList<Location>();
        locationList.add(new Location("1235 Upper Village Dr", "Mississauga", "ON", "L5E3J6", 10));
        locationList.add(new Location("2800 Erin Centre Blvd", "Mississauga", "ON", "L5M6R5", 12));
        locationList.add(new Location("2840 Duncairn Dr", "Mississauga", "ON", "L5M5C6", 13));
        Location finalLoc = locationList.get(locationList.size() - 1);
        locationList.remove(finalLoc);
        return ShortestPath.shortestPath(startLocation, locationList, finalLoc, System.currentTimeMillis());
    }
}
