package com.example.graduationproject.Sign;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.graduationproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUp_Fragment extends Fragment implements OnClickListener {
    private static final String TAG = SignUp_Fragment.class.getName();
    public static String uid;
    private static View view;
    private static EditText fullName, emailId, mobileNumber, //location,
            password, confirmPassword;
    private static Spinner location;
    //  private static Spinner dropdown;
    private static TextView login;
    private static Button signUpButton;
    private static CheckBox terms_conditions;
    public FirebaseAuth mAuth;
    public Uri generatedFilePathURI;
    public FirebaseUser firebaseUser;
    public FirebaseDatabase database;
    public DatabaseReference myRef;
    ImageView user_profile;
    DatabaseReference databaseReference;
    Uri image_uri;
    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;
    User user = new User();
    //

    public SignUp_Fragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.signup_layout, container, false);
        mAuth = FirebaseAuth.getInstance();
        initViews();
        setListeners();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //new SignMainActivity().replaceLoginFragment();
    }


    // Initialize all views
    private void initViews() {
        fullName = view.findViewById(R.id.fullName);
        emailId = view.findViewById(R.id.userEmailId);
        mobileNumber = view.findViewById(R.id.mobileNumber);
        location = view.findViewById(R.id.spinner);
        password = view.findViewById(R.id.password);
        confirmPassword = view.findViewById(R.id.confirmPassword);
        signUpButton = view.findViewById(R.id.signUpBtn);
        login = view.findViewById(R.id.already_user);
//		terms_conditions = (CheckBox) view.findViewById(R.id.terms_conditions);
        user_profile = view.findViewById(R.id.add_pic);
        //   dropdown = view.findViewById(R.id.spinner);



        //create a list of items for the spinner.
        String[] items = new String[]{"Choose Your Location", "Alexandria", "Aswan", "Asyut", "Beheira", "Beni Suef", "Cairo", "Dakahlia", "Damietta", "Faiyum", "Gharbia", "Giza", "Ismailia", "Kafr El Sheikh", "Luxor", "Matruh", "Minya", "Monufia", "New Valley", "North Sinai", "Port Said", "Qalyubia", "Qena", "Red Sea", "Sharqia", "Sohag", "South Sinai", "Suez"};


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.color_spinner, items);
        location.setAdapter(adapter);



        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(), xrp);

            login.setTextColor(Color.rgb(	69	,166,	222));
//			terms_conditions.setTextColor(csl);
        } catch (Exception e) {
            Toast.makeText(getActivity(), e + " ", Toast.LENGTH_SHORT).show();
        }
    }

    /////////////for saving all data in realetime data base/////////////////////// , String photo, String name, String email, String phone, String location, String password

    /*public void getImageFromStorageDB(){


        File localFile = File.createTempFile("images", "jpg");
        riversRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to local file
                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }
        });
    }

     */



    public void saveToRealFirebase(String uid, String name, String email, String phone, String location, String password) {
        //saveToRealFirebase(String uid, Uri uri, String name, String email, String phone, String location, String password)
        //User user = new User();
        user.setEmail(email);
        //user.setUri(uri);
        user.setLocation(location);
        user.setPhone(phone);
        user.setPassword(password);
        user.setName(name);
        user.setid(uid);


        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("Users").child(uid);
        myRef = database.getReference("Users").child(uid);
        databaseReference = myRef;

        myRef.child("id").setValue(user.getid());
        myRef.child("email").setValue(user.getEmail());
        //myRef.child("uri").setValue(user.getUri().toString());
        myRef.child("location").setValue(user.getLocation());
        myRef.child("phone").setValue(user.getPhone());
        myRef.child("pass").setValue(user.getPassword());
        myRef.child("name").setValue(user.getName());


    }


    // Set Listeners
    private void setListeners() {
        signUpButton.setOnClickListener(this);
        login.setOnClickListener(this);
        user_profile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);//zero can be replaced with any action code (called requestCode)

                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
                */

                SelectProfilePic();


            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBtn:

                // Call checkValidation method
                checkValidation();
                break;

            case R.id.already_user:

                // Replace login fragment
                new SignMainActivity().replaceLoginFragment();
                break;
        }

    }

    // Check Validation Method
    private void checkValidation() {

        // Get all edittext texts
        String getFullName = fullName.getText().toString();
        String getEmailId = emailId.getText().toString();
        String getMobileNumber = mobileNumber.getText().toString();
        String getLocation = location.getSelectedItem().toString();
        String getPassword = password.getText().toString();
        String getConfirmPassword = confirmPassword.getText().toString();


        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);

        // Check if all strings are null or not
        if (getFullName.equals("") || getFullName.length() == 0
                || getEmailId.equals("") || getEmailId.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() == 0
                || getLocation.equals("Choose Your Location") || location.getSelectedItemPosition() == 0
                || getPassword.equals("") || getPassword.length() == 0
                || getConfirmPassword.equals("")
                || getConfirmPassword.length() == 0) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "All fields are required.");
        }

        // Check if email id valid or not
        else if (!m.find()) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "Your Email Id is Invalid.");
        }
        // Check if both password should be equal
        else if (!getConfirmPassword.equals(getPassword)) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "Both password doesn't match.");
        }
        // Check if pass > 6
        else if (getPassword.length() < 6) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "Password should be at least 6 characters");
        } else if (image_uri == null) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "choose a profile image");
        }
        else if (mobileNumber.length() != 11) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "Mobile should be 11 Numbers");
        }


        /*
		// Make sure user should check Terms and Conditions checkbox
		else if (!terms_conditions.isChecked())
			new CustomToast().Show_Toast(getActivity(), view,
					"Please select Terms and Conditions.");*/

        // Else do signup or do your stuff
        else {
            //createAccount(getEmailId, getPassword);
            createAccount(getEmailId, getPassword, getMobileNumber, getLocation, getFullName);
            //userProfile(getFullName);
			/*Toast.makeText(getActivity(), "welcome (" + getFullName + ")", Toast.LENGTH_SHORT)
					.show();

			 */
            // Replace login fragment
            //new SignMainActivity().replaceLoginFragment();
        }
    }

    // UploadImage method
    public void uploadImage() {
        if (image_uri != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");

            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);

            // Defining the child of storageReference
            storageReference = FirebaseStorage.getInstance().getReference();
            final StorageReference ref = storageReference.child("images/").child(uid + "/UserImage");

            // adding listeners on upload
            // or failure of image
            generatedFilePathURI = image_uri;
            byte[] data = new byte[0];
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image_uri);
                bmp = modifyOrientation(bmp,getPath(getContext(),image_uri));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 30, baos);
                data = baos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.d("urlll1", generatedFilePathURI + "");
            UploadTask uploadTask = ref.putBytes(data);//ref.putFile(image_uri);
            ref.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Image uploaded successfully
                    // Dismiss dialog
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //Now play with downloadPhotoUrl
                            //Store data into Firebase Realtime Database
                            generatedFilePathURI = uri;
                            Log.d("urlll", generatedFilePathURI + "");

                               // imageToRealFirebase(generatedFilePathURI);
                            user.setUri(uri);

                            database = FirebaseDatabase.getInstance();
                            myRef = database.getReference("Users").child(uid);
                            myRef.child("uri").setValue(user.getUri().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(getActivity(),
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                }
                            });
        }
    }


    public void createAccount(final String email, final String password, final String phone, final String location, final String name) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            firebaseUser = mAuth.getCurrentUser();
                            uid = firebaseUser.getUid();
                            // Toast.makeText(getActivity(), "3 ", Toast.LENGTH_SHORT).show();
                            uploadImage();

                            //Toast.makeText(getActivity(), generatedFilePathURI.toString() , Toast.LENGTH_SHORT).show();
                            // Toast.makeText(getActivity(), "2 ", Toast.LENGTH_SHORT).show();
                            Log.d("urlll2", generatedFilePathURI.toString());
                            saveToRealFirebase(uid, name, email, phone, location, password);
                            //saveToRealFirebase(uid, generatedFilePathURI, name, email, phone, location, password);
                            //Toast.makeText(getActivity(), "1 ", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(), "Sign up success " + uid, Toast.LENGTH_SHORT).show();
                            userProfile(name);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Sign up failed: " + task.getException().toString(), Toast.LENGTH_SHORT).show();

                        }

                        // ...
                        FirebaseUser currentUser = mAuth.getCurrentUser();

                    }
                });


    }



    /*----------For saving image and user name in Auth Firebase Database-------*/

    public void userProfile(String name) {
        firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name).setPhotoUri(generatedFilePathURI).build();
            firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    verifyEmailRequest();
                }
            });
        }
    }

    /*-------For sending verification email on user's registered email------*/
    private void verifyEmailRequest() {
        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Email verification sent on\n" + emailId.getText().toString(), Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                            startActivity(new Intent(getActivity(),SignMainActivity.class));
                        } else {
                            Toast.makeText(getActivity(), "Sign up Success But Failed to send verification email.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /*-------- Below Code is for selecting image from galary or camera -----------*/
    private void SelectProfilePic() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                                getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                            String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permission, 1000);
                        } else {
                            openCamera();
                        }
                    } else {
                        openCamera();
                    }
                } else if (options[item].equals("Choose from Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }
            }
        });
        builder.show();

    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        //Camera intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(takePictureIntent, 1);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1000:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    //permisiion from pop up was denied.
                    Toast.makeText(getActivity(), "Permission Denied...", Toast.LENGTH_LONG).show();
                }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    //user_profile.setImageURI(image_uri);
                    Glide.with(getActivity())
                            .load(image_uri)
                            .centerCrop()
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .placeholder(R.drawable.person_24)
                            .into(user_profile);
                    //Toast.makeText(getActivity(),"case 1 " ,Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    //data.getData returns the content URI for the selected Image
                    image_uri = data.getData();
                    //user_profile.setImageURI(image_uri);
                    Glide.with(getActivity())
                            .load(image_uri)
                            .centerCrop()
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .placeholder(R.drawable.person_24)
                            .into(user_profile);
                    //Toast.makeText(getActivity(),"case 2 " ,Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    ///////////////////save image url in realtime database /////////////
    public void imageToRealFirebase(Uri uri) {
        user.setUri(uri);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users").child(uid);
        myRef.child("uri").setValue(user.getUri().toString());
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////// image uri to bitmap ///////////////

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    ////////////////////////////////oriantation

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    ///////////////////////////////////////////////////////////////////////////////////


}

