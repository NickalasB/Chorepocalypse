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

    public Chore(String cn, String cr, String cpu, long ct, int arq) {
        choreName = cn;
        choreReward = cr;
        chorePhotoUrl = cpu;
        choreTime = ct;
        alarmRequestCode = arq;
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
        return chorePhotoUrl != null ? chorePhotoUrl.equals(chore.chorePhotoUrl) : chore.chorePhotoUrl == null;

    }

    @Override
    public int hashCode() {
        int result = choreName != null ? choreName.hashCode() : 0;
        result = 31 * result + (choreReward != null ? choreReward.hashCode() : 0);
        result = 31 * result + (chorePhotoUrl != null ? chorePhotoUrl.hashCode() : 0);
        result = 31 * result + (int) (choreTime ^ (choreTime >>> 32));
        result = 31 * result + alarmRequestCode;
        return result;
    }
}