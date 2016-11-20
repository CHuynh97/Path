package hma.path;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    LinkedList<EditText[]> tasks = new LinkedList<EditText[]>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

                final Location startLocation = (Location) getIntent().getExtras().getSerializable("base_loc");
                List<Location> locationList = new ArrayList<Location>();
                locationList.add(new Location("1235 Upper Village Dr", "Mississauga", "ON", "L5E3J6", 10));
                locationList.add(new Location("2800 Erin Centre Blvd", "Mississauga", "ON", "L5M6R5", 12));
                locationList.add(new Location("2840 Duncairn Dr", "Mississauga", "ON", "L5M5C6", 13));
                Location finalLoc = locationList.get(locationList.size() - 1);
                locationList.remove(finalLoc);
                startLocation.setTimeLeft(System.currentTimeMillis());
                ShortestPath.getShortestAnalPath(startLocation, locationList, finalLoc);
                long minTime = ShortestPath.getMinimumTime();
                ArrayList<Location> locations = ShortestPath.getFinalPath();

                Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
                intent.putExtra("minTime", minTime);
                intent.putExtra("path", locations);
                startActivity(intent);
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


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


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://hma.path/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://hma.path/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
