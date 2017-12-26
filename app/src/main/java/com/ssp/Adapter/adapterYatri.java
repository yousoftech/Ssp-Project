package com.ssp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ssp.Model.YatriDetails;
import com.ssp.R;

import java.util.ArrayList;

/**
 * Created by DELL on 12/20/2017.
 */

public class adapterYatri extends RecyclerView.Adapter<adapterYatri.RecyclerViewHolder> {
    Context context;
    ArrayList<YatriDetails> event;
    LayoutInflater inflater;

    public adapterYatri(Context context, ArrayList<YatriDetails> event) {
        this.event = event;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.app_adminreport, parent, false);
        RecyclerViewHolder holder = new RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.txtYatriNumber.setText("Yatri No: " + event.get(position).getStrUserCode());
        holder.txtYatriName.setText("" + event.get(position).getStrUserName());
    }

    @Override
    public int getItemCount() {
        return event.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView txtYatriNumber, txtYatriName;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            txtYatriNumber = (TextView) itemView.findViewById(R.id.txtYatriAdminNumber);
            txtYatriName = (TextView) itemView.findViewById(R.id.txtYatriAdminName);
        }
    }
}
