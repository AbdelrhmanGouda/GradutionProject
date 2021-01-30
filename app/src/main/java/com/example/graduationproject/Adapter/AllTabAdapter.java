package com.example.graduationproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Data.AllTabData;
import com.example.graduationproject.R;

import java.util.List;

public class AllTabAdapter extends RecyclerView.Adapter<AllTabAdapter.AllTabAdapterViewHolder>{
    List<AllTabData> allTabDataList;
    Context context;
    public AllTabAdapter (List<AllTabData> allTabDataList ,Context context){
        this.allTabDataList=allTabDataList;
        this.context=context;
    }
    @NonNull
    @Override
    public AllTabAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_tab_row, parent, false);
        return new AllTabAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllTabAdapterViewHolder holder, int position) {
        holder.date.setText(allTabDataList.get(position).getDate());
        holder.time.setText(allTabDataList.get(position).getTime());
        holder.state.setText(allTabDataList.get(position).getState());
        holder.docName.setText(allTabDataList.get(position).getDocName());
    }

    @Override
    public int getItemCount() {
        return allTabDataList.size();
    }

    public class AllTabAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView date,time,state,docName;
        public AllTabAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.all_tab_data);
            time=itemView.findViewById(R.id.all_tab_time);
            state=itemView.findViewById(R.id.all_tab_state);
            docName=itemView.findViewById(R.id.all_tab_doctor_name);
        }
    }
}
