package hma.path;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    LinkedList<EditText[]> tasks = new LinkedList<EditText[]>();
    ArrayList<Location> locationList = new ArrayList<>();

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
                int size = tasks.size();
                for (int i = 0; i < size; i++) {
                    String info = tasks.get(i)[0].getText().toString() + "\n" + tasks.get(i)[1].getText().toString() + "\n";
                    TextView printer = new TextView(getApplicationContext());
                    printer.setText(info);
                    printer.setTextColor(Color.BLACK);
                    LLayout.addView(printer);
                }
            }
        });

    }

    public EditText[] addTask(LinearLayout layout, int entryCount) {

        View entry = getLayoutInflater().inflate(R.layout.task_layout, layout);
        if (entry instanceof ViewGroup) {
            EditText[] results = new EditText[2];
            results[0] = (EditText)entry.findViewById(R.id.task_location);
            results[0].setId(R.id.task_location + entryCount*2);
            results[1] = (EditText)entry.findViewById(R.id.task_duration);
            results[1].setId(R.id.task_duration + entryCount*2);
            return results;
        }
        return null;

    }

    public void getData(List<EditText[]> list) {

        for (EditText[] entry : list) {
            String[] address = Location.parseAdress(entry[0].getText().toString());
            int duration = Integer.parseInt(entry[1].getText().toString());
            Location location = new Location(address, duration);
            locationList.add(location);
        }

    }


}
