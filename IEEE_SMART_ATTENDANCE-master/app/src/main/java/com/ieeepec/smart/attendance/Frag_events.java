package com.ieeepec.smart.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class Frag_events extends Fragment {
    ArrayList<IEEE_EVENT> ieee_events = new ArrayList<>();
    private FirestoreRecyclerAdapter eAdapter;
   // private String qr;
    //private ArrayList<String>regList;
    private String android_id;
    //private int id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.event_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        //EventsAdapter eAdapter = new EventsAdapter(getActivity(), ieee_events);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
       // DocumentReference dr = db.collection("Events").document();
        Query exploreDataQuery=db.collection("Event");
        FirestoreRecyclerOptions<IEEE_EVENT> feedResponse = new FirestoreRecyclerOptions.Builder<IEEE_EVENT>()
                .setQuery(exploreDataQuery, IEEE_EVENT.class)
                .build();
        eAdapter =new FirestoreRecyclerAdapter<IEEE_EVENT,EventsAdapter.ViewHolder>(feedResponse){


            @NonNull
            @Override
            public EventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View v;
                EventsAdapter.ViewHolder vh;
                android_id=Settings.Secure.getString(getContext().getContentResolver(),Settings.Secure.ANDROID_ID);
                LayoutInflater li = (LayoutInflater) (viewGroup.getContext().getSystemService(LAYOUT_INFLATER_SERVICE));
                v = li.inflate(R.layout.event_items, viewGroup, false);
                vh = new EventsAdapter.ViewHolder(v);
                return vh;
            }

            @Override
            protected void onBindViewHolder(@NonNull EventsAdapter.ViewHolder holder, int position, @NonNull final IEEE_EVENT model) {
                String date = model.getDate();
                String name =model.getName();
                //id=model.getId();
                 //flag=0;
                //qr=model.getQr();
                //regList=model.getReg();
                holder.textView1.setText(date);
                holder.textView2.setText(name);
                if(FirebaseAuth.getInstance().getCurrentUser()==null)
                {
                    holder.button1.setVisibility(View.GONE);
                    holder.button2.setVisibility(View.GONE);
                }else
                {
                    holder.button1.setVisibility(View.VISIBLE);
                    holder.button2.setVisibility(View.VISIBLE);
                }
                holder.button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getActivity(),Barcode.class);
                        intent.putExtra("qr",model.getQr());
                        intent.putExtra("did",android_id);
                        intent.putExtra("id",model.getId());
                        intent.putStringArrayListExtra("reg",model.getReg());
                        if((!model.getReg().isEmpty())&&model.getReg().contains(android_id))
                        {
                            Toast.makeText(getContext(),"Only one attendance from device allowed",Toast.LENGTH_SHORT).show();
                        }else
                        {
                            startActivity(intent);
                        }


                    }
                });
            }

        };
        this.eAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(this.eAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        eAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        eAdapter.stopListening();
    }
}
