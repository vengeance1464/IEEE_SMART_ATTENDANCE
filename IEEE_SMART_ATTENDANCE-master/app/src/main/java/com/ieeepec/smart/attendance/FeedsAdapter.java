package com.ieeepec.smart.attendance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class FeedsAdapter {
    //ArrayList<IEEE_FEEDS> data;
    //Context ctx;

    /*public FeedsAdapter(Context ctx, ArrayList<IEEE_FEEDS> al) {
        this.ctx = ctx;
        this.data = al;
    }*/
   /* public FeedsAdapter(FirestoreRecyclerOptions<IEEE_FEEDS> feedsResponse)
    {
        super(feedsResponse);
    }

    @NonNull
    @Override
    public FeedsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        ViewHolder vh;
        LayoutInflater li = (LayoutInflater) (parent.getContext().getSystemService(LAYOUT_INFLATER_SERVICE));
        v = li.inflate(R.layout.feeds_list, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull IEEE_FEEDS model) {
        String text1 = model.getDate();
        String text2 = model.getName();
        String text3 = model.getDescription();
        holder.txt1.setText(text1);
        holder.txt2.setText(text2);
        holder.txt3.setText(text3);

    }
*/
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt1;
        TextView txt2;
        TextView txt3;

        public ViewHolder(View itemView) {
            super(itemView);
            txt1 = itemView.findViewById(R.id.date);
            txt2 = itemView.findViewById(R.id.titleBlog);
            txt3 = itemView.findViewById(R.id.txtdescription);
        }
    }
}