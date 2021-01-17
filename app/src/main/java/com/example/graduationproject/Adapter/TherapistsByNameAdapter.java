package com.example.graduationproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Fragments.CartFragment;
import com.example.graduationproject.Fragments.TherapyDataFragment;
import com.example.graduationproject.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TherapistsByNameAdapter extends RecyclerView.Adapter <TherapistsByNameAdapter.ViewHolder>{

    private Context mContext;
    String name[],description[];


    public TherapistsByNameAdapter(Context mContext, String[] name, String[] description) {
        this.mContext = mContext;
        this.name = name;
        this.description = description;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.therapists_custom_item,parent,false);
        return new TherapistsByNameAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.therapyName.setText(name[position]);
        holder.therapyDescription.setText(description[position]);

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
        return description.length;
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
