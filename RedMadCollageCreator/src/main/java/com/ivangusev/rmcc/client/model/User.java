package com.ivangusev.rmcc.client.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONObject;

/**
 * Created by ivan on 21.02.14.
 */
public class User implements Parcelable {
    private String id;
    private String userName;
    private String fullName;
    private String profilePicture;

    public User() {
    }

    public User(Parcel in) {
        id = in.readString();
        userName = in.readString();
        fullName = in.readString();
        profilePicture = in.readString();
    }

    public User(JSONObject json) {
        id = json.optString("id");
        userName = json.optString("username");
        fullName = json.optString("full_name");
        profilePicture = json.optString("profile_picture");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userName);
        dest.writeString(fullName);
        dest.writeString(profilePicture);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private static Creator<User> CREATOR = new Creator<User>() {
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
