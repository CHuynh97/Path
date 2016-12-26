package hma.path.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import hma.path.mapcontrol.Location;
import hma.path.mapcontrol.MapManager;
import hma.path.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GoogleMapsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GoogleMapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoogleMapsFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String START_LOC_KEY = "START_LOCATION";
    private static final String END_LOC_KEY = "END_LOCATION";
    private static final String PATH_COORDINATES_KEY = "PATH_COORDINATES";
    private static final String PATH_INSTRUCTIONS_KEY = "PATH_INSTRUCTIONS";

    // TODO: Rename and change types of parameters
    private Location startLocation;
    private Location endLocation;
    private List<LatLng> pathLine;
    private String instructions;

    private SupportMapFragment mapFragment;
    private GoogleMap map;

    //private OnFragmentInteractionListener mListener;

    public GoogleMapsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param start Start location.
     * @param end End location.
     * @param path List of LatLng objects which create the path from start to end
     * //@param instructions Route directions to be displayed in the TextView
     * @return A new instance of fragment GoogleMapsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GoogleMapsFragment newInstance(Location start, Location end, ArrayList<LatLng> path) {
        GoogleMapsFragment fragment = new GoogleMapsFragment();

        Bundle args = new Bundle();
        args.putSerializable(START_LOC_KEY, start);
        args.putSerializable(END_LOC_KEY, end);
        args.putSerializable(PATH_COORDINATES_KEY, path);
        //args.putString(PATH_INSTRUCTIONS_KEY, instructions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            startLocation = (Location)getArguments().get(START_LOC_KEY);
            endLocation = (Location)getArguments().get(END_LOC_KEY);
            pathLine = (ArrayList)getArguments().get(PATH_COORDINATES_KEY);
            instructions = getArguments().getString(PATH_INSTRUCTIONS_KEY);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_google_maps, container, false);

        final TextView directions = (TextView)view.findViewById(R.id.directions_tv);
        directions.setTextColor(Color.BLACK);


        FloatingActionButton showDirections = (FloatingActionButton)view.findViewById(R.id.show_directions_fab);
        showDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (directions.getVisibility() == View.INVISIBLE) {
                    directions.setVisibility(View.VISIBLE);
                } else if (directions.getVisibility() == View.VISIBLE) {
                    directions.setVisibility(View.INVISIBLE);
                }
            }
        });

        mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map_view);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng startCoords = MapManager.convertToLatLng(startLocation);
        LatLng endCoords = MapManager.convertToLatLng(endLocation);

        map.addMarker(new MarkerOptions().position(startCoords));
        map.addMarker(new MarkerOptions().position(endCoords));
        //CameraPosition position = new CameraPosition(startCoords, 13, 0, 0);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(startCoords, 13));

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(pathLine);
        polylineOptions.color(Color.BLUE);
        map.addPolyline(polylineOptions);

        /*
        try {
            map.setMyLocationEnabled(true);
        }
        catch (SecurityException se) {

        }
        */

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */
    }

    @Override
    public void onDetach() {
        mapFragment.onDetach();
        super.onDetach();
        //mListener = null;
    }

    @Override
    public void onDestroy() {
        mapFragment.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        mapFragment.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mapFragment.onResume();
        super.onResume();
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String newLoc);
    }
    */

}
