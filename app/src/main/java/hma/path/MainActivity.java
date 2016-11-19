package hma.path;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.maps.model.DistanceMatrixElement;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {
    LinkedList<EditText[]> tasks = new LinkedList<EditText[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final LinearLayout LLayout = (LinearLayout) findViewById(R.id.LinLayout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText[] entry = addTask(LLayout);
                tasks.add(entry);
            }
        });

        //String[] parsed = Location.parseAdress;
        //Location location = new Locatrion(pasrsed);
        Button btnSubmit = new Button(getApplicationContext());
        btnSubmit.setText("Submit");
        LLayout.addView(btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Location> locationList = getData(tasks);
                DistanceMatrixElement[] durations = MapManager.getLocationsFrom(locationList.remove(0), locationList);
                int size = locationList.size();
                long minTime = durations[0].duration.inSeconds;
                for (int i = 1; i < size; i++) {
                    long elapsedTime = durations[i].duration.inSeconds;
                    if (elapsedTime < minTime) {
                        minTime = elapsedTime;
                    }
                }
            }
        });

    }


    public Location

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public EditText[] addTask(LinearLayout layout) {
        TextView locationHeader = new TextView(getApplicationContext());
        locationHeader.setTextColor(Color.BLACK);
        locationHeader.setText("Location:");
        layout.addView(locationHeader);
        EditText location = new EditText(getApplicationContext());
        location.setTextColor(Color.BLACK);
        location.setBackgroundResource(R.drawable.edittext_shape);
        layout.addView(location);

        TextView timeHeader = new TextView(getApplicationContext());
        timeHeader.setTextColor(Color.BLACK);
        timeHeader.setText("Time:");
        layout.addView(timeHeader);
        EditText timeDuration = new EditText(getApplicationContext());
        timeDuration.setTextColor(Color.BLACK);
        timeDuration.setBackgroundResource(R.drawable.edittext_shape);
        layout.addView(timeDuration);
        return new EditText[]{location, timeDuration};
    }


    public List<Location> getData(List<EditText[]> list) {
        List<Location> results = new ArrayList<>();
        for (EditText[] entry : list) {
            String[] address = Location.parseAdress(entry[0].getText().toString());
            int duration = Integer.parseInt(entry[1].getText().toString());
            Location location = new Location(address, duration);
            results.add(location);
        }
        return results;
    }


}
