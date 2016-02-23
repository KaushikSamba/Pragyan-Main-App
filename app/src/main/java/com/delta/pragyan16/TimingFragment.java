package com.delta.pragyan16;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String eventName;

    private OnFragmentInteractionListener mListener;

    public TimingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment TimingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimingFragment newInstance(String param1) {
        TimingFragment fragment = new TimingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventName = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_timing, container, false);
        TextView startTime = (TextView)v. findViewById(R.id.startTime);
        EventsAdapter adapter = new EventsAdapter(getActivity());
        EventInfo eventInfo = adapter.getEventInfo(eventName);
        startTime.setText(eventInfo.start_time);
        TextView endTime = (TextView)v. findViewById(R.id.endTime);
        endTime.setText(eventInfo.end_time);
        TextView date = (TextView)v. findViewById(R.id.eventDate);
        date.setText(eventInfo.date);
        TextView day = (TextView) v.findViewById(R.id.eventDay);
        switch(eventInfo.date){
            case "2016-02-28":
                day.setText("Day 3");
                break;
            case "2016-02-27":
                day.setText("Day 2");
                break;
            case "2016-02-26":
                day.setText("Day 1");
                break;
            default:
                day.setText("Day 0");

        }
        TextView venue = (TextView)v. findViewById(R.id.eventVenue);
        venue.setText(eventInfo.venue);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
