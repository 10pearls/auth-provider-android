package com.tenpearls.auth.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Class to contain user information
 */
public class User implements Parcelable {
    private final String userName;
    private final Attributes attributes;

    /**
     * Constructs the user
     * @param cognitoUser CognitoUser instance got from login call.
     * @param cognitoUserDetails CognitoUserDetails instance got from user details call.
     */
    public User(CognitoUser cognitoUser, CognitoUserDetails cognitoUserDetails) {
        userName = cognitoUser.getUserId();
        attributes = new Attributes();
        attributes.fromCognitoAttributes(cognitoUserDetails.getAttributes());
    }

    /**
     * Provides the username of this user.
     * @return username of the user.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Provides the attributes of this user.
     * @return attributes of the user.
     */
    public Attributes getAttributes() {
        return attributes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userName);
        dest.writeSerializable(this.attributes);
    }

    protected User(Parcel in) {
        this.userName = in.readString();
        this.attributes = (Attributes) in.readSerializable();
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

    public static class Attributes extends HashMap<String, String> {

        public CognitoUserAttributes toCognitoAttributes() {
            CognitoUserAttributes cognitoAttributes = new CognitoUserAttributes();

            for (Entry<String, String> entry : this.entrySet())
                cognitoAttributes.addAttribute(entry.getKey(), entry.getValue());
            return cognitoAttributes;
        }

        public void fromCognitoAttributes(CognitoUserAttributes cognitoAttributes) {
            this.clear();
            this.putAll(cognitoAttributes.getAttributes());
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static User fromString(String json) {
        return new Gson().fromJson(json, User.class);
    }
}
