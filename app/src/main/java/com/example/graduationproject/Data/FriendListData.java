package com.example.graduationproject.Data;

import android.os.Parcel;
import android.os.Parcelable;

public class FriendListData implements Parcelable {
    private String uri;
    private String name;
    private  String id;
    private boolean State;


    public FriendListData() {
    }

    public FriendListData(String uri, String name, String id) {
        this.uri = uri;
        this.name = name;
        this.id = id;
    }

    public FriendListData(String uri, String name, String id, boolean state) {
        this.uri = uri;
        this.name = name;
        this.id = id;
        State = state;
    }

    protected FriendListData(Parcel in) {
        uri = in.readString();
        name = in.readString();
        id = in.readString();
        State = in.readByte() != 0;
    }

    public static final Creator<FriendListData> CREATOR = new Creator<FriendListData>() {
        @Override
        public FriendListData createFromParcel(Parcel in) {
            return new FriendListData(in);
        }

        @Override
        public FriendListData[] newArray(int size) {
            return new FriendListData[size];
        }
    };

    public boolean isState() {
        return State;
    }

    public void setState(boolean state) {
        State = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uri);
        dest.writeString(name);
        dest.writeString(id);
        dest.writeByte((byte) (State ? 1 : 0));
    }
}
