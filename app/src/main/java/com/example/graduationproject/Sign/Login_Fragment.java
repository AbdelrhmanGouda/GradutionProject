package com.example.graduationproject.Sign;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login_Fragment extends Fragment implements OnClickListener {
    private static final String TAG = "GoogleActivity";
    private static final String FTAG = "FacebookLogin";
    private static final int RC_SIGN_IN = 12345;
    private static View view;
    private static EditText emailid, password;
    private static Button loginButton, faceBookBtn, googleBtn;
    private static TextView forgotPassword, signUp;
    private static CheckBox show_hide_password;
    private static LinearLayout loginLayout;
    private static Animation shakeAnimation;
    private static FragmentManager fragmentManager;
    private static LoginButton loginButtonF;
    public GoogleSignInClient mGoogleSignInClient;
    public FirebaseAuth mAuth;
    public FirebaseUser user;
    public String nameReal, idReal, emailReal;
    public Uri uriReal;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth.AuthStateListener mAuthListener;
    User userData = new User();
    private CallbackManager mCallbackManager;
    public boolean IdTokenFlag;
    public String IdToken,UserType;
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;


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



        pref = getActivity().getSharedPreferences("Token",0);
        editor = pref.edit();

        IdToken = pref.getString("IdToken", "");
        Log.d("IdTokenCreate",IdToken);
        UserType = pref.getString("UserType", "");
        Log.d("IdTokenUserTypeCreate",UserType);
        IdTokenFlag = pref.getBoolean("IdTokenFlag",false);
        Log.d("IdTokenFlagCreate",String.valueOf(IdTokenFlag));

        initViews();
        setListeners();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        // String ll = user.getUid() + "$$";
        //  Log.d("5555555",ll);
        // Check if user or account is signed in (non-null) and update UI accordingly.
        updateUI(user);




    }


    // Initiate Views
    private void initViews() {
        fragmentManager = getActivity().getSupportFragmentManager();

        loginButtonF = view.findViewById(R.id.login_button);
        loginButtonF.setFragment(Login_Fragment.this);
        emailid = view.findViewById(R.id.login_emailid);
        password = view.findViewById(R.id.login_password);
        loginButton = view.findViewById(R.id.loginBtn);
        forgotPassword = view.findViewById(R.id.forgot_password);
        signUp = view.findViewById(R.id.createAccount);
        show_hide_password = view
                .findViewById(R.id.show_hide_password);
        loginLayout = view.findViewById(R.id.login_layout);
        googleBtn = view.findViewById(R.id.google);
        faceBookBtn = view.findViewById(R.id.facebook);
        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.shake);


        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            forgotPassword.setTextColor(Color.rgb(	69	,166,	222));
            show_hide_password.setTextColor(Color.rgb(	69	,166,	222));
            signUp.setTextColor(Color.rgb(	69	,166,	222));
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
                    public void onCheckedChanged(CompoundButton button, boolean isChecked) {

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
                                if (name == null) {
                                    Log.d("urlllname", "Welcome");
                                    Toast.makeText(getActivity(), "Welcome++++", Toast.LENGTH_SHORT)
                                            .show();
                                    updateUI(user);
                                } else {
                                    Log.d("urllName", name);
                                    Toast.makeText(getActivity(), "Welcome" + name, Toast.LENGTH_SHORT)
                                            .show();

                                    IdToken="";
                                    IdTokenFlag = false;
                                    editor.putString("IdToken", IdToken);
                                    editor.putString("UserType","N");
                                    editor.putBoolean("IdTokenFlag",IdTokenFlag);
                                    editor.apply();

                                    IdToken = pref.getString("IdToken", "");
                                    Log.d("IdTokenNorm",IdToken);

                                    UserType = pref.getString("UserType", "");
                                    Log.d("IdTokenUserTypeNorm",UserType);

                                    IdTokenFlag = pref.getBoolean("IdTokenFlag",false);
                                    Log.d("IdTokenFlagNorm",String.valueOf(IdTokenFlag));
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

        /*-------- Check if G user is already logged in or not--------*/
        if (user != null && IdTokenFlag && UserType.equals("G")) {
            String name = user.getDisplayName();
            Toast.makeText(getActivity(), "Google Login Success." + name, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), MainActivity.class));
        }else if (user != null && IdTokenFlag && UserType.equals("F")){
            String name = user.getDisplayName();
            Toast.makeText(getActivity(), "Facebook Login Success " + name, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), MainActivity.class));
        }
        else if (user != null) {
            /*------------ If user's email is verified then access login -----------*/
            if (user.isEmailVerified()) {
                String name = user.getDisplayName();
                Log.d("555555","Login Success " +"/" + user.isEmailVerified() +"/" +name);

                startActivity(new Intent(getActivity(), MainActivity.class));
                //Toast.makeText(getActivity(), "Login Success."  +name, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Your Email is not verified.",
                        Toast.LENGTH_SHORT).show();
                Log.d("555556","Login Success." +"/" + user.isEmailVerified() +"/" );

               // startActivity(new Intent(getActivity(), MainActivity.class));
            }
        } else {
            Toast.makeText(getActivity(), "Welcome , none :)",
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
                    String personName = account.getDisplayName();
                    nameReal = personName;
                    String personEmail = account.getEmail();
                    emailReal = personEmail;
                    String personId = account.getId();
                    Uri personPhoto = account.getPhotoUrl();
                    uriReal = personPhoto;

                    //final String gId = firebaseUser.getUid();//firebaseUser.getUid();


                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    while (mAuth.getCurrentUser() == null) {
                        mAuth = FirebaseAuth.getInstance();

                        Log.v("555555", "id: " + "nulllllllll");

                        if (mAuth.getCurrentUser() != null) {
                            //Your action here

                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String fId = firebaseUser.getUid();
                            idReal = fId;
                            Log.v("5555555", "id: " + idReal);
                            saveGoogleAndFacebookAuthToRealFirebase(idReal, nameReal, emailReal, uriReal);
                            Log.v("555555", ": " + fId);
                            break;

                        }
                    }


                }
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        } else {

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
                            IdTokenFlag =true;


                            editor.putString("IdToken", idToken);
                            editor.putString("UserType","G");
                            editor.putBoolean("IdTokenFlag",IdTokenFlag);
                            editor.apply();

                            IdToken = pref.getString("IdToken", "");
                            Log.d("IdTokenGoogle",IdToken);

                            UserType = pref.getString("UserType", "");
                            Log.d("IdTokenUserTypeGoogle",UserType);

                            IdTokenFlag = pref.getBoolean("IdTokenFlag",false);
                            Log.d("IdTokenFlagGoogle",String.valueOf(IdTokenFlag));

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

    private void signInFacebook() {
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
                                    nameReal = name;
                                    String email = object.getString("email");
                                    emailReal = email;
                                    //String birthday = object.getString("birthday"); // 01/31/1980 format
                                    String id = object.getString("id");
                                    //String gender = object.getString("gender");
                                    String profileURL = "";

                                    if (Profile.getCurrentProfile() != null) {

                                        //add this check because some people don't have profile picture
                                        profileURL = ImageRequest.getProfilePictureUri(Profile.getCurrentProfile().getId(), 400, 400) + "&access_token=2615626898730021|JUNggramyAICJhX_cwcl0M4Vs48";
                                        //after getting the profile url you can easily set this to image view using Glide or retrofit library . simple :)
                                        Log.v("facebookdata1", profileURL);
                                    }


                                    Log.v("facebookdata2", obj);
                                    Log.v("facebookdata", "id: " + id);
                                    Log.v("facebookdata", "name: " + name);
                                    Log.v("facebookdata", "email: " + email);
                                    // Log.v("facebookdata","birthday: "+birthday);
                                    //Log.v("facebookdata","gender: "+gender);
                                    Log.v("facebookdata", "profileURL: " + profileURL);
                                    Uri uri = Uri.parse(profileURL);
                                    uriReal = uri;
                                    //FirebaseAuth m2Auth = FirebaseAuth.getInstance();
                                    //FirebaseUser user = mAuth.getCurrentUser();
                                    //String UID =user.getUid();
                                   /*
                                    FirebaseUser firebaseUser =mAuth.getCurrentUser();
                                    String fId =firebaseUser.getUid();*/


                                    mAuth = FirebaseAuth.getInstance();

                                    while (mAuth.getCurrentUser() == null) {

                                        Log.v("555555", "id: " + "nulllllllll");

                                        if (mAuth.getCurrentUser() != null) {
                                            //Your action here
                                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                            String fId = firebaseUser.getUid();
                                            idReal = fId;
                                            Log.v("5555555", "id: " + idReal);
                                            saveGoogleAndFacebookAuthToRealFirebase(idReal, nameReal, emailReal, uriReal);
                                            Log.v("555555", ": " + fId);
                                            break;

                                        }
                                    }

                                    mAuthListener = new FirebaseAuth.AuthStateListener() {
                                        @Override
                                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                                        }
                                    };


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
                            IdTokenFlag = true;
                            IdToken = token.getToken();

                            editor.putString("IdToken", IdToken);
                            editor.putString("UserType","F");
                            editor.putBoolean("IdTokenFlag",IdTokenFlag);
                            editor.apply();

                            IdToken = pref.getString("IdToken", "");
                            Log.d("IdTokenFace",IdToken);

                            UserType = pref.getString("UserType", "");
                            Log.d("IdTokenUserTypeFace",UserType);

                            IdTokenFlag = pref.getBoolean("IdTokenFlag",false);
                            Log.d("IdTokenFlagFace",String.valueOf(IdTokenFlag));

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
