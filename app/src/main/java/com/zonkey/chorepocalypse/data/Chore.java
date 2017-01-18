package com.zonkey.chorepocalypse.data;

/**
 * Created by nickbradshaw on 1/18/17.
 */

public class Chore {

    private String choreName;
    private String choreReward;

    public Chore(){
    }

    public Chore(String choreName, String choreReward){
        this.choreName = choreName;
        this.choreReward = choreReward;
    }

    public String getChoreName(){return choreName;}

    public void setChoreName(String choreName){this.choreName = choreName;}

    public String getChoreReward() {return choreReward;}

    public void setChoreReward(String choreReward) {this.choreReward = choreReward;}

//    public int getChoreReward() {return choreReward;}
//
//    public void setChoreReward(int choreReward) {this.choreReward = choreReward;}
}
