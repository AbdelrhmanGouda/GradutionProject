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

                }else if(chooseOne.getText().toString().equals("Ok let's do Depression test")){

                    startTestWithFirstQuestion("Depression","How often have you been bothered by feeling down," +
                            "depressed, irritable, or hopeless over the last two weeks?");
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
                    adhdfunctionQuestions();

                }

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
                }
            }
        });
        getPrefrance();

        return view;
    }

    private void adhdfunctionQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber132", Context.MODE_PRIVATE);
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
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter132", Context.MODE_PRIVATE);
        int adhdCounterDegree=preferences.getInt("counter",0);
        SharedPreferences.Editor editor=preferences.edit();
        if(count==0){
        }else if(count==1){
            adhdCounterDegree++;
        }
        editor.putInt("counter",adhdCounterDegree);
        editor.apply();
        double totalDegreeOfTest=Math.round(((float)adhdCounterDegree));
        reportRefrence.child("Attention-Deficit Hyperactivity Disorder (ADHD)").child("totalDegree").setValue(String.valueOf(totalDegreeOfTest));

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
        reportRefrence.child("Psychosexual Dysfunction").child("totalDegree").setValue(String.valueOf(totalDegreeOfTest));

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

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getDrugAddictionQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber113", Context.MODE_PRIVATE);
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
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter113", Context.MODE_PRIVATE);
        int drugAddictionCounterDegree=preferences.getInt("counter",0);
        SharedPreferences.Editor editor=preferences.edit();
        if(count==0){
        }else if(count==1){
            drugAddictionCounterDegree++;
        }
        editor.putInt("counter",drugAddictionCounterDegree);
        editor.apply();
        double totalDegreeOfTest=Math.round(((float)drugAddictionCounterDegree));
        reportRefrence.child("drugAddiction").child("totalDegree").setValue(String.valueOf(totalDegreeOfTest));

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
        reportRefrence.child("Alcohol Addiction").child("totalDegree").setValue(String.valueOf(totalDegreeOfTest));

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

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getstressQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber73", Context.MODE_PRIVATE);
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
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter73", Context.MODE_PRIVATE);
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
        reportRefrence.child("Stress").child("totalDegree").setValue(String.valueOf(totalDegreeOfTest));

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

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void anixietyTestQuestionsCounter(int count) {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter82", Context.MODE_PRIVATE);
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
        reportRefrence.child("Anixiety").child("totalDegree").setValue(String.valueOf(totalDegreeOfTest));

        Toast.makeText(getActivity(), " dep "+depressionCounterDegree+" total "+totalDegreeOfTest, Toast.LENGTH_SHORT).show();


    }

    private void getAnixietyQuestions() {
        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber82", Context.MODE_PRIVATE);
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
        sendUserMessage(ilness+"test");
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

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void setChooseVisable() {
        relativeLayout.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);

    }

    private void setRelativeVisable() {
        relativeLayout.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);

    }
    private void depressionTestQuestionsCounter(int count){

        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceCounter92", Context.MODE_PRIVATE);
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
                reportRefrence.child("Depression").child("totalDegree").setValue(String.valueOf(totalDegreeOfTest));

       Toast.makeText(getActivity(), " dep "+depressionCounterDegree+" total "+totalDegreeOfTest, Toast.LENGTH_SHORT).show();


    }
    private void getDepressionQuestions() {

        final SharedPreferences preferences=getActivity().getSharedPreferences("PrefrenceneNumber91", Context.MODE_PRIVATE);
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
                            }else if(chat.getMessage().equals(recommendedTest("Attention-Deficit Hyperactivity Disorder (ADHD)"))){
                                chooseForStartTest("Attention-Deficit Hyperactivity Disorder (ADHD)");
                            }
                            else if(chat.getMessage().equals("Starting Depression test ....!")){
                                chooseFour.setVisibility(View.VISIBLE);
                                chooseThree.setVisibility(View.VISIBLE);
                                depressionTest();
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

    private void AdhdTest() {
        chooseThree.setVisibility(View.GONE);
        chooseFive.setVisibility(View.GONE);
        chooseFour.setVisibility(View.GONE);
        chooseOne.setText("VERY LITTLE");
        chooseTwo.setText("TOO MUCH CALLS");
    }

    private void psychosexualDysfunctionTest() {
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
        chooseFive.setVisibility(View.GONE);
        chooseFour.setVisibility(View.GONE);
        setChooseVisable();
        chooseOne.setText("YES.");
        chooseTwo.setText("NO.");

    }

    private void alcoholAddictiontest() {
        chooseFive.setVisibility(View.GONE);
        chooseFour.setVisibility(View.GONE);
        setChooseVisable();
        chooseOne.setText("YES");
        chooseTwo.setText("NO");
    }

    private void stressTest() {
        setChooseVisable();
        chooseFive.setVisibility(View.VISIBLE);
        chooseOne.setText("Never");
        chooseTwo.setText("Rarely");
        chooseThree.setText("Sometimes");
        chooseFour.setText("often");
        chooseFive.setText("very often");

    }

    private void nixietyTest() {
        setChooseVisable();
        chooseOne.setText("Not  at all ");
        chooseTwo.setText("Several  day ");
        chooseThree.setText("More  than half of the days ");
        chooseFour.setText("Nearly  everyday ");

    }

    private String recommendedTest(String illness) {
        return "I recommend for us to begin with "+illness+" test ,what do you see?";
    }

    private void chooseForStartTest(String illness) {
        chooseOne.setText("Ok let's do "+illness+" test");
        chooseTwo.setText("I'd prefere to begain with another one");
        chooseThree.setVisibility(View.GONE);
        chooseFour.setVisibility(View.GONE);
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

                         chats.add(response.body());
                   //  Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                     sendBotMessage("After analysis we see that you have "+response.body().getMessage());
                     sendBotMessage("I recommend for us to begin with "+response.body().getMessage()+
                             " test ,what do you see?");
                     reportRefrence.child(response.body().getMessage()).child("illness").setValue(response.body().getMessage());
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
        setChooseVisable();
        chooseOne.setText("Not at all");
        chooseTwo.setText("Several days");
        chooseThree.setText("More than half of the days");
        chooseFour.setText("Nearly everyday");
    }


}
