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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class Frag_feeds extends Fragment {
    ArrayList<IEEE_FEEDS> ieee_feeds = new ArrayList<>();

    public Frag_feeds() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_frag_feeds, container, false);
        RecyclerView recyclerView = getActivity().findViewById(R.id.rvFeeds);
        FeedsAdapter feedsAdapter = new FeedsAdapter(getActivity(), ieee_feeds);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference dr = db.collection("Feeds").document();
        recyclerView.setAdapter(feedsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }
}
