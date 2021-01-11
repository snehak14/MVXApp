package com.mvxapp.login.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPasswordResponseData implements Parcelable {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("user")
    @Expose
    private UserResponseData user;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserResponseData getUser() {
        return user;
    }

    public void setUser(UserResponseData user) {
        this.user = user;
    }

    protected ForgotPasswordResponseData(Parcel in) {
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.user = ((UserResponseData) in.readValue((UserResponseData.class.getClassLoader())));
    }

    public static final Creator<ForgotPasswordResponseData> CREATOR = new Creator<ForgotPasswordResponseData>() {
        @Override
        public ForgotPasswordResponseData createFromParcel(Parcel in) {
            return new ForgotPasswordResponseData(in);
        }

        @Override
        public ForgotPasswordResponseData[] newArray(int size) {
            return new ForgotPasswordResponseData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeValue(message);
        dest.writeValue(user);
    }
}
