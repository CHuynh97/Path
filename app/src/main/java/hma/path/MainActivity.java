package hma.path;


import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{
    LinkedList<EditText[]> tasks = new LinkedList<EditText[]>();
    boolean demo = true;



    public int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    public int minute = Calendar.getInstance().get(Calendar.MINUTE);

    public Button timeSetUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LinearLayout LLayout = (LinearLayout) findViewById(R.id.map_entry_layout);

        timeSetUp = (Button)findViewById(R.id.time_setup);
        timeSetUp.setText(hour + ":" + minute);
        timeSetUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(MainActivity.this, MainActivity.this, hour, minute, false);
                tpd.show();
            }
        });


        Button addEntry = (Button) findViewById(R.id.addentry_btn);
        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText[] entry = addTask(LLayout, tasks.size()+1);
                tasks.add(entry);
            }
        });

       // TimePickerDialog timePickerDialog =

        FloatingActionButton submitFAB = (FloatingActionButton)findViewById(R.id.submit_fab);
        submitFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        Location finalLoc = new Location("2840 Duncairn Dr", "Mississauga", "ON", "L5M5C6", 13);
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
        Location finalLoc = new Location("2840 Duncairn Dr", "Mississauga", "ON", "L5M5C6", 13);
        return ShortestPath.shortestPath(startLocation, locationList, finalLoc, System.currentTimeMillis());
    }


    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        this.hour = hour;
        this.minute = minute;


        StringBuilder display = new StringBuilder();

        if (hour == 12 || hour == 0) {
            display.append("12:");
        }
        else if (hour > 12) {
            display.append(hour % 12 + ":");
        }
        else {
            display.append(hour + ":");
        }

        if (minute < 10) {
            display.append(0);
        }
        display.append(minute + ":");

        if (hour >= 12) {
            display.append(" pm");
        }
        else {
            display.append(" am");
        }


        timeSetUp.setText(display.toString());


    }
}
