package com.example.graduationproject.Sign;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.graduationproject.Activities.MainActivity;
import com.example.graduationproject.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.internal.ImageRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONObject;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login_Fragment extends Fragment implements OnClickListener {
    private static View view;
    private static EditText emailid, password;
    private static Button loginButton , faceBookBtn , googleBtn;
    private static TextView forgotPassword, signUp;
    private static CheckBox show_hide_password;
    private static LinearLayout loginLayout;
    private static Animation shakeAnimation;
    private static FragmentManager fragmentManager;
    private static LoginButton loginButtonF ;
    FirebaseStorage storage;
    StorageReference storageReference;
    User userData = new User();

    private static final String TAG = "GoogleActivity";



    private CallbackManager mCallbackManager;

    private static final String FTAG = "FacebookLogin";
    private static final int RC_SIGN_IN = 12345;


    public GoogleSignInClient mGoogleSignInClient;
    public FirebaseAuth mAuth;
    public FirebaseUser user;




    public Login_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_layout, container, false);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);


        mCallbackManager = CallbackManager.Factory.create();

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        initViews();
        setListeners();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        // Check if user or account is signed in (non-null) and update UI accordingly.
        updateUI(user);


    }


    // Initiate Views
    private void initViews() {
        fragmentManager = getActivity().getSupportFragmentManager();

        loginButtonF = (LoginButton) view.findViewById(R.id.login_button);
        loginButtonF.setFragment(Login_Fragment.this);
        emailid = (EditText) view.findViewById(R.id.login_emailid);
        password = (EditText) view.findViewById(R.id.login_password);
        loginButton = (Button) view.findViewById(R.id.loginBtn);
        forgotPassword = (TextView) view.findViewById(R.id.forgot_password);
        signUp = (TextView) view.findViewById(R.id.createAccount);
        show_hide_password = (CheckBox) view
                .findViewById(R.id.show_hide_password);
        loginLayout = (LinearLayout) view.findViewById(R.id.login_layout);
        googleBtn = (Button) view.findViewById(R.id.google) ;
        faceBookBtn = (Button) view.findViewById(R.id.facebook) ;
        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.shake);




        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            forgotPassword.setTextColor(csl);
            show_hide_password.setTextColor(csl);
            signUp.setTextColor(csl);
        } catch (Exception e) {
        }
    }

    // Set Listeners
    private void setListeners() {
        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        signUp.setOnClickListener(this);
        googleBtn.setOnClickListener(this);
        faceBookBtn.setOnClickListener(this);
        loginButtonF.setOnClickListener(this);



        // Set check listener over checkbox for showing and hiding password
        show_hide_password
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide
                        // password
                        if (isChecked) {

                            show_hide_password.setText(R.string.hide_pwd);// change checkbox text
                            password.setInputType(InputType.TYPE_CLASS_TEXT);
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());// show password
                        } else {
                            show_hide_password.setText(R.string.show_pwd);// change checkbox text
                            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());// hide password

                        }

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                checkValidation();
                break;

            case R.id.forgot_password:

                // Replace forgot password fragment with animation
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameContainer,
                                new ForgotPassword_Fragment(),
                                Utils.ForgotPassword_Fragment).commit();
                break;
            case R.id.createAccount:

                // Replace signup frgament with animation
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameContainer, new SignUp_Fragment(),
                                Utils.SignUp_Fragment).commit();

                break;
            case R.id.google:

                signIn();

                break;

            case R.id.facebook:
                loginButtonF.performClick();
                signInFacebook();
                break;


        }

    }


    // Check Validation before login
    private void checkValidation() {
        // Get email id and password
        String getEmailId = emailid.getText().toString();
        String getPassword = password.getText().toString();

        // Check patter for email id
        Pattern p = Pattern.compile(Utils.regEx);

        Matcher m = p.matcher(getEmailId);

        // Check for both field is empty or not
        if (getEmailId.equals("") || getEmailId.length() == 0
                || getPassword.equals("") || getPassword.length() == 0) {
            loginLayout.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(getActivity(), view,
                    "Enter both credentials.");

        }
        // Check if email id is valid or not
        else if (!m.find())
            new CustomToast().Show_Toast(getActivity(), view,
                    "Your Email Id is Invalid.");
            // Else do login and do your stuff
        else
            signIn(getEmailId, getPassword);


    }

    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            user = FirebaseAuth.getInstance().getCurrentUser();

                            if (user != null) {
                                // Name, email address, and profile photo Url
                                String name = user.getDisplayName();
                                if (name == null){
                                    Log.d("urlllname","Welcome");
                                    Toast.makeText(getActivity(), "Welcome++++" , Toast.LENGTH_SHORT)
                                            .show();
                                    updateUI(user);
                                }else {
                                    Log.d("urllName", name.toString());
                                    Toast.makeText(getActivity(), "Welcome" + name, Toast.LENGTH_SHORT)
                                            .show();
                                    updateUI(user);
                                }
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getActivity(), "Authentication failed: " + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        /*-------- Check if user is already logged in or not--------*/
        if (user != null) {
            /*------------ If user's email is verified then access login -----------*/
            if (user.isEmailVerified()) {
                String name = user.getDisplayName();
                Toast.makeText(getActivity(), "Login Success." + name,
                        Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getActivity(),MainActivity.class));
            } else {
                Toast.makeText(getActivity(), "Your Email is not verified.",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Welcome , none :)",
                    Toast.LENGTH_SHORT).show();
        }
        /*-------- Check if G user is already logged in or not--------*/
        if (user != null) {
            String name = user.getDisplayName();
            Toast.makeText(getActivity(), "Google or Facebook Login Success." + name,
                    Toast.LENGTH_SHORT).show();

            startActivity(new Intent(getActivity(),MainActivity.class));

        } else {
            Toast.makeText(getActivity(), "Welcome , G , F none :)",
                    Toast.LENGTH_SHORT).show();
        }

    }


    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 9001) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
                if (account != null) {
                    String personName = account.getDisplayName() ;
                    String personEmail = account.getEmail();
                    String personId = account.getId();
                    Uri personPhoto = account.getPhotoUrl();
                    saveGoogleAndFacebookAuthToRealFirebase(personId,personName,personEmail,personPhoto);
                }
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }else {

            // Pass the activity result back to the Facebook SDK
            mCallbackManager.onActivityResult(requestCode, resultCode, data);

        }

    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 9001);
    }
    // [END signin]

    public void saveGoogleAndFacebookAuthToRealFirebase(String uid, String name, String email, Uri uri) {

        userData.setEmail(email);
        userData.setUri(uri);
        //userData.setLocation(location);
        //userData.setPhone(phone);
        ///userData.setPassword(password);
        userData.setName(name);
        userData.setid(uid);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child(uid);

        myRef.child("id").setValue(userData.getid());
        myRef.child("email").setValue(userData.getEmail());
        myRef.child("uri").setValue(userData.getUri().toString());
        //myRef.child("location").setValue(userData.getLocation());
        //myRef.child("phone").setValue(userData.getPhone());
        //myRef.child("pass").setValue(userData.getPassword());
        myRef.child("name").setValue(userData.getName());


    }

    private void signInFacebook(){
        // [START initialize_fblogin]
        // Initialize Facebook Login button




        loginButtonF.setPermissions("email", "public_profile");
        loginButtonF.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(FTAG, "facebook:onSuccess:" + loginResult);

                handleFacebookAccessToken(loginResult.getAccessToken());
                //Use GraphApi to get the information into the app.
                GraphRequest request = GraphRequest.newMeRequest(
                        //pass two parameter
                        loginResult.getAccessToken(),                       //one is the current token
                        new GraphRequest.GraphJSONObjectCallback()          //2nd is grahJSONObject callback
                        {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("facebookdata", response.toString());

                                // Application code
                                try {
                                    String obj = object.toString();                     //get complete JSON object refrence.
                                    String name = object.getString("name");                 //get particular JSON Object
                                    String email = object.getString("email");
                                    //String birthday = object.getString("birthday"); // 01/31/1980 format
                                    String id = object.getString("id");
                                    //String gender = object.getString("gender");
                                    String profileURL = "";
                                    //Log.v("facebookdata1", loginResult.getAccessToken().toString());
                                    if (Profile.getCurrentProfile() != null) {                  //add this check because some people don't have profile picture
                                        profileURL = ImageRequest.getProfilePictureUri(Profile.getCurrentProfile().getId(), 400, 400).toString();
                                        //after getting the profile url you can easily set this to image view using Glide or retrofit library . simple :)
                                    }

                                    Log.v("facebookdata2",obj);
                                    Log.v("facebookdata","id: "+id);
                                    Log.v("facebookdata","name: "+name);
                                    Log.v("facebookdata","email: "+email);
                                   // Log.v("facebookdata","birthday: "+birthday);
                                    //Log.v("facebookdata","gender: "+gender);
                                    Log.v("facebookdata","profileURL: "+profileURL);
                                    Uri uri = Uri.parse(profileURL);

                                    saveGoogleAndFacebookAuthToRealFirebase(id,name,email,uri);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");                    //set these parameter
                request.setParameters(parameters);
                request.executeAsync();                                 //exuecute task in seprate thread


            }

            @Override
            public void onCancel() {
                Log.d(FTAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(FTAG, "facebook:onError", error);
            }
        });
        // [END initialize_fblogin]
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(FTAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(FTAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(FTAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }



}
