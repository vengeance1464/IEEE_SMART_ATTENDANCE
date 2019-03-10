package com.ieeepec.smart.attendance;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Frag_events extends Fragment {
    ArrayList<IEEE_EVENT> ieee_events = new ArrayList<>();

    public Frag_events() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_frag_events, container, false);
        RecyclerView recyclerView = getActivity().findViewById(R.id.rvEvents);
        EventsAdapter eventsAdapter = new EventsAdapter(getActivity(), ieee_events);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference dr = db.collection("Events").document();
        recyclerView.setAdapter(eventsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
