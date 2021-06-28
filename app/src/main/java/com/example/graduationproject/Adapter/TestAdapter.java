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
import com.example.graduationproject.Data.Chat;
import com.example.graduationproject.Data.TestData;
import com.example.graduationproject.Fragments.ChangePasswordFragment;
import com.example.graduationproject.Fragments.ChatBotFragment;
import com.example.graduationproject.Fragments.ChatMessageFragment;
import com.example.graduationproject.Fragments.LearnMoreFragment;
import com.example.graduationproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestAdapterViewHolder> {
    List<TestData> testDataList;
    Context context;
    DatabaseReference databaseAllReference;
    FirebaseUser firebaseUser;
   public TestAdapter(List<TestData> testDataList,Context context)
    {
       this.testDataList=testDataList;
       this.context=context;
        databaseAllReference= FirebaseDatabase.getInstance().getReference();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

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
               AppCompatActivity activity =  (AppCompatActivity)view.getContext();
                ChatBotFragment messageFragment=new ChatBotFragment();
                Bundle bundle=new Bundle();
                bundle.putString("name", testDataList.get(position).getRightName());
                bundle.putInt("image", testDataList.get(position).getRightImage());

               if(testDataList.get(position).getRightName().equals("Depression")){
                   startTestWithFirstQuestion(testDataList.get(position).getRightName(),"How often have you been bothered by feeling down,depressed," +
                           " irritable, or hopeless over the last two weeks?");

               }if(testDataList.get(position).getRightName().equals("Borderline personality disorder (BPD)")){
                    startTestWithFirstQuestion("Borderline personality disorder (BPD)","Is the social and emotional relationship unstable ?");

                }if(testDataList.get(position).getRightName().equals("Anixiety")){
                    startTestWithFirstQuestion("Anixiety","How often have you been bothered by feeling nervous," +
                            " anxious or on edge over the last two weeks?");

                }if(testDataList.get(position).getRightName().equals("Stress")){
                    startTestWithFirstQuestion("Stress","Do you experience any of the following symptoms: headaches, chest pain," +
                            " muscle tension, nausea, or changes in sex drive?");
                }if(testDataList.get(position).getRightName().equals("Alcohol Addiction")){
                    startTestWithFirstQuestion("Alcohol Addiction","Do you lose time from work or school due to drinking?");

                }if(testDataList.get(position).getRightName().equals("Drug Addiction")){
                    startTestWithFirstQuestion("Drug Addiction","Do you feel like you need to use the drug regularly?");

                }if(testDataList.get(position).getRightName().equals("Psychosexual Dysfunction")){
                    startTestWithFirstQuestion("Psychosexual Dysfunction","Over the past month, how many days have you experienced a sexual impulse?");

                }if(testDataList.get(position).getRightName().equals("Attention-Deficit Hyperactivity Disorder (ADHD)")){
                    startTestWithFirstQuestion("Attention-Deficit Hyperactivity Disorder (ADHD)","HOW OFTEN ARE YOU GETTING CALLS FROM PRESCHOOL?");

                }if(testDataList.get(position).getRightName().equals("low self esteem")){
                    startTestWithFirstQuestion("low self esteem","HOW OFTEN ARE YOU GETTING CALLS FROM PRESCHOOL?");

                }if(testDataList.get(position).getRightName().equals("Bipolar Disorder")){
                    startTestWithFirstQuestion("Bipolar Disorder","You experienced feelings of anguish or desperation:");

                }if(testDataList.get(position).getRightName().equals("Mania")){
                    startTestWithFirstQuestion("Mania","Do you ever experience a persistent elevated or irritable mood for more than a week?");

                }if(testDataList.get(position).getRightName().equals("Narcissistic Personality Disorder")){
                    startTestWithFirstQuestion("Narcissistic Personality Disorder","Do you experience an exaggerated sense of self-importance?");

                }if(testDataList.get(position).getRightName().equals("Empathy Deficit Disorder")){
                    startTestWithFirstQuestion("Empathy Deficit Disorder","I find it hard to feel sympathetic for someone who is experiencing unkind or unfair behavior");

                }if(testDataList.get(position).getRightName().equals("Dissociative Identity Disorder (DID)")){
                    startTestWithFirstQuestion("Dissociative Identity Disorder (DID)","Do you feel the radical change towards your thoughts and your behavior ?");

                }if(testDataList.get(position).getRightName().equals("Illness anxiety disorder")){
                    startTestWithFirstQuestion("Illness anxiety disorder","How often do you worry about your health?");

                }if(testDataList.get(position).getRightName().equals("Psychosis")){
                    startTestWithFirstQuestion("Psychosis","I sometimes see things that others tell me they cannot see");

                }if(testDataList.get(position).getRightName().equals("Autism")){
                    startTestWithFirstQuestion("Autism","is old-fashioned or precocious?");

                }if(testDataList.get(position).getRightName().equals("Pseudobulbar Affect (PBA)")){
                    startTestWithFirstQuestion("Pseudobulbar Affect (PBA)","There are times when I feel fine one minute, and then I’ll become tearful the next over something small or for no reason at all");

                }if(testDataList.get(position).getRightName().equals("Social Anxiety Disorder")){
                    startTestWithFirstQuestion("Social Anxiety Disorder","Do you feel anxious or panicky before social situations?");

                }if(testDataList.get(position).getRightName().equals("Bullying")){
                    startTestWithFirstQuestion("Bullying","Do others make hurtful comments about you?");

                }if(testDataList.get(position).getRightName().equals("Imposter syndrome")){
                    startTestWithFirstQuestion("Imposter syndrome","I think my success was just a coincidence.");

                }if(testDataList.get(position).getRightName().equals("Schizophrenia")){
                    startTestWithFirstQuestion("Schizophrenia","Do you hear or see any things other people cannot see?");

                }if(testDataList.get(position).getRightName().equals("Obsessive-Compulsive Disorder (OCD)")){
                    startTestWithFirstQuestion("Obsessive-Compulsive Disorder (OCD)","1-Do you ever experience repetitive thoughts that cause you anxiety?");

                }if(testDataList.get(position).getRightName().equals("Posttraumatic stress disorder (PTSD)")){
                    startTestWithFirstQuestion("Posttraumatic stress disorder (PTSD)","1-Any reminder brought back feelings about the event/s");

                }if(testDataList.get(position).getRightName().equals("Eating Disorder")){
                    startTestWithFirstQuestion("Eating Disorder","Do you ever eat more food than planned at a meal?");

                }
                //set Fragmentclass Arguments
                messageFragment.setArguments(bundle);

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,messageFragment).commit();

            }

        });

    }
    private void startTestWithFirstQuestion(String ilness, String question) {
        sendUserMessage(ilness+" test");
        sendBotMessage("Starting "+ilness+" test ....!");
        sendBotMessage(question);
    }
    private void sendUserMessage(String msg) {
        Chat chat3=new Chat(firebaseUser.getUid(),"Bot",msg);
        databaseAllReference.child("ChatBot").child(firebaseUser.getUid()).push().setValue(chat3);

    } private void sendBotMessage(String msg) {
        Chat chat3=new Chat("Bot",firebaseUser.getUid(),msg);
        databaseAllReference.child("ChatBot").child(firebaseUser.getUid()).push().setValue(chat3);

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
