package com.mvxapp.login.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResendOtpResponseData implements Parcelable
{
    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    protected ResendOtpResponseData(Parcel in) {
        this.message = ((String) in.readValue((String.class.getClassLoader())));
    }

    public static final Creator<ResendOtpResponseData> CREATOR = new Creator<ResendOtpResponseData>() {
        @Override
        public ResendOtpResponseData createFromParcel(Parcel in) {
            return new ResendOtpResponseData(in);
        }

        @Override
        public ResendOtpResponseData[] newArray(int size) {
            return new ResendOtpResponseData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeValue(message);
    }
}
