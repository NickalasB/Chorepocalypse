package com.zonkey.chorepocalypse.models;

/**
 * Created by nickbradshaw on 1/18/17.
 */

public class Chore {

    private String choreName;
    private String choreReward;
    private String chorePhotoUrl;
    private long choreTime;
    private int alarmRequestCode;
    private String choreKey;

    public Chore(String cn, String cr, String cpu, long ct, int arq, String ck) {
        choreName = cn;
        choreReward = cr;
        chorePhotoUrl = cpu;
        choreTime = ct;
        alarmRequestCode = arq;
        choreKey = ck;
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

    public int getAlarmRequestCode() {
        return alarmRequestCode;
    }

    public void setAlarmRequestCode(int alarmRequestCode) {
        this.alarmRequestCode = alarmRequestCode;
    }

    public String getChoreKey() {
        return choreKey;
    }

    public void setChoreKey(String choreKey) {
        this.choreKey = choreKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chore chore = (Chore) o;

        if (choreTime != chore.choreTime) return false;
        if (alarmRequestCode != chore.alarmRequestCode) return false;
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
        result = 31 * result + alarmRequestCode;
        result = 31 * result + (choreKey != null ? choreKey.hashCode() : 0);
        return result;
    }

}