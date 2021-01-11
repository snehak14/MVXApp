package com.mvxapp.login.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserResponseData implements Parcelable {
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("isMobileVerified")
    @Expose
    private Boolean isMobileVerified;
    @SerializedName("submittedAt")
    @Expose
    private Long submittedAt;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("mobile")
    @Expose
    private Long mobile;
    @SerializedName("updatedAt")
    @Expose
    private Long updatedAt;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("entryType")
    @Expose
    private String entryType;
    @SerializedName("isEmailVerified")
    @Expose
    private Boolean isEmailVerified;
    @SerializedName("age")
    @Expose
    private Integer age;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsMobileVerified() {
        return isMobileVerified;
    }

    public void setIsMobileVerified(Boolean isMobileVerified) {
        this.isMobileVerified = isMobileVerified;
    }

    public Long getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Long submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getMobile() {
        return mobile;
    }

    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public Boolean getIsEmailVerified() {
        return isEmailVerified;
    }

    public void setIsEmailVerified(Boolean isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    protected UserResponseData(Parcel in) {
        this.address = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.isMobileVerified = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.submittedAt = ((Long) in.readValue((Long.class.getClassLoader())));
        this.state = ((String) in.readValue((String.class.getClassLoader())));
        this.city = ((String) in.readValue((String.class.getClassLoader())));
        this.password = ((String) in.readValue((String.class.getClassLoader())));
        this.mobile = ((Long) in.readValue((Long.class.getClassLoader())));
        this.updatedAt = ((Long) in.readValue((Long.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.entryType = ((String) in.readValue((String.class.getClassLoader())));
        this.isEmailVerified = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.age = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public static final Creator<UserResponseData> CREATOR = new Creator<UserResponseData>() {
        @Override
        public UserResponseData createFromParcel(Parcel in) {
            return new UserResponseData(in);
        }

        @Override
        public UserResponseData[] newArray(int size) {
            return new UserResponseData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeValue(address);
        dest.writeValue(email);
        dest.writeValue(name);
        dest.writeValue(isMobileVerified);
        dest.writeValue(submittedAt);
        dest.writeValue(state);
        dest.writeValue(city);
        dest.writeValue(password);
        dest.writeValue(mobile);
        dest.writeValue(updatedAt);
        dest.writeValue(id);
        dest.writeValue(entryType);
        dest.writeValue(isEmailVerified);
        dest.writeValue(age);
    }

}
