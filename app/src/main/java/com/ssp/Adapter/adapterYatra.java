package com.ssp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssp.Model.beanYatra;
import com.ssp.R;

import java.util.ArrayList;

/**
 * Created by DELL on 12/9/2017.
 */

public class adapterYatra extends RecyclerView.Adapter<adapterYatra.RecyclerViewHolder> {

    Context context;
    ArrayList<beanYatra> event;
    LayoutInflater inflater;

    public adapterYatra(Context context, ArrayList<beanYatra> event) {
        this.event = event;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.app_yatradetail, parent, false);
        RecyclerViewHolder holder = new RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.txtYatraNo.setText(""+event.get(position).getYatraNo());
        holder.txtSpotNo.setText(""+event.get(position).getSpotNo());
        holder.txtDatetime.setText(event.get(position).getYatraTime());
        holder.txtUpDown.setText(event.get(position).getYatraUpDown());
    }

    @Override
    public int getItemCount() {
        return event.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView txtYatraNo, txtSpotNo, txtDatetime, txtUpDown;


        public RecyclerViewHolder(View itemView) {
            super(itemView);

            txtYatraNo = (TextView) itemView.findViewById(R.id.txtYatraNumber);
            txtSpotNo = (TextView) itemView.findViewById(R.id.txtSNumber);
            txtDatetime = (TextView) itemView.findViewById(R.id.txtDateTime);
            txtUpDown = (TextView) itemView.findViewById(R.id.txtUpDown);
        }
    }
}
