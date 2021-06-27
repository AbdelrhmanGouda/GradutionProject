package com.example.graduationproject.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationproject.R;


public class LearnMoreFragment extends Fragment {
    ImageView ilnessImage;
    TextView illnessDefination,illnessSymptoms;
    String name;
    int image;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_learn_more, container, false);
        illnessDefination=view.findViewById(R.id.illness_defination);
        illnessSymptoms=view.findViewById(R.id.illness_symptoms);
        ilnessImage=view.findViewById(R.id.ilness_image);
        if(getArguments()!=null){
            name=getArguments().getString("name");
            image=getArguments().getInt("image",0);
        }
        ilnessImage.setImageResource(image);
        setDifenationSymptoms();
        return view;
    }

    private void setDifenationSymptoms() {
        if(name.equals("Depression")){
            illnessDefination.setText("Depression is a common and serious medical illness that negatively affects how you feel, the way you think and how you act. Depression causes feelings of sadness and/or a loss of interest in activities you once enjoyed. It can lead to a variety of emotional and physical problems and can decrease your ability to function " +
                    "at work and at home. It can also disturb sleep and appetite; tiredness and poor concentration are common.");
            illnessSymptoms.setText("Feeling sad or having a depressed mood\n" +
                    "Loss of interest or pleasure in activities once enjoyed\n" +
                    "Changes in appetite — weight loss or gain unrelated to dieting\n" +
                    "Trouble sleeping or sleeping too much\n" +
                    "Loss of energy or increased fatigue\n" +
                    "Increase in purposeless physical activity (e.g., inability to sit still, pacing, handwringing) or slowed movements or speech.\n" +
                    "Feeling worthless or guilty\n" +
                    "Difficulty thinking, concentrating or making decisions\n" +
                    "Thoughts of death or suicide\n");
        }else if(name.equals("Anixiety")){
            illnessDefination.setText("Anxiety is an emotion characterized by feelings of tension, worried thoughts and physical changes like increased blood pressure.\n" +
                    "\n" +
                    "People with anxiety disorders usually have recurring intrusive thoughts or concerns. They may avoid certain situations out of worry. They may also have physical symptoms such as sweating, trembling, dizziness or a rapid heartbeat.\n" +
                    "Anxiety is a normal and often healthy emotion. However, when a person regularly feels disproportionate levels of anxiety, it might become a medical disorder.\n");
        illnessSymptoms.setText("restlessness, and a feeling of being “on-edge”\n" +
                "uncontrollable feelings of worry\n" +
                "increased irritability\n" +
                "concentration difficulties\n" +
                "increased heart rate\n" +
                "rapid breathing\n" +
                "difficulty falling asleep\n");
        }else if(name.equals("Stress")){
            illnessDefination.setText("Stress can be defined as the degree to which you feel" +
                    " overwhelmed or unable to cope as a result of pressures that are unmanageable.");
            illnessSymptoms.setText("Emotional changes\n" +
                    "       You may experience many different feelings, including anxiety, fear, anger, sadness, or frustration. These feelings can sometimes feed on each other and produce physical symptoms, making you feel even worse. For some people, stressful life events can contribute to symptoms of depression.\n" +
                    "Behavioral changes\n" +
                    "Behave differently. For example, you may become withdrawn, indecisive or inflexible. You may not be able to sleep properly. You may be irritable or tearful. There may be a change in your sexual habits.  Stress can make you feel angrier or more aggressive than normal, it also affect the way we interact with our close family and friends.\n" +
                    "Bodily changes\n" +
                    "Headaches, nausea and indigestion. You may breathe more quickly, perspire more, have palpitations or suffer from various aches and pains.\n" +
                    "you may notice your sleep and memory are affected,\n" +
                    "\n" +
                    "disbelief, shock, and numbness\n" +
                    "feeling sad, frustrated, and helpless\n" +
                    "difficulty concentrating and making decisions\n" +
                    "headaches, back pains, and stomach problems\n" +
                    "smoking or use of alcohol or drugs");
        }else if(name.equals("Alcohol Addiction")){
            illnessDefination.setText("Addiction is a treatable, chronic medical disease involving complex interactions among brain circuits, genetics, the environment, and an individual’s life experiences. People with addiction use substances" +
                    " or engage in behaviors that become compulsive and often continue despite harmful consequences");
            illnessSymptoms.setText("Addiction is not a habit,\n" +
                    "The important distinction is that addiction is a medical disorder, but habits are kind of a milder manifestation\n" +
                    "\n" +
                    "Psychological symptoms of addictions:\n" +
                    "Mood swings\n" +
                    "Tiredness\n" +
                    "Paranoia\n" +
                    "Defensiveness\n" +
                    "Agitation\n" +
                    "Inability to focus or concentrate\n" +
                    "Poor judgement\n" +
                    "Memory problems\n" +
                    "Diminished self-esteem and self-worth\n" +
                    "Feelings of hopelessness\n" +
                    "Exacerbation of any existing mental health conditions such as depression, anxiety or stress\n" +
                    "\n" +
                    "Behavioural and social signs of addictions:\n" +
                    "Secretive or dishonest behavior\n" +
                    "Poor performance and/or attendance at work or school\n" +
                    "Withdrawing from responsibility and socialising\n" +
                    "Losing interest in activities, hobbies or events that were once important to you\n" +
                    "Continuing to use the substance, or engage in certain behaviors, despite the negative consequences that these cause\n" +
                    "Trying but failing to reduce or stop misusing a substance, or engaging in certain behaviors\n" +
                    "\n" +
                    "Physical symptoms of addictions:\n" +
                    "Lack of concern over physical appearance/personal hygiene\n" +
                    "Disrupted sleep patterns, including insomnia \n");
        }else if(name.equals("Drug Addiction")){
            illnessDefination.setText("Addiction is a treatable, chronic medical disease involving complex interactions among brain circuits, genetics, the environment, and an individual’s life experiences. People with addiction use substances or" +
                    " engage in behaviors that become compulsive and often continue despite harmful consequences.");
            illnessSymptoms.setText("Addiction is not a habit,\n" +
                    "The important distinction is that addiction is a medical disorder, but habits are kind of a milder manifestation\n" +
                    "\n" +
                    "Psychological symptoms of addictions:\n" +
                    "Mood swings\n" +
                    "Tiredness\n" +
                    "Paranoia\n" +
                    "Defensiveness\n" +
                    "Agitation\n" +
                    "Inability to focus or concentrate\n" +
                    "Poor judgement\n" +
                    "Memory problems\n" +
                    "Diminished self-esteem and self-worth\n" +
                    "Feelings of hopelessness\n" +
                    "Exacerbation of any existing mental health conditions such as depression, anxiety or stress\n" +
                    "\n" +
                    "Behavioural and social signs of addictions:\n" +
                    "Secretive or dishonest behavior\n" +
                    "Poor performance and/or attendance at work or school\n" +
                    "Withdrawing from responsibility and socialising\n" +
                    "Losing interest in activities, hobbies or events that were once important to you\n" +
                    "Continuing to use the substance, or engage in certain behaviors, despite the negative consequences that these cause\n" +
                    "Trying but failing to reduce or stop misusing a substance, or engaging in certain behaviors\n" +
                    "\n" +
                    "Physical symptoms of addictions:\n" +
                    "Lack of concern over physical appearance/personal hygiene\n" +
                    "Disrupted sleep patterns, including insomnia\n");
        }else if(name.equals("Psychosexual Dysfunction")){
            illnessDefination.setText("Psychosexual Dysfunction, the inability of a person to experience sexual arousal or to achieve sexual" +
                    " satisfaction under appropriate circumstances, as a result of psychological problems.");
            illnessSymptoms.setText("In men:\n" +
                    "Inability to achieve or maintain an erection suitable for intercourse  \n" +
                    "Retarded ejaculation.\n" +
                    "Inability to control the timing of ejaculation (early, or premature, ejaculation).\n" +
                    "\n" +
                    "In women:\n" +
                    "Inability to achieve orgasm.\n" +
                    "Inadequate vaginal lubrication before and during intercourse.\n" +
                    "Inability to relax the vaginal muscles enough to allow intercourse.\n" +
                    "\n" +
                    "In men and women:\n" +
                    "Lack of interest in or desire for sex.\n" +
                    "Inability to become aroused.\n" +
                    "Pain with intercourse.\n");
        }else if(name.equals("Attention-Deficit Hyperactivity Disorder (ADHD)")){
            illnessDefination.setText("Attention-deficit hyperactivity disorder (ADHD) is a neurobehavioral condition that interferes with a person’s ability to pay attention and exercise age-appropriate inhibition. A person with ADHD is so inattentive or impulsively hyperactive-or both-that daily functioning at home, school and work is compromised." +
                    " ADHD usually becomes apparent in children during preschool and early school years.");
            illnessSymptoms.setText("The same child may behave well at certain times but not at others, so parents may wonder why the child can’t “get it together” more consistently.  This likely has something to do with the fact that children with ADHD are often: More sensitive to environmental factors than other children; tend to do better in one-on-one situations; and act differently in the presence of an authority figure and when rewards or high-interest motivators are present.  Despite this day-to-day or moment-to-moment variability in performance, children with condition show ADHD-related" +
                    " behaviors in more than one setting. Not just at home or just at school, but in both.");
        }else if(name.equals("Autism")){
            illnessDefination.setText("Autism, or autism spectrum disorder (ASD), refers to a broad range of conditions characterized by challenges with social skills, repetitive behaviors, speech and nonverbal communication. According to the Centers for Disease Control, autism" +
                    " affects an estimated 1 in 54 children in the United States today.");
            illnessSymptoms.setText("Social Skills\n" +
                    "A child with ASD has a hard time interacting with others. Problems with social skills are some of the most common signs. They might want to have close relationships but not know how.\n" +
                    "If your child is on the spectrum, they might show some social symptoms by the time they're 8 to 10 months old. These may include any of the following:\n" +
                    "They don't respond to their name by their first birthday.\n" +
                    "Playing, sharing, or talking with other people don’t interest them.\n" +
                    "They prefer to be alone.\n" +
                    "They avoid or reject physical contact.\n" +
                    "They avoid eye contact.\n" +
                    "When they’re upset, they don’t like to be comforted.\n" +
                    "They don’t understand emotions -- their own or others’.\n" +
                    "They may not stretch out their arms to be picked up or guided with walking.\n" +
                    "Communication\n" +
                    "About 40% of kids with autism spectrum disorders don’t talk at all, and between 25% and 30% develop some language skills during infancy but then lose them later. Some children with ASD start talking later in life.\n" +
                    "Most have some problems with communication, including these:\n" +
                    "Delayed speech and language skills\n" +
                    "Flat, robotic speaking voice, or singsong voice\n" +
                    "Echolalia (repeating the same phrase over and over)\n" +
                    "Problems with pronouns (saying “you” instead of “I,” for example)\n" +
                    "Not using or rarely using common gestures (pointing or waving), and not responding to them\n" +
                    "Inability to stay on topic when talking or answering questions\n" +
                    "Not recognizing sarcasm or joking\n" +
                    "\n" +
                    "Patterns of Behavior\n" +
                    "Children with ASD also act in ways that seem unusual or have interests that aren’t typical. Examples of this can include:\n" +
                    " \n" +
                    "Repetitive behaviors like hand-flapping, rocking, jumping, or twirling\n" +
                    "Constant moving (pacing) and “hyper” behavior\n" +
                    "Fixations on certain activities or objects\n" +
                    "Specific routines or rituals (and getting upset when a routine is changed, even slightly)\n" +
                    "Extreme sensitivity to touch, light, and sound\n" +
                    "Not taking part in “make-believe” play or imitating others’ behaviors\n" +
                    "Fussy eating habits\n" +
                    "Lack of coordination, clumsiness\n" +
                    "Impulsiveness (acting without thinking)\n" +
                    "Aggressive behavior, both with self and others\n" +
                    "Short attention span\n" +
                    "Spotting Signs and Symptoms\n" +
                    "The earlier treatment for autism spectrum disorder begins, the more like it is to be effective. That’s why knowing how to identify the signs and symptoms is so important.\n" +
                    "Make an appointment with your child’s pediatrician if they don’t meet these specific developmental milestones, or if they meet but lose them later on:\n" +
                    "Smiles by 6 months\n" +
                    "Imitates facial expressions or sounds by 9 months\n" +
                    "Coos or babbles by 12 months\n" +
                    "Gestures (points or waves) by 14 months\n" +
                    "Speaks with single words by 16 months and uses phrases of two words or more by 24 months\n" +
                    "Plays pretend or “make-believe” by 18 months\n");
        }else if(name.equals("Pseudobulbar Affect (PBA)")){
            illnessDefination.setText("(PBA) is a condition that's characterized by episodes of sudden uncontrollable and inappropriate laughing or crying. Pseudobulbar affect typically occurs in people with certain neurological conditions or injuries," +
                    " which might affect the way the brain controls emotion.");
            illnessSymptoms.setText("The primary sign of pseudobulbar affect (PBA) is frequent, involuntary and uncontrollable outbursts of crying or laughing that are exaggerated or not connected to your emotional state. Laughter often turns to tears." +
                    " Your mood will appear normal between episodes, which can occur at any time.");
        }else if(name.equals("Bullying")){
            illnessDefination.setText("Bullying is an ongoing and deliberate misuse of power in relationships through repeated verbal, physical and/or social behaviour that intends to cause physical, social and/or psychological harm. It can involve an individual or a group misusing their power, or perceived power, over one or more persons" +
                    " who feel unable to stop it from happening.");
            illnessSymptoms.setText("Some signs that may point to a bullying problem are: \n" +
                    "Unexplainable injuries\n" +
                    "Lost or destroyed clothing, books, electronics, or jewelry\n" +
                    "Frequent headaches or stomach aches, feeling sick or faking illness\n" +
                    "Changes in eating habits, like suddenly skipping meals or binge eating. Kids may come home from school hungry because they did not eat lunch.\n" +
                    "Difficulty sleeping or frequent nightmares\n" +
                    "Declining grades, loss of interest in schoolwork, or not wanting to go to school\n" +
                    "Sudden loss of friends or avoidance of social situations\n" +
                    "Feelings of helplessness or decreased self esteem\n" +
                    "Self-destructive behaviors such as running away from home, harming themselves, or talking about suicide\n");
        }else if(name.equals("Schizophrenia")){
            illnessDefination.setText("Schizophrenia (or: Schizophrenia) is an acute disorder of the brain that distorts the person's way of: thinking, acting, expressing his feelings, looking at reality and seeing the facts and the interrelationships between him and those around him. People with schizophrenia (which is the hardest and most restrictive of all known mental illnesses) generally suffer from functional problems in the community, in the workplace," +
                    " at school and in their relationships with their wives / husbands.");
            illnessSymptoms.setText("A change in grades\n" +
                    "Social withdrawal\n" +
                    "Trouble concentrating\n" +
                    "Temper flares\n" +
                    "Difficulty sleeping\n");
        }else if(name.equals("Attention-Deficit Hyperactivity Disorder (ADHD)")){
            illnessDefination.setText("Attention-deficit hyperactivity disorder (ADHD) is a neurobehavioral condition that interferes with a person’s ability to pay attention and exercise age-appropriate inhibition. A person with ADHD is so inattentive or impulsively hyperactive-or both-that daily functioning at home, school and work is compromised." +
                    " ADHD usually becomes apparent in children during preschool and early school years.");
            illnessSymptoms.setText("The same child may behave well at certain times but not at others, so parents may wonder why the child can’t “get it together” more consistently.  This likely has something to do with the fact that children with ADHD are often: More sensitive to environmental factors than other children; tend to do better in one-on-one situations; and act differently in the presence of an authority figure and when rewards or high-interest motivators are present.  Despite this day-to-day or moment-to-moment variability in performance, children with condition show ADHD-related" +
                    " behaviors in more than one setting. Not just at home or just at school, but in both.");
        }else if(name.equals("Dissociative Identity Disorder (DID)")){
            illnessDefination.setText("Dissociative identity disorder (DID) was formerly called multiple personality disorder. People with DID develop one or more alternate personalities that function with " +
                    "or without the awareness of the person’s usual personality.");
            illnessSymptoms.setText("How common is (DID)?\n" +
                    "Instances of true DID are very rare. When they occur, they can occur at any age. Females are more likely than males to get DID.\n" +
                    "\n" +
                    "What causes dissociative identity disorder (DID)?\n" +
                    "A history of trauma is a key feature of dissociative identity disorder. \n" +
                    "\n" +
                    "About 90% of the cases of DID involve some history of abuse.\n" +
                    "\n" +
                    "The trauma often involves severe emotional, physical, and/or sexual abuse. It might also be linked to accidents, natural disasters, and war. An important early loss, such as the loss of a parent or prolonged periods of isolation due to illness, may be a factor in developing DID.\n" +
                    "Rarely\n" +
                    "What are the symptoms of DID?\n" +
                    "A person with DID has two or more different and distinct personalities, the person’s usual (“core”) personality and what are known as alternate personalities, or “alters.” The person may experience amnesia when an alter takes control over the person’s behavior.\n" +
                    "\n" +
                    "Each alter has distinct individual traits, a personal history, and a way of thinking about and relating to his or her surroundings. An alter may be of a different gender, have a different name, or a distinct set of manners and preferences. (An alter may even have different allergies than the core person.)\n" +
                    "\n" +
                    "DID shares many psychological symptoms as those found in other mental disorders, including:\n" +
                    "\n" +
                    "Changing levels of functioning, from highly effective to disturbed/disabled\n" +
                    "Severe headaches or pain in other parts of the body\n" +
                    "Depersonalization (feeling disconnected from one’s own thoughts, feelings, and body)\n" +
                    "Derealization (feeling that the surrounding environment is foreign, odd, or unreal)\n" +
                    "Depression and/or mood swings\n" +
                    "Anxiety\n" +
                    "Eating and sleeping disturbances\n" +
                    "Problems with functioning sexuality\n" +
                    "Substance abuse\n" +
                    "Amnesia (memory loss or feeling a time distortion)\n" +
                    "Hallucinations (false perceptions or sensory experiences, such as hearing voices)\n" +
                    "Self-injurious behaviors such as “cutting”\n" +
                    "Suicide risk — 70% of people with DID have attempted suicide\n");
        }else if(name.equals("Borderline personality disorder (BPD)")){
            illnessDefination.setText("Borderline personality disorder (BPD) is a serious psychological condition that's characterized by unstable moods and emotions, relationships, and behavior. It's one of several personality disorders recognized by the American Psychiatric Association (APA).\n" +
                    "\n" +
                    "Personality disorders are psychological conditions that begin in adolescence or early adulthood, continue over many years, and, when left untreated, can cause a great deal of distress. Thankfully," +
                    " the right treatments targeted for BPD can help significantly.\n");
            illnessSymptoms.setText("BPD can often interfere with your ability to enjoy life or achieve fulfillment in relationships, work, or school. It's associated with specific and significant problems in interpersonal relationships, self-image, emotions, behaviors, and thinking, including:\n" +
                    "\n" +
                    "Behaviors: BPD is associated with a tendency to engage in risky and impulsive behaviors, such as going on shopping sprees, drinking excessive amounts of alcohol or abusing drugs, engaging in promiscuous or risky sex, or binge eating. Also, people with BPD are more prone to engage in self-harming behaviors, such as cutting or burning and attempting suicide.\n" +
                    "\n" +
                    "Emotions: Emotional instability is a key feature of BPD. \n" +
                    "\n" +
                    "Individuals feel like they're on an emotional roller coaster with quick mood shifts (i.e., going from feeling OK to feeling extremely down or blue within a few minutes). \n" +
                    "Mood changes can last from minutes to days and are often intense. Anger, anxiety\n" +
                    "\n" +
                    "Relationships: People with BPD tend to have intense relationships with loved ones characterized by frequent conflicts, arguments, and break-ups. BPD is associated with an intense fear of being abandoned by loved ones and attempts to avoid real or imagined abandonment. This usually leads to difficulty trusting others, putting a strain on relationships.\n" +
                    "\n" +
                    "Self-image: Individuals with BPD have difficulties related to the stability of their sense of self. They report many ups and downs in how they feel about themselves. One moment they may feel good about themselves, but the next they may feel they are bad or even evil.\n" +
                    "\n" +
                    "Stress-related changes in thinking: Under conditions of stress, people with BPD may experience changes in thinking, including paranoid thoughts (for example, thoughts that others may be trying to cause them harm), or dissociation (feeling spaced out, numb, or like they're not really in their body).\n.");
        }else if(name.equals("low self esteem")){
            illnessDefination.setText("In psychology, the term self-esteem is used to describe a person's overall sense of self-worth or personal value. In other words, how much you appreciate and like yourself. It involves a variety of beliefs about yourself," +
                    " such as the appraisal of your own appearance, beliefs, emotions, and behaviors.");
            illnessSymptoms.setText("feel worthless\n" +
                    "seek to please others\n" +
                    "obsession with perfection\n" +
                    "not accept criticism from others\n");
        }else if(name.equals("Bipolar Disorder")){
            illnessDefination.setText("Bipolar disorder (formerly called manic-depressive illness or manic depression) is a mental disorder that causes unusual shifts in mood, energy, activity levels, concentration, and the ability to carry out day-to-day tasks.\n" +
                    "\n" +
                    "There are three types of bipolar disorder. All three types involve clear changes in mood, energy, and activity levels. These moods range from periods of extremely “up,” elated, irritable, or energized behavior (known as manic episodes) to very “down,” sad, indifferent, or hopeless periods (known as depressive episodes). " +
                    "Less severe manic periods are known as hypomanic episodes.\n");
            illnessSymptoms.setText("These moods range from periods of extremely “up,” elated, irritable, or energized behavior (known as manic episodes) to very “down,” sad, indifferent, or hopeless periods (known as depressive episodes). Less severe manic periods are known as hypomanic episodes.\n");
        }else if(name.equals("Mania")){
            illnessDefination.setText("Mania is a temporary but extreme emotional high. It often occurs in individuals with bipolar disorder, but may also be prevalent in those suffering from depression. In bipolar disorder, manic episodes are offset by extreme emotional lows. As such, mania can be clearly recognized by people interacting with or observing the affected individual, but the individual may not recognize it in themselves.\n" +
                    "\n" +
                    "Mania is characterized by sudden and intense changes in mood, thought patterns, energy levels, and even self-esteem. These changes can cause an individual to engage in risky behaviors during a manic episode. Risky behaviors can put the individual’s health at risk and are a major concern related to mania.\n" +
                    "\n" +
                    "Receiving treatment as soon as possible is essential in the management of mania. Manic episodes may " +
                    "worsen and lead to increasingly dangerous behaviors when left untreated.\n");
            illnessSymptoms.setText("Mood swings\n" +
                    "Mood swings are one of the most evident signs of mania. Someone experiencing mania may have intense excitability, inflated self-confidence, extreme friendliness or sociability, feelings of superiority, and unexpected changes to anger or irritability.\n" +
                    "Erratic judgment\n" +
                    "People experiencing a manic episode may have impaired judgments and make poor or risky decisions. This may involve making commitments that they can’t keep, such as agreeing to new projects without the time or resources to complete them. Or, the individual may participate in risky activities like gambling, spending large amounts of money, or engaging sexual activity impulsively.\n" +
                    "\n" +
                    "Poor judgments during a manic episode can have long-lasting implications. For example, if an individual spend their entire life savings during a manic episode, they could experience financial hardship for years to come.\n" +
                    "Shifts in thought processes\n" +
                    "Someone with mania may appear to think in a completely different way during an episode. The individual may have huge boosts in creativity and claim to have an epiphany (or several). Mania may also cause racing thoughts, a sudden fixation on religion, disorientation, or “flight of ideas”, which is a symptom commonly associated with bipolar disorder.\n" +
                    "Changes in energy level\n" +
                    "Mania usually causes sudden and extremely high energy levels. High energy can cause the individual to move constantly for no apparent reason and have a sudden interest in accomplishing goals (completing a project, for example). High energy levels can also lead to restlessness, talkativeness, an inability to sit still, and difficulty sleeping.\n" +
                    "\n" +
                    "Psychosis\n" +
                    "Severe cases of mania can cause symptoms of psychosis. This most commonly occurs in bipolar patients. Symptoms of psychosis include hallucinations, paranoia, and delusions.\n");
        }else if(name.equals("Psychosis")){
            illnessDefination.setText("Psychosis is a condition that affects the way your brain processes information. It causes you to lose touch with reality. You might see, hear, or believe things that aren’t real. Psychosis is a symptom, not an illness. A mental or physical illness, substance abuse, or extreme stress or trauma can cause it.\n" +
                    "\n" +
                    "Experiences in psychosis may be seen or heard. Psychosis may also encompass beliefs that aren’t rooted in reality. Experiencing psychosis may be bewildering and induce feelings of extreme anxiety. Someone experiencing psychosis can have episodes of extreme emotion and unintentionally hurt themselves or others, which is a significant risk.\n" +
                    "Psychosis is generally seen as a symptom of other conditions, rather than a condition in and of itself. Conditions that may induce psychosis include:\n" +
                    "Post-Traumatic Stress Disorder (PTSD)\n" +
                    "Schizophrenia\n" +
                    "Bipolar Disorder\n" +
                    "Depression.\n");
            illnessSymptoms.setText("consist of two main symptoms: hallucinations and delusions:\n" +
                    "Hallucination\n" +
                    "A hallucination is a visual or auditory sensation that isn’t real and occurs only in the affected individual’s mind. Hallucinations can also be bizarre sensations or feelings that don’t make sense in the context of reality. During a hallucination, things or people may be distorted or appear when they’re not actually there. An auditory hallucination during a psychotic episode often takes the form of voices that are only heard inside the individual’s mind.\n" +
                    "Delusions\n" +
                    "Delusions are thoughts or beliefs that may not be rooted in truth, go against the individual’s cultural norms, or are perceived as senseless to others. Delusions that occur during a psychotic episode may be confusing to both the affected individual and the people around them examples of delusions include:\n" +
                    "\n" +
                    "The belief that commonplace or inconsequential things, events, or objects have great significance, meaning, or power.\n" +
                    "The belief that extrinsic powers have control over your mind, body, and behaviors.\n" +
                    "The belief that you have other-worldly powers or the powers of God.\n");
        }else if(name.equals("Social Anxiety Disorder ")){
            illnessDefination.setText("Social Anxiety Disorder  is a type of anxiety disorder characterized by excessive fear, anxiety, discomfort, and self-consciousness in social settings. Individuals with social anxiety disorder (social phobia) have a heightened fear of interaction with others in a variety of social interactions" +
                    " and worry they will be scrutinized by others.");
            illnessSymptoms.setText("Physical Symptoms\n" +
                    "Rapid heat-beat\n" +
                    "Dizziness\n" +
                    "Muscle tension or twitches\n" +
                    "Stomach trouble\n" +
                    "Blushing\n" +
                    "Trembling\n" +
                    "Excessive sweating\n" +
                    "Dry throat and mouth\n" +
                    "Emotional Symptoms\n" +
                    "High levels of anxiety and fear\n" +
                    "Nervousness\n" +
                    "Panic attacks\n" +
                    "Negative emotional cycles\n" +
                    "Dysmorphia concerning part of their body (most commonly the face)\n" +
                    "\n");
        }else if(name.equals("Narcissistic Personality Disorder")){
            illnessDefination.setText("Narcissistic Personality Disorder is a mental condition in which people have an inflated sense of their own importance, a deep need for excessive attention and admiration, troubled relationships, and a lack of empathy for others. But behind this mask of extreme confidence lies a" +
                    " fragile self-esteem that's vulnerable to the slightest criticism.");
            illnessSymptoms.setText("Have an exaggerated sense of self-importance\n" +
                    "Expect to be recognized as superior even without achievements that warrant it\n" +
                    "Exaggerate achievements and talents\n" +
                    "Be preoccupied with fantasies about success, power, brilliance, beauty or the perfect mate\n" +
                    "Believe they are superior and can only associate with equally special people\n" +
                    "Expect special favors and unquestioning compliance with their expectations\n" +
                    "Take advantage of others to get what they want\n" +
                    "Have an inability or unwillingness to recognize the needs and feelings of others\n" +
                    "Be envious of others and believe others envy them\n" +
                    "Insist on having the best of everything \n");
        }else if(name.equals("Empathy Deficit Disorder")){
            illnessDefination.setText("Empathy is an important human ability that allows us to relate to one another. Specifically, it’s the act of recognizing and sharing the feelings of another person." +
                    " In psychology, two types of empathy are recognized.");
            illnessSymptoms.setText("Coldness or indifference towards people who are struggling.\n" +
                    "Quickness to criticize others before considering their circumstances.\n" +
                    "Difficulty showing appreciation for favors.\n" +
                    "Difficulty feeling happy or congratulatory towards others.\n" +
                    "Difficulty in relationships with family and friends.\n" +
                    "Difficulty making new friends.\n" +
                    "Difficulty building meaningful emotional connections with others.\n" +
                    "Only discussing themselves and poor listening skills in social situations.\n" +
                    "Belief that when others are hurt as a result of their actions, it’s due to the other person’s oversensitivity.\n");
        }else if(name.equals("Illness anxiety disorder")) {
            illnessDefination.setText("Illness anxiety disorder is worrying excessively that you are or may become seriously ill. You may have no physical symptoms. Or you may believe that normal body sensations or minor symptoms are signs of severe illness, even though a thorough medical exam" +
                    " doesn't reveal a serious medical condition.");
            illnessSymptoms.setText("Being preoccupied with having or getting a serious disease or health condition\n" +
                    "Worrying that minor symptoms or body sensations mean you have a serious illness\n" +
                    "Being easily alarmed about your health status\n" +
                    "Worrying excessively about a specific medical condition or your risk of developing a medical condition because it runs in your family\n" +
                    "Having so much distress about possible illnesses that it's hard for you to function\n" +
                    "Repeatedly checking your body for signs of illness or disease\n" +
                    "Frequently making medical appointments for reassurance — or avoiding medical care for fear of being diagnosed with a serious illness\n" +
                    "Avoiding people, places or activities for fear of health risks\n" +
                    "Constantly talking about your health and possible illnesses\n" +
                    "Frequently searching the internet for causes of symptoms or possible illnesses\n" +
                    "\n");
        }else if(name.equals("Imposter syndrome")) {
            illnessDefination.setText("Imposter syndrome can be defined as a collection of feelings of inadequacy that persist despite evident success. ‘Imposters’ suffer from chronic self-doubt and a sense of intellectual fraudulence that override any feelings of success or external proof of their competence\n" +
                    "\n" +
                    "Impostor syndrome (IS) refers to an internal experience of believing that you are not as competent as others perceive you to be. While this definition is usually narrowly applied to intelligence and achievement, it has links to perfectionism and the social context.\n" +
                    "\n" +
                    "To put it simply, imposter syndrome is the experience of feeling like a phony—you feel as though at any moment you are going to be found out as a fraud—like you don't belong where you are, and you only got there through dumb luck. It can affect anyone no matter their social status," +
                    " work background, skill level, or degree of expertise.\n");
            illnessSymptoms.setText("Some signs that may point to a bullying problem are: \n" +
                    "Extreme lack of self confidence\n" +
                    "Feelings of inadequacy\n" +
                    "Constant comparison to other people\n" +
                    "Anxiety\n" +
                    "Self doubt\n" +
                    "Distrust in one’s own intuition and capabilities\n" +
                    "Negative self-talk\n" +
                    "Dwelling on the past\n" +
                    "Irrational fears of the future\n");
        }else if(name.equals("Obsessive-Compulsive Disorder (OCD)")) {
            illnessDefination.setText("-experts define obsessions as unwanted, repeated and persistent thoughts, memories and urges \n" +
                    "\n" +
                    "-for people with OCD, obsessive thoughts can generate anxiety and distress since it’s challenging to go about their daily activities while dealing with an annoying, anxiety-generating thought, people with OCD often compensate with compulsive behaviors\n" +
                    "\n" +
                    "-people with this condition may experience obsessive thoughts, compulsive behaviors, or both\n");
            illnessSymptoms.setText("Common obsessive thoughts in OCD include:\n" +
                    "- Fear of being contaminated by germs or dirt or contaminating others\n" +
                    "- Fear of losing control and harming yourself or others\n" +
                    "- Excessive focus on religious or moral ideas.\n" +
                    "- Fear of losing or not having things you might need.\n" +
                    "\n" +
                    "Common compulsive behaviors in OCD include:\n" +
                    "- Excessive double-checking of things, such as locks, appliances, and switches\n" +
                    "- Repeatedly checking in on loved ones to make sure they’re safe\n" +
                    "- Counting, tapping, repeating certain words, or doing other senseless things to reduce anxiety\n" +
                    "- Spending a lot of time washing or cleaning\n");
        }else if(name.equals("Eating Disorder")) {
            illnessDefination.setText("Eating disorders are characterized by a persistent disturbance in eating behaviors which impairs the consumption and absorption of food that in turn has a profoundly negative impact on your physical health and psychosocial functioning in other words that’s make people eat excessive amounts of food, starve themselves or adopt a distorted and" +
                    " unhealthy attitude towards food and bodyweight");
            illnessSymptoms.setText("Although each type of eating disorder has a specific set of symptoms, there are some common sings which may point toward problematic eating behaviors\n" +
                    "\n" +
                    "-chronic dieting, despite having a healthy body-mass index\n" +
                    "-constant weight fluctuations\n" +
                    "-counting calories and eliminating fat from all food consumed\n" +
                    "-depression or lethargy\n" +
                    "-constant variations between excessive eating and fasting\n");
        }else if(name.equals("Posttraumatic stress disorder (PTSD)")) {
            illnessDefination.setText("Posttraumatic stress disorder (PTSD) is a psychiatric disorder that may occur in people who have experienced or witnessed a traumatic event such as a natural disaster, a serious accident, a terrorist act, war/combat, or rape or who have been threatened with death, sexual violence or serious injury.\n" +
                    "\n" +
                    "PTSD has been known by many names in the past, such as “shell shock” during the years of World War I and “combat fatigue” after World War II, but PTSD does not just happen to combat veterans. PTSD can occur in all people, of any ethnicity, nationality or culture, and at any age. PTSD affects approximately 3.5 percent of U.S. adults every year, and an estimated one in 11 people will be diagnosed with PTSD in their lifetime. Women are twice as likely as men to have PTSD. Three ethnic groups – U.S. Latinos, African Americans, and American Indians – are disproportionately" +
                    " affected and have higher rates of PTSD than non-Latino whites.\n");
            illnessSymptoms.setText("Intrusion: Intrusive thoughts such as repeated, involuntary memories; distressing dreams; or flashbacks of the traumatic event. Flashbacks may be so vivid that people feel they are re-living the traumatic experience or seeing it before their eyes.\n" +
                    "Avoidance: Avoiding reminders of the traumatic event may include avoiding people, places, activities, objects and situations that may trigger distressing memories. People may try to avoid remembering or thinking about the traumatic event. They may resist talking about what happened or how they feel about it.\n" +
                    "Alterations in cognition and mood: Inability to remember important aspects of the traumatic event, negative thoughts, and feelings leading to ongoing and distorted beliefs about oneself or others \n" +
                    "Alterations in arousal and reactivity: Arousal and reactive symptoms may include being irritable and having angry outbursts; behaving recklessly or in a self-destructive way; being overly watchful of one's surroundings in a suspecting way; being easily startled; or having problems concentrating or sleeping.\n");
        }

    }
}