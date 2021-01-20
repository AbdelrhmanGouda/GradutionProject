package com.example.graduationproject.Sign;

import android.net.Uri;

public class User {
    private String uid;
    private String photo;
    private Uri uri;
    private String name;
    private String email;
    private String phone;
    private String location;
    private String password;

    public User() {
    }

    public User(String uid, Uri uri, String name, String email, String phone, String location, String password) {
        this.uid = uid;
        this.uri = uri;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.location = location;
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }




   /*
    private void uploadProfileImage() {
        final StorageReference mStorageReference = FirebaseStorage.getInstance()
                .getReference()
                .child("pics" +
                        uid +
                        photo.substring(photo.lastIndexOf(".")));

        UploadTask uploadTask = mStorageReference.putFile(Uri.fromFile(new File(photo)));
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful())
                    throw task.getException();

                return mStorageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    photo = task.getResult().toString();
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(uid)
                            .child("image")
                            .setValue(photo);
                }
            }
        });
    }
        */

}