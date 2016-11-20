package hma.path;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class HomePage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        final EditText editText = (EditText)findViewById(R.id.home_search);

        Button next = (Button)findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Location location = new  Location("2242 Yorktown circle", "mississauga", "ON", "L6M0G2");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("base_loc", location);
                startActivity(intent);
            }
        });
        Button aboutButton = (Button)findViewById(R.id.helpButton);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Help.class);
                startActivity(intent);
            }
        });

    }
}
