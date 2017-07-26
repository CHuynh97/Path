package hma.path.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import hma.path.mapcontrol.Location;
import hma.path.R;
import hma.path.mapcontrol.MapManager;

public class HomePage extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{
    GoogleApiClient apiClient;
    LatLng currentLatLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        apiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        final boolean demo = true;

        final EditText editText = (EditText)findViewById(R.id.home_search);


        int gPlayAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplicationContext());
        String defText = "Google Play Services: " + String.valueOf(gPlayAvailable);
        editText.setText(defText);
        final CheckBox currentLocationCB = (CheckBox)findViewById(R.id.currentloc_cb);

        Button next = (Button)findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                final boolean useCurrent = currentLocationCB.isChecked();
                intent.putExtra("USE_CURRENT", useCurrent);
                String baseLoc;
                if (demo) {
                   baseLoc = "2242 Yorktown Circle, Mississauga, ON, L5M5X9";
                    if (useCurrent) {
                        intent.putExtra("CURRENT_LATLNG", currentLatLng);
                    }
                }
                else {
                    if (useCurrent) {
                        intent.putExtra("CURRENT_LATLNG", currentLatLng);
                    }
                    else {
                        baseLoc = editText.getText().toString();
                    }
                }
                intent.putExtra("BASE_LOCATION", baseLoc);
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

    @Override
    public void onStart() {
        apiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        apiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "Failed to connect to Google Play Services: "
                + connectionResult.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Toast.makeText(getApplicationContext(), "Connection to Google Play Services Suspended", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(120000);
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, new LocationListener() {
                @Override
                public void onLocationChanged(android.location.Location location) {
                    currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                }
            });
        }
        catch (SecurityException se) {


        }

    }



    private void getCurrentLatLng() {
        try {


            android.location.Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
            if (currentLocation != null) {
                currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            }
            else {
                currentLatLng = new LatLng(0, 0);
            }
        }
        catch (SecurityException se) {
            currentLatLng = new LatLng(-1, -1);
        }
    }


}
