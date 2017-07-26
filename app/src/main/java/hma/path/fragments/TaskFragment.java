package hma.path.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import hma.path.R;
import hma.path.mapcontrol.Location;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TaskFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String END_LOCATION_CB = "END_LOCATION_CB";
    private static final String INSTANCE_NUM = "INSTANCE_NUM";
    // TODO: Rename and change types of parameters
    private boolean isEndLocation;
    private boolean isPriority;
    private int instanceNum;

    private EditText locationText;
    private EditText durationText;
    private CheckBox priorityCB;
    private CheckBox endLocCB;

    private OnFragmentInteractionListener mListener;

    public TaskFragment() {
        // Required empty public constructor
    }

    public Location getLocation() {
        String locText = locationText.getText().toString();
        String timeText = durationText.getText().toString();
        long duration = 0;
        if (!timeText.equals("")) {
            duration = Long.parseLong(timeText);
        }
        return new Location(Location.parseAddress(locText), duration);
    }

    public CheckBox getEndLocCB() {
        return endLocCB;
    }

    public boolean isPriority() {
        return isPriority;
    }
    public boolean isEndLocation() {
        return isEndLocation;
    }


    public void setIsEndLocation(boolean set) {
        isEndLocation = set;
        endLocCB.setChecked(set);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param isEndLocation Used to determine is fragment will be marked as end location.
     * @return A new instance of fragment TaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskFragment newInstance(boolean isEndLocation, int instanceNum) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putBoolean(END_LOCATION_CB, isEndLocation);
        args.putInt(INSTANCE_NUM, instanceNum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.isEndLocation = getArguments().getBoolean(END_LOCATION_CB);
            this.instanceNum = getArguments().getInt(INSTANCE_NUM);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_layout, container, false);
        TextView taskCount = (TextView)view.findViewById(R.id.task_count);
        taskCount.setText(String.valueOf(instanceNum));

        locationText = (EditText)view.findViewById(R.id.task_location);
        durationText = (EditText)view.findViewById(R.id.task_duration);

        priorityCB = (CheckBox)view.findViewById(R.id.priority_cb);
        priorityCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPriority = priorityCB.isChecked();
            }
        });

        endLocCB = (CheckBox)view.findViewById(R.id.end_location);
        endLocCB.setChecked(isEndLocation);
        endLocCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEndLocation = endLocCB.isChecked();
                mListener.onTaskEndLocInteraction(TaskFragment.this);
            }
        });

        return view;
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
        void onTaskEndLocInteraction(TaskFragment taskFragment);
        void onDestroyRequest(TaskFragment taskFragment);
    }




}
