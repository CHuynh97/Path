package hma.path;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GoogleMapsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GoogleMapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoogleMapsFragment extends Fragment implements OnMapReadyCallback{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String START_LOC_KEY = "START_LOCATION";
    private static final String END_LOC_KEY = "END_LOCATION";

    // TODO: Rename and change types of parameters
    private Location startLocation;
    private Location endLocation;
    private MapView mapView;
    private GoogleMap map;

    private OnFragmentInteractionListener mListener;

    public GoogleMapsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param start Start location.
     * @param end End location.
     * @return A new instance of fragment GoogleMapsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GoogleMapsFragment newInstance(String start, String end) {
        GoogleMapsFragment fragment = new GoogleMapsFragment();
        Bundle args = new Bundle();
        args.putString(START_LOC_KEY, start);
        args.putString(END_LOC_KEY, end);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            startLocation = (Location)getArguments().get(START_LOC_KEY);
            endLocation = (Location)getArguments().get(END_LOC_KEY);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_google_maps, container, false);
        mapView = (MapView)view.findViewById(R.id.map_view);
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng startCoords = new LatLng(43.655096, -79.386308);
        LatLng endCoords = new LatLng(43.472435, -80.543306);
        map.addMarker(new MarkerOptions().position(startCoords));
        map.addPolyline(new PolylineOptions().add(startCoords).add(endCoords));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
