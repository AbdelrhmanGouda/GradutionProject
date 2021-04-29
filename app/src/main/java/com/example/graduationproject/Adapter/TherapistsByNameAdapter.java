package com.example.graduationproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Data.TherapistsByNameData;
import com.example.graduationproject.Fragments.TherapyDataFragment;
import com.example.graduationproject.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TherapistsByNameAdapter extends RecyclerView.Adapter <TherapistsByNameAdapter.ViewHolder>{

    private Context mContext;
    List<TherapistsByNameData> therapistsData;


    public TherapistsByNameAdapter(Context mContext, List<TherapistsByNameData> getData) {
        this.mContext = mContext;
        this.therapistsData = getData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.therapists_custom_item,parent,false);
        return new TherapistsByNameAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ViewHolder viewHolder = holder;
        TherapistsByNameData model = therapistsData.get(position);
        viewHolder.therapyName.setText(model.getName());
        viewHolder.clinicLocation.setText(model.getLocation());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                TherapyDataFragment fragment = new TherapyDataFragment();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });

    }




    @Override
    public int getItemCount() {
        return therapistsData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView therapyPhoto;
        TextView therapyName,therapyDescription,sessionCost,clinicLocation;
        RatingBar therapyRate;
        Button book;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            therapyPhoto = itemView.findViewById(R.id.therapy_photo);
            therapyName = itemView.findViewById(R.id.therapy_name);
            therapyDescription = itemView.findViewById(R.id.therapy_description);
            sessionCost = itemView.findViewById(R.id.therapy_session_cost);
            clinicLocation = itemView.findViewById(R.id.therapy_clinic_location);
            therapyRate = itemView.findViewById(R.id.therapy_rate_bar);
            book = itemView.findViewById(R.id.therapy_book_button);


        }
    }
}
