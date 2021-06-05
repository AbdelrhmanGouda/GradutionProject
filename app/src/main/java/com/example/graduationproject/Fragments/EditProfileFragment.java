package com.example.graduationproject.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.graduationproject.R;
import com.example.graduationproject.Sign.User;
import com.example.graduationproject.Sign.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class EditProfileFragment extends Fragment implements View.OnClickListener {
    private ImageView editImage;
    private EditText editName, editEmail, editPhone;
    private Button updateBtn;
    private TextView editPassword;
    private FloatingActionButton floatingEditImageBtn;
    public FirebaseAuth auth ;
    String userImage,userName,userEmail,userLocation,userPhone;

    /////////////osama///////////
    public FirebaseAuth mAuth ;
    public Uri generatedFilePathURI;
    public FirebaseUser firebaseUser;
    public FirebaseDatabase database;
    public DatabaseReference myRef;
    DatabaseReference databaseReference;
    Uri image_uri;
    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;
    User user = new User();
    String uid;
    private static Spinner editLocation;
    //////////////////////////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        setHasOptionsMenu(false);
        editImage =view.findViewById(R.id.edit_profile_image);
        floatingEditImageBtn=view.findViewById(R.id.floating_edit_image_btn);
        editName =view.findViewById(R.id.edit_name);
        editEmail =view.findViewById(R.id.edit_email);
        editLocation =view.findViewById(R.id.spinner);
        editPhone =view.findViewById(R.id.edit_phone);
        updateBtn =view.findViewById(R.id.edit_profile_update_btn);
        editPassword=view.findViewById(R.id.edit_password_txt);
        editPassword.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
        mAuth = auth;

        //create a list of items for the spinner.
        String[] items = new String[]{"Choose Your Location", "Alexandria", "Aswan", "Asyut", "Beheira", "Beni Suef", "Cairo", "Dakahlia", "Damietta", "Faiyum", "Gharbia", "Giza", "Ismailia", "Kafr El Sheikh", "Luxor", "Matruh", "Minya", "Monufia", "New Valley", "North Sinai", "Port Said", "Qalyubia", "Qena", "Red Sea", "Sharqia", "Sohag", "South Sinai", "Suez"};


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        editLocation.setAdapter(adapter);


        dataBase();
        setListeners();


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
        return view;
    }
    private void dataBase() {
        FirebaseUser firebaseUser =auth.getCurrentUser();
        final String id = firebaseUser.getUid();

        Query query = FirebaseDatabase.getInstance().getReference().child("Users").child(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!= null){
                    if (snapshot.exists()&& snapshot.getChildrenCount()>0&&snapshot.getValue().toString().length()>0){
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            userName=snapshot.child("name").getValue(String.class);
                            userImage=snapshot.child("uri").getValue(String.class);
                            userEmail=snapshot.child("email").getValue(String.class);
                            userLocation=snapshot.child("location").getValue(String.class);
                            userPhone=snapshot.child("phone").getValue(String.class);
                            editName.setText(userName);
                            editEmail.setText(userEmail);
                            editLocation.setSelection(((ArrayAdapter<String>)editLocation.getAdapter()).getPosition(userLocation));
                            editPhone.setText(userPhone);
                            Picasso.get().load(userImage).into(editImage);
                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateData() {
        FirebaseUser firebaseUser =auth.getCurrentUser();
        final String id = firebaseUser.getUid();
        final String name = editName.getText().toString();
        final String email = editEmail.getText().toString();
        final String location = editLocation.getSelectedItem().toString();
        final String phone = editPhone.getText().toString();
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").child(id);
        uploadImage();
        userProfile(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!= null){
                    if (snapshot.exists()&& snapshot.getChildrenCount()>0&&snapshot.getValue().toString().length()>0){
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            snapshot.getRef().child("name").setValue(name);
                            snapshot.getRef().child("email").setValue(email);
                            snapshot.getRef().child("location").setValue(location);
                            snapshot.getRef().child("phone").setValue(phone);
                            Toast.makeText(getContext(), "update", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void checkValidation() {

        // Get all edittext texts
        String getFullName = editName.getText().toString();
        String getEmailId = editEmail.getText().toString();
        String getMobileNumber = editPhone.getText().toString();
        String getLocation = editLocation.getSelectedItem().toString();
        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);

        // Check if all strings are null or not
        if (getFullName.equals("") || getFullName.length() == 0
                || getEmailId.equals("") || getEmailId.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() == 0
                || getLocation.equals("Choose Your Location") || editLocation.getSelectedItemPosition() == 0
           ) {
            Toast.makeText(getContext(), "All fields are required.", Toast.LENGTH_SHORT).show();
        }

        // Check if email id valid or not
        else if (!m.find()) {
            Toast.makeText(getContext(), "Your Email Id is Invalid.", Toast.LENGTH_SHORT).show();
        }
        else if (editPhone.length() != 11)
        {
            Toast.makeText(getContext(), "Mobile should be 11 Numbers", Toast.LENGTH_SHORT).show();
        }
        else {
            updateData();
        }
    }



    ////////////////////// osama ///////////////////////////


    // Set Listeners
    private void setListeners() {
        floatingEditImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectProfilePic();
            }
        });
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
            FirebaseUser firebaseUser =mAuth.getCurrentUser();
            uid = firebaseUser.getUid();

            // Defining the child of storageReference
            storageReference = FirebaseStorage.getInstance().getReference();
            final StorageReference ref
                    = storageReference
                    .child("images/").child(uid + "/UserImage");

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
                    //camera
                    //user_profile.setImageURI(image_uri);
                    Glide.with(getActivity())
                            .load(image_uri)
                            .centerCrop()
                            .fitCenter()
                            //.apply(new RequestOptions().override(1155, 866))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .placeholder(R.drawable.person_24)
                            .into(editImage);
                    //Toast.makeText(getActivity(),"case 1 " ,Toast.LENGTH_SHORT).show();
                    Log.d("8888888", image_uri.toString());
                    break;
                case 2:
                    //gallary
                    //data.getData returns the content URI for the selected Image
                    image_uri = data.getData();
                    //user_profile.setImageURI(image_uri);
                    editImage.setBackground(null);
                    Glide.with(getActivity())
                            .load(image_uri)
                            .centerCrop()
                            .fitCenter()
                            //.submit(1155,866)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(editImage);
                    //Toast.makeText(getActivity(),"case 2 " ,Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    ///////////////////save image url in realtime database /////////////
    public void imageToRealFirebase(Uri uri) {
        user.setUri(uri);
        FirebaseUser firebaseUser =auth.getCurrentUser();
        final String uid = firebaseUser.getUid();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users").child(uid);
        myRef.child("uri").setValue(user.getUri().toString());
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
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
      AppCompatActivity activity =  (AppCompatActivity)view.getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ChangePasswordFragment()).commit();

    }


    ///////////////////////////////////////////////////////////////////////
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


}