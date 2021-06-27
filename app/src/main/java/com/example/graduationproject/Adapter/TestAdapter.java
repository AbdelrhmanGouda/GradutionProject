package com.example.graduationproject.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Data.AllTabData;
import com.example.graduationproject.Data.TestData;
import com.example.graduationproject.Fragments.ChangePasswordFragment;
import com.example.graduationproject.Fragments.ChatBotFragment;
import com.example.graduationproject.Fragments.ChatMessageFragment;
import com.example.graduationproject.Fragments.LearnMoreFragment;
import com.example.graduationproject.R;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestAdapterViewHolder> {
    List<TestData> testDataList;
    Context context;
   public TestAdapter(List<TestData> testDataList,Context context)
    {
       this.testDataList=testDataList;
       this.context=context;
    }
    @NonNull
    @Override
    public TestAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_row, parent, false);
        return new TestAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestAdapterViewHolder holder, int position) {
       TestData testData=testDataList.get(position);
        holder.rightName.setText(testDataList.get(position).getRightName());
        holder.rightImage.setImageResource(testDataList.get(position).getRightImage());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity =  (AppCompatActivity)view.getContext();
                LearnMoreFragment messageFragment=new LearnMoreFragment();
                Bundle bundle=new Bundle();
                bundle.putString("name", testDataList.get(position).getRightName());
                bundle.putInt("image", testDataList.get(position).getRightImage());

                //set Fragmentclass Arguments
                messageFragment.setArguments(bundle);

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,messageFragment).commit();
            }
        }); holder.rightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  AppCompatActivity activity =  (AppCompatActivity)view.getContext();
                ChatMessageFragment messageFragment=new ChatMessageFragment();
                Bundle bundle=new Bundle();
                bundle.putString("name", testData.getRightName());
                bundle.putInt("image", testData.getRightImage());

                //set Fragmentclass Arguments
                messageFragment.setArguments(bundle);

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ChatBotFragment()).commit();
            */
            }

        });

    }

    @Override
    public int getItemCount() {
        return testDataList.size();
    }

    public class TestAdapterViewHolder extends RecyclerView.ViewHolder {
       TextView rightName,learnMore ;
       ImageView rightImage;
       LinearLayout linearLayout;
        public TestAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            rightName=itemView.findViewById(R.id.right_name);
            rightImage=itemView.findViewById(R.id.right_image);
            linearLayout=itemView.findViewById(R.id.learn_more);
            //learnMore=itemView.findViewById(R.id.learn_more);
        }
    }
}
