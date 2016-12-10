package hma.path;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ArrayList<Location> paths = (ArrayList<Location>) getIntent().getSerializableExtra("path");
        Long timeTaken = getIntent().getExtras().getLong("minTime");
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
        int[] order = getIntent().getExtras().getIntArray("order");
        //ArrayList<Location> ogList = (ArrayList<Location>)getIntent().getSerializableExtra("OGList");
        for (int i : order) {
            patheronies2.append(i + "\n");
        }
        TextView pathTaken2 = new TextView(getApplicationContext());
        pathTaken2.setTextColor(Color.BLUE);
        pathTaken2.setText(patheronies2.toString());
        LLayout.addView(pathTaken2);


    }
}
