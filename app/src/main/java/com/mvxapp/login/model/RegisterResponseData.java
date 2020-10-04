package com.mvxapp.login.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterResponseData implements Parcelable {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private RegisterData data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RegisterData getData() {
        return data;
    }

    public void setData(RegisterData data) {
        this.data = data;
    }

    protected RegisterResponseData(Parcel in) {
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.data = ((RegisterData) in.readValue((RegisterData.class.getClassLoader())));
    }

    public static final Creator<RegisterResponseData> CREATOR = new Creator<RegisterResponseData>() {
        @Override
        public RegisterResponseData createFromParcel(Parcel in) {
            return new RegisterResponseData(in);
        }

        @Override
        public RegisterResponseData[] newArray(int size) {
            return new RegisterResponseData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeValue(message);
        dest.writeValue(data);
    }
}
