package com.example.memes;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

public class VKUser implements Parcelable {
    public String firstName = "";
    public int id = 0;
    public String lastName = "";
    public String photo = "";


    protected VKUser(Parcel in) {
        firstName = in.readString();
        id = in.readInt();
        lastName = in.readString();
        photo = in.readString();
    }

    public VKUser(VKUser vkUser) {
        this.id = vkUser.id;
        this.firstName = vkUser.firstName;
        this.lastName = vkUser.lastName;
        this.photo = vkUser.photo;
    }

    public static final Creator<VKUser> CREATOR = new Creator<VKUser>() {
        @Override
        public VKUser createFromParcel(Parcel in) {
            return new VKUser(in);
        }

        @Override
        public VKUser[] newArray(int size) {
            return new VKUser[size];
        }


    };

    public VKUser(String firstName, int id, String lastName, String photo) {
    }


    public static String[] parse(JSONObject json) {

        String id = String.valueOf(json.optInt("id", 0));
        String firstName = json.optString("first_name", "");
        Log.d("first.name", firstName);
        String lastName = json.optString("last_name", "");
        String photo = json.optString("photo_200", "");
        Log.d("VKUser", id + firstName + lastName + photo);
        return new String[]{id, firstName, lastName, photo};
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(photo);
    }
}