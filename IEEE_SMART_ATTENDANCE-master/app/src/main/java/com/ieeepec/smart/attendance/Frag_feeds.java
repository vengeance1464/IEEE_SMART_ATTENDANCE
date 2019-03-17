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

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class Frag_feeds extends Fragment {
    ArrayList<IEEE_FEEDS> ieee_feeds = new ArrayList<>();
    private FirestoreRecyclerAdapter fAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View view=inflater.inflate(R.layout.feeds_recycler, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // DocumentReference dr = db.collection("Events").document();
        Query exploreDataQuery=db.collection("Event");
        /*FirestoreRecyclerOptions<IEEE_FEEDS> feedResponse = new FirestoreRecyclerOptions.Builder<IEEE_FEEDS>()
                .setQuery(exploreDataQuery, IEEE_FEEDS.class)
                .build();*/
        FirestoreRecyclerOptions<IEEE_FEEDS> feedResponse = new FirestoreRecyclerOptions.Builder<IEEE_FEEDS>()
                .setQuery(exploreDataQuery, IEEE_FEEDS.class)
                .build();
        this.fAdapter =new FirestoreRecyclerAdapter<IEEE_FEEDS,FeedsAdapter.ViewHolder>(feedResponse){

            @NonNull
            @Override
            public FeedsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View v;
                FeedsAdapter.ViewHolder vh;
                LayoutInflater li = (LayoutInflater) (viewGroup.getContext().getSystemService(LAYOUT_INFLATER_SERVICE));
                v = li.inflate(R.layout.feeds_list, viewGroup, false);
                vh = new FeedsAdapter.ViewHolder(v);
                return vh;
            }
            @Override
            protected void onBindViewHolder(@NonNull FeedsAdapter.ViewHolder holder, int position, @NonNull IEEE_FEEDS model) {
                String text1 = model.getDate();
                String text2 = model.getName();
                String text3 = model.getDesc();
                holder.txt1.setText(text1);
                holder.txt2.setText(text2);
                holder.txt3.setText(text3);
            }


        };
        fAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(fAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        fAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        fAdapter.stopListening();
    }
}
