package com.leitner.tabbedexample;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab1ActualRecyclerView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab1ActualRecyclerView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab1ActualRecyclerView extends Fragment {
    private Tab1ActualRecyclerView.OnFragmentInteractionListener mListener;
    AlarmDB db;
    public static AlarmAdapterRecycle adapter;
    RecyclerView historicAlarms;

    public Tab1ActualRecyclerView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab2HistoryRecyclerView.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab1ActualRecyclerView newInstance(String param1, String param2) {
        Tab1ActualRecyclerView fragment = new Tab1ActualRecyclerView();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab1_actual_recycler_view, container, false);
        final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.framelayhout_history);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        historicAlarms = (RecyclerView) view.findViewById(R.id.recycler_view);
        RefreshView();


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void RefreshView(){
        db = new AlarmDB(getActivity().getApplication().getApplicationContext());
        db.getWritableDatabase();
        ArrayList<Alarm> jsonList = db.getAllAlarmsActual();
        Log.d("LISTCOUNT", String.valueOf(jsonList.size()));
        Log.d("REFRESHED", "history");
//        if (jsonList.size() > 0){
//            adapter = new AlarmAdapterRecycle(jsonList, getContext(), true);
//            historicAlarms.setAdapter(adapter);
//            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) historicAlarms.getLayoutParams();
//            mlp.setMargins(0, 0, 0, getSoftButtonsBarHeight());
//            noAlarms.setVisibility(View.GONE);
//            Log.d("VISIBILITY", String.valueOf(noAlarms.getVisibility()));
//        }
//        else
        adapter = new AlarmAdapterRecycle(jsonList, getContext(), true);
        historicAlarms.setAdapter(adapter);
    }
}
