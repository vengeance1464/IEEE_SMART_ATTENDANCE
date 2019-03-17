package com.ieeepec.smart.attendance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

class EventsAdapter  {
    //ArrayList<IEEE_EVENT> data;
    //Context ctx;

    /*public EventsAdapter(Context ctx, ArrayList<IEEE_EVENT> al) {

        this.ctx = ctx;
        this.data = al;
    }*/
  /* public EventsAdapter(FirestoreRecyclerOptions<IEEE_EVENT> eventsResponse)
    {
       super(eventsResponse);
    }

    @NonNull
    @Override
    public EventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        EventsAdapter.ViewHolder vh;
        LayoutInflater li = (LayoutInflater) (parent.getContext().getSystemService(LAYOUT_INFLATER_SERVICE));
        v = li.inflate(R.layout.event_items, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

   /* @Override
    public void onBindViewHolder(@NonNull EventsAdapter.ViewHolder holder, int position) {
        String date = data.get(position).getDate();
        String name = data.get(position).getEvent();
        holder.textView1.setText(date);
        holder.textView2.setText(name);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull IEEE_EVENT model) {
        String date = model.getDate();
        String name =model.getEvent();
        holder.textView1.setText(date);
        holder.textView2.setText(name);
    }*/

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView textView2;
        Button button1;
        Button button2;

        public ViewHolder(View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.txtv1);
            textView2 = itemView.findViewById(R.id.txtv2);
            button1 = itemView.findViewById(R.id.btn1);
            button2 = itemView.findViewById(R.id.btn2);
        }
    }
}