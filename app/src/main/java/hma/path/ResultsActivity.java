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
        String patheronies = "";
        for(Location loc : paths) {
            patheronies = patheronies + loc.getAddressName() + "\n";
        }
        TextView pathTaken = new TextView(getApplicationContext());
        pathTaken.setTextColor(Color.BLACK);
        pathTaken.setText(patheronies);
        LLayout.addView(pathTaken);

    }
}
