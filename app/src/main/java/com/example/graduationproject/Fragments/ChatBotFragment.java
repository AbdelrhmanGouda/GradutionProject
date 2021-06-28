package com.example.graduationproject.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Adapter.MessageAdapter;
import com.example.graduationproject.Data.Chat;
import com.example.graduationproject.Data.TestDegree;
import com.example.graduationproject.R;
import com.example.graduationproject.Remote.IChatBotApi;
import com.example.graduationproject.Remote.RetrofitBot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatBotFragment extends Fragment {

    String id;
    String illness1;
    RecyclerView recyclerView;
    ArrayList<Chat> chats;
    MessageAdapter messageAdapter;
    CircleImageView circleImageView;
    TextView userName;
    Button chooseOne,chooseTwo,chooseThree,chooseFour,chooseFive;
    FirebaseUser firebaseUser;
    DatabaseReference databaseAllReference,databaseReference,reportRefrence;
    EditText textSend;
    ImageButton send;
    String illnessName1;
    int image;
    RelativeLayout relativeLayout;
    LinearLayout linearLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_bot, container, false);

        textSend=view.findViewById(R.id.Edit_text_send);
        send=view.findViewById(R.id.btn_send);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        relativeLayout=view.findViewById(R.id.bottom);
        linearLayout=view.findViewById(R.id.linear_choose);
        recyclerView=view.findViewById(R.id.recycler);
        chooseFive=view.findViewById(R.id.choose_five);
        chooseOne=view.findViewById(R.id.choose_one);
        chooseTwo=view.findViewById(R.id.choose_two);
        chooseThree=view.findViewById(R.id.choose_three);
        chooseFour=view.findViewById(R.id.choose_four);
        circleImageView=view.findViewById(R.id.profile_image);
        userName=view.findViewById(R.id.username);
        Toolbar toolbar=view.findViewById(R.id.toolbar);
       databaseAllReference= FirebaseDatabase.getInstance().getReference();
        reportRefrence=FirebaseDatabase.getInstance().getReference().child("PatientReportChatBot")
                .child(firebaseUser.getUid());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
        final AppCompatActivity activity=(AppCompatActivity)view.getContext();
        activity.setSupportActionBar(toolbar);
        // ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        activity.getSupportActionBar().setTitle("");
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getArguments()!=null){
            illness1=getArguments().getString("name");
            image=getArguments().getInt("image",0);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();

            }
        });
        if(getArguments()!=null){
            id=getArguments().getString("id");

        }

        userName.setText("Bot");
        readMessages(firebaseUser.getUid(), "Bot");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg= textSend.getText().toString();
                if(!msg.equals("")){
                    if(textSend.getInputType()==InputType.TYPE_CLASS_NUMBER){
                        if(Integer.parseInt(msg)>=12
                                &&Integer.parseInt(msg)<100){
                            sendUserMessage(msg);
                            reportRefrence.child("age").setValue(msg);
                            reportRefrence.child("id").setValue(firebaseUser.getUid());
                            sendBotMessage("Are you single?");

                        }else {
                            Toast.makeText(getActivity(), "Age must from 12 to 100 ", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }else if(textSend.getInputType()== InputType.TYPE_CLASS_TEXT) {
                        sendUserMessage(msg);
                        reportRefrence.child("currentState").setValue(msg);
                        sendBotMessage("i see");
                        sendBotMessage("Have you ever been in therapy before?");

                    }else {
                        sendUserMessage(msg);
                        fetchChatBotReply(textSend.getText().toString());

                    }
                    textSend.setText("");

                }else {
                    Toast.makeText(getActivity(), "Cant send empty message!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        chooseOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chooseOne.getText().toString().equals("Ok")){
                    sendBotMessage("How old are you?");
                    setRelativeVisable();
                }
                else if(chooseOne.getText().toString().equals("Yeah!")) {
                    sendUserMessage("Yeah!");
                    reportRefrence.child("relationship").setValue("single");
                    sendBotMessage("Are you still studying or working?");

                }
                else if(chooseOne.getText().toString().equals("Working")){
                    sendUserMessage(chooseOne.getText().toString());
                    reportRefrence.child("workOrStudy").setValue(chooseOne.getText().toString());
                    sendBotMessage("Wow good , what's your job?");
                }else if(chooseOne.getText().toString().equals("Yes")){
                    sendUserMessage(chooseOne.getText().toString());
                   sendBotMessage("Lets start our sesion");
                    sendBotMessage("How do you feel right now ?");
                }else if(chooseOne.getText().toString().equals("Sad")){
                    sendUserMessage(chooseOne.getText().toString());
                    sendBotMessage("Tell me about your problem");
                    sendBotMessage("I'm listening");
                /////
                }else if(chooseOne.getText().toString().equals("Ok let's do Depression test")){

                    startTestWithFirstQuestion("Depression","How often have you been bothered by feeling down," +
                            "depressed, irritable, or hopeless over the last two weeks?");
                }else if(chooseOne.getText().toString().equals("Ok let's do Borderline personality disorder (BPD) test")){

                    startTestWithFirstQuestion("Borderline personality disorder (BPD)","Is the social and emotional relationship unstable ?");
                }else if(chooseOne.getText().toString().equals("Ok let's do Anixiety test")){
                        startTestWithFirstQuestion("Anixiety","How often have you been bothered by feeling nervous," +
                                " anxious or on edge over the last two weeks?");
                }else if(chooseOne.getText().toString().equals("Ok let's do Stress test")){
                    startTestWithFirstQuestion("Stress","Do you experience any of the following symptoms: headaches, chest pain," +
                            " muscle tension, nausea, or changes in sex drive?");
                }else if(chooseOne.getText().toString().equals("Ok let's do Alcohol Addiction test")){
                    startTestWithFirstQuestion("Alcohol Addiction","Do you lose time from work or school due to drinking?");
                }else if(chooseOne.getText().toString().equals("Ok let's do Drug Addiction test")){
                    startTestWithFirstQuestion("Drug Addiction","Do you feel like you need to use the drug regularly?");
                }else if(chooseOne.getText().toString().equals("Ok let's do Psychosexual Dysfunction test")){
                    startTestWithFirstQuestion("Psychosexual Dysfunction","Over the past month, how many days have you experienced a sexual impulse?");
                }else if(chooseOne.getText().toString().equals("Ok let's do Attention-Deficit Hyperactivity Disorder (ADHD) test")){
                    startTestWithFirstQuestion("Attention-Deficit Hyperactivity Disorder (ADHD)","HOW OFTEN ARE YOU GETTING CALLS FROM PRESCHOOL?");
                }else if(chooseOne.getText().toString().equals("Ok let's do low self esteem test")){
                    startTestWithFirstQuestion("low self esteem","HOW OFTEN ARE YOU GETTING CALLS FROM PRESCHOOL?");
                }else if(chooseOne.getText().toString().equals("Ok let's do Bipolar Disorder test")){
                    startTestWithFirstQuestion("Bipolar Disorder","You experienced feelings of anguish or desperation:");
                }else if(chooseOne.getText().toString().equals("Ok let's do Mania test")){
                    startTestWithFirstQuestion("Mania","Do you ever experience a persistent elevated or irritable mood for more than a week?");
                }else if(chooseOne.getText().toString().equals("Ok let's do Narcissistic Personality Disorder test")){
                    startTestWithFirstQuestion("Narcissistic Personality Disorder","Do you experience an exaggerated sense of self-importance?");
                }else if(chooseOne.getText().toString().equals("Ok let's do Empathy Deficit Disorder test")){
                    startTestWithFirstQuestion("Empathy Deficit Disorder","I find it hard to feel sympathetic for someone who is experiencing unkind or unfair behavior");
                }else if(chooseOne.getText().toString().equals("Ok let's do Dissociative Identity Disorder (DID) test")){
                    startTestWithFirstQuestion("Dissociative Identity Disorder (DID)","Do you feel the radical change towards your thoughts and your behavior ?");
                }else if(chooseOne.getText().toString().equals("Ok let's do Illness anxiety disorder test")){
                    startTestWithFirstQuestion("Illness anxiety disorder","How often do you worry about your health?");
                }
                // farid
                else if(chooseOne.getText().toString().equals("Ok let's do Psychosis test")){
                    startTestWithFirstQuestion("Psychosis","I sometimes see things that others tell me they cannot see");
                }else if(chooseOne.getText().toString().equals("Ok let's do Autism test")){
                    startTestWithFirstQuestion("Autism","is old-fashioned or precocious?");
                }else if(chooseOne.getText().toString().equals("Ok let's do Pseudobulbar Affect (PBA) test")){
                    startTestWithFirstQuestion("Pseudobulbar Affect (PBA)","There are times when I feel fine one minute, and then I’ll become tearful the next over something small or for no reason at all");
                }else if(chooseOne.getText().toString().equals("Ok let's do Social Anxiety Disorder test")){
                    startTestWithFirstQuestion("Social Anxiety Disorder","Do you feel anxious or panicky before social situations?");
                }else if(chooseOne.getText().toString().equals("Ok let's do Bullying test")){
                    startTestWithFirstQuestion("Bullying","Do others make hurtful comments about you?");
                }else if(chooseOne.getText().toString().equals("Ok let's do Imposter syndrome test")){
                    startTestWithFirstQuestion("Imposter syndrome","I think my success was just a coincidence.");
                }else if(chooseOne.getText().toString().equals("Ok let's do Schizophrenia test")){
                    startTestWithFirstQuestion("Schizophrenia","Do you hear or see any things other people cannot see?");
                }else if(chooseOne.getText().toString().equals("Ok let's do Obsessive-Compulsive Disorder (OCD) test")){
                    startTestWithFirstQuestion("Obsessive-Compulsive Disorder (OCD)","1-Do you ever experience repetitive thoughts that cause you anxiety?");
                }else if(chooseOne.getText().toString().equals("Ok let's do Posttraumatic stress disorder (PTSD) test")){
                    startTestWithFirstQuestion("Posttraumatic stress disorder (PTSD)","1-Any reminder brought back feelings about the event/s");
                }else if(chooseOne.getText().toString().equals("Ok let's do Eating Disorder test")){
                    startTestWithFirstQuestion("Eating Disorder","Do you ever eat more food than planned at a meal?");
                }
                else if(chooseOne.getText().toString().equals("Not at all")){
                    sendUserMessage(chooseOne.getText().toString());
                    depressionTestQuestionsCounter(0);
                    getDepressionQuestions();

                }else if(chooseOne.getText().toString().equals("Not at all.")){
                    sendUserMessage(chooseOne.getText().toString());
                    depressionTestQuestionsCounter(0);
                   readDepressionDegree();

                }else if(chooseOne.getText().toString().equals("Not  at all ")){
                    sendUserMessage(chooseOne.getText().toString());
                    anixietyTestQuestionsCounter(0);
                        getAnixietyQuestions();
                }else if(chooseOne.getText().toString().equals("Not  at all. ")) {
                    sendUserMessage(chooseOne.getText().toString());
                    anixietyTestQuestionsCounter(0);
                    readAnixietyDegree();
                }else if(chooseOne.getText().toString().equals("Never")) {
                    sendUserMessage(chooseOne.getText().toString());
                    stressTestQuestionsCounter(0);
                    getstressQuestions();
                }else if(chooseOne.getText().toString().equals("Never")) {
                    sendUserMessage(chooseOne.getText().toString());
                    stressTestQuestionsCounter(0);
                    getstressQuestions();
                }else if(chooseOne.getText().toString().equals("Never ")){
                    sendUserMessage(chooseOne.getText().toString());
                    stressTestQuestionsCounter(0);
                    readStressDegree();

                }
                else if(chooseOne.getText().toString().equals("YES")){
                    sendUserMessage(chooseOne.getText().toString());
                    lcoholAddictionTestQuestionsCounter(1);
                    getAlcoholAddictionQuestions();

                }
                else if(chooseOne.getText().toString().equals("YES ")){
                    sendUserMessage(chooseOne.getText().toString());
                    lcoholAddictionTestQuestionsCounter(1);
                    readAlcoholAddictionDegree();

                }else if(chooseOne.getText().toString().equals("YES.")){
                    sendUserMessage(chooseOne.getText().toString());
                    drugAddictionTestQuestionsCounter(1);
                    getDrugAddictionQuestions();


                }else if(chooseOne.getText().toString().equals("YES. ")){
                    sendUserMessage(chooseOne.getText().toString());
                    drugAddictionTestQuestionsCounter(1);
                    readDrugAddictionDegree();
                }else if(chooseOne.getText().toString().equals("NEVER")){
                    sendUserMessage(chooseOne.getText().toString());
                    psychosexualDysfunctionTestQuestionsCounter(0);
                    getPsychosexualDysfunctionQuestions();


                }else if(chooseOne.getText().toString().equals("NEVER.")){
                    sendUserMessage(chooseOne.getText().toString());
                    psychosexualDysfunctionTestQuestionsCounter(0);
                    getPsychosexualDysfunctionDegree();

                }else if(chooseOne.getText().toString().equals("VERY LITTLE")){
                    sendUserMessage(chooseOne.getText().toString());
                    adhdTestQuestionsCounter(0);
                    adhdfunctionQuestions();

                }else if(chooseOne.getText().toString().equals("HE PREFER CIRCLE TIME")){
                    sendUserMessage(chooseOne.getText().toString());
                    adhdTestQuestionsCounter(0);
                    chooseOne.setText("HE CAN");
                    adhdfunctionQuestions();

                }else if(chooseOne.getText().toString().equals("HE CAN")){
                    sendUserMessage(chooseOne.getText().toString());
                    adhdTestQuestionsCounter(0);
                    chooseOne.setText("YES, HE DOES");
                    adhdfunctionQuestions();

                }else if(chooseOne.getText().toString().equals("YES, HE DOES")){
                    sendUserMessage(chooseOne.getText().toString());
                    adhdTestQuestionsCounter(1);
                    chooseOne.setText("YES, I DO");
                    adhdfunctionQuestions();

                }else if(chooseOne.getText().toString().equals("YES, I DO")){
                    sendUserMessage(chooseOne.getText().toString());
                    adhdTestQuestionsCounter(1);
                    chooseOne.setText("");
                    adhdfunctionQuestions();

                }else if(chooseOne.getText().toString().equals(" YES ")){
                    sendUserMessage(chooseOne.getText().toString());
                    adhdTestQuestionsCounter(1);
                    chooseOne.setText("");
                    adhdfunctionQuestions();

                }else if(chooseOne.getText().toString().equals("NO, HE DOESN'T UNDERSTAND ME")){
                    sendUserMessage(chooseOne.getText().toString());
                    adhdTestQuestionsCounter(0);
                    chooseOne.setText("");
                    adhdfunctionQuestions();

                }else if(chooseOne.getText().toString().equals("YES, HE COULD")) {
                    sendUserMessage(chooseOne.getText().toString());
                    adhdTestQuestionsCounter(0);
                    chooseOne.setText("");
                    adhdfunctionQuestions();
                }else if(chooseOne.getText().toString().equals("YES, BUT MORE THAN 6 MONTHS AGO")){
                    sendUserMessage(chooseOne.getText().toString());
                    adhdTestQuestionsCounter(1);
                    chooseOne.setText("");
                    adhdfunctionQuestions();

                }else if(chooseOne.getText().toString().equals(" YES. ")){
                    sendUserMessage(chooseOne.getText().toString());
                    adhdTestQuestionsCounter(1);
                    chooseOne.setText("");
                    getadhdDegree();


                }else if(chooseOne.getText().toString().equals("YES . ")){
                    sendUserMessage(chooseOne.getText().toString());
                    BorderlinePersonalityDisorderTestQuestionsCounter(1);
                    getBorderlinePersonalityDisorderQuestions();
                    chooseOne.setText("");

                }else if(chooseOne.getText().toString().equals(" YES   . ")){
                    sendUserMessage(chooseOne.getText().toString());
                    BorderlinePersonalityDisorderTestQuestionsCounter(1);
                    readBorderlinePersonalityDisorderDegree();
                    chooseOne.setText("");

                }else if(chooseOne.getText().toString().equals(" Yes  ")){
                    sendUserMessage(chooseOne.getText().toString());
                    lowselfesteemTestQuestionsCounter(1);
                    getlowselfesteemQuestions();
                    chooseOne.setText("");

                }else if(chooseOne.getText().toString().equals(". Yes ")){
                    sendUserMessage(chooseOne.getText().toString());
                    lowselfesteemTestQuestionsCounter(1);
                    readlowselfesteemDegree();
                    chooseOne.setText("");

                }
                else if(chooseOne.getText().toString().equals(" Yes  .")){
                    sendUserMessage(chooseOne.getText().toString());
                    BipolarDisorderTestQuestionsCounter(1);
                    getBipolarDisorderQuestions();
                    chooseOne.setText("");

                }else if(chooseOne.getText().toString().equals(" . Yes ")){
                    sendUserMessage(chooseOne.getText().toString());
                    BipolarDisorderTestQuestionsCounter(1);
                    readBipolarDisorderDegree();
                    chooseOne.setText("");

                }
                ////// farid
                else if(chooseOne.getText().toString().equals("Agree")){
                    sendUserMessage(chooseOne.getText().toString());
                    psychosisTestQuestionsCounter(1);
                    getPsychosisQuestions();


                }else if(chooseOne.getText().toString().equals(" Agree")){
                    sendUserMessage(chooseOne.getText().toString());
                    psychosisTestQuestionsCounter(1);
                    readPsychosisDegree();


                }else if(chooseOne.getText().toString().equals("\nNever\n")){
                    sendUserMessage(chooseOne.getText().toString());
                    socialAnxietyDisorderTestQuestionsCounter(0);
                    getSocialAnxietyDisorderQuestions();


                }else if(chooseOne.getText().toString().equals("\nNever.\n")){
                    sendUserMessage(chooseOne.getText().toString());
                    socialAnxietyDisorderTestQuestionsCounter(0);
                    readSocialAnxietyDisorderDegree();

                }else if(chooseOne.getText().toString().equals("\n Never \n")){
                    sendUserMessage(chooseOne.getText().toString());
                    empathyDeficitDisorderTestQuestionsCounter(0);
                    getEmpathyDeficitDisorderQuestions();


                }else if(chooseOne.getText().toString().equals("\n Never.\n")){
                    sendUserMessage(chooseOne.getText().toString());
                    empathyDeficitDisorderTestQuestionsCounter(0);
                    readEmpathyDeficitDisorderDegree();

                }else if(chooseOne.getText().toString().equals("\n No \n")){
                    sendUserMessage(chooseOne.getText().toString());
                    autismTestQuestionsCounter(0);
                    getAutismQuestions();

                }else if(chooseOne.getText().toString().equals("\n No. \n")){
                    sendUserMessage(chooseOne.getText().toString());
                    autismTestQuestionsCounter(0);
                    readAutismDegree();

                }else if(chooseOne.getText().toString().equals("Applies never")){
                    sendUserMessage(chooseOne.getText().toString());
                    pbaTestQuestionsCounter(1);
                    getPBAQuestions();

                }else if(chooseOne.getText().toString().equals("Applies never.")){
                    sendUserMessage(chooseOne.getText().toString());
                    pbaTestQuestionsCounter(1);
                    readPBADegree();

                }else if(chooseOne.getText().toString().equals(". Never")){
                    sendUserMessage(chooseOne.getText().toString());
                    ManiaTestQuestionsCounter(0);
                    getManiaQuestions();

                }else if(chooseOne.getText().toString().equals(" .Never")){
                    sendUserMessage(chooseOne.getText().toString());
                    ManiaTestQuestionsCounter(0);
                    readManiaDegree();

                }else if(chooseOne.getText().toString().equals(".  Never")){
                    sendUserMessage(chooseOne.getText().toString());
                    NarcissisticPersonalityDisorderTestQuestionsCounter(0);
                    getNarcissisticPersonalityDisorderQuestions();

                }else if(chooseOne.getText().toString().equals("  .Never")){
                    sendUserMessage(chooseOne.getText().toString());
                    NarcissisticPersonalityDisorderTestQuestionsCounter(0);
                    readNarcissisticPersonalityDisorderDegree();

                }else if(chooseOne.getText().toString().equals("\nNever \n")){
                    sendUserMessage(chooseOne.getText().toString());
                    imposterSyndromeTestQuestionsCounter(0);
                    getimposterSyndromerQuestions();

                }else if(chooseOne.getText().toString().equals("\nNever. \n")){
                        sendUserMessage(chooseOne.getText().toString());
                        imposterSyndromeTestQuestionsCounter(0);
                        readImposterSyndromeDegree();

                    }else if(chooseOne.getText().toString().equals("  \n yes  \n")){
                    sendUserMessage(chooseOne.getText().toString());
                    schizophreniaTestQuestionsCounter(1);
                    getSchizophreniaQuestions();

                }else if(chooseOne.getText().toString().equals("  \n yes.  \n")){
                    sendUserMessage(chooseOne.getText().toString());
                    schizophreniaTestQuestionsCounter(1);
                    readSchizophreniaDegree();

                }else if(chooseOne.getText().toString().equals(".  Never ")){
                    sendUserMessage(chooseOne.getText().toString());
                    IllnessanxietydisorderTestQuestionsCounter(0);
                    getIllnessanxietydisorderQuestions();

                }else if(chooseOne.getText().toString().equals("  .Never ")){
                    sendUserMessage(chooseOne.getText().toString());
                    IllnessanxietydisorderTestQuestionsCounter(0);
                    readIllnessanxietydisorderDegree();

                }else if(chooseOne.getText().toString().equals(" Yes . ")){
                    sendUserMessage(chooseOne.getText().toString());
                    DissociativeIdentityDisorderTestQuestionsCounter(1);
                    getDissociativeIdentityDisorderQuestions();

                }else if(chooseOne.getText().toString().equals(" Yes .  ")){
                    sendUserMessage(chooseOne.getText().toString());
                    DissociativeIdentityDisorderTestQuestionsCounter(1);
                    readDissociativeIdentityDisorderDegree();

                }else if(chooseOne.getText().toString().equals(". NEVER")){
                    sendUserMessage(chooseOne.getText().toString());
                    bullyingTestQuestionsCounter(0);
                    getBullyingQuestions();


                }else if(chooseOne.getText().toString().equals(". NEVER.")){
                    sendUserMessage(chooseOne.getText().toString());
                    bullyingTestQuestionsCounter(0);
                    readBullyingDegree();


                }else if(chooseOne.getText().toString().equals("\n  Never \n")){
                    sendUserMessage(chooseOne.getText().toString());
                    obsessiveCompulsiveDisorderTestQuestionsCounter(0);
                    getObsessiveCompulsiveDisorderQuestions();

                }else if(chooseOne.getText().toString().equals("\n  Never.\n")){
                    sendUserMessage(chooseOne.getText().toString());
                    obsessiveCompulsiveDisorderTestQuestionsCounter(0);
                    readObsessiveCompulsiveDisordeDegree();

                }else if(chooseOne.getText().toString().equals("\n   Never \n")){
                    sendUserMessage(chooseOne.getText().toString());
                    posttraumaticStressDisorderTestQuestionsCounter(0);
                    getPosttraumaticStressDisorderQuestions();

                }else if(chooseOne.getText().toString().equals("\n   Never. \n")){
                    sendUserMessage(chooseOne.getText().toString());
                    posttraumaticStressDisorderTestQuestionsCounter(0);
                    readPosttraumaticStressDisorderDegree();

                }else if(chooseOne.getText().toString().equals("Show "+illness1+" discreption")){
                    sendUserMessage(chooseOne.getText().toString());
                    //budle to test fragment
                    LearnMoreFragment messageFragment=new LearnMoreFragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("name", illness1);
                    bundle.putInt("image", image);
                    messageFragment.setArguments(bundle);

                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,messageFragment).commit();

                    sendBotMessage("feel free to snooze me any time :)");
                }else if(chooseOne.getText().toString().equals("Start Session Again")){
                    sendUserMessage(chooseOne.getText().toString());
                    sendBotMessage("Tell me about your problem");
                    sendBotMessage("I'm listening");

                }else if(chooseOne.getText().toString().equals("\n   Never  \n")){
                    sendUserMessage(chooseOne.getText().toString());
                    eatingDisorderTestQuestionsCounter(0);
                    getEmpathyDeficitDisorderQuestions();

                }else if(chooseOne.getText().toString().equals("\n   Never.  \n")){
                    sendUserMessage(chooseOne.getText().toString());
                    eatingDisorderTestQuestionsCounter(0);
                    readEmpathyDeficitDisorderDegree();

                }

///s7

            }
        });
        chooseTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chooseTwo.getText().toString().equals("Nope!")){
                    sendUserMessage("Nope!");
                    reportRefrence.child("relationship").setValue("in relationship");
                    sendBotMessage("Are you still studying or working?");

                }else if (chooseTwo.getText().toString().equals("Studying")){
                    sendUserMessage(chooseTwo.getText().toString());
                    reportRefrence.child("workOrStudy").setValue(chooseTwo.getText().toString());
                    sendBotMessage("Amazing , what do you study?");

                }else if(chooseTwo.getText().toString().equals("Nope")){
                    sendUserMessage(chooseTwo.getText().toString());
                    sendBotMessage("Lets start our sesion");
                    sendBotMessage("How do you feel right now ?");


                }else if(chooseTwo.getText().toString().equals("Depressed")){
                    sendUserMessage(chooseTwo.getText().toString());
                    sendBotMessage("Tell me about your problem");
                    sendBotMessage("I'm listening");

                }else if(chooseTwo.getText().toString().equals("I'd prefere to begain with another one")){

                }else if(chooseTwo.getText().toString().equals("Several days")){
                    sendUserMessage(chooseTwo.getText().toString());
                    depressionTestQuestionsCounter(1);
                    getDepressionQuestions();

                }else if(chooseTwo.getText().toString().equals("Several days.")) {
                    sendUserMessage(chooseTwo.getText().toString());
                    depressionTestQuestionsCounter(1);
                    readDepressionDegree();

                }else if(chooseTwo.getText().toString().equals("Several  day ")){
                    sendUserMessage(chooseTwo.getText().toString());
                    anixietyTestQuestionsCounter(1);
                    getAnixietyQuestions();
                }else if(chooseTwo.getText().toString().equals("Several  day. ")) {
                    sendUserMessage(chooseTwo.getText().toString());
                    anixietyTestQuestionsCounter(1);
                    readAnixietyDegree();
                }else if(chooseTwo.getText().toString().equals("Rarely")){
                    sendUserMessage(chooseTwo.getText().toString());
                    stressTestQuestionsCounter(1);
                    getstressQuestions();
                }else if(chooseTwo.getText().toString().equals("Rarely ")){
                    sendUserMessage(chooseTwo.getText().toString());
                    stressTestQuestionsCounter(1);
                    readStressDegree();
                }else if(chooseTwo.getText().toString().equals("NO")){
                    sendUserMessage(chooseTwo.getText().toString());
                    lcoholAddictionTestQuestionsCounter(0);
                    getAlcoholAddictionQuestions();

                }else if(chooseTwo.getText().toString().equals("NO ")){
                    sendUserMessage(chooseTwo.getText().toString());
                    lcoholAddictionTestQuestionsCounter(0);
                    readAlcoholAddictionDegree();

                }else if(chooseTwo.getText().toString().equals("NO . ")){
                    sendUserMessage(chooseTwo.getText().toString());
                    BorderlinePersonalityDisorderTestQuestionsCounter(0);
                    getBorderlinePersonalityDisorderQuestions();

                }else if(chooseTwo.getText().toString().equals(" NO   . ")){
                    sendUserMessage(chooseTwo.getText().toString());
                    BorderlinePersonalityDisorderTestQuestionsCounter(0);
                    readBorderlinePersonalityDisorderDegree();

                }else if(chooseTwo.getText().toString().equals("NO.")){
                    sendUserMessage(chooseTwo.getText().toString());
                    drugAddictionTestQuestionsCounter(0);
                    getDrugAddictionQuestions();
                }else if(chooseTwo.getText().toString().equals("NO. ")){
                    sendUserMessage(chooseTwo.getText().toString());
                    drugAddictionTestQuestionsCounter(0);
                    readDrugAddictionDegree();
                }else if(chooseTwo.getText().toString().equals("RARELY")){
                    sendUserMessage(chooseTwo.getText().toString());
                    psychosexualDysfunctionTestQuestionsCounter(1);
                    getPsychosexualDysfunctionQuestions();


                }else if(chooseTwo.getText().toString().equals("RARELY.")){
                    sendUserMessage(chooseTwo.getText().toString());
                    psychosexualDysfunctionTestQuestionsCounter(1);
                    getPsychosexualDysfunctionDegree();

                }else if(chooseTwo.getText().toString().equals("TOO MUCH CALLS")){
                    sendUserMessage(chooseTwo.getText().toString());
                    adhdTestQuestionsCounter(1);
                    adhdfunctionQuestions();
                }else if(chooseTwo.getText().toString().equals("HE DOESN'T PREFER CIRCLE TIME")){
                    sendUserMessage(chooseTwo.getText().toString());
                    adhdTestQuestionsCounter(1);
                    adhdfunctionQuestions();
                }else if(chooseTwo.getText().toString().equals("HE GETS DISTRACTED")){
                    sendUserMessage(chooseTwo.getText().toString());
                    adhdTestQuestionsCounter(1);
                    adhdfunctionQuestions();
                }else if(chooseTwo.getText().toString().equals("NO, HE DOESN'T")){
                    sendUserMessage(chooseTwo.getText().toString());
                    adhdTestQuestionsCounter(0);
                    adhdfunctionQuestions();
                }else if(chooseTwo.getText().toString().equals("NO, I DON'T")){
                    sendUserMessage(chooseTwo.getText().toString());
                    adhdTestQuestionsCounter(0);
                    adhdfunctionQuestions();
                }else if(chooseTwo.getText().toString().equals(" NO ")){
                    sendUserMessage(chooseTwo.getText().toString());
                    adhdTestQuestionsCounter(0);
                    adhdfunctionQuestions();
                }else if(chooseTwo.getText().toString().equals("NO, HE DOESN'T UNDERSTAND ME")){
                    sendUserMessage(chooseTwo.getText().toString());
                    adhdTestQuestionsCounter(1);
                    adhdfunctionQuestions();
                }else if(chooseTwo.getText().toString().equals("NO, HE COULDN'T")){
                    sendUserMessage(chooseTwo.getText().toString());
                    adhdTestQuestionsCounter(1);
                    adhdfunctionQuestions();
                }else if(chooseTwo.getText().toString().equals("NOT AT ALL")){
                    sendUserMessage(chooseTwo.getText().toString());
                    adhdTestQuestionsCounter(0);
                    adhdfunctionQuestions();
                }else if(chooseTwo.getText().toString().equals(" NO. ")){
                    sendUserMessage(chooseTwo.getText().toString());
                    adhdTestQuestionsCounter(0);
                    adhdfunctionQuestions();
                }else if(chooseTwo.getText().toString().equals(" NO  ")){
                    sendUserMessage(chooseTwo.getText().toString());
                    lowselfesteemTestQuestionsCounter(0);
                    getlowselfesteemQuestions();
                }else if(chooseTwo.getText().toString().equals(". NO ")){
                    sendUserMessage(chooseTwo.getText().toString());
                    lowselfesteemTestQuestionsCounter(0);
                    readlowselfesteemDegree();
                }
                else if(chooseTwo.getText().toString().equals(" NO  .")){
                    sendUserMessage(chooseTwo.getText().toString());
                    BipolarDisorderTestQuestionsCounter(0);
                    getBipolarDisorderQuestions();
                }else if(chooseTwo.getText().toString().equals(" . No ")){
                    sendUserMessage(chooseTwo.getText().toString());
                    BipolarDisorderTestQuestionsCounter(0);
                    readBipolarDisorderDegree();
                }
                ///////farid
                else if(chooseTwo.getText().toString().equals("Disagree")){
                    sendUserMessage(chooseTwo.getText().toString());
                    psychosisTestQuestionsCounter(0);
                    getPsychosisQuestions();
                }else if(chooseTwo.getText().toString().equals(" Disagree")){
                    sendUserMessage(chooseTwo.getText().toString());
                    psychosisTestQuestionsCounter(0);
                    readPsychosisDegree();
                }else if(chooseTwo.getText().toString().equals("\n Somewhat \n")){
                    sendUserMessage(chooseTwo.getText().toString());
                    autismTestQuestionsCounter(1);
                    getAutismQuestions();

                }else if(chooseTwo.getText().toString().equals("\n Somewhat. \n")){
                    sendUserMessage(chooseTwo.getText().toString());
                    autismTestQuestionsCounter(1);
                    readAutismDegree();

                }else if(chooseTwo.getText().toString().equals("Applies rarely")){
                    sendUserMessage(chooseTwo.getText().toString());
                    pbaTestQuestionsCounter(2);
                    getPBAQuestions();

                }else if(chooseTwo.getText().toString().equals("Applies rarely.")){
                    sendUserMessage(chooseTwo.getText().toString());
                    pbaTestQuestionsCounter(2);
                    readPBADegree();

                }else if(chooseTwo.getText().toString().equals(". Rarely")){
                    sendUserMessage(chooseTwo.getText().toString());
                    ManiaTestQuestionsCounter(1);
                    getManiaQuestions();

                }else if(chooseTwo.getText().toString().equals(" .Rarely")){
                    sendUserMessage(chooseTwo.getText().toString());
                    ManiaTestQuestionsCounter(1);
                    readManiaDegree();

                }else if(chooseTwo.getText().toString().equals(".  Rarely")){
                    sendUserMessage(chooseTwo.getText().toString());
                    NarcissisticPersonalityDisorderTestQuestionsCounter(1);
                    getNarcissisticPersonalityDisorderQuestions();

                }else if(chooseTwo.getText().toString().equals("  .Rarely")){
                    sendUserMessage(chooseTwo.getText().toString());
                    NarcissisticPersonalityDisorderTestQuestionsCounter(1);
                    readNarcissisticPersonalityDisorderDegree();

                }
                else if(chooseTwo.getText().toString().equals("\nRarely\n")){
                    sendUserMessage(chooseTwo.getText().toString());
                    socialAnxietyDisorderTestQuestionsCounter(1);
                    getSocialAnxietyDisorderQuestions();

                }else if(chooseTwo.getText().toString().equals("\nRarely.\n")){
                    sendUserMessage(chooseTwo.getText().toString());
                    socialAnxietyDisorderTestQuestionsCounter(1);
                    readSocialAnxietyDisorderDegree();
                }else if(chooseTwo.getText().toString().equals("\n Rarely \n")){
                    sendUserMessage(chooseTwo.getText().toString());
                    empathyDeficitDisorderTestQuestionsCounter(1);
                    getEmpathyDeficitDisorderQuestions();


                }else if(chooseTwo.getText().toString().equals("\n Rarely.\n")) {
                    sendUserMessage(chooseTwo.getText().toString());
                    empathyDeficitDisorderTestQuestionsCounter(1);
                    readEmpathyDeficitDisorderDegree();

                }else if(chooseTwo.getText().toString().equals("\nRarely \n")){
                    sendUserMessage(chooseTwo.getText().toString());
                    imposterSyndromeTestQuestionsCounter(1);
                    getimposterSyndromerQuestions();

                }else if(chooseTwo.getText().toString().equals("\nRarely. \n")){
                    sendUserMessage(chooseTwo.getText().toString());
                    imposterSyndromeTestQuestionsCounter(1);
                    readImposterSyndromeDegree();

                }else if(chooseTwo.getText().toString().equals("  \n no  \n")){
                    sendUserMessage(chooseTwo.getText().toString());
                    schizophreniaTestQuestionsCounter(0);
                    getSchizophreniaQuestions();

                }else if(chooseTwo.getText().toString().equals("  \n no.  \n")){
                    sendUserMessage(chooseTwo.getText().toString());
                    schizophreniaTestQuestionsCounter(0);
                    readSchizophreniaDegree();

                }else if(chooseTwo.getText().toString().equals(".  Rarely ")){
                    sendUserMessage(chooseTwo.getText().toString());
                    IllnessanxietydisorderTestQuestionsCounter(1);
                    getIllnessanxietydisorderQuestions();

                }else if(chooseTwo.getText().toString().equals("  .Rarely ")){
                    sendUserMessage(chooseTwo.getText().toString());
                    IllnessanxietydisorderTestQuestionsCounter(1);
                    readIllnessanxietydisorderDegree();

                }else if(chooseTwo.getText().toString().equals(" No . ")){
                    sendUserMessage(chooseTwo.getText().toString());
                    DissociativeIdentityDisorderTestQuestionsCounter(0);
                    getDissociativeIdentityDisorderQuestions();

                }else if(chooseTwo.getText().toString().equals(" No .  ")){
                    sendUserMessage(chooseTwo.getText().toString());
                    DissociativeIdentityDisorderTestQuestionsCounter(0);
                    readDissociativeIdentityDisorderDegree();

                }else if(chooseTwo.getText().toString().equals(". RARELY")){
                    sendUserMessage(chooseTwo.getText().toString());
                    bullyingTestQuestionsCounter(1);
                    getBullyingQuestions();

                }else if(chooseTwo.getText().toString().equals(". RARELY.")){
                    sendUserMessage(chooseTwo.getText().toString());
                    bullyingTestQuestionsCounter(1);
                    readBullyingDegree();

                }else if(chooseTwo.getText().toString().equals("\n  Rarely \n")){
                    sendUserMessage(chooseTwo.getText().toString());
                    obsessiveCompulsiveDisorderTestQuestionsCounter(1);
                    getObsessiveCompulsiveDisorderQuestions();

                }else if(chooseTwo.getText().toString().equals("\n  Rarely.\n")) {
                    sendUserMessage(chooseTwo.getText().toString());
                    obsessiveCompulsiveDisorderTestQuestionsCounter(1);
                    readObsessiveCompulsiveDisordeDegree();

                }else if(chooseTwo.getText().toString().equals("\n   Rarely \n")){
                    sendUserMessage(chooseTwo.getText().toString());
                    posttraumaticStressDisorderTestQuestionsCounter(1);
                    getPosttraumaticStressDisorderQuestions();

                }else if(chooseTwo.getText().toString().equals("\n   Rarely. \n")) {
                    sendUserMessage(chooseTwo.getText().toString());
                    posttraumaticStressDisorderTestQuestionsCounter(1);
                    readPosttraumaticStressDisorderDegree();

                }else if(chooseTwo.getText().toString().equals("No, Thanks")){
                    sendUserMessage(chooseTwo.getText().toString());
                    sendBotMessage("feel free to snooze me any time :)");
                }
                else if(chooseTwo.getText().toString().equals("\n   Rarely  \n")){
                    sendUserMessage(chooseTwo.getText().toString());
                    eatingDisorderTestQuestionsCounter(1);
                    getEmpathyDeficitDisorderQuestions();

                }else if(chooseTwo.getText().toString().equals("\n   Rarely.  \n")) {
                    sendUserMessage(chooseTwo.getText().toString());
                    eatingDisorderTestQuestionsCounter(1);
                    readEatingDisorderDegree();

                }



            }
        });
        chooseThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chooseThree.getText().toString().equals("It just complecated!")){
                    sendUserMessage("It just complecated!");
                    reportRefrence.child("relationship").setValue("complecated");
                    sendBotMessage("Are you still studying or working?");

                }else if(chooseThree.getText().toString().equals("Doing both")){
                    sendUserMessage(chooseThree.getText().toString());
                    reportRefrence.child("workOrStudy").setValue(chooseThree.getText().toString());
                    sendBotMessage("Tell me more about that ?");

                }else if(chooseThree.getText().toString().equals("Happy")){
                    sendUserMessage(chooseThree.getText().toString());
                    sendBotMessage("Tell me about your problem");
                    sendBotMessage("I'm listening");

                }else if (chooseThree.getText().toString().equals("More than half of the days")){
                    sendUserMessage(chooseThree.getText().toString());
                    depressionTestQuestionsCounter(2);
                    getDepressionQuestions();

                }else if (chooseThree.getText().toString().equals("More than half of the days.")){
                    sendUserMessage(chooseThree.getText().toString());
                    depressionTestQuestionsCounter(2);
                    readDepressionDegree();


                }else if (chooseThree.getText().toString().equals("More  than half of the days ")){
                    sendUserMessage(chooseThree.getText().toString());
                    anixietyTestQuestionsCounter(2);
                    getAnixietyQuestions();

                }else if(chooseThree.getText().toString().equals("More  than half of the days. ")) {
                    sendUserMessage(chooseThree.getText().toString());
                    anixietyTestQuestionsCounter(2);
                    readAnixietyDegree();
                }else if (chooseThree.getText().toString().equals("Sometimes")){
                    sendUserMessage(chooseThree.getText().toString());
                    stressTestQuestionsCounter(2);
                    getstressQuestions();

                }else if(chooseThree.getText().toString().equals("Sometimes ")){
                    sendUserMessage(chooseThree.getText().toString());
                    stressTestQuestionsCounter(2);
                    readStressDegree();

                }else if(chooseThree.getText().toString().equals("SOMETIMES")){
                    sendUserMessage(chooseThree.getText().toString());
                    psychosexualDysfunctionTestQuestionsCounter(2);
                    getPsychosexualDysfunctionQuestions();


                }else if(chooseThree.getText().toString().equals("SOMETIMES.")){
                    sendUserMessage(chooseThree.getText().toString());
                    psychosexualDysfunctionTestQuestionsCounter(2);
                    getPsychosexualDysfunctionDegree();

                }else if(chooseThree.getText().toString().equals("\n Yes \n")){
                    sendUserMessage(chooseThree.getText().toString());
                    autismTestQuestionsCounter(2);
                    getAutismQuestions();

                }else if(chooseThree.getText().toString().equals("\n Yes. \n")){
                    sendUserMessage(chooseThree.getText().toString());
                    autismTestQuestionsCounter(2);
                    readAutismDegree();

                }else if(chooseThree.getText().toString().equals("Applies occasionally")){
                    sendUserMessage(chooseThree.getText().toString());
                    pbaTestQuestionsCounter(3);
                    getPBAQuestions();

                }else if(chooseThree.getText().toString().equals("Applies occasionally.")){
                    sendUserMessage(chooseThree.getText().toString());
                    pbaTestQuestionsCounter(3);
                    readPBADegree();

                }else if(chooseThree.getText().toString().equals(". Sometimes")){
                    sendUserMessage(chooseThree.getText().toString());
                    ManiaTestQuestionsCounter(2);
                    getManiaQuestions();

                }else if(chooseThree.getText().toString().equals(" .Sometimes")){
                    sendUserMessage(chooseThree.getText().toString());
                    ManiaTestQuestionsCounter(2);
                    readManiaDegree();

                }else if(chooseThree.getText().toString().equals(".  Sometimes")){
                    sendUserMessage(chooseThree.getText().toString());
                    NarcissisticPersonalityDisorderTestQuestionsCounter(2);
                    getNarcissisticPersonalityDisorderQuestions();

                }else if(chooseThree.getText().toString().equals("  .Sometimes")){
                    sendUserMessage(chooseThree.getText().toString());
                    NarcissisticPersonalityDisorderTestQuestionsCounter(2);
                    readNarcissisticPersonalityDisorderDegree();

                }
                else if(chooseThree.getText().toString().equals("\nSometimes\n")){
                    sendUserMessage(chooseThree.getText().toString());
                    socialAnxietyDisorderTestQuestionsCounter(2);
                    getSocialAnxietyDisorderQuestions();


                }else if(chooseThree.getText().toString().equals("\nSometimes.\n")){
                    sendUserMessage(chooseThree.getText().toString());
                    socialAnxietyDisorderTestQuestionsCounter(2);
                    readSocialAnxietyDisorderDegree();


                }else if(chooseThree.getText().toString().equals("\n Sometimes \n")){
                    sendUserMessage(chooseThree.getText().toString());
                    empathyDeficitDisorderTestQuestionsCounter(2);
                    getEmpathyDeficitDisorderQuestions();


                }else if(chooseThree.getText().toString().equals("\n Sometimes.\n")) {
                    sendUserMessage(chooseThree.getText().toString());
                    empathyDeficitDisorderTestQuestionsCounter(2);
                    readEmpathyDeficitDisorderDegree();

                }else if(chooseThree.getText().toString().equals("\nSometimes \n")){
                    sendUserMessage(chooseThree.getText().toString());
                    imposterSyndromeTestQuestionsCounter(2);
                    getimposterSyndromerQuestions();

                }else if(chooseThree.getText().toString().equals("\nSometimes. \n")){
                    sendUserMessage(chooseThree.getText().toString());
                    imposterSyndromeTestQuestionsCounter(2);
                    readImposterSyndromeDegree();

                }else if(chooseThree.getText().toString().equals(".  Sometimes ")){
                    sendUserMessage(chooseThree.getText().toString());
                    IllnessanxietydisorderTestQuestionsCounter(2);
                    getIllnessanxietydisorderQuestions();

                }else if(chooseThree.getText().toString().equals("  .Sometimes ")){
                    sendUserMessage(chooseThree.getText().toString());
                    IllnessanxietydisorderTestQuestionsCounter(2);
                    readIllnessanxietydisorderDegree();

                }else if(chooseThree.getText().toString().equals(". SOMETIMES")){
                    sendUserMessage(chooseThree.getText().toString());
                    bullyingTestQuestionsCounter(2);
                    getBullyingQuestions();

                }else if(chooseThree.getText().toString().equals(". SOMETIMES.")){
                    sendUserMessage(chooseThree.getText().toString());
                    bullyingTestQuestionsCounter(2);
                    readBullyingDegree();

                }else if(chooseThree.getText().toString().equals("\n  Sometimes \n")){
                    sendUserMessage(chooseThree.getText().toString());
                    obsessiveCompulsiveDisorderTestQuestionsCounter(2);
                    getObsessiveCompulsiveDisorderQuestions();


                }else if(chooseThree.getText().toString().equals("\n  Sometimes.\n")) {
                    sendUserMessage(chooseThree.getText().toString());
                    obsessiveCompulsiveDisorderTestQuestionsCounter(2);
                    readObsessiveCompulsiveDisordeDegree();

                }else if(chooseThree.getText().toString().equals("\n   Sometimes \n")){
                    sendUserMessage(chooseThree.getText().toString());
                    posttraumaticStressDisorderTestQuestionsCounter(2);
                    getPosttraumaticStressDisorderQuestions();

                }else if(chooseThree.getText().toString().equals("\n   Sometimes.\n")) {
                    sendUserMessage(chooseThree.getText().toString());
                    posttraumaticStressDisorderTestQuestionsCounter(2);
                    readPosttraumaticStressDisorderDegree();

                }
                else if(chooseThree.getText().toString().equals("\n   Sometimes  \n")){
                    sendUserMessage(chooseThree.getText().toString());
                    eatingDisorderTestQuestionsCounter(2);
                    getEatingDisorderQuestions();

                }else if(chooseThree.getText().toString().equals("\n   Sometimes. \n")) {
                    sendUserMessage(chooseThree.getText().toString());
                    eatingDisorderTestQuestionsCounter(2);
                    readEatingDisorderDegree();

                }
                }
        });
        chooseFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chooseFour.getText().toString().equals("Neither both")){
                    sendUserMessage(chooseFour.getText().toString());
                    reportRefrence.child("workOrStudy").setValue(chooseFour.getText().toString());
                    sendBotMessage("ok , i see");

                }else if(chooseFour.getText().toString().equals("Angry")){
                    sendUserMessage(chooseFour.getText().toString());
                    sendBotMessage("Tell me about your problem");
                    sendBotMessage("I'm listening");

                }else if(chooseFour.getText().toString().equals("Nearly everyday")){
                    sendUserMessage(chooseFour.getText().toString());
                    depressionTestQuestionsCounter(3);
                    getDepressionQuestions();
                }else if(chooseFour.getText().toString().equals("Nearly everyday.")){
                    sendUserMessage(chooseFour.getText().toString());
                    depressionTestQuestionsCounter(3);
                   readDepressionDegree();
                }else if(chooseFour.getText().toString().equals("Nearly  everyday ")){
                sendUserMessage(chooseFour.getText().toString());
                    anixietyTestQuestionsCounter(3);
                getAnixietyQuestions();
                }else if(chooseFour.getText().toString().equals("Nearly  everyday. ")) {
                    sendUserMessage(chooseFour.getText().toString());
                    anixietyTestQuestionsCounter(3);
                    readAnixietyDegree();
                }else if (chooseFour.getText().toString().equals("often")){
                    sendUserMessage(chooseFour.getText().toString());
                    stressTestQuestionsCounter(3);
                    getstressQuestions();

                }else if(chooseFour.getText().toString().equals("Often ")){
                    sendUserMessage(chooseFour.getText().toString());
                    stressTestQuestionsCounter(3);
                    readStressDegree();

                }else if(chooseFour.getText().toString().equals("OFTEN")){
                    sendUserMessage(chooseFour.getText().toString());
                    psychosexualDysfunctionTestQuestionsCounter(3);
                    getPsychosexualDysfunctionQuestions();

                }else if(chooseFour.getText().toString().equals("OFTEN.")){
                    sendUserMessage(chooseFour.getText().toString());
                    psychosexualDysfunctionTestQuestionsCounter(3);
                    getPsychosexualDysfunctionDegree();

                }else if(chooseFour.getText().toString().equals("Applies frequently")){
                    sendUserMessage(chooseFour.getText().toString());
                    pbaTestQuestionsCounter(4);
                    getPBAQuestions();

                }else if(chooseFour.getText().toString().equals("Applies frequently.")){
                    sendUserMessage(chooseFour.getText().toString());
                    pbaTestQuestionsCounter(4);
                    readPBADegree();

                }else if(chooseFour.getText().toString().equals(". Often")){
                    sendUserMessage(chooseFour.getText().toString());
                    ManiaTestQuestionsCounter(3);
                    getManiaQuestions();

                }else if(chooseFour.getText().toString().equals(" .Often")){
                    sendUserMessage(chooseFour.getText().toString());
                    ManiaTestQuestionsCounter(3);
                    readManiaDegree();

                }else if(chooseFour.getText().toString().equals(".  Often")){
                    sendUserMessage(chooseFour.getText().toString());
                    NarcissisticPersonalityDisorderTestQuestionsCounter(3);
                    getNarcissisticPersonalityDisorderQuestions();

                }else if(chooseFour.getText().toString().equals("  .Often")){
                    sendUserMessage(chooseFour.getText().toString());
                    NarcissisticPersonalityDisorderTestQuestionsCounter(3);
                    readNarcissisticPersonalityDisorderDegree();

                }
                else if(chooseFour.getText().toString().equals("\nOften\n")){
                    sendUserMessage(chooseFour.getText().toString());
                    socialAnxietyDisorderTestQuestionsCounter(3);
                    getSocialAnxietyDisorderQuestions();


                }else if(chooseFour.getText().toString().equals("\nOften.\n")){
                    sendUserMessage(chooseFour.getText().toString());
                    socialAnxietyDisorderTestQuestionsCounter(3);
                    readSocialAnxietyDisorderDegree();


                }else if(chooseFour.getText().toString().equals("\n Often \n")){
                    sendUserMessage(chooseFour.getText().toString());
                    empathyDeficitDisorderTestQuestionsCounter(3);
                    getEmpathyDeficitDisorderQuestions();


                }else if(chooseFour.getText().toString().equals("\n Often.\n")) {
                    sendUserMessage(chooseFour.getText().toString());
                    empathyDeficitDisorderTestQuestionsCounter(3);
                    readEmpathyDeficitDisorderDegree();

                }else if(chooseFour.getText().toString().equals("\nOften \n")){
                    sendUserMessage(chooseFour.getText().toString());
                    imposterSyndromeTestQuestionsCounter(3);
                    getimposterSyndromerQuestions();

                }else if(chooseFour.getText().toString().equals("\nOften. \n")){
                    sendUserMessage(chooseFour.getText().toString());
                    imposterSyndromeTestQuestionsCounter(3);
                    readImposterSyndromeDegree();

                }else if(chooseFour.getText().toString().equals(".  Often ")){
                    sendUserMessage(chooseFour.getText().toString());
                    IllnessanxietydisorderTestQuestionsCounter(3);
                    getIllnessanxietydisorderQuestions();

                }else if(chooseFour.getText().toString().equals("  .Often ")){
                    sendUserMessage(chooseFour.getText().toString());
                    IllnessanxietydisorderTestQuestionsCounter(3);
                    readIllnessanxietydisorderDegree();

                }else if(chooseFour.getText().toString().equals(". OFTEN")){
                    sendUserMessage(chooseFour.getText().toString());
                    bullyingTestQuestionsCounter(3);
                    getBullyingQuestions();

                }else if(chooseFour.getText().toString().equals(". OFTEN.")){
                    sendUserMessage(chooseFour.getText().toString());
                    bullyingTestQuestionsCounter(3);
                    readBullyingDegree();

                }else if(chooseFour.getText().toString().equals("\n  Often \n")){
                    sendUserMessage(chooseFour.getText().toString());
                    obsessiveCompulsiveDisorderTestQuestionsCounter(3);
                    getObsessiveCompulsiveDisorderQuestions();

                }else if(chooseFour.getText().toString().equals("\n  Often.\n")) {
                    sendUserMessage(chooseFour.getText().toString());
                    obsessiveCompulsiveDisorderTestQuestionsCounter(3);
                    readObsessiveCompulsiveDisordeDegree();

                }else if(chooseFour.getText().toString().equals("\n   Often \n")){
                    sendUserMessage(chooseFour.getText().toString());
                    posttraumaticStressDisorderTestQuestionsCounter(3);
                    getObsessiveCompulsiveDisorderQuestions();

                }else if(chooseFour.getText().toString().equals("\n   Often.\n")) {
                    sendUserMessage(chooseFour.getText().toString());
                    posttraumaticStressDisorderTestQuestionsCounter(3);
                    readPosttraumaticStressDisorderDegree();

                }else if(chooseFour.getText().toString().equals("\n   Often  \n")){
                    sendUserMessage(chooseFour.getText().toString());
                    eatingDisorderTestQuestionsCounter(3);
                    getEatingDisorderQuestions();

                }else if(chooseFour.getText().toString().equals("\n   Often.  \n")) {
                    sendUserMessage(chooseFour.getText().toString());
                    eatingDisorderTestQuestionsCounter(3);
                    readEatingDisorderDegree();

                }
                }

        });
        chooseFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseFive.getText().toString().equals("very often")){
                    sendUserMessage(chooseFive.getText().toString());
                    stressTestQuestionsCounter(4);
                    getstressQuestions();

                }else if(chooseFive.getText().toString().equals("Very often ")){
                    sendUserMessage(chooseFive.getText().toString());
                    stressTestQuestionsCounter(4);
                    readStressDegree();

                }else if(chooseFive.getText().toString().equals("VERY OFTEN")){
                    sendUserMessage(chooseFive.getText().toString());
                    psychosexualDysfunctionTestQuestionsCounter(4);
                    getPsychosexualDysfunctionQuestions();


                }else if(chooseFive.getText().toString().equals("VERY OFTEN.")){
                    sendUserMessage(chooseFive.getText().toString());
                    psychosexualDysfunctionTestQuestionsCounter(4);
                    getPsychosexualDysfunctionDegree();
                }else if(chooseFive.getText().toString().equals("Applies most of the time")){
                    sendUserMessage(chooseFive.getText().toString());
                    pbaTestQuestionsCounter(5);
                    getPBAQuestions();

                }else if(chooseFive.getText().toString().equals("Applies most of the time.")){
                    sendUserMessage(chooseFive.getText().toString());
                    pbaTestQuestionsCounter(5);
                    readPBADegree();

                }else if(chooseFive.getText().toString().equals(". Very Often")){
                    sendUserMessage(chooseFive.getText().toString());
                    ManiaTestQuestionsCounter(4);
                    getManiaQuestions();

                }else if(chooseFive.getText().toString().equals(" .Very Often")){
                    sendUserMessage(chooseFive.getText().toString());
                    ManiaTestQuestionsCounter(4);
                    readManiaDegree();

                }else if(chooseFive.getText().toString().equals(".  Very Often")){
                    sendUserMessage(chooseFive.getText().toString());
                    NarcissisticPersonalityDisorderTestQuestionsCounter(4);
                    getNarcissisticPersonalityDisorderQuestions();

                }else if(chooseFive.getText().toString().equals("  .Very Often")){
                    sendUserMessage(chooseFive.getText().toString());
                    NarcissisticPersonalityDisorderTestQuestionsCounter(4);
                    readNarcissisticPersonalityDisorderDegree();

                }
                else if(chooseFive.getText().toString().equals("\nVery Often\n")){
                    sendUserMessage(chooseFive.getText().toString());
                    socialAnxietyDisorderTestQuestionsCounter(4);
                    getSocialAnxietyDisorderQuestions();

                }else if(chooseFive.getText().toString().equals("\nVery Often.\n")){
                    sendUserMessage(chooseFive.getText().toString());
                    socialAnxietyDisorderTestQuestionsCounter(4);
                    readSocialAnxietyDisorderDegree();

                }else if(chooseFive.getText().toString().equals("\n Very often \n")){
                    sendUserMessage(chooseFive.getText().toString());
                    empathyDeficitDisorderTestQuestionsCounter(4);
                    getEmpathyDeficitDisorderQuestions();


                }else if(chooseFive.getText().toString().equals("\n Very Often.\n")) {
                    sendUserMessage(chooseFive.getText().toString());
                    empathyDeficitDisorderTestQuestionsCounter(4);
                    readEmpathyDeficitDisorderDegree();

                }else if(chooseFive.getText().toString().equals("\nVery often \n")){
                    sendUserMessage(chooseFive.getText().toString());
                    imposterSyndromeTestQuestionsCounter(4);
                    getimposterSyndromerQuestions();

                }else if(chooseFive.getText().toString().equals("\nVery often. \n")){
                    sendUserMessage(chooseFive.getText().toString());
                    imposterSyndromeTestQuestionsCounter(4);
                    readImposterSyndromeDegree();

                }else if(chooseFive.getText().toString().equals(".  Very Often ")){
                    sendUserMessage(chooseFive.getText().toString());
                    IllnessanxietydisorderTestQuestionsCounter(4);
                    getIllnessanxietydisorderQuestions();

                }else if(chooseFive.getText().toString().equals("  .Very Often ")){
                    sendUserMessage(chooseFive.getText().toString());
                    IllnessanxietydisorderTestQuestionsCounter(4);
                    readIllnessanxietydisorderDegree();

                }else if(chooseFive.getText().toString().equals(". VERY OFTEN")){
                    sendUserMessage(chooseFive.getText().toString());
                    bullyingTestQuestionsCounter(4);
                    getBullyingQuestions();

                }else if(chooseFive.getText().toString().equals(". VERY OFTEN.")){
                    sendUserMessage(chooseFive.getText().toString());
                    bullyingTestQuestionsCounter(4);
                    readBullyingDegree();

                }else if(chooseFive.getText().toString().equals("\n  Very often \n")){
                    sendUserMessage(chooseFive.getText().toString());
                    obsessiveCompulsiveDisorderTestQuestionsCounter(4);
                    getObsessiveCompulsiveDisorderQuestions();



                }else if(chooseFive.getText().toString().equals("\n  Very often. \n")) {
                    sendUserMessage(chooseFive.getText().toString());
                    obsessiveCompulsiveDisorderTestQuestionsCounter(4);
                    readObsessiveCompulsiveDisordeDegree();

                  }else if(chooseFive.getText().toString().equals("\n   Very often \n")){
                    sendUserMessage(chooseFive.getText().toString());
                    posttraumaticStressDisorderTestQuestionsCounter(4);
                    getPosttraumaticStressDisorderQuestions();



                }else if(chooseFive.getText().toString().equals("\n   Very often. \n")) {
                    sendUserMessage(chooseFive.getText().toString());
                    posttraumaticStressDisorderTestQuestionsCounter(4);
                    readPosttraumaticStressDisorderDegree();
                }
                else if(chooseFive.getText().toString().equals("\n   Very often  \n")){
                    sendUserMessage(chooseFive.getText().toString());
                    eatingDisorderTestQuestionsCounter(4);
                    getEatingDisorderQuestions();



                }else if(chooseFive.getText().toString().equals("\n   Very often.  \n")) {
                    sendUserMessage(chooseFive.getText().toString());
                    eatingDisorderTestQuestionsCounter(4);
                    readEatingDisorderDegree();
                }
                }
        });
        getPrefrance();

        return view;
    }

    private void getadhdDegree() {
        Query query6 = FirebaseDatabase.getInstance().getReference().child("PatientReportChatBot").child(firebaseUser.getUid()).child("Attention-Deficit Hyperactivity Disorder (ADHD)");
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            String degree;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                            degree=dataSnapshot.child("totalDegree").getValue(String.class);



                        } sendBotMessage("Your total degree from Attention-Deficit Hyperactivity Disorder (ADHD) test is \n"+degree+"%");
                        if(Double.parseDouble(degree)<(double) 4){
                            sendBotMessage("Your degree is lower than 4 ,\n" +
                                    " I think you're a normal person");

                        }else {
                            sendBotMessage("Your degree is more than 4 ,\n" +
                                    " I think you should visit a doctor");


                        }
                        sendBotMessage("You can see some tips and discription about "+illness1+" that can help you");

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void readBullyingDegree() {
        Query query6 = FirebaseDatabase.getInstance().getReference().child("PatientReportChatBot").child(firebaseUser.getUid()).child("Bullying");
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            String degree;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                            degree=dataSnapshot.child("totalDegree").getValue(String.class);



                        } sendBotMessage("Your total degree from Bullying test is \n"+degree+"%");
                        if(Double.parseDouble(degree)<(double) 40){
                            sendBotMessage("Your degree is lower than 40% ,\n" +
                                    " I think you're a normal person");

                        }else {
                            sendBotMessage("Your degree is more than 40% ,\n" +
                                    " I think you should visit a doctor");


                        }
                        sendBotMessage("You can see some tips and discription about "+illness1+" that can help you");

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getBullyingQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber340", Context.MODE_PRIVATE);
        int numberOfAdhdQuestion=preferences.getInt("number",0);
        SharedPreferences.Editor editor=preferences.edit();
        String [] depressionTest={"Are you being harassed online or do others post mean things about you?"
                ,"Do others imitate you or make fun of your appearance?"
                ,"Do you feel isolated at work, school, home, or other places?"
                ,"Are others spreading rumors or false information about you?"
                ,"Are you ever afraid to go to work, school, or places where you feel hurt or excluded?"
                ,"Do others physically hurt you or damage your possessions?"
                ,"Do you feel anxious or depressed when you have to interact with a hurtful person?"
                ,"Do you experience headaches or stomach aches before you have to interact with a hurtful person?"

        };
        Toast.makeText(getActivity(), " "+numberOfAdhdQuestion, Toast.LENGTH_SHORT).show();
        while (numberOfAdhdQuestion<=8) {
            if (numberOfAdhdQuestion <= 7) {


                sendBotMessage(depressionTest[numberOfAdhdQuestion]);
                numberOfAdhdQuestion++;
                editor.putInt("number", numberOfAdhdQuestion);
                editor.apply();
            }
            break;
        }

    }

    private void bullyingTestQuestionsCounter(int count) {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter340", Context.MODE_PRIVATE);
        int pbaCounterDegree=preferences.getInt("counter",0);
        SharedPreferences.Editor editor=preferences.edit();
       if(count==0){

       }
        else if(count==1){
            pbaCounterDegree++;
        }else if(count==2){
            pbaCounterDegree+=2;
        }else if(count==3){
            pbaCounterDegree+=3;
        }else if(count==4){
            pbaCounterDegree+=4;
        }
        editor.putInt("counter",pbaCounterDegree);
        editor.apply();
        double totalDegreeOfTest=Math.round(((float)pbaCounterDegree/36)*100);
        TestDegree testDegree=new TestDegree("Bullying",String.valueOf(totalDegreeOfTest));
        reportRefrence.child("tests").child("Bullying").setValue(testDegree);

        Toast.makeText(getActivity(), " dep  "+pbaCounterDegree+" total "+totalDegreeOfTest, Toast.LENGTH_SHORT).show();



    }

    private void readSchizophreniaDegree() {
        Query query6 = FirebaseDatabase.getInstance().getReference().child("PatientReportChatBot").child(firebaseUser.getUid()).child("Schizophrenia");
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            String degree;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                            degree=dataSnapshot.child("totalDegree").getValue(String.class);



                        } sendBotMessage("Your total degree from Schizophrenia test is \n"+degree+"%");
                        if(Double.parseDouble(degree)<(double) 40){
                            sendBotMessage("Your degree is lower than 40% ,\n" +
                                    " I think you're a normal person");

                        }else {
                            sendBotMessage("Your degree is more than 40% ,\n" +
                                    " I think you should visit a doctor");


                        }
                        sendBotMessage("You can see some tips and discription about "+illness1+" that can help you");


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getSchizophreniaQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber331", Context.MODE_PRIVATE);
        int numberOfAdhdQuestion=preferences.getInt("number",0);
        SharedPreferences.Editor editor=preferences.edit();
        String [] depressionTest={"Do you find it difficult to think regularly?"
                ,"Do some people comment on their inability to understand what you are saying?"
                ,"Do you feel that you have little in common with family and friends?"
                ,"Do you sometimes feel that someone is watching you?"
                ,"Do other people have a hard time guessing your emotions from your facial expressions?"
                ,"Do you feel that you have capabilities that others cannot understand or appreciate?"
                ,"Are you struggling to trust that what you are thinking is real?"
                ,"Do you feel that others are taking advantage of your thoughts or emotions?"
                ,"Are you struggling to keep up with everyday tasks like showering, changing clothes, paying bills, cleaning, cooking, etc.?"

        };
        Toast.makeText(getActivity(), " "+numberOfAdhdQuestion, Toast.LENGTH_SHORT).show();
        while (numberOfAdhdQuestion<=9) {
            if (numberOfAdhdQuestion <= 8) {


                sendBotMessage(depressionTest[numberOfAdhdQuestion]);
                numberOfAdhdQuestion++;
                editor.putInt("number", numberOfAdhdQuestion);
                editor.apply();
            }
            break;
        }

    }

    private void schizophreniaTestQuestionsCounter(int count) {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter331", Context.MODE_PRIVATE);
        int schizophreniaCounterDegree=preferences.getInt("counter",0);
        SharedPreferences.Editor editor=preferences.edit();
        if(count==0){
            
        }else if(count==1){
            schizophreniaCounterDegree++;
        }
        editor.putInt("counter",schizophreniaCounterDegree);
        editor.apply();
        double totalDegreeOfTest=Math.round(((float)schizophreniaCounterDegree/10)*100);
        TestDegree testDegree=new TestDegree("Schizophrenia",String.valueOf(totalDegreeOfTest));
        reportRefrence.child("tests").child("Schizophrenia").setValue(testDegree);

        Toast.makeText(getActivity(), " dep  "+schizophreniaCounterDegree+" total "+totalDegreeOfTest, Toast.LENGTH_SHORT).show();


    }

    //hahaa
    private void readPBADegree() {
        Query query6 = FirebaseDatabase.getInstance().getReference().child("PatientReportChatBot").child(firebaseUser.getUid()).child("Pseudobulbar Affect (PBA)");
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            String degree;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                            degree=dataSnapshot.child("totalDegree").getValue(String.class);



                        } sendBotMessage("Your total degree from Pseudobulbar Affect (PBA) test is \n"+degree+"%");
                        if(Double.parseDouble(degree)<(double) 13){
                            sendBotMessage("Your degree is lower than 13% ,\n" +
                                    " I think you're a normal person");

                        }else {
                            sendBotMessage("Your degree is more than 13% ,\n" +
                                    " I think you should visit a doctor");


                        }
                        sendBotMessage("You can see some tips and discription about "+illness1+" that can help you");

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getPBAQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber314", Context.MODE_PRIVATE);
        int numberOfAdhdQuestion=preferences.getInt("number",0);
        SharedPreferences.Editor editor=preferences.edit();
        String [] depressionTest={"Others have told me that I seem to become amused very easily or that I seem to become amused about things that really aren’t funny"
                ,"I find myself crying very easily"
                ,"I find that even when I try to control my laughter, I am often unable to do so"
                ,"There are times when I won’t be thinking of anything happy or funny at all, but will suddenly be overcome by funny or happy thoughts"
                ,"I find that even when I try to control my crying, I am often unable to do so."
                ,"I find that I am easily overcome by laughter."

        };
        Toast.makeText(getActivity(), " "+numberOfAdhdQuestion, Toast.LENGTH_SHORT).show();
        while (numberOfAdhdQuestion<=6) {
            if (numberOfAdhdQuestion <= 5) {


                sendBotMessage(depressionTest[numberOfAdhdQuestion]);
                numberOfAdhdQuestion++;
                editor.putInt("number", numberOfAdhdQuestion);
                editor.apply();
            }
            break;
        }

    }

    private void pbaTestQuestionsCounter(int count) {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter314", Context.MODE_PRIVATE);
        int pbaCounterDegree=preferences.getInt("counter",0);
        SharedPreferences.Editor editor=preferences.edit();
        if(count==1){
            pbaCounterDegree++;
        }else if(count==2){
            pbaCounterDegree+=2;
        }else if(count==3){
            pbaCounterDegree+=3;
        }else if(count==4){
            pbaCounterDegree+=4;
        }else if(count==5){
            pbaCounterDegree+=5;
        }
        editor.putInt("counter",pbaCounterDegree);
        editor.apply();
        double totalDegreeOfTest=Math.round(((float)pbaCounterDegree/35)*100);
        TestDegree testDegree=new TestDegree("Pseudobulbar Affect (PBA)",String.valueOf(totalDegreeOfTest));
        reportRefrence.child("tests").child("Pseudobulbar Affect (PBA)").setValue(testDegree);

        Toast.makeText(getActivity(), " dep  "+pbaCounterDegree+" total "+totalDegreeOfTest, Toast.LENGTH_SHORT).show();

    }



    private void readManiaDegree() {
        Query query6 = FirebaseDatabase.getInstance().getReference().child("PatientReportChatBot").child(firebaseUser.getUid()).child("Mania");
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            String degree;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                            degree=dataSnapshot.child("totalDegree").getValue(String.class);



                        } sendBotMessage("Your total degree from Mania test is \n"+degree+"%");
                        if(Double.parseDouble(degree)<(double) 40){
                            sendBotMessage("Your degree is lower than 40% ,\n" +
                                    " I think you're a normal person");

                        }else {
                            sendBotMessage("Your degree is more than 40% ,\n" +
                                    " I think you should visit a doctor");


                        }
                        sendBotMessage("You can see some tips and discription about "+illness1+" that can help you");

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getManiaQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber172", Context.MODE_PRIVATE);
        int numberOfAdhdQuestion=preferences.getInt("number",0);
        SharedPreferences.Editor editor=preferences.edit();
        String [] depressionTest={"Do you ever experience persistently increased goal-directed activity for more than a week?"
                ,"Do you ever experience inflated self-esteem or grandiose thoughts about yourself?"
                ,"Do you ever feel little need for sleep, feeling rested after only a few hours?"
                ,"Do you ever find yourself more talkative than usual?"
                ,"Do you experience racing thoughts or a flight of ideas?"
                ," Do you notice (or others comment) that you are easily distracted?"
                ,"Do you engage excessively in risky behaviors, sexually or financially?"

        };
        Toast.makeText(getActivity(), " "+numberOfAdhdQuestion, Toast.LENGTH_SHORT).show();
        while (numberOfAdhdQuestion<=7) {
            if (numberOfAdhdQuestion <= 6) {


                sendBotMessage(depressionTest[numberOfAdhdQuestion]);
                numberOfAdhdQuestion++;
                editor.putInt("number", numberOfAdhdQuestion);
                editor.apply();
            }
            break;
        }

    }

    private void ManiaTestQuestionsCounter(int count) {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter172", Context.MODE_PRIVATE);
        int pbaCounterDegree=preferences.getInt("counter",0);
        SharedPreferences.Editor editor=preferences.edit();
        if(count==0){}
        else if(count==1){
            pbaCounterDegree++;
        }else if(count==2){
            pbaCounterDegree+=2;
        }else if(count==3){
            pbaCounterDegree+=3;
        }else if(count==4){
            pbaCounterDegree+=4;
        }
        editor.putInt("counter",pbaCounterDegree);
        editor.apply();
        double totalDegreeOfTest=Math.round(((float)pbaCounterDegree/32)*100);
        TestDegree testDegree=new TestDegree("Mania",String.valueOf(totalDegreeOfTest));
        reportRefrence.child("tests").child("Mania").setValue(testDegree);

        Toast.makeText(getActivity(), " dep  "+pbaCounterDegree+" total "+totalDegreeOfTest, Toast.LENGTH_SHORT).show();

    }



    private void readNarcissisticPersonalityDisorderDegree() {
        Query query6 = FirebaseDatabase.getInstance().getReference().child("PatientReportChatBot").child(firebaseUser.getUid()).child("Narcissistic Personality Disorder");
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            String degree;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                            degree=dataSnapshot.child("totalDegree").getValue(String.class);



                        } sendBotMessage("Your total degree from Narcissistic Personality Disorder test is \n"+degree+"%");
                        if(Double.parseDouble(degree)<(double) 40){
                            sendBotMessage("Your degree is lower than 40% ,\n" +
                                    " I think you're a normal person");

                        }else {
                            sendBotMessage("Your degree is more than 40% ,\n" +
                                    " I think you should visit a doctor");


                        }
                        sendBotMessage("You can see some tips and discription about "+illness1+" that can help you");

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getNarcissisticPersonalityDisorderQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber180", Context.MODE_PRIVATE);
        int numberOfAdhdQuestion=preferences.getInt("number",0);
        SharedPreferences.Editor editor=preferences.edit();
        String [] depressionTest={"Do you expect to be seen as superior to other people?"
                ,"Do you ever exaggerate your talents or accomplishments?"
                ,"Do you require constant admiration from others?"
                ,"Do you engage in fantasies about being successful, powerful, or beautiful?"
                ,"Do you struggle to recognize the emotions and needs of other people?"
                ,"Do others perceive you as arrogant or haughty?"
                ,"How likely are you to experience heightened jealousy about the success or accomplishments of others?"
                ,"How likely are you to insist on having the best of everything (office, car, home, etc.)?"
                ,"How likely are you to assume that others are jealous of your talents and success?"
        };
        Toast.makeText(getActivity(), " "+numberOfAdhdQuestion, Toast.LENGTH_SHORT).show();
        while (numberOfAdhdQuestion<=9) {
            if (numberOfAdhdQuestion <= 8) {


                sendBotMessage(depressionTest[numberOfAdhdQuestion]);
                numberOfAdhdQuestion++;
                editor.putInt("number", numberOfAdhdQuestion);
                editor.apply();
            }
            break;
        }

    }

    private void NarcissisticPersonalityDisorderTestQuestionsCounter(int count) {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter180", Context.MODE_PRIVATE);
        int pbaCounterDegree=preferences.getInt("counter",0);
        SharedPreferences.Editor editor=preferences.edit();
        if(count==0){}
        else if(count==1){
            pbaCounterDegree++;
        }else if(count==2){
            pbaCounterDegree+=2;
        }else if(count==3){
            pbaCounterDegree+=3;
        }else if(count==4){
            pbaCounterDegree+=4;
        }
        editor.putInt("counter",pbaCounterDegree);
        editor.apply();
        double totalDegreeOfTest=Math.round(((float)pbaCounterDegree/40)*100);
        TestDegree testDegree=new TestDegree("Narcissistic Personality Disorder",String.valueOf(totalDegreeOfTest));
        reportRefrence.child("tests").child("Narcissistic Personality Disorder").setValue(testDegree);

        Toast.makeText(getActivity(), " dep  "+pbaCounterDegree+" total "+totalDegreeOfTest, Toast.LENGTH_SHORT).show();

    }





    private void readIllnessanxietydisorderDegree() {
        Query query6 = FirebaseDatabase.getInstance().getReference().child("PatientReportChatBot").child(firebaseUser.getUid()).child("Illness anxiety disorder");
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            String degree;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                            degree=dataSnapshot.child("totalDegree").getValue(String.class);



                        } sendBotMessage("Your total degree from Illness anxiety disorder test is \n"+degree+"%");
                        if(Double.parseDouble(degree)<(double) 40){
                            sendBotMessage("Your degree is lower than 40% ,\n" +
                                    " I think you're a normal person");

                        }else {
                            sendBotMessage("Your degree is more than 40% ,\n" +
                                    " I think you should visit a doctor");


                        }
                        sendBotMessage("You can see some tips and discription about "+illness1+" that can help you");

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getIllnessanxietydisorderQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber420", Context.MODE_PRIVATE);
        int numberOfAdhdQuestion=preferences.getInt("number",0);
        SharedPreferences.Editor editor=preferences.edit();
        String [] depressionTest={"Do your worries distract you from enjoying normal activities?"
                ,"Do you frequently visit health-related web sites?"
                ,"Are other people tired of hearing about your ailment(s)??"
                ,"When reading about a disease, does the thought that you might have some of the symptoms cross your mind?"
                ,"When you feel an ache or pain, do you instinctively wonder what serious illness might be causing it?"
                ,"Do you get frustrated when others dismiss your health worries?"
                ,"Are you afraid to see a doctor for fear of what he or she might tell you?"
                ,"Would you say you have a lot of stress?"
                ,"Have you switched doctors because you believed your regular doc wasn't taking your complaints seriously??"
        };
        Toast.makeText(getActivity(), " "+numberOfAdhdQuestion, Toast.LENGTH_SHORT).show();
        while (numberOfAdhdQuestion<=9) {
            if (numberOfAdhdQuestion <= 8) {


                sendBotMessage(depressionTest[numberOfAdhdQuestion]);
                numberOfAdhdQuestion++;
                editor.putInt("number", numberOfAdhdQuestion);
                editor.apply();
            }
            break;
        }

    }

    private void IllnessanxietydisorderTestQuestionsCounter(int count) {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter420", Context.MODE_PRIVATE);
        int pbaCounterDegree=preferences.getInt("counter",0);
        SharedPreferences.Editor editor=preferences.edit();
        if(count==0){}
        else if(count==1){
            pbaCounterDegree++;
        }else if(count==2){
            pbaCounterDegree+=2;
        }else if(count==3){
            pbaCounterDegree+=3;
        }else if(count==4){
            pbaCounterDegree+=4;
        }
        editor.putInt("counter",pbaCounterDegree);
        editor.apply();
        double totalDegreeOfTest=Math.round(((float)pbaCounterDegree/40)*100);
        TestDegree testDegree=new TestDegree("Illness anxiety disorder",String.valueOf(totalDegreeOfTest));
        reportRefrence.child("tests").child("Illness anxiety disorder").setValue(testDegree);

        Toast.makeText(getActivity(), " dep  "+pbaCounterDegree+" total "+totalDegreeOfTest, Toast.LENGTH_SHORT).show();

    }




    private void readDissociativeIdentityDisorderDegree() {
        Query query6 = FirebaseDatabase.getInstance().getReference().child("PatientReportChatBot").child(firebaseUser.getUid()).child("Dissociative Identity Disorder (DID)");
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            String degree;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                            degree=dataSnapshot.child("totalDegree").getValue(String.class);



                        } sendBotMessage("Your total degree from Dissociative Identity Disorder (DID) test is \n"+degree+"%");
                        if(Double.parseDouble(degree)<(double) 40){
                            sendBotMessage("Your degree is lower than 40% ,\n" +
                                    " I think you're a normal person");

                        }else {
                            sendBotMessage("Your degree is more than 40% ,\n" +
                                    " I think you should visit a doctor");


                        }
                        sendBotMessage("You can see some tips and discription about "+illness1+" that can help you");

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getDissociativeIdentityDisorderQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber401", Context.MODE_PRIVATE);
        int numberOfAdhdQuestion=preferences.getInt("number",0);
        SharedPreferences.Editor editor=preferences.edit();
        String [] depressionTest={"Do you feel memory problems?"
                ,"Are your symptoms (various conditions, memory problems) causing you a lot of pain and suffering?"
                ,"Are you experiencing major problems in school, work or home life because of your symptoms?"
                ,"Do your symptoms make it difficult for you to form friendships and relationships with others?"

        };
        Toast.makeText(getActivity(), " "+numberOfAdhdQuestion, Toast.LENGTH_SHORT).show();
        while (numberOfAdhdQuestion<=4) {
            if (numberOfAdhdQuestion <= 3) {


                sendBotMessage(depressionTest[numberOfAdhdQuestion]);
                numberOfAdhdQuestion++;
                editor.putInt("number", numberOfAdhdQuestion);
                editor.apply();
            }
            break;
        }

    }

    private void DissociativeIdentityDisorderTestQuestionsCounter(int count) {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter401", Context.MODE_PRIVATE);
        int pbaCounterDegree=preferences.getInt("counter",0);
        SharedPreferences.Editor editor=preferences.edit();
        if(count==0){}
        else if(count==1){
            pbaCounterDegree++;
        }
        editor.putInt("counter",pbaCounterDegree);
        editor.apply();
        double totalDegreeOfTest=Math.round(((float)pbaCounterDegree/5)*100);
        TestDegree testDegree=new TestDegree("Dissociative Identity Disorder (DID)",String.valueOf(totalDegreeOfTest));
        reportRefrence.child("tests").child("Dissociative Identity Disorder (DID)").setValue(testDegree);

        Toast.makeText(getActivity(), " dep  "+pbaCounterDegree+" total "+totalDegreeOfTest, Toast.LENGTH_SHORT).show();

    }


    private void readAutismDegree() {
        Query query6 = FirebaseDatabase.getInstance().getReference().child("PatientReportChatBot").child(firebaseUser.getUid()).child("Autism");
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            String degree;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                            degree=dataSnapshot.child("totalDegree").getValue(String.class);



                        } sendBotMessage("Your total degree from Autism test is \n"+degree+"%");
                        if(Double.parseDouble(degree)<=(double) 28.7){
                            sendBotMessage("Your degree is lower than 28.7% ,\n" +
                                    " I think you're a normal person");

                        }else {
                            sendBotMessage("Your degree is more than 28.7% ,\n" +
                                    " I think you should visit a doctor");


                        }
                        sendBotMessage("You can see some tips and discription about "+illness1+" that can help you");

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void getAutismQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber303", Context.MODE_PRIVATE);
        int numberOfAdhdQuestion=preferences.getInt("number",0);
        SharedPreferences.Editor editor=preferences.edit();
        String [] depressionTest={" is regarded as an \"eccentric professor\" by the other children?"
                ,"lives somewhat in a world of his/her own with restricted idiosyncratic intellectual interests?"
                ,"accumulates facts on certain subjects (good rote memory) but does not really understand the meaning?"
                ,"has a literal understanding of ambiguous and metaphorical language?"
                ,"has a deviant style of communication with a formal, fussy, old-fashioned or \"robot like\" language?"
                ,"invents idiosyncratic words and expressions?"
                ," has a different voice or speech?"
                ,"expresses sounds involuntarily; clears throat, grunts, smacks, cries or screams?"
                ,"is surprisingly good at some things and surprisingly poor at others?"
                ,"uses language freely but fails to make adjustment to fit social contexts or the needs of different listeners?"
                ,"lacks empathy?"
                ,"makes naive and embarrassing remarks?"
                ,"has a deviant style of gaze?"
                ,"wishes to be sociable but fails to make relationships with peers?"
                ,"can be with other children but only on his/her terms?"
                ,"lacks best friend?"
                ,"lacks common sense?"
                ,"is poor at games: no idea of cooperating in a team, scores \"own goals?"
                ,"has clumsy, ill coordinated, ungainly, awkward movements or gestures?"
                ,"has involuntary face or body movements?"
                ,"has difficulties in completing simple daily activities because of compulsory repetition of certain actions or thoughts?"
                ,"has special routines: insists on no change?"
                ,"shows idiosyncratic attachment to objects?"
                ,"is bullied by other children?"
                ,"has markedly unusual facial expression?"
                ,"has markedly unusual posture?"

        };
        Toast.makeText(getActivity(), " "+numberOfAdhdQuestion, Toast.LENGTH_SHORT).show();
        while (numberOfAdhdQuestion<=26) {
            if (numberOfAdhdQuestion <= 25) {


                sendBotMessage(depressionTest[numberOfAdhdQuestion]);
                numberOfAdhdQuestion++;
                editor.putInt("number", numberOfAdhdQuestion);
                editor.apply();
            }
            break;
        }


    }

    private void autismTestQuestionsCounter(int count) {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter303", Context.MODE_PRIVATE);
        int autismCounterDegree=preferences.getInt("counter",0);
        SharedPreferences.Editor editor=preferences.edit();
        if(count==0){
        }else if(count==1){
            autismCounterDegree++;
        }else if(count==2){
            autismCounterDegree+=2;
        }
        editor.putInt("counter",autismCounterDegree);
        editor.apply();
        double totalDegreeOfTest=Math.round(((float)autismCounterDegree/54)*100);
        TestDegree testDegree=new TestDegree("Autism",String.valueOf(totalDegreeOfTest));
        reportRefrence.child("tests").child("Autism").setValue(testDegree);

        Toast.makeText(getActivity(), " dep "+autismCounterDegree+" total "+totalDegreeOfTest, Toast.LENGTH_SHORT).show();

    }

    private void adhdfunctionQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber135", Context.MODE_PRIVATE);
        int numberOfAdhdQuestion=preferences.getInt("number",0);
        SharedPreferences.Editor editor=preferences.edit();
        String [] depressionTest={"HOW IS YOUR CHILD DOING WITH CIRCLE TIME?"
                ,"CAN YOUR CHILD FOLLOW ONE- OR TWO-STEP DIRECTIONS WITHOUT GETTING DISTRACTED?"
                ,"DOES YOUR CHILD “DART” AT THE STORE OR AT PRESCHOOL?"
                ,"DO YOU AVOID TAKING YOUR CHILD OUT IN PUBLIC, EVEN TO CASUAL, FAMILY-FRIENDLY PLACES?"
                ,"HAS YOUR CHILD HAD HER HEARING CHECKED?"
                ,"DO YOU THINK YOUR CHILD UNDERSTANDS YOUR WORDS WHEN YOU SPEAK TO HIM?"
                ,"COULD YOUR CHILD HAVE ANOTHER HEALTH CONDITION, LIKE SLEEP APNEA?"
                ,"HAS YOUR CHILD UNDERGONE AN ACUTE STRESSOR?"
                ,"DOES YOUR CHILD’S BEHAVIOUR DIFFER IN THE PRESENCE OF AUTHORITY (TEACHER , TRAINER)?"

        };
        Toast.makeText(getActivity(), " "+numberOfAdhdQuestion, Toast.LENGTH_SHORT).show();
        while (numberOfAdhdQuestion<=9) {
            if (numberOfAdhdQuestion <= 8) {


                sendBotMessage(depressionTest[numberOfAdhdQuestion]);
                numberOfAdhdQuestion++;
                editor.putInt("number", numberOfAdhdQuestion);
                editor.apply();
            }
            break;
        }



    }

    private void adhdTestQuestionsCounter(int count) {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter135", Context.MODE_PRIVATE);
        int adhdCounterDegree=preferences.getInt("counter",0);
        SharedPreferences.Editor editor=preferences.edit();
        if(count==0){
        }else if(count==1){
            adhdCounterDegree++;
        }
        editor.putInt("counter",adhdCounterDegree);
        editor.apply();
        double totalDegreeOfTest=Math.round(((float)adhdCounterDegree));
        TestDegree testDegree=new TestDegree("Attention-Deficit Hyperactivity Disorder (ADHD)",String.valueOf(totalDegreeOfTest));
        reportRefrence.child("tests").child("Attention-Deficit Hyperactivity Disorder (ADHD)").setValue(testDegree);

        Toast.makeText(getActivity(), " dep "+adhdCounterDegree+" total "+totalDegreeOfTest, Toast.LENGTH_SHORT).show();




    }

    private void getPsychosexualDysfunctionDegree() {
        Query query6 = FirebaseDatabase.getInstance().getReference().child("PatientReportChatBot").child(firebaseUser.getUid()).child("Psychosexual Dysfunction");
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            String degree;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                            degree=dataSnapshot.child("totalDegree").getValue(String.class);



                        } sendBotMessage("Your total degree from Psychosexual Dysfunction test is \n"+degree+"");
                        if(Double.parseDouble(degree)<(double) 40){
                            sendBotMessage("Your degree is lower than 40% ,\n" +
                                    " I think you're a normal person");

                        }else {
                            sendBotMessage("Your degree is more than 40% ,\n" +
                                    " I think you should visit a doctor");


                        }
                        sendBotMessage("You can see some tips and discription about "+illness1+" that can help you");

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void getPsychosexualDysfunctionQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber120", Context.MODE_PRIVATE);
        int numberOfQuestion=preferences.getInt("number",0);
        SharedPreferences.Editor editor=preferences.edit();
        String [] depressionTest={" How concerned have you been about your sexual performance?"
                ,"How much pain did you experience after or during sex?"
                ,"How happy have you felt with the emotional intimacy you experienced with your sexual companion?"
                ,"How often did you enjoy sex?"
                ,"While you were having sex, how would you describe the intensity of your sexual arousal?"
                ,"Over the past month, how would you rate the strength of your sexual impulses?"
                ,"Are you feeling depressed or stressed?"
                ,"Have you ever faced a childhood abuse or sexual trauma?"
                ,"Are you feeling low self-esteem, or losing interest in sex?"
                ,"Are you not satisfying your partner?"

        };
        Toast.makeText(getActivity(), " "+numberOfQuestion, Toast.LENGTH_SHORT).show();
        while (numberOfQuestion<=10) {
            if (numberOfQuestion <= 9) {
                sendBotMessage(depressionTest[numberOfQuestion]);
                numberOfQuestion++;
                editor.putInt("number", numberOfQuestion);
                editor.apply();
            }
            break;
        }

    }

    private void psychosexualDysfunctionTestQuestionsCounter(int count) {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter120", Context.MODE_PRIVATE);
        int psychosexualDysfunctionCounterDegree=preferences.getInt("counter",0);
        SharedPreferences.Editor editor=preferences.edit();
        if(count==0){
        }else if(count==1){
            psychosexualDysfunctionCounterDegree++;
        }else if(count==2){
            psychosexualDysfunctionCounterDegree+=2;
        }else if(count==3){
            psychosexualDysfunctionCounterDegree+=3;
        }else if(count==4){
            psychosexualDysfunctionCounterDegree+=4;
        }
        editor.putInt("counter",psychosexualDysfunctionCounterDegree);
        editor.apply();
        double totalDegreeOfTest=Math.round(((float)psychosexualDysfunctionCounterDegree/44)*100);
        TestDegree testDegree=new TestDegree("Psychosexual Dysfunction",String.valueOf(totalDegreeOfTest));
        reportRefrence.child("tests").child("Psychosexual Dysfunction").setValue(testDegree);

        Toast.makeText(getActivity(), " dep "+psychosexualDysfunctionCounterDegree+" total "+totalDegreeOfTest, Toast.LENGTH_SHORT).show();

    }

    private void readDrugAddictionDegree() {
        Query query6 = FirebaseDatabase.getInstance().getReference().child("PatientReportChatBot").child(firebaseUser.getUid()).child("drugAddiction");
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            String degree;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                            degree=dataSnapshot.child("totalDegree").getValue(String.class);



                        } sendBotMessage("Your total degree from Drug Addiction test is \n"+degree+"");
                        if(Double.parseDouble(degree)>(double) 3){
                            sendBotMessage("Your degree is more than 3 ,\n" +
                                    " I think you should visit a doctor");

                        }else if(Double.parseDouble(degree)==(double) 3){

                            sendBotMessage("Your degree is 3  ,\n" +
                                    " I think you're a normal person");


                        }else {
                            sendBotMessage("Your degree is less than 3  ,\n" +
                                    " I think you're a normal person");
                        }
                        sendBotMessage("You can see some tips and discription about "+illness1+" that can help you");


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getDrugAddictionQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber115", Context.MODE_PRIVATE);
        int numberOfQuestion=preferences.getInt("number",0);
        SharedPreferences.Editor editor=preferences.edit();
        String [] depressionTest={"Do you make sure you have a steady supply of your drug of choice on hand?"
                ,"Do you want to stop, but can’t?"
                ,"If you feel you can’t stop using, do you do things you normally would not do to get drugs?"
                ,"Do you feel you need drugs to function normally?"
                ,"Are you willing to do something dangerous while taking drugs, like operating a " +
                "motor vehicle, or some kind of equipment that could cause bodily harm?"

        };
        Toast.makeText(getActivity(), " "+numberOfQuestion, Toast.LENGTH_SHORT).show();
        while (numberOfQuestion<=5) {
            if (numberOfQuestion <= 4) {
                sendBotMessage(depressionTest[numberOfQuestion]);
                numberOfQuestion++;
                editor.putInt("number", numberOfQuestion);
                editor.apply();
            }
            break;
        }


    }

    private void drugAddictionTestQuestionsCounter(int count) {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter115", Context.MODE_PRIVATE);
        int drugAddictionCounterDegree=preferences.getInt("counter",0);
        SharedPreferences.Editor editor=preferences.edit();
        if(count==0){
        }else if(count==1){
            drugAddictionCounterDegree++;
        }
        editor.putInt("counter",drugAddictionCounterDegree);
        editor.apply();
        double totalDegreeOfTest=Math.round(((float)drugAddictionCounterDegree));
        TestDegree testDegree=new TestDegree("drugAddiction",String.valueOf(totalDegreeOfTest));
        reportRefrence.child("tests").child("drugAddiction").setValue(testDegree);

        Toast.makeText(getActivity(), " dep "+drugAddictionCounterDegree+" total "+totalDegreeOfTest, Toast.LENGTH_SHORT).show();



    }

    private void readAlcoholAddictionDegree() {
        Query query6 = FirebaseDatabase.getInstance().getReference().child("PatientReportChatBot").child(firebaseUser.getUid()).child("Alcohol Addiction");
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            String degree;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                            degree=dataSnapshot.child("totalDegree").getValue(String.class);



                        } sendBotMessage("Your total degree from Alcohol Addiction test is \n"+degree+"");
                        if(Double.parseDouble(degree)>(double) 5){
                            sendBotMessage("Your degree is more than 5 ,\n" +
                                    " I think you should visit a doctor");

                        }else if(Double.parseDouble(degree)==(double) 5){

                            sendBotMessage("Your degree is 5  ,\n" +
                                    " I think you're a normal person");


                        }else {
                            sendBotMessage("Your degree is less than 5  ,\n" +
                                    " I think you're a normal person");
                        }
                        sendBotMessage("You can see some tips and discription about "+illness1+" that can help you");


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void BorderlinePersonalityDisorderTestQuestionsCounter(int count) {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter140", Context.MODE_PRIVATE);
        int alcoholAddictionCounterDegree=preferences.getInt("counter",0);
        SharedPreferences.Editor editor=preferences.edit();
        if(count==0){
        }else if(count==1){
            alcoholAddictionCounterDegree++;
        }
        editor.putInt("counter",alcoholAddictionCounterDegree);
        editor.apply();
        double totalDegreeOfTest=Math.round(((float)alcoholAddictionCounterDegree/14)*100);
        TestDegree testDegree=new TestDegree("Borderline personality disorder (BPD)",String.valueOf(totalDegreeOfTest));
        reportRefrence.child("tests").child("Borderline personality disorder (BPD)").setValue(testDegree);

        Toast.makeText(getActivity(), " dep "+alcoholAddictionCounterDegree+" total "+totalDegreeOfTest, Toast.LENGTH_SHORT).show();


    }

    private void getBorderlinePersonalityDisorderQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber140", Context.MODE_PRIVATE);
        int numberOfQuestion=preferences.getInt("number",0);
        SharedPreferences.Editor editor=preferences.edit();
        String [] depressionTest={"Have you had a sense of overwhelming emptiness or feeling \"hollow\"?"
                ,"Have you intentionally harmed or hurt yourself (for example, cutting yourself) or attempted suicide?"
                ,"Have you been angry or irritable much of the time, or responded angrily to those close to you?"
                ,"Have you often felt detached from reality, like things are imaginary?"
                ,"Have you found that you mistrust those around you?"
                ,"Have you suffered from extreme mood swings?"
                ,"Have you engaged in two or more impulsive behaviors such as sudden emotional outbursts, compulsive eating, gambling more than you can afford, or bingeing alcohol?"
                ,"Have you often felt that you lack a clear identity or you just aren't sure who you are inside?"
                ,"Have you made frantic attempts to avoid feelings of abandonment (for example, repeated calls to a friend or partner to gain reassurance that they still care)?"
                ,"Have your relationships with those nearest to you been damaged by quarrels or frequent breakups?"
                ,"Is your level of anger often inappropriate, intense, and difficult to control?"
                ,"Do you work hard to prevent those close to me from abandoning me, whether that feeling is real or illusory ?"
                ,"Do your feelings and mood fluctuate quickly ?"


        };
        Toast.makeText(getActivity(), " "+numberOfQuestion, Toast.LENGTH_SHORT).show();
        while (numberOfQuestion<=13) {
            if (numberOfQuestion <= 12) {
                sendBotMessage(depressionTest[numberOfQuestion]);
                numberOfQuestion++;
                editor.putInt("number", numberOfQuestion);
                editor.apply();
            }
            break;
        }

    }

    private void readBorderlinePersonalityDisorderDegree() {
        Query query6 = FirebaseDatabase.getInstance().getReference().child("PatientReportChatBot").child(firebaseUser.getUid()).child("Borderline personality disorder (BPD)");
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            String degree;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                            degree=dataSnapshot.child("totalDegree").getValue(String.class);



                        } sendBotMessage("Your total degree from Borderline personality disorder (BPD) test is \n"+degree+"%");
                        if(Double.parseDouble(degree)<(double) 60){
                            sendBotMessage("Your degree is lower than 60% ,\n" +
                                    " I think you're a normal person");

                        }else {
                            sendBotMessage("Your degree is more than 60% ,\n" +
                                    " I think you should visit a doctor");


                        }
                        sendBotMessage("You can see some tips and discription about "+illness1+" that can help you");


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void lowselfesteemTestQuestionsCounter(int count) {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter150", Context.MODE_PRIVATE);
        int alcoholAddictionCounterDegree=preferences.getInt("counter",0);
        SharedPreferences.Editor editor=preferences.edit();
        if(count==0){
        }else if(count==1){
            alcoholAddictionCounterDegree++;
        }
        editor.putInt("counter",alcoholAddictionCounterDegree);
        editor.apply();
        double totalDegreeOfTest=Math.round(((float)alcoholAddictionCounterDegree/7)*100);
        TestDegree testDegree=new TestDegree("low self esteem",String.valueOf(totalDegreeOfTest));
        reportRefrence.child("tests").child("low self esteem").setValue(testDegree);

        Toast.makeText(getActivity(), " dep "+alcoholAddictionCounterDegree+" total "+totalDegreeOfTest, Toast.LENGTH_SHORT).show();


    }

    private void getlowselfesteemQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber150", Context.MODE_PRIVATE);
        int numberOfQuestion=preferences.getInt("number",0);
        SharedPreferences.Editor editor=preferences.edit();
        String [] depressionTest={"Do you feel hate about your outward appearance?"
                ,"Do you feel that yourself is worthless?"
                ,"Do you not accept criticism from others?"
                ,"Do you always feel fear and anxiety?"
                ,"Do you always feel quick rage?"
                ,"Do you always seek to please others?"



        };
        Toast.makeText(getActivity(), " "+numberOfQuestion, Toast.LENGTH_SHORT).show();
        while (numberOfQuestion<=6) {
            if (numberOfQuestion <= 5) {
                sendBotMessage(depressionTest[numberOfQuestion]);
                numberOfQuestion++;
                editor.putInt("number", numberOfQuestion);
                editor.apply();
            }
            break;
        }

    }

    private void readlowselfesteemDegree() {
        Query query6 = FirebaseDatabase.getInstance().getReference().child("PatientReportChatBot").child(firebaseUser.getUid()).child("low self esteem");
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            String degree;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                            degree=dataSnapshot.child("totalDegree").getValue(String.class);



                        } sendBotMessage("Your total degree low self esteem test is \n"+degree+"%");
                        if(Double.parseDouble(degree)<(double) 60){
                            sendBotMessage("Your degree is lower than 60% ,\n" +
                                    " I think you're a normal person");

                        }else {
                            sendBotMessage("Your degree is more than 60% ,\n" +
                                    " I think you should visit a doctor");


                        }
                        sendBotMessage("You can see some tips and discription about "+illness1+" that can help you");


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void BipolarDisorderTestQuestionsCounter(int count) {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter162", Context.MODE_PRIVATE);
        int alcoholAddictionCounterDegree=preferences.getInt("counter",0);
        SharedPreferences.Editor editor=preferences.edit();
        if(count==0){
        }else if(count==1){
            alcoholAddictionCounterDegree++;
        }
        editor.putInt("counter",alcoholAddictionCounterDegree);
        editor.apply();
        double totalDegreeOfTest=Math.round(((float)alcoholAddictionCounterDegree/17)*100);
        TestDegree testDegree=new TestDegree("Bipolar Disorder",String.valueOf(totalDegreeOfTest));
        reportRefrence.child("tests").child("Bipolar Disorder").setValue(testDegree);

        Toast.makeText(getActivity(), " dep "+alcoholAddictionCounterDegree+" total "+totalDegreeOfTest, Toast.LENGTH_SHORT).show();


    }

    private void getBipolarDisorderQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber162", Context.MODE_PRIVATE);
        int numberOfQuestion=preferences.getInt("number",0);
        SharedPreferences.Editor editor=preferences.edit();
        String [] depressionTest={"You were much more socially active than normal, and at all hours:"
                ,"Your behavior was unsafe or risky in a way that wasn't normal for you:"
                ,"You felt exhausted or unusually fatigued:"
                ,"You got angry and you were aggressive or violent towards others:"
                ,"You talked a lot more or a lot faster than normal:"
                ,"You took risks with your finances which caused problems for yourself or others:"
                ,"Nothing could make you happy or joyful:"
                ,"Your sex drive was much higher than normal:"
                ,"You felt slower than normal, in your body or your mind, for a noticeable period of time:"
                ,"You were a lot more confident or self-assured than normal:"
                ,"You didn’t sleep very much and felt like you didn’t really need to:"
                ,"You felt inadequate or to blame for things:"
                ,"You were much more lively and energetic than normal:"
                ,"You became absent-minded and had difficulty focusing on tasks:"
                ,"You were much more productive than normal:"
                ,"You became so excitable or agitated that others around you were surprised by your behavior, or your behavior caused problems for you:"



        };
        Toast.makeText(getActivity(), " "+numberOfQuestion, Toast.LENGTH_SHORT).show();
        while (numberOfQuestion<=16) {
            if (numberOfQuestion <= 15) {
                sendBotMessage(depressionTest[numberOfQuestion]);
                numberOfQuestion++;
                editor.putInt("number", numberOfQuestion);
                editor.apply();
            }
            break;
        }

    }

    private void readBipolarDisorderDegree() {
        Query query6 = FirebaseDatabase.getInstance().getReference().child("PatientReportChatBot").child(firebaseUser.getUid()).child("Bipolar Disorder");
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            String degree;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                            degree=dataSnapshot.child("totalDegree").getValue(String.class);



                        } sendBotMessage("Your total degree from Bipolar Disorder test is \n"+degree+"%");
                        if(Double.parseDouble(degree)<(double) 60){
                            sendBotMessage("Your degree is lower than 60% ,\n" +
                                    " I think you're a normal person");

                        }else {
                            sendBotMessage("Your degree is more than 60% ,\n" +
                                    " I think you should visit a doctor");


                        }
                        sendBotMessage("You can see some tips and discription about "+illness1+" that can help you");


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }




    private void lcoholAddictionTestQuestionsCounter(int count) {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter101", Context.MODE_PRIVATE);
        int alcoholAddictionCounterDegree=preferences.getInt("counter",0);
        SharedPreferences.Editor editor=preferences.edit();
        if(count==0){
        }else if(count==1){
            alcoholAddictionCounterDegree++;
        }
        editor.putInt("counter",alcoholAddictionCounterDegree);
        editor.apply();
        double totalDegreeOfTest=Math.round(((float)alcoholAddictionCounterDegree));
        TestDegree testDegree=new TestDegree("Alcohol Addiction",String.valueOf(totalDegreeOfTest));
        reportRefrence.child("tests").child("Alcohol Addiction").setValue(testDegree);

        Toast.makeText(getActivity(), " dep "+alcoholAddictionCounterDegree+" total "+totalDegreeOfTest, Toast.LENGTH_SHORT).show();


    }

    private void getAlcoholAddictionQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber101", Context.MODE_PRIVATE);
        int numberOfQuestion=preferences.getInt("number",0);
        SharedPreferences.Editor editor=preferences.edit();
        String [] depressionTest={"Do you drink because you are uncomfortable in social situations?"
                ,"Do you drink alcohol to build up your self-confidence?"
                ,"Is drinking affecting your relationships with friends?"
                ,"Do you drink alone?"
                ,"Do you drink to escape from studies or home worries?"
                ,"-Do you feel guilty or depressed after drinking alcohol?"
                ,"Does it bother you when someone expresses concern over your drinking habits?"
                ,"Do you have to take a drink when you go out to socialize?"
                ,"Do you get along better with other people when you drink?"
                ,"Do you get into financial troubles over buying liquor?"
                ,"Do you feel more important when you drink?"
                ,"Have you lost friends since you started drinking alcohol?"
                ,"Do you drink more than most of your friends?"
                ,"Have you started hanging around with a crowd that drinks more than your old friends?"

        };
        Toast.makeText(getActivity(), " "+numberOfQuestion, Toast.LENGTH_SHORT).show();
        while (numberOfQuestion<=14) {
            if (numberOfQuestion <= 13) {
                sendBotMessage(depressionTest[numberOfQuestion]);
                numberOfQuestion++;
                editor.putInt("number", numberOfQuestion);
                editor.apply();
            }
            break;
        }

    }

    private void readStressDegree() {
        Query query6 = FirebaseDatabase.getInstance().getReference().child("PatientReportChatBot").child(firebaseUser.getUid()).child("Stress");
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            String degree;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                            degree=dataSnapshot.child("totalDegree").getValue(String.class);



                        } sendBotMessage("Your total degree from Stress test is \n"+degree+"%");
                        if(Double.parseDouble(degree)<(double) 40){
                            sendBotMessage("Your degree is lower than 40% ,\n" +
                                    " I think you're a normal person");

                        }else {
                            sendBotMessage("Your degree is more than 40% ,\n" +
                                    " I think you should visit a doctor");


                        }
                        sendBotMessage("You can see some tips and discription about "+illness1+" that can help you");


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getstressQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber75", Context.MODE_PRIVATE);
        int numberOfStressQuestion=preferences.getInt("number",0);
        SharedPreferences.Editor editor=preferences.edit();
        String [] depressionTest={"Do you experience fatigue and/or struggle to fall or stay asleep?"
                ,"Do you worry excessively and feel overwhelmed with responsibilities?"
                ,"Do you struggle to focus on tasks or stay motivated?"
                ,"Do you experience irritability, sadness, or anger?"
                ,"Do you have little appetite or find that you are overeating?"
                ,"Do you struggle to regulate how much caffeine, alcohol, or tobacco you use?"
                ,"Do you withdraw from others or feel overwhelmed in groups of people?"

        };
        Toast.makeText(getActivity(), " "+numberOfStressQuestion, Toast.LENGTH_SHORT).show();
        while (numberOfStressQuestion<=7) {
            if (numberOfStressQuestion <= 6) {
                sendBotMessage(depressionTest[numberOfStressQuestion]);
                numberOfStressQuestion++;
                editor.putInt("number", numberOfStressQuestion);
                editor.apply();
            }
            break;
        }

    }

    private void stressTestQuestionsCounter(int count) {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter75", Context.MODE_PRIVATE);
        int stressCounterDegree=preferences.getInt("counter",0);
        SharedPreferences.Editor editor=preferences.edit();
        if(count==0){
        }else if(count==1){
            stressCounterDegree++;
        }else if(count==2){
            stressCounterDegree+=2;
        }else if(count==3){
            stressCounterDegree+=3;
        }else if(count==4){
            stressCounterDegree+=4;
        }
        editor.putInt("counter",stressCounterDegree);
        editor.apply();
        double totalDegreeOfTest=Math.round(((float)stressCounterDegree/32)*100);
        TestDegree testDegree=new TestDegree("Stress",String.valueOf(totalDegreeOfTest));
        reportRefrence.child("tests").child("Stress").setValue(testDegree);

        Toast.makeText(getActivity(), " dep "+stressCounterDegree+" total "+totalDegreeOfTest, Toast.LENGTH_SHORT).show();


    }

    private void readAnixietyDegree() {
        Query query6 = FirebaseDatabase.getInstance().getReference().child("PatientReportChatBot").child(firebaseUser.getUid()).child("Anixiety");
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            String degree;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                            degree=dataSnapshot.child("totalDegree").getValue(String.class);



                        } sendBotMessage("Your total degree from Anixiety test is \n"+degree+"%");
                        if(Double.parseDouble(degree)<(double) 40){
                            sendBotMessage("Your degree is lower than 40% ,\n" +
                                    " I think you're a normal person");

                        }else {
                            sendBotMessage("Your degree is more than 40% ,\n" +
                                    " I think you should visit a doctor");


                        }
                        sendBotMessage("You can see some tips and discription about "+illness1+" that can help you");


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void anixietyTestQuestionsCounter(int count) {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter85", Context.MODE_PRIVATE);
        int depressionCounterDegree=preferences.getInt("counter",0);
        SharedPreferences.Editor editor=preferences.edit();
        if(count==0){
        }else if(count==1){
            depressionCounterDegree++;
        }else if(count==2){
            depressionCounterDegree+=2;
        }else if(count==3){
            depressionCounterDegree+=3;
        }
        editor.putInt("counter",depressionCounterDegree);
        editor.apply();
        double totalDegreeOfTest=Math.round(((float)depressionCounterDegree/21)*100);
        TestDegree testDegree=new TestDegree("Anixiety",String.valueOf(totalDegreeOfTest));
        reportRefrence.child("tests").child("Anixiety").setValue(testDegree);

        Toast.makeText(getActivity(), " dep "+depressionCounterDegree+" total "+totalDegreeOfTest, Toast.LENGTH_SHORT).show();


    }

    private void getAnixietyQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber85", Context.MODE_PRIVATE);
        int numberOfAnixietyQuestion=preferences.getInt("number",0);
        SharedPreferences.Editor editor=preferences.edit();
        String [] depressionTest={"How often have you been bothered by not being able to stop or control worrying over the last two weeks?"
                ,"How often have you been bothered by worrying too much about different things over the last two weeks?"
                ,"How often have you been bothered by having trouble relaxing over the last two weeks?"
                ," How often have you been bothered by being so restless that it is hard to sit still over the last two weeks?"
                ," How often have you been bothered by becoming easily annoyed or irritable over the last two weeks?"
                ,"How often have you been bothered by feeling afraid as if something awful might happen over the last two weeks?"

        };
           Toast.makeText(getActivity(), " "+numberOfAnixietyQuestion, Toast.LENGTH_SHORT).show();
        while (numberOfAnixietyQuestion<=6) {
            if (numberOfAnixietyQuestion <= 5) {
                sendBotMessage(depressionTest[numberOfAnixietyQuestion]);
                numberOfAnixietyQuestion++;
                editor.putInt("number", numberOfAnixietyQuestion);
                editor.apply();
            }
            break;
        }


    }

    private void startTestWithFirstQuestion(String ilness, String question) {
        sendUserMessage(ilness+" test");
        sendBotMessage("Starting "+ilness+" test ....!");
        sendBotMessage(question);
    }

    private void readDepressionDegree() {

        Query query6 = FirebaseDatabase.getInstance().getReference().child("PatientReportChatBot").child(firebaseUser.getUid()).child("Depression");
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            String degree;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                              degree=dataSnapshot.child("totalDegree").getValue(String.class);



                        } sendBotMessage("Your total degree from Depression test is \n"+degree+"%");
                        if(Double.parseDouble(degree)<(double) 40){
                            sendBotMessage("Your degree is lower than 40% ,\n" +
                                    " I think you're a normal person");

                        }else {
                            sendBotMessage("Your degree is more than 40% ,\n" +
                                    " I think you should visit a doctor");


                        }
                        sendBotMessage("You can see some tips and discription about "+illness1+" that can help you");


                    }





                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    // farid////////////////////////////////
    private void psychosisTestQuestionsCounter(int count) {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter200", Context.MODE_PRIVATE);
        int alcoholAddictionCounterDegree=preferences.getInt("counter",0);
        SharedPreferences.Editor editor=preferences.edit();
        if(count==0){
        }else if(count==1){
            alcoholAddictionCounterDegree++;
        }
        editor.putInt("counter",alcoholAddictionCounterDegree);
        editor.apply();
        double totalDegreeOfTest=Math.round(((float)alcoholAddictionCounterDegree/10)*100);
        TestDegree testDegree=new TestDegree("Psychosis",String.valueOf(totalDegreeOfTest));
        reportRefrence.child("tests").child("Psychosis").setValue(testDegree);

        Toast.makeText(getActivity(), " dep "+alcoholAddictionCounterDegree+" total "+totalDegreeOfTest, Toast.LENGTH_SHORT).show();


    }

    private void getPsychosisQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber200", Context.MODE_PRIVATE);
        int numberOfQuestion=preferences.getInt("number",0);
        SharedPreferences.Editor editor=preferences.edit();
        String [] depressionTest={"I sometimes feel that someone or something else controls my thoughts"
                ,"I frequently feel like other people are plotting against me"
                ,"I feel that my body is different or behaves in a different way to how it used to"
                ,"I have lost interest in things I used to like"
                ,"I have seen someone's face change in front of me, or my own face has changed in the mirror"
                ,"Meeting new people makes me very nervous or stressed"
                ,"I am not always sure if something has actually happened, or whether I imagined it"
                ,"I sometimes spot hidden messages in adverts on television, in shop displays, or in how things are arranged"
                ,"I get the feeling that I have lived through the present situation before, like things are repeating"


        };
        Toast.makeText(getActivity(), " "+numberOfQuestion, Toast.LENGTH_SHORT).show();
        while (numberOfQuestion<= 9) {
            if (numberOfQuestion <= 8) {
                sendBotMessage(depressionTest[numberOfQuestion]);
                numberOfQuestion++;
                editor.putInt("number", numberOfQuestion);
                editor.apply();
            }
            break;
        }

    }

    private void readPsychosisDegree() {
        Query query6 = FirebaseDatabase.getInstance().getReference().child("PatientReportChatBot").child(firebaseUser.getUid()).child("Psychosis");
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            String degree;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                            degree=dataSnapshot.child("totalDegree").getValue(String.class);



                        } sendBotMessage("Your total degree from Psychosis test is \n"+degree+"%");
                        if(Double.parseDouble(degree)<(double) 40){
                            sendBotMessage("Your degree is lower than 60% ,\n" +
                                    " I think you're a normal person");

                        }else {
                            sendBotMessage("Your degree is more than 60% ,\n" +
                                    " I think you should visit a doctor");


                        }
                        sendBotMessage("You can see some tips and discription about "+illness1+" that can help you");


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void socialAnxietyDisorderTestQuestionsCounter(int count) {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter210", Context.MODE_PRIVATE);
        int alcoholAddictionCounterDegree=preferences.getInt("counter",0);
        SharedPreferences.Editor editor=preferences.edit();
        if(count==0){
        }else if(count==1){
            alcoholAddictionCounterDegree++;
        }else if(count==2){
            alcoholAddictionCounterDegree+=2;
        }else if (count==3){
            alcoholAddictionCounterDegree+=3;
        }else if (count==4){
            alcoholAddictionCounterDegree+=4;
        }
        editor.putInt("counter",alcoholAddictionCounterDegree);
        editor.apply();
        double totalDegreeOfTest=Math.round(((float)alcoholAddictionCounterDegree/40)*100);
        TestDegree testDegree=new TestDegree("Social Anxiety Disorder",String.valueOf(totalDegreeOfTest));
        reportRefrence.child("tests").child("Social Anxiety Disorder").setValue(testDegree);

        Toast.makeText(getActivity(), " dep "+alcoholAddictionCounterDegree+" total "+totalDegreeOfTest, Toast.LENGTH_SHORT).show();


    }

    private void getSocialAnxietyDisorderQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber210", Context.MODE_PRIVATE);
        int numberOfQuestion=preferences.getInt("number",0);
        SharedPreferences.Editor editor=preferences.edit();
        String [] depressionTest={"Do you fear that you will be negatively evaluated by others when in social situations?"
                ,"Do you avoid social situations because of fear or anxiety?"
                ,"Do you avoid situations, or feel uncomfortable, where you do not know people well?"
                ,"Do you panic when you have to do something in front of others, whether speaking up in a meeting or presenting to a group?"
                ,"Is it hard to imagine that others are judging you as anxious, weak, crazy, stupid, boring, intimidating, dirty, or unlikeable when you are in a group setting?"
                ,"When in social situations, do you worry that people will notice you are experiencing anxiety symptoms such as blushing, trembling, sweating, stumbling over your words, or staring?"
                ,"Are you extremely conscious of your actions when in social settings because you fear they might offend someone or you could be rejected?"
                ,"Do you experience significant worrying about being in certain social situations which is out of proportion to the threat posed by the social situation?"
                ,"Are your work life, home life, social life, and/or relationships affected by your anxiety?"



        };
        Toast.makeText(getActivity(), " "+numberOfQuestion, Toast.LENGTH_SHORT).show();
        while (numberOfQuestion<= 9) {
            if (numberOfQuestion <= 8) {
                sendBotMessage(depressionTest[numberOfQuestion]);
                numberOfQuestion++;
                editor.putInt("number", numberOfQuestion);
                editor.apply();
            }
            break;
        }

    }

    private void readSocialAnxietyDisorderDegree() {
        Query query6 = FirebaseDatabase.getInstance().getReference().child("PatientReportChatBot").child(firebaseUser.getUid()).child("Social Anxiety Disorder");
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            String degree;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                            degree=dataSnapshot.child("totalDegree").getValue(String.class);



                        } sendBotMessage("Your total degree from Social Anxiety Disorder test is \n"+degree+"%");
                        if(Double.parseDouble(degree)<(double) 40){
                            sendBotMessage("Your degree is lower than 40% ,\n" +
                                    " I think you're a normal person");

                        }else {
                            sendBotMessage("Your degree is more than 40% ,\n" +
                                    " I think you should visit a doctor");


                        }
                        sendBotMessage("You can see some tips and discription about "+illness1+" that can help you");


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void empathyDeficitDisorderTestQuestionsCounter(int count) {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter221", Context.MODE_PRIVATE);
        int alcoholAddictionCounterDegree=preferences.getInt("counter",0);
        SharedPreferences.Editor editor=preferences.edit();
        if(count==0){
        }else if(count==1){
            alcoholAddictionCounterDegree++;
        }else if(count==2){
            alcoholAddictionCounterDegree+=2;
        }else if (count==3){
            alcoholAddictionCounterDegree+=3;
        }else if (count==4){
            alcoholAddictionCounterDegree+=4;
        }
        editor.putInt("counter",alcoholAddictionCounterDegree);
        editor.apply();
        double totalDegreeOfTest=Math.round(((float)alcoholAddictionCounterDegree/40)*100);
        TestDegree testDegree=new TestDegree("Empathy Deficit Disorder",String.valueOf(totalDegreeOfTest));
        reportRefrence.child("tests").child("Empathy Deficit Disorder").setValue(testDegree);

        Toast.makeText(getActivity(), " dep "+alcoholAddictionCounterDegree+" total "+totalDegreeOfTest, Toast.LENGTH_SHORT).show();


    }

    private void getEmpathyDeficitDisorderQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber221", Context.MODE_PRIVATE);
        int numberOfQuestion=preferences.getInt("number",0);
        SharedPreferences.Editor editor=preferences.edit();
        String [] depressionTest={"I attempt to change the topic of conversation when a family member or friend wants to talk about what’s troubling them?"
                ,"Other people’s emotional states and moods aren't that important to me"
                ,"When someone is ill because of their own unhealthy behaviors, I am not sorry for them"
                ,"I get annoyed when someone I'm around gets tearful"
                ,"I don’t get it if another person is unhappy even if they have told me so"
                ,"When bad things to happen to other people, it doesn’t bother me very much"
                ,"I don’t feel compassionate and sympathetic towards people who aren’t as lucky as I am"
                ,"Cheering other people up doesn’t change my feeling"
                ,"I don’t sympathize with people who are being exploited"



        };
        Toast.makeText(getActivity(), " "+numberOfQuestion, Toast.LENGTH_SHORT).show();
        while (numberOfQuestion<= 9) {
            if (numberOfQuestion <= 8) {
                sendBotMessage(depressionTest[numberOfQuestion]);
                numberOfQuestion++;
                editor.putInt("number", numberOfQuestion);
                editor.apply();
            }
            break;
        }

    }

    private void readEmpathyDeficitDisorderDegree() {
        Query query6 = FirebaseDatabase.getInstance().getReference().child("PatientReportChatBot").child(firebaseUser.getUid()).child("Empathy Deficit Disorder");
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            String degree;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                            degree=dataSnapshot.child("totalDegree").getValue(String.class);



                        } sendBotMessage("Your total degree from Empathy Deficit Disorder test is \n"+degree+"%");
                        if(Double.parseDouble(degree)<(double) 40){
                            sendBotMessage("Your degree is lower than 40% ,\n" +
                                    " I think you're a normal person");

                        }else {
                            sendBotMessage("Your degree is more than 40% ,\n" +
                                    " I think you should visit a doctor");


                        }
                        sendBotMessage("You can see some tips and discription about "+illness1+" that can help you");


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void imposterSyndromeTestQuestionsCounter(int count) {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter231", Context.MODE_PRIVATE);
        int alcoholAddictionCounterDegree=preferences.getInt("counter",0);
        SharedPreferences.Editor editor=preferences.edit();
        if(count==0){
        }else if(count==1){
            alcoholAddictionCounterDegree++;
        }else if(count==2){
            alcoholAddictionCounterDegree+=2;
        }else if (count==3){
            alcoholAddictionCounterDegree+=3;
        }else if (count==4){
            alcoholAddictionCounterDegree+=4;
        }
        editor.putInt("counter",alcoholAddictionCounterDegree);
        editor.apply();
        double totalDegreeOfTest=Math.round(((float)alcoholAddictionCounterDegree/40)*100);
        TestDegree testDegree=new TestDegree("Imposter syndrome",String.valueOf(totalDegreeOfTest));
        reportRefrence.child("tests").child("Imposter syndrome").setValue(testDegree);

        Toast.makeText(getActivity(), " dep "+alcoholAddictionCounterDegree+" total "+totalDegreeOfTest, Toast.LENGTH_SHORT).show();


    }

    private void getimposterSyndromerQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber231", Context.MODE_PRIVATE);
        int numberOfQuestion=preferences.getInt("number",0);
        SharedPreferences.Editor editor=preferences.edit();
        String [] depressionTest={"Even when I'm doing well, I don't think I really deserve it"
                ,"I worry about feeling very shy if my incompetence is exposed."
                ,"I fear that people will discover that I am not as smart as they think."
                ,"I underestimated my accomplishments"
                ,"It's hard for me to accept compliments."
                ,"I compare myself to others"
                ,"I feel failure is not an option."
                ,"I hesitate to show off my accomplishments"
                ,"I don't like drawing attention to my successes."



        };
        Toast.makeText(getActivity(), " "+numberOfQuestion, Toast.LENGTH_SHORT).show();
        while (numberOfQuestion<= 9) {
            if (numberOfQuestion <= 8) {
                sendBotMessage(depressionTest[numberOfQuestion]);
                numberOfQuestion++;
                editor.putInt("number", numberOfQuestion);
                editor.apply();
            }
            break;
        }

    }

    private void readImposterSyndromeDegree() {
        Query query6 = FirebaseDatabase.getInstance().getReference().child("PatientReportChatBot").child(firebaseUser.getUid()).child("Imposter syndrome");
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            String degree;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                            degree=dataSnapshot.child("totalDegree").getValue(String.class);



                        } sendBotMessage("Your total degree from Imposter syndrome test is \n"+degree+"%");
                        if(Double.parseDouble(degree)<(double) 40){
                            sendBotMessage("Your degree is lower than 40% ,\n" +
                                    " I think you're a normal person");

                        }else {
                            sendBotMessage("Your degree is more than 40% ,\n" +
                                    " I think you should visit a doctor");


                        }
                        sendBotMessage("You can see some tips and discription about "+illness1+" that can help you");


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }




    private void obsessiveCompulsiveDisorderTestQuestionsCounter(int count) {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter242", Context.MODE_PRIVATE);
        int alcoholAddictionCounterDegree=preferences.getInt("counter",0);
        SharedPreferences.Editor editor=preferences.edit();
        if(count==0){
        }else if(count==1){
            alcoholAddictionCounterDegree++;
        }else if(count==2){
            alcoholAddictionCounterDegree+=2;
        }else if (count==3){
            alcoholAddictionCounterDegree+=3;
        }else if (count==4){
            alcoholAddictionCounterDegree+=4;
        }
        editor.putInt("counter",alcoholAddictionCounterDegree);
        editor.apply();
        double totalDegreeOfTest=Math.round(((float)alcoholAddictionCounterDegree/40)*100);
        TestDegree testDegree=new TestDegree("Obsessive-Compulsive Disorder (OCD)",String.valueOf(totalDegreeOfTest));
        reportRefrence.child("tests").child("Obsessive-Compulsive Disorder (OCD)").setValue(testDegree);

        Toast.makeText(getActivity(), " dep "+alcoholAddictionCounterDegree+" total "+totalDegreeOfTest, Toast.LENGTH_SHORT).show();


    }
    private void getObsessiveCompulsiveDisorderQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber242", Context.MODE_PRIVATE);
        int numberOfQuestion=preferences.getInt("number",0);
        SharedPreferences.Editor editor=preferences.edit();
        String [] depressionTest={"2-Do you ever fear germs or engage in excessive cleaning?"
                ,"3-Do you experience the need to constantly check on something or arrange things?"
                ,"4-Do you experience intrusive thoughts that are aggressive or about taboo topics?"
                ,"5-Do you struggle to control obsessive thoughts or compulsive behaviors?"
                ,"6-Do you engage in rituals that provide temporary relief to your anxiety, such as counting, checking, or cleaning?"
                ,"7-Do you spend at least one hour a day thinking obsessive thoughts or performing these ritual behaviors?"
                ,"8-Are work life, home life, or relationships affected by your obsessive thinking or ritual behaviors?"
                ,"9-to what extent did these thoughts make you feel distressed or upset"
                ,"10-how anxious would you have been if you had been prevented acting out the compulsive behaviors"


        };
        Toast.makeText(getActivity(), " "+numberOfQuestion, Toast.LENGTH_SHORT).show();
        while (numberOfQuestion<= 9) {
            if (numberOfQuestion <= 8) {
                sendBotMessage(depressionTest[numberOfQuestion]);
                numberOfQuestion++;
                editor.putInt("number", numberOfQuestion);
                editor.apply();
            }
            break;
        }

    }

    private void readObsessiveCompulsiveDisordeDegree() {
        Query query6 = FirebaseDatabase.getInstance().getReference().child("PatientReportChatBot").child(firebaseUser.getUid()).child("Obsessive-Compulsive Disorder (OCD)");
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            String degree;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                            degree=dataSnapshot.child("totalDegree").getValue(String.class);



                        } sendBotMessage("Your total degree from Obsessive-Compulsive Disorder (OCD) test is \n"+degree+"%");
                        if(Double.parseDouble(degree)<(double) 40){
                            sendBotMessage("Your degree is lower than 40% ,\n" +
                                    " I think you're a normal person");

                        }else {
                            sendBotMessage("Your degree is more than 40% ,\n" +
                                    " I think you should visit a doctor");


                        }
                        sendBotMessage("You can see some tips and discription about "+illness1+" that can help you");


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private void posttraumaticStressDisorderTestQuestionsCounter(int count) {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter252", Context.MODE_PRIVATE);
        int alcoholAddictionCounterDegree=preferences.getInt("counter",0);
        SharedPreferences.Editor editor=preferences.edit();
        if(count==0){
        }else if(count==1){
            alcoholAddictionCounterDegree++;
        }else if(count==2){
            alcoholAddictionCounterDegree+=2;
        }else if (count==3){
            alcoholAddictionCounterDegree+=3;
        }else if (count==4){
            alcoholAddictionCounterDegree+=4;
        }
        editor.putInt("counter",alcoholAddictionCounterDegree);
        editor.apply();
        double totalDegreeOfTest=Math.round(((float)alcoholAddictionCounterDegree/40)*100);
        TestDegree testDegree=new TestDegree("Posttraumatic stress disorder (PTSD)",String.valueOf(totalDegreeOfTest));
        reportRefrence.child("tests").child("Posttraumatic stress disorder (PTSD)").setValue(testDegree);

        Toast.makeText(getActivity(), " dep "+alcoholAddictionCounterDegree+" total "+totalDegreeOfTest, Toast.LENGTH_SHORT).show();


    }

    private void getPosttraumaticStressDisorderQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber252", Context.MODE_PRIVATE);
        int numberOfQuestion=preferences.getInt("number",0);
        SharedPreferences.Editor editor=preferences.edit();
        String [] depressionTest={"2- I had trouble staying asleep"
                ,"3- Other things kept making me think about it"
                ,"4- I felt irritable and angry"
                ,"5- I avoided letting myself get upset when I thought about it or was reminded of it"
                ,"6- I thought about the event when I didn't mean to"
                ,"7- I felt as if the event hadn't happened or it wasn't real"
                ,"8- I have stayed away from reminders about the situation"
                ,"9- Images and pictures of the event pop into my mind"
                ,"10- I have been jumpy and easily startled"

        };
        Toast.makeText(getActivity(), " "+numberOfQuestion, Toast.LENGTH_SHORT).show();
        while (numberOfQuestion<= 9) {
            if (numberOfQuestion <= 8) {
                sendBotMessage(depressionTest[numberOfQuestion]);
                numberOfQuestion++;
                editor.putInt("number", numberOfQuestion);
                editor.apply();
            }
            break;
        }

    }


    private void readPosttraumaticStressDisorderDegree() {
        Query query6 = FirebaseDatabase.getInstance().getReference().child("PatientReportChatBot").child(firebaseUser.getUid()).child("Posttraumatic stress disorder (PTSD)");
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            String degree;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                            degree=dataSnapshot.child("totalDegree").getValue(String.class);



                        } sendBotMessage("Your total degree from Posttraumatic stress disorder (PTSD) test is \n"+degree+"%");
                        if(Double.parseDouble(degree)<(double) 40){
                            sendBotMessage("Your degree is lower than 40% ,\n" +
                                    " I think you're a normal person");

                        }else {
                            sendBotMessage("Your degree is more than 40% ,\n" +
                                    " I think you should visit a doctor");


                        }
                        sendBotMessage("You can see some tips and discription about "+illness1+" that can help you");


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void eatingDisorderTestQuestionsCounter(int count) {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter262", Context.MODE_PRIVATE);
        int alcoholAddictionCounterDegree=preferences.getInt("counter",0);
        SharedPreferences.Editor editor=preferences.edit();
        if(count==0){
        }else if(count==1){
            alcoholAddictionCounterDegree++;
        }else if(count==2){
            alcoholAddictionCounterDegree+=2;
        }else if (count==3){
            alcoholAddictionCounterDegree+=3;
        }else if (count==4){
            alcoholAddictionCounterDegree+=4;
        }
        editor.putInt("counter",alcoholAddictionCounterDegree);
        editor.apply();
        double totalDegreeOfTest=Math.round(((float)alcoholAddictionCounterDegree/44)*100);
        TestDegree testDegree=new TestDegree("Eating Disorder",String.valueOf(totalDegreeOfTest));
        reportRefrence.child("tests").child("Eating Disorder").setValue(testDegree);

        Toast.makeText(getActivity(), " dep "+alcoholAddictionCounterDegree+" total "+totalDegreeOfTest, Toast.LENGTH_SHORT).show();


    }

    private void getEatingDisorderQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber262", Context.MODE_PRIVATE);
        int numberOfQuestion=preferences.getInt("number",0);
        SharedPreferences.Editor editor=preferences.edit();
        String [] depressionTest={"Do you find yourself eating large amounts of food when you aren’t hungry?"
                ,"Do you experience feelings of shame or guilt about how much you eat?"
                ,"Do you hide your eating behaviors from loved ones?"
                ,"Do you struggle to control when and how much you eat?"
                ,"Do you hoard food or hide empty food containers?"
                ,"Do you find yourself eating more rapidly than you’d like?"
                ,"Do you find yourself eating more when you try to restrict how much you eat?"
                ,"A close family member has, has had an eating disorder"
                ,"when others critique my eating or pressure me to eat more this makes me very annoyed"
                ,"I am not happy at all with my eating patterns  "

        };
        Toast.makeText(getActivity(), " "+numberOfQuestion, Toast.LENGTH_SHORT).show();
        while (numberOfQuestion<= 10) {
            if (numberOfQuestion <= 9) {
                sendBotMessage(depressionTest[numberOfQuestion]);
                numberOfQuestion++;
                editor.putInt("number", numberOfQuestion);
                editor.apply();
            }
            break;
        }

    }


    private void readEatingDisorderDegree() {
        Query query6 = FirebaseDatabase.getInstance().getReference().child("PatientReportChatBot").child(firebaseUser.getUid()).child("Eating Disorder");
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            String degree;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                            degree=dataSnapshot.child("totalDegree").getValue(String.class);



                        } sendBotMessage("Your total degree from Eating Disorder test is \n"+degree+"%");
                        if(Double.parseDouble(degree)<(double) 40){
                            sendBotMessage("Your degree is lower than 40% ,\n" +
                                    " I think you're a normal person");

                        }else {
                            sendBotMessage("Your degree is more than 40% ,\n" +
                                    " I think you should visit a doctor");


                        }
                        sendBotMessage("You can see some tips and discription about "+illness1+" that can help you");


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



///////////////////////////////////////////////////////////////////////////




    private void setChooseVisable() {
        relativeLayout.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);

    }

    private void setRelativeVisable() {
        relativeLayout.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);

    }
    private void depressionTestQuestionsCounter(int count){

        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter93", Context.MODE_PRIVATE);
        int depressionCounterDegree=preferences.getInt("counter",0);
        SharedPreferences.Editor editor=preferences.edit();
        if(count==0){
        }else if(count==1){
            depressionCounterDegree++;
        }else if(count==2){
            depressionCounterDegree+=2;
        }else if(count==3){
            depressionCounterDegree+=3;
        }
        editor.putInt("counter",depressionCounterDegree);
        editor.apply();
        double totalDegreeOfTest=Math.round(((float)depressionCounterDegree/27)*100);
        TestDegree testDegree=new TestDegree("Depression",String.valueOf(totalDegreeOfTest));
        reportRefrence.child("tests").child("Depression").setValue(testDegree);

       Toast.makeText(getActivity(), " dep "+depressionCounterDegree+" total "+totalDegreeOfTest, Toast.LENGTH_SHORT).show();


    }
    private void getDepressionQuestions() {

        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber92", Context.MODE_PRIVATE);
        int numberOfQuestion=preferences.getInt("number",0);
        SharedPreferences.Editor editor=preferences.edit();
        String [] depressionTest={"How often have you been bothered that you have little interest or pleasure in doing things over the last two weeks?"
                ,"How often have you been bothered by trouble falling asleep, staying asleep, or sleeping too much over the last two weeks?"
                ,"How often have you been bothered that you have poor appetite, weight loss, or overeating over the last two weeks?"
                ," How often have you been bothered by feeling tired, or having little energy over the last two weeks?"
                ," How often have you been bothered by feeling bad about yourself – or feeling that you are a failure, or that you have let yourself or your family down over the last two weeks?"
                ,"How often have you been bothered that you have trouble concentrating on things like school work, reading, or watching TV over the last two weeks?"
                ,"How often have you been bothered by moving or speaking so slowly that other people could have noticed? Or the opposite – being so fidgety or restless that you were moving around a lot more than usual over the last two weeks?"
                ,"How often have you been bothered by moving or speaking so slowly that other people could have noticed? Or the opposite – being so fidgety or restless that you were moving around a lot more than usual over the last two weeks?."
        };
        Toast.makeText(getActivity(), " "+numberOfQuestion, Toast.LENGTH_SHORT).show();
        while (numberOfQuestion<=8) {
            if (numberOfQuestion <= 7) {
                sendBotMessage(depressionTest[numberOfQuestion]);
                numberOfQuestion++;
                editor.putInt("number", numberOfQuestion);
                editor.apply();
            }

            break;
        }


    }


    private void sendBotMessage(String msg) {
        Chat chat3=new Chat("Bot",firebaseUser.getUid(),msg);
        databaseAllReference.child("ChatBot").child(firebaseUser.getUid()).push().setValue(chat3);

    }
    private void sendUserMessage(String msg) {
        Chat chat3=new Chat(firebaseUser.getUid(),"Bot",msg);
        databaseAllReference.child("ChatBot").child(firebaseUser.getUid()).push().setValue(chat3);

    }

    private void getPrefrance() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        String botFirstTime=preferences.getString("first","n");
        final String botSecondTimeMessage=preferences.getString("second","n");
        final String botThirdTimeMessage=preferences.getString("third","n");

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        //  databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(id);
        Query query6 = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                            String  name=dataSnapshot.child("name").getValue(String.class);
                            String  image=dataSnapshot.child("uri").getValue(String.class);
                            SharedPreferences.Editor editor=preferences.edit();
                            editor.putString("first","Hello "+name);
                            editor.putString("second","I am a myndiabot! and i will help you checking if you have such a mental illness or not,\n" +
                                    "specify it, recommend doctors for you and more");
                            editor.putString("third","But first i need to know more about you");

                            reportRefrence.child("name").setValue(name);
                            editor.apply();



                        }

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        sendFirstBotMessage(firebaseUser.getUid(),botFirstTime,botSecondTimeMessage,botThirdTimeMessage);
    }

    private void readMessages(final String myId, final String botId) {

        chats=new ArrayList<>();
        databaseReference=FirebaseDatabase.getInstance().getReference("ChatBot").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chats.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Chat chat=dataSnapshot.getValue(Chat.class);
                    if(chat.getSender().equals(myId)&&chat.getReciever().equals(botId)||
                            chat.getReciever().equals(myId)&&chat.getSender().equals(botId)){
                        chats.add(chat);

                        try {
                            if(chat.getMessage().equals("How old are you?")){
                                setRelativeVisable();
                                textSend.setInputType(InputType.TYPE_CLASS_NUMBER);
                            }
                            else if (chat.getMessage().equals("Are you single?")){
                                setChooseVisable();

                                chooseTwo.setVisibility(View.VISIBLE);
                                chooseThree.setVisibility(View.VISIBLE);
                                chooseOne.setText("Yeah!");
                                chooseTwo.setText("Nope!");
                                chooseThree.setText("It just complecated!");
                                chooseFour.setVisibility(View.GONE);

                            }else if(chat.getMessage().equals("Yeah!")||chat.getMessage().equals("Nope!")||
                                    chat.getMessage().equals("It just complecated!")){
                                chooseOne.setText("Working");
                                chooseTwo.setText("Studying");
                                chooseThree.setText("Doing both");
                                chooseFour.setText("Neither both");
                                chooseFour.setVisibility(View.VISIBLE);
                            }else if(chat.getMessage().equals("Wow good , what's your job?")||
                                    chat.getMessage().equals("Amazing , what do you study?")||
                                    chat.getMessage().equals("Tell me more about that ?"))
                            {
                                    setRelativeVisable();
                                    textSend.setInputType(InputType.TYPE_CLASS_TEXT);

                            }else if(chat.getMessage().equals("ok , i see")||
                                    chat.getMessage().equals("i see")){
                                setChooseVisable();
                                chooseOne.setText("Yes");
                                chooseTwo.setText("Nope");
                                chooseThree.setVisibility(View.GONE);
                                chooseFour.setVisibility(View.GONE);
                            }else if(chat.getMessage().equals("How do you feel right now ?")){
                               chooseOne.setText("Sad");
                                chooseTwo.setText("Depressed");
                                chooseThree.setText("Happy");
                                chooseFour.setText("Angry");
                                chooseThree.setVisibility(View.VISIBLE);
                                chooseFour.setVisibility(View.VISIBLE);

                            }else if(chat.getMessage().equals("I'm listening")){
                                textSend.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
                                setRelativeVisable();
                            }else if(chat.getMessage().equals(recommendedTest("Borderline personality disorder (BPD)"))){
                                chooseForStartTest("Borderline personality disorder (BPD)");
                            }else if(chat.getMessage().equals(recommendedTest("Depression"))){
                                chooseForStartTest("Depression");
                            }else if(chat.getMessage().equals(recommendedTest("Anixiety"))){
                                chooseForStartTest("Anixiety");
                            }else if(chat.getMessage().equals(recommendedTest("Stress"))) {
                                chooseForStartTest("Stress");
                            }else if(chat.getMessage().equals(recommendedTest("Alcohol Addiction"))){
                                chooseForStartTest("Alcohol Addiction");
                            }else if(chat.getMessage().equals(recommendedTest("Drug Addiction"))){
                                chooseForStartTest("Drug Addiction");
                            }else if(chat.getMessage().equals(recommendedTest("Psychosexual Dysfunction"))){
                                chooseForStartTest("Psychosexual Dysfunction");
                            }else if(chat.getMessage().equals(recommendedTest("low self esteem"))){
                                chooseForStartTest("low self esteem");
                            }else if(chat.getMessage().equals(recommendedTest("Attention-Deficit Hyperactivity Disorder (ADHD)"))){
                                chooseForStartTest("Attention-Deficit Hyperactivity Disorder (ADHD)");
                            }else if(chat.getMessage().equals(recommendedTest("Bipolar Disorder"))){
                                chooseForStartTest("Bipolar Disorder");
                                ////////farid
                            }else if(chat.getMessage().equals(recommendedTest("Psychosis"))){
                                chooseForStartTest("Psychosis");
                            }else if(chat.getMessage().equals(recommendedTest("Social Anxiety Disorder"))){
                                chooseForStartTest("Social Anxiety Disorder");
                            }else if(chat.getMessage().equals(recommendedTest("Autism"))){
                                chooseForStartTest("Autism");
                            }else if(chat.getMessage().equals(recommendedTest("Pseudobulbar Affect (PBA)"))){
                                chooseForStartTest("Pseudobulbar Affect (PBA)");
                            }else if(chat.getMessage().equals(recommendedTest("Mania"))){
                                chooseForStartTest("Mania");
                            }else if(chat.getMessage().equals(recommendedTest("Empathy Deficit Disorder"))){
                                chooseForStartTest("Empathy Deficit Disorder");
                            }else if(chat.getMessage().equals(recommendedTest("Imposter syndrome"))){
                                chooseForStartTest("Imposter syndrome");
                            }else if(chat.getMessage().equals(recommendedTest("Schizophrenia"))){
                                chooseForStartTest("Schizophrenia");
                            }else if(chat.getMessage().equals(recommendedTest("Dissociative Identity Disorder (DID)"))){
                                chooseForStartTest("Dissociative Identity Disorder (DID)");
                            }else if(chat.getMessage().equals(recommendedTest("Illness anxiety disorder"))){
                                chooseForStartTest("Illness anxiety disorder");
                            }else if(chat.getMessage().equals(recommendedTest("Bullying"))){
                                chooseForStartTest("Bullying");
                            }else if(chat.getMessage().equals(recommendedTest("Obsessive-Compulsive Disorder (OCD)"))){
                                chooseForStartTest("Obsessive-Compulsive Disorder (OCD)");
                            }else if(chat.getMessage().equals(recommendedTest("Posttraumatic stress disorder (PTSD)"))){
                                chooseForStartTest("Posttraumatic stress disorder (PTSD)");
                            }else if(chat.getMessage().equals(recommendedTest("Eating Disorder"))){
                                chooseForStartTest("Eating Disorder");
                            }
                            else if(chat.getMessage().equals("Starting Depression test ....!")){
                                chooseFour.setVisibility(View.VISIBLE);
                                chooseThree.setVisibility(View.VISIBLE);
                                depressionTest();
                            } else if(chat.getMessage().equals("Starting Borderline personality disorder (BPD) test ....!")){
                                chooseFour.setVisibility(View.VISIBLE);
                                chooseThree.setVisibility(View.VISIBLE);
                                BorderlinePersonalityDisordertest();
                            }else if(chat.getMessage().equals("Starting Anixiety test ....!")){
                                chooseFour.setVisibility(View.VISIBLE);
                                chooseThree.setVisibility(View.VISIBLE);
                                nixietyTest();
                            }else if(chat.getMessage().equals("Starting Stress test ....!")){
                                chooseFour.setVisibility(View.VISIBLE);
                                chooseThree.setVisibility(View.VISIBLE);
                                stressTest();
                            }else if(chat.getMessage().equals("Starting Alcohol Addiction test ....!")){
                                alcoholAddictiontest();
                            }else if(chat.getMessage().equals("Starting Drug Addiction test ....!")){
                                drugAddictiontest();

                            }else if(chat.getMessage().equals("Starting Psychosexual Dysfunction test ....!")){
                                psychosexualDysfunctionTest();
                            }else if(chat.getMessage().equals("Starting Attention-Deficit Hyperactivity Disorder (ADHD) test ....!")){
                                AdhdTest();
                            }else if(chat.getMessage().equals("Starting low self esteem test ....!")){
                                lowselfesteemTest();
                            }else if(chat.getMessage().equals("Starting Bipolar Disorder test ....!")){
                                BipolarDisorderTest();
                            }
                            else if(chat.getMessage().equals("Starting Psychosis test ....!")){
                                psychosisTest();

                            }else if(chat.getMessage().equals("Starting Autism test ....!")){
                                autismTest();
                            }else if(chat.getMessage().equals("Starting Pseudobulbar Affect (PBA) test ....!")){
                                pbaTest();
                            }else if(chat.getMessage().equals("Starting Mania test ....!")){
                                ManiaTest();
                            }else if(chat.getMessage().equals("Starting Narcissistic Personality Disorder test ....!")){
                                NarcissisticPersonalityDisorderTest();
                            }
                            else if(chat.getMessage().equals("Starting Social Anxiety Disorder test ....!")){
                                socialAnxietyDisorderTest();
                            }else if(chat.getMessage().equals("Starting Empathy Deficit Disorder test ....!")){
                                empathyDeficitDisorderTest();
                            }else if(chat.getMessage().equals("Starting Imposter syndrome test ....!")){
                                imposterSyndromeTest();
                            }else if(chat.getMessage().equals("Starting Schizophrenia test ....!")){
                                schizophreniaTest();
                            }else if(chat.getMessage().equals("Starting Dissociative Identity Disorder (DID) test ....!")){
                                DissociativeIdentityDisorderTest();
                            }else if(chat.getMessage().equals("Starting Illness anxiety disorder test ....!")){
                                IllnessanxietydisorderTest();
                            }else if(chat.getMessage().equals("Starting Bullying test ....!")){
                                bullyingTest();
                            }else if(chat.getMessage().equals("Starting Obsessive-Compulsive Disorder (OCD) test ....!")){
                                obsessiveCompulsiveDisorderTest();
                            }else if(chat.getMessage().equals("Starting Posttraumatic stress disorder (PTSD) test ....!")){
                                posttraumaticStressDisorderTest();
                            }else if(chat.getMessage().equals("Starting Eating Disorder test ....!")){
                                eatingDisorderTest();
                            }

                            else if(chat.getMessage().equals("How often have you been bothered by moving or speaking so slowly that other people could have noticed? Or the opposite" +
                                    " – being so fidgety or restless that you were moving around a lot more than usual over the last two weeks?.")){
                                chooseOne.setText("Not at all.");
                                chooseTwo.setText("Several days.");
                                chooseThree.setText("More than half of the days.");
                                chooseFour.setText("Nearly everyday.");
                            }else if(chat.getMessage().equals("How often have you been bothered by feeling afraid as " +
                                    "if something awful might happen over the last two weeks?")){
                                chooseOne.setText("Not at  all. ");
                                chooseTwo.setText("Several  days. ");
                                chooseThree.setText("More  than half of the days. ");
                                chooseFour.setText("Nearly  everyday. ");
                            }else if(chat.getMessage().equals("Do you withdraw from others " +
                                    "or feel overwhelmed in groups of people?")){
                                chooseOne.setText("Never ");
                                chooseTwo.setText("Rerely ");
                                chooseThree.setText("Sometimes ");
                                chooseFour.setText("Often ");
                                chooseFive.setText("Very often ");
                            }else if(chat.getMessage().equals("Have you started hanging around with " +
                                    "a crowd that drinks more than your old friends?")){
                                chooseOne.setText("YES ");
                                chooseTwo.setText("NO ");
                            }else if(chat.getMessage().equals("Are you willing to do something dangerous while taking drugs," +
                                    " like operating a motor vehicle, or some kind of equipment that could cause bodily harm?")){
                                chooseOne.setText("YES. ");
                                chooseTwo.setText("NO. ");
                            }
                            else if(chat.getMessage().equals("Are you not satisfying your partner?")){
                                chooseOne.setText("NEVER.");
                                chooseTwo.setText("RARELY.");
                                chooseThree.setText("SOMETIMES.");
                                chooseFour.setText("OFTEN.");
                                chooseFive.setText("VERY OFTEN.");

                            }else if(chat.getMessage().equals("HOW IS YOUR CHILD DOING WITH CIRCLE TIME?")){
                                chooseOne.setText("HE PREFER CIRCLE TIME");
                                chooseTwo.setText("HE DOESN'T PREFER CIRCLE TIME");

                            }else if(chat.getMessage().equals("CAN YOUR CHILD FOLLOW ONE- OR TWO-STEP DIRECTIONS WITHOUT GETTING DISTRACTED?")){
                                chooseOne.setText("HE CAN");
                                chooseTwo.setText("HE GETS DISTRACTED");

                            }else if(chat.getMessage().equals("DOES YOUR CHILD “DART” AT THE STORE OR AT PRESCHOOL?")){
                                chooseOne.setText("YES, HE DOES");
                                chooseTwo.setText("NO, HE DOESN'T");

                            }else if(chat.getMessage().equals("DO YOU AVOID TAKING YOUR CHILD OUT IN PUBLIC, EVEN TO CASUAL, FAMILY-FRIENDLY PLACES?")){
                                chooseOne.setText("YES, I DO");
                                chooseTwo.setText("NO, I DON'T");

                            }else if(chat.getMessage().equals("HAS YOUR CHILD HAD HER HEARING CHECKED?")){

                                chooseOne.setText(" YES ");
                                chooseTwo.setText(" NO ");

                            }else if(chat.getMessage().equals("DO YOU THINK YOUR CHILD UNDERSTANDS YOUR WORDS WHEN YOU SPEAK TO HIM?")){
                                chooseOne.setText("YES HE OBEYES ME");
                                chooseTwo.setText("NO, HE DOESN'T UNDERSTAND ME");

                            }else if(chat.getMessage().equals("COULD YOUR CHILD HAVE ANOTHER HEALTH CONDITION, LIKE SLEEP APNEA?")){
                                chooseOne.setText("YES, HE COULD");
                                chooseTwo.setText("NO, HE COULDN'T");
                            }else if(chat.getMessage().equals("HAS YOUR CHILD UNDERGONE AN ACUTE STRESSOR?")){
                                chooseOne.setText("YES, BUT MORE THAN 6 MONTHS AGO");
                                chooseTwo.setText("NOT AT ALL");


                            }else if(chat.getMessage().equals("DOES YOUR CHILD’S BEHAVIOUR DIFFER IN THE PRESENCE OF AUTHORITY (TEACHER , TRAINER)?")){
                                chooseOne.setText(" YES. ");
                                chooseTwo.setText(" NO. ");


                            }
                            else if(chat.getMessage().equals("Do your feelings and mood fluctuate quickly ?")){
                                chooseOne.setText(" YES   . ");
                                chooseTwo.setText(" NO   . ");


                            }
                            else if(chat.getMessage().equals("Do you always seek to please others?")){
                                chooseOne.setText(". Yes ");
                                chooseTwo.setText(". No ");


                            }
                            else if(chat.getMessage().equals("You became so excitable or agitated that others around you were surprised by your behavior, or your behavior caused problems for you:")){
                                chooseOne.setText(" . Yes ");
                                chooseTwo.setText(" . No ");


                            }else if(chat.getMessage().equals("Do your symptoms make it difficult for you to form friendships and relationships with others?")){
                                chooseOne.setText(" Yes .  ");
                                chooseTwo.setText(" No .  ");


                            }
                            else if(chat.getMessage().equals("I get the feeling that I have lived through the present situation before, like things are repeating")){
                                chooseOne.setText(" Agree");
                                chooseTwo.setText(" Disagree");
                            }else if(chat.getMessage().equals("has markedly unusual posture?")){
                                chooseOne.setText("\n No. \n");
                                chooseTwo.setText("\n Somewhat. \n");
                                chooseThree.setText("\n Yes. \n");
                            }else if(chat.getMessage().equals("I find that I am easily overcome by laughter.")){
                                chooseOne.setText("Applies never.");
                                chooseTwo.setText("Applies rarely.");
                                chooseThree.setText("Applies occasionally.");
                                chooseFour.setText("Applies frequently.");
                                chooseFive.setText("Applies most of the time.");

                            }else if(chat.getMessage().equals("Do you engage excessively in risky behaviors, sexually or financially?")){
                                chooseOne.setText(" .Never");
                                chooseTwo.setText(" .Rarely");
                                chooseThree.setText(" .Sometimes");
                                chooseFour.setText(" .Often");
                                chooseFive.setText(" .Very Often");

                            }else if(chat.getMessage().equals("Are your work life, home life, social life, and/or relationships affected by your anxiety?")){
                                chooseOne.setText("\nNever.\n");
                                chooseTwo.setText("\nRarely.\n");
                                chooseThree.setText("\nSometimes.\n");
                                chooseFour.setText("\nOften.\n");
                                chooseFive.setText("\nVery Often.\n");

                            }
                            else if(chat.getMessage().equals("I don’t sympathize with people who are being exploited")){
                                chooseOne.setText("\n Never.\n");
                                chooseTwo.setText("\n Rarely.\n");
                                chooseThree.setText("\n Sometimes.\n");
                                chooseFour.setText("\n Often.\n");
                                chooseFive.setText("\n Very Often.\n");

                            }
                            else if(chat.getMessage().equals("How likely are you to assume that others are jealous of your talents and success?")){
                                chooseOne.setText("  .Never");
                                chooseTwo.setText("  .Rarely");
                                chooseThree.setText("  .Sometimes");
                                chooseFour.setText("  .Often");
                                chooseFive.setText("  .Very Often");

                            }else if(chat.getMessage().equals("Have you switched doctors because you believed your regular doc wasn't taking your complaints seriously??")){
                                chooseOne.setText("  .Never ");
                                chooseTwo.setText("  .Rarely ");
                                chooseThree.setText("  .Sometimes ");
                                chooseFour.setText("  .Often ");
                                chooseFive.setText("  .Very Often ");

                            }  else if(chat.getMessage().equals("I don't like drawing attention to my successes.")){
                                chooseOne.setText("\nNever. \n");
                                chooseTwo.setText("\nRarely. \n");
                                chooseThree.setText("\nSometimes. \n");
                                chooseFour.setText("\nOften. \n");
                                chooseFive.setText("\nVery often. \n");
                            }else if(chat.getMessage().equals("Are you struggling to keep up with everyday tasks like showering, changing clothes, paying bills, cleaning, cooking, etc.?")){
                                chooseOne.setText("  \n yes.  \n");
                                chooseTwo.setText("  \n no.  \n");


                            }else if(chat.getMessage().equals("Do you experience headaches or stomach aches before you have to interact with a hurtful person?")){
                                chooseFive.setText(". VERY OFTEN.");
                                chooseFour.setText(". OFTEN.");
                                chooseThree.setText(". SOMETIMES.");
                                chooseOne.setText(". NEVER.");
                                chooseTwo.setText(". RARELY.");

                            }else if(chat.getMessage().equals("10-how anxious would you have been if you had been prevented acting out the compulsive behaviors")){
                                chooseOne.setText("\n  Never. \n");
                                chooseTwo.setText("\n  Rarely. \n");
                                chooseThree.setText("\n  Sometimes. \n");
                                chooseFour.setText("\n  Often. \n");
                                chooseFive.setText("\n  Very often. \n");
                            }

                            else if(chat.getMessage().equals("10- I have been jumpy and easily startled")){
                                chooseOne.setText("\n   Never. \n");
                                chooseTwo.setText("\n   Rarely. \n");
                                chooseThree.setText("\n   Sometimes. \n");
                                chooseFour.setText("\n   Often. \n");
                                chooseFive.setText("\n   Very often. \n");
                            }else if(chat.getMessage().contains("You can see some tips and discription about "+illness1+" that can help you")){
                                chooseThree.setVisibility(View.GONE);
                               chooseFive.setVisibility(View.GONE);
                               chooseFour.setVisibility(View.GONE);
                                chooseOne.setText("Show "+illness1+" discreption");
                                chooseTwo.setText("No, Thanks");
                            }else if(chat.getMessage().equals("feel free to snooze me any time :)")){
                                chooseTwo.setVisibility(View.GONE);

                                chooseOne.setText("Start Session Again");
                            }

                            else if(chat.getMessage().equals("I am not happy at all with my eating patterns  ")){
                                chooseOne.setText("\n   Never.  \n");
                                chooseTwo.setText("\n   Rarely.  \n");
                                chooseThree.setText("\n   Sometimes.  \n");
                                chooseFour.setText("\n   Often.  \n");
                                chooseFive.setText("\n   Very often.  \n");
                            }

                            else  if(chat.getMessage().equals("Not at all.")||chat.getMessage().equals("Several days.")||
                            chat.getMessage().equals("More than half of the days.")||chat.getMessage().equals("Nearly everyday.")){

                               /* chooseOne.setVisibility(View.GONE);
                                chooseTwo.setVisibility(View.GONE);
                                chooseThree.setVisibility(View.GONE);
                                chooseFour.setVisibility(View.GONE);

                                */

                            }
                        }catch (Exception e){

                        }

                    //    Toast.makeText(getActivity(), chat.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                       new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.smoothScrollToPosition(chats.size());


                            }
                        });





                    messageAdapter=new MessageAdapter(chats,getActivity(),"uri");
                    recyclerView.setAdapter(messageAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void bullyingTest() {
        chooseOne.setVisibility(View.VISIBLE);
        chooseTwo.setVisibility(View.VISIBLE);
        chooseThree.setVisibility(View.VISIBLE);
        chooseFive.setVisibility(View.VISIBLE);
        chooseFour.setVisibility(View.VISIBLE);
        chooseFive.setText(". VERY OFTEN");
        chooseFour.setText(". OFTEN");
        chooseThree.setText(". SOMETIMES");
        chooseOne.setText(". NEVER");
        chooseTwo.setText(". RARELY");

    }

    private void schizophreniaTest() {
        chooseOne.setVisibility(View.VISIBLE);
        chooseTwo.setVisibility(View.VISIBLE);

        chooseThree.setVisibility(View.GONE);
        chooseFive.setVisibility(View.GONE);
        chooseFour.setVisibility(View.GONE);
        chooseOne.setText("  \n yes  \n");
        chooseTwo.setText("  \n no  \n");

    }

    private void pbaTest() {
        chooseOne.setVisibility(View.VISIBLE);
        chooseTwo.setVisibility(View.VISIBLE);
        chooseThree.setVisibility(View.VISIBLE);
        chooseFive.setVisibility(View.VISIBLE);
        chooseFour.setVisibility(View.VISIBLE);
        chooseOne.setText("Applies never");
        chooseTwo.setText("Applies rarely");
        chooseThree.setText("Applies occasionally");
        chooseFour.setText("Applies frequently");
        chooseFive.setText("Applies most of the time");


    }

    private void autismTest() {
        chooseOne.setVisibility(View.VISIBLE);
        chooseTwo.setVisibility(View.VISIBLE);
        chooseThree.setVisibility(View.VISIBLE);
        chooseFour.setVisibility(View.GONE);
        chooseFive.setVisibility(View.GONE);
        chooseOne.setText("\n No \n");
        chooseTwo.setText("\n Somewhat \n");
        chooseThree.setText("\n Yes \n");
    }

    private void AdhdTest() {
        chooseOne.setVisibility(View.VISIBLE);
        chooseTwo.setVisibility(View.VISIBLE);
        chooseThree.setVisibility(View.GONE);
        chooseFive.setVisibility(View.GONE);
        chooseFour.setVisibility(View.GONE);
        chooseOne.setText("VERY LITTLE");
        chooseTwo.setText("TOO MUCH CALLS");
    }
    private void ManiaTest() {
        chooseOne.setVisibility(View.VISIBLE);
        chooseTwo.setVisibility(View.VISIBLE);
        chooseThree.setVisibility(View.VISIBLE);
        chooseFive.setVisibility(View.VISIBLE);
        chooseFour.setVisibility(View.VISIBLE);
        chooseFive.setText(". Very Often");
        chooseFour.setText(". Often");
        chooseThree.setText(". Sometimes");
        chooseOne.setText(". Never");
        chooseTwo.setText(". Rarely");
    }
    private void NarcissisticPersonalityDisorderTest() {
        chooseOne.setVisibility(View.VISIBLE);
        chooseTwo.setVisibility(View.VISIBLE);

        chooseThree.setVisibility(View.VISIBLE);
        chooseFive.setVisibility(View.VISIBLE);
        chooseFour.setVisibility(View.VISIBLE);
        chooseFive.setText(".  Very Often");
        chooseFour.setText(".  Often");
        chooseThree.setText(".  Sometimes");
        chooseOne.setText(".  Never");
        chooseTwo.setText(".  Rarely");
    }
    private void  lowselfesteemTest() {
        chooseOne.setVisibility(View.VISIBLE);
        chooseTwo.setVisibility(View.VISIBLE);

        chooseThree.setVisibility(View.GONE);
        chooseFive.setVisibility(View.GONE);
        chooseFour.setVisibility(View.GONE);
        chooseOne.setText(" Yes  ");
        chooseTwo.setText(" NO  ");
    }
    private void IllnessanxietydisorderTest() {
        chooseOne.setVisibility(View.VISIBLE);
        chooseTwo.setVisibility(View.VISIBLE);
        chooseThree.setVisibility(View.VISIBLE);
        chooseFive.setVisibility(View.VISIBLE);
        chooseFour.setVisibility(View.VISIBLE);
        chooseFive.setText(".  Very Often ");
        chooseFour.setText(".  Often ");
        chooseThree.setText(".  Sometimes ");
        chooseOne.setText(".  Never ");
        chooseTwo.setText(".  Rarely ");
    }
    private void  DissociativeIdentityDisorderTest() {

        chooseThree.setVisibility(View.GONE);
        chooseFive.setVisibility(View.GONE);
        chooseFour.setVisibility(View.GONE);
        chooseOne.setVisibility(View.VISIBLE);
        chooseTwo.setVisibility(View.VISIBLE);
        chooseOne.setText(" Yes . ");
        chooseTwo.setText(" No . ");
    }
    private void  BipolarDisorderTest() {
        chooseOne.setVisibility(View.VISIBLE);
        chooseTwo.setVisibility(View.VISIBLE);
        chooseThree.setVisibility(View.GONE);
        chooseFive.setVisibility(View.GONE);
        chooseFour.setVisibility(View.GONE);
        chooseOne.setText(" Yes  .");
        chooseTwo.setText(" NO  .");
    }
    private void BorderlinePersonalityDisordertest() {
        chooseOne.setVisibility(View.VISIBLE);
        chooseTwo.setVisibility(View.VISIBLE);
        chooseThree.setVisibility(View.GONE);
        chooseFive.setVisibility(View.GONE);
        chooseFour.setVisibility(View.GONE);
        chooseOne.setText("YES . ");
        chooseTwo.setText("NO . ");
    }

    private void psychosexualDysfunctionTest() {
        chooseOne.setVisibility(View.VISIBLE);
        chooseTwo.setVisibility(View.VISIBLE);
        chooseThree.setVisibility(View.VISIBLE);
        chooseFive.setVisibility(View.VISIBLE);
        chooseFour.setVisibility(View.VISIBLE);
        setChooseVisable();
        chooseOne.setText("NEVER");
        chooseTwo.setText("RARELY");
        chooseThree.setText("SOMETIMES");
        chooseFour.setText("OFTEN");
        chooseFive.setText("VERY OFTEN");
    }

    private void drugAddictiontest() {
        chooseOne.setVisibility(View.VISIBLE);
        chooseTwo.setVisibility(View.VISIBLE);
        chooseThree.setVisibility(View.GONE);
        chooseFive.setVisibility(View.GONE);
        chooseFour.setVisibility(View.GONE);
        setChooseVisable();
        chooseOne.setText("YES.");
        chooseTwo.setText("NO.");

    }

    private void alcoholAddictiontest() {
        chooseOne.setVisibility(View.VISIBLE);
        chooseTwo.setVisibility(View.VISIBLE);
        chooseThree.setVisibility(View.GONE);
        chooseFive.setVisibility(View.GONE);
        chooseFour.setVisibility(View.GONE);
        setChooseVisable();
        chooseOne.setText("YES");
        chooseTwo.setText("NO");
    }

    private void stressTest() {
        chooseOne.setVisibility(View.VISIBLE);
        chooseTwo.setVisibility(View.VISIBLE);
        chooseFour.setVisibility(View.VISIBLE);
        chooseThree.setVisibility(View.VISIBLE);
        chooseFive.setVisibility(View.VISIBLE);

        setChooseVisable();
        chooseOne.setText("Never");
        chooseTwo.setText("Rarely");
        chooseThree.setText("Sometimes");
        chooseFour.setText("often");
        chooseFive.setText("very often");

    }

    private void nixietyTest() {
        chooseOne.setVisibility(View.VISIBLE);
        chooseTwo.setVisibility(View.VISIBLE);
        chooseFour.setVisibility(View.VISIBLE);
        chooseThree.setVisibility(View.VISIBLE);
        chooseFive.setVisibility(View.VISIBLE);


        setChooseVisable();
        chooseOne.setText("Not  at all ");
        chooseTwo.setText("Several  day ");
        chooseThree.setText("More  than half of the days ");
        chooseFour.setText("Nearly  everyday ");

    }

    private void psychosisTest(){
        chooseOne.setVisibility(View.VISIBLE);
        chooseTwo.setVisibility(View.VISIBLE);
        setChooseVisable();
        chooseOne.setText("Agree");
        chooseTwo.setText("Disagree");
        chooseThree.setVisibility(View.GONE);
        chooseFour.setVisibility(View.GONE);
        chooseFive.setVisibility(View.GONE);
    }
///// farid
    private void socialAnxietyDisorderTest(){
        chooseThree.setVisibility(View.VISIBLE);
        chooseFour.setVisibility(View.VISIBLE);
        chooseFive.setVisibility(View.VISIBLE);
        chooseOne.setVisibility(View.VISIBLE);
        chooseTwo.setVisibility(View.VISIBLE);


        setChooseVisable();
        chooseOne.setText("\nNever\n");
        chooseTwo.setText("\nRarely\n");
        chooseThree.setText("\nSometimes\n");
        chooseFour.setText("\nOften\n");
        chooseFive.setText("\nVery often\n");
    }

    private void empathyDeficitDisorderTest(){
        chooseThree.setVisibility(View.VISIBLE);
        chooseFour.setVisibility(View.VISIBLE);
        chooseFive.setVisibility(View.VISIBLE);
        chooseOne.setVisibility(View.VISIBLE);
        chooseTwo.setVisibility(View.VISIBLE);

        setChooseVisable();
        chooseOne.setText("\n Never \n");
        chooseTwo.setText("\n Rarely \n");
        chooseThree.setText("\n Sometimes \n");
        chooseFour.setText("\n Often \n");
        chooseFive.setText("\n Very often \n");
    }

    private void imposterSyndromeTest(){
        chooseThree.setVisibility(View.VISIBLE);
        chooseFour.setVisibility(View.VISIBLE);
        chooseFive.setVisibility(View.VISIBLE);
        chooseOne.setVisibility(View.VISIBLE);
        chooseTwo.setVisibility(View.VISIBLE);
        setChooseVisable();
        chooseOne.setText("\nNever \n");
        chooseTwo.setText("\nRarely \n");
        chooseThree.setText("\nSometimes \n");
        chooseFour.setText("\nOften \n");
        chooseFive.setText("\nVery often \n");
    }

    private void obsessiveCompulsiveDisorderTest(){
        chooseThree.setVisibility(View.VISIBLE);
        chooseFour.setVisibility(View.VISIBLE);
        chooseFive.setVisibility(View.VISIBLE);
        chooseOne.setVisibility(View.VISIBLE);
        chooseTwo.setVisibility(View.VISIBLE);
        setChooseVisable();
        chooseOne.setText("\n  Never \n");
        chooseTwo.setText("\n  Rarely \n");
        chooseThree.setText("\n  Sometimes \n");
        chooseFour.setText("\n  Often \n");
        chooseFive.setText("\n  Very often \n");
    }

    private void posttraumaticStressDisorderTest(){
        chooseThree.setVisibility(View.VISIBLE);
        chooseFour.setVisibility(View.VISIBLE);
        chooseFive.setVisibility(View.VISIBLE);
        chooseOne.setVisibility(View.VISIBLE);
        chooseTwo.setVisibility(View.VISIBLE);
        setChooseVisable();
        chooseOne.setText("\n   Never \n");
        chooseTwo.setText("\n   Rarely \n");
        chooseThree.setText("\n   Sometimes \n");
        chooseFour.setText("\n   Often \n");
        chooseFive.setText("\n   Very often \n");
    }

    private void eatingDisorderTest(){
        chooseThree.setVisibility(View.VISIBLE);
        chooseFour.setVisibility(View.VISIBLE);
        chooseFive.setVisibility(View.VISIBLE);
        setChooseVisable();
        chooseOne.setText("\n   Never  \n");
        chooseTwo.setText("\n   Rarely  \n");
        chooseThree.setText("\n   Sometimes  \n");
        chooseFour.setText("\n   Often  \n");
        chooseFive.setText("\n   Very often  \n");
    }

    private String recommendedTest(String illness) {
        return "I recommend for us to begin with "+illness+" test ,what do you see?";
    }

    private void chooseForStartTest(String illness) {
        chooseOne.setText("Ok let's do "+illness+" test");
        chooseTwo.setText("I'd prefere to begain with another one");
        chooseOne.setVisibility(View.VISIBLE);
        chooseTwo.setVisibility(View.VISIBLE);
        chooseThree.setVisibility(View.GONE);
        chooseFour.setVisibility(View.GONE);
        chooseFive.setVisibility(View.GONE);
        setChooseVisable();

    }


    private void sendFirstBotMessage(String sender, String message1,String message2,String message3) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        Chat chat1=new Chat("Bot",sender,message1);
        Chat chat2=new Chat("Bot",sender,message2);
        Chat chat3=new Chat("Bot",sender,message3);

        reference.child("ChatBot").child(firebaseUser.getUid()).child("1").setValue(chat1);
        reference.child("ChatBot").child(firebaseUser.getUid()).child("2").setValue(chat2);
        reference.child("ChatBot").child(firebaseUser.getUid()).child("3").setValue(chat3);

        messageAdapter=new MessageAdapter(chats,getActivity(),"uri");
        recyclerView.setAdapter(messageAdapter);
        setChooseVisable();
        chooseOne.setText("Ok");
        chooseTwo.setVisibility(View.GONE);
        chooseThree.setVisibility(View.GONE);
        chooseFour.setVisibility(View.GONE);


    }
    public void fetchChatBotReply(String illness){
        IChatBotApi iChatBotApi= RetrofitBot.cteareAPI();
        Log.d("TAGDATA", "onResponse: no ");

        Call<Chat> chatCall = iChatBotApi.fetchIlnessName(illness);
        Log.d("TAGDATA", "onResponse: no1 ");

        chatCall.enqueue(new Callback<Chat>() {
             @Override
             public void onResponse(Call<Chat> call, Response<Chat> response) {
                 Log.d("TAGDATA", "onResponse: " + response.body().getMessage() + " " + response.code());
                 if(response.isSuccessful()&&response.body()!=null) {
                        if(response.body().getMessage().equals("I didn't get that, try again")){
                            sendBotMessage("Explain More");
                        }else {
                            chats.add(response.body());
                            //  Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            sendBotMessage("After analysis we see that you have "+response.body().getMessage());
                            sendBotMessage("I recommend for us to begin with "+response.body().getMessage()+
                                    " test ,what do you see?");
                            illness1=response.body().getMessage();
                          //  reportRefrence.child("tests").child("illness").setValue(response.body().getMessage());
                        }

                     }else {
                     Toast.makeText(getActivity(), "Something was wrong", Toast.LENGTH_SHORT).show();
                 }

             }

             @Override
             public void onFailure(Call<Chat> call, Throwable t) {
                 Log.e("TAG", "onFailure: "+t.getMessage() );
                    //getLocalIpAddress();

             }
         });
    }
    private void depressionTest() {
        chooseOne.setVisibility(View.VISIBLE);
        chooseTwo.setVisibility(View.VISIBLE);
        chooseFour.setVisibility(View.VISIBLE);
        chooseThree.setVisibility(View.VISIBLE);
        chooseFive.setVisibility(View.VISIBLE);
        setChooseVisable();
        chooseFive.setVisibility(View.GONE);
        chooseOne.setText("Not at all");
        chooseTwo.setText("Several days");
        chooseThree.setText("More than half of the days");
        chooseFour.setText("Nearly everyday");
        chooseFive.setText("hahaha");
    }
    private void ayhaga(){
        Toast.makeText(getActivity(), "hahha", Toast.LENGTH_SHORT).show();
    }
//Abdo Gouda
    //sasasasa
//jjjjjjjjjj
    //jskdj
    //21212
}
