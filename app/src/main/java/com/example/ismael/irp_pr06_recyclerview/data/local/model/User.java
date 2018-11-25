package com.example.ismael.irp_pr06_recyclerview.data.local.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private static long idCount = 1;
    private static final AvatarDatabase avatarDatabase = AvatarDatabase.getInstance();
    private long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String web;
    private Avatar avatar;

    public User(String name, String email, String phone, String address, String web, Avatar avatar) {
        this.id = idCount;
        idCount++;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.web = web;
        this.avatar = avatar;
    }

    public static User getDefaultUser(){
        return new User("", "", "", "", "", avatarDatabase.getDefaultAvatar());
    }

    public long getId() {
        return id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.phone);
        dest.writeString(this.address);
        dest.writeString(this.web);
        dest.writeParcelable(this.avatar, flags);
    }

    private User(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.email = in.readString();
        this.phone = in.readString();
        this.address = in.readString();
        this.web = in.readString();
        this.avatar = in.readParcelable(Avatar.class.getClassLoader());
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
