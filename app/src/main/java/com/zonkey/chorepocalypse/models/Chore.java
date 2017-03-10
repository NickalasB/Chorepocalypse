package com.zonkey.chorepocalypse.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nickbradshaw on 1/18/17.
 */

public class Chore implements Parcelable {

    private String choreName;
    private String choreReward;
    private String chorePhotoUrl;
    private long choreTime;
    private String choreKey;
    private boolean isChoreApprovalRequired;
    private boolean isChoreApproved;

    public Chore(String cName, String cReward, String cPhotoUrl, long cTime, String cKey, boolean cApprovalRequired, boolean cApproved) {
        choreName = cName;
        choreReward = cReward;
        chorePhotoUrl = cPhotoUrl;
        choreTime = cTime;
        choreKey = cKey;
        isChoreApprovalRequired = cApprovalRequired;
        isChoreApproved = cApproved;
    }

    public Chore() {

    }

    public String getChoreName() {
        return choreName;
    }

    public void setChoreName(String choreName) {
        this.choreName = choreName;
    }

    public String getChoreReward() {
        return choreReward;
    }

    public void setChoreReward(String choreReward) {
        this.choreReward = choreReward;
    }

    public String getChorePhotoUrl() {
        return chorePhotoUrl;
    }

    public void setChorePhotoUrl(String chorePhotoUrl) {
        this.chorePhotoUrl = chorePhotoUrl;
    }

    public long getChoreTime() {
        return choreTime;
    }

    public void setChoreTime(long choreTime) {
        this.choreTime = choreTime;
    }

    public String getChoreKey() {
        return choreKey;
    }

    public void setChoreKey(String choreKey) {
        this.choreKey = choreKey;
    }

    public boolean getIsChoreApprovalRequired() {
        return isChoreApprovalRequired;
    }

    public void setChoreApprovalRequired(boolean choreApprovalRequired) {
        this.isChoreApprovalRequired = choreApprovalRequired;
    }

    public boolean getIsChoreApproved() {
        return isChoreApproved;
    }

    public void setChoreApproved(boolean choreApproved) {
        this.isChoreApproved = choreApproved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chore chore = (Chore) o;

        if (choreTime != chore.choreTime) return false;
        if (isChoreApprovalRequired != chore.isChoreApprovalRequired) return false;
        if (isChoreApproved != chore.isChoreApproved) return false;
        if (choreName != null ? !choreName.equals(chore.choreName) : chore.choreName != null)
            return false;
        if (choreReward != null ? !choreReward.equals(chore.choreReward) : chore.choreReward != null)
            return false;
        if (chorePhotoUrl != null ? !chorePhotoUrl.equals(chore.chorePhotoUrl) : chore.chorePhotoUrl != null)
            return false;
        return choreKey != null ? choreKey.equals(chore.choreKey) : chore.choreKey == null;

    }

    @Override
    public int hashCode() {
        int result = choreName != null ? choreName.hashCode() : 0;
        result = 31 * result + (choreReward != null ? choreReward.hashCode() : 0);
        result = 31 * result + (chorePhotoUrl != null ? chorePhotoUrl.hashCode() : 0);
        result = 31 * result + (int) (choreTime ^ (choreTime >>> 32));
        result = 31 * result + (choreKey != null ? choreKey.hashCode() : 0);
        result = 31 * result + (isChoreApprovalRequired ? 1 : 0);
        result = 31 * result + (isChoreApproved ? 1 : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.choreName);
        dest.writeString(this.choreReward);
        dest.writeString(this.chorePhotoUrl);
        dest.writeLong(this.choreTime);
        dest.writeString(this.choreKey);
        dest.writeByte(this.isChoreApprovalRequired ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isChoreApproved ? (byte) 1 : (byte) 0);
    }

    protected Chore(Parcel in) {
        this.choreName = in.readString();
        this.choreReward = in.readString();
        this.chorePhotoUrl = in.readString();
        this.choreTime = in.readLong();
        this.choreKey = in.readString();
        this.isChoreApprovalRequired = in.readByte() != 0;
        this.isChoreApproved = in.readByte() != 0;
    }

    public static final Creator<Chore> CREATOR = new Creator<Chore>() {
        @Override
        public Chore createFromParcel(Parcel source) {
            return new Chore(source);
        }

        @Override
        public Chore[] newArray(int size) {
            return new Chore[size];
        }
    };
}