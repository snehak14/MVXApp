package com.mvxapp.login.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponseData implements Parcelable {
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private LoginData data;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoginData getData() {
        return data;
    }

    public void setData(LoginData data) {
        this.data = data;
    }

    protected LoginResponseData(Parcel in) {
        this.token = ((String) in.readValue((String.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.data = ((LoginData) in.readValue((LoginData.class.getClassLoader())));
    }

    public static final Creator<LoginResponseData> CREATOR = new Creator<LoginResponseData>() {
        @Override
        public LoginResponseData createFromParcel(Parcel in) {
            return new LoginResponseData(in);
        }

        @Override
        public LoginResponseData[] newArray(int size) {
            return new LoginResponseData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeValue(token);
        dest.writeValue(message);
        dest.writeValue(data);
    }
}
