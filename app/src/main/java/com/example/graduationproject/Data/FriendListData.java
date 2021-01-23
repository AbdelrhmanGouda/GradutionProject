package com.example.graduationproject.Data;

import android.graphics.Bitmap;
import android.media.Image;

public class FriendListData {
    private Bitmap friendListImage;
    private String friendListName;

    public FriendListData( String friendListName) {
    //    this.friendListImage = friendListImage;
        this.friendListName = friendListName;
    }

    public Bitmap getFriendListImage() {
        return friendListImage;
    }

    public void setFriendListImage(Bitmap friendListImage) {
        this.friendListImage = friendListImage;
    }

    public String getFriendListName() {
        return friendListName;
    }

    public void setFriendListName(String friendListName) {
        this.friendListName = friendListName;
    }
}
