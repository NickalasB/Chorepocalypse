package com.zonkey.chorepocalypse.models;

/**
 * Created by nickbradshaw on 1/18/17.
 */

public class Chore {

    private String choreName;
    private String choreReward;

    public Chore(String cn, String cr) {
        choreName = cn;
        choreReward = cr;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chore chore = (Chore) o;

        if (choreName != null ? !choreName.equals(chore.choreName) : chore.choreName != null)
            return false;
        return choreReward != null ? choreReward.equals(chore.choreReward) : chore.choreReward == null;

    }

    @Override
    public int hashCode() {
        int result = choreName != null ? choreName.hashCode() : 0;
        result = 31 * result + (choreReward != null ? choreReward.hashCode() : 0);
        return result;
    }
}
