package com.example.mightworthit.Matches;

public class MatchesObject {
    private String UserID, name, profileImageURL;

    public MatchesObject(String ID,String name,String URL){
        this.UserID = ID;
        this.name = name;
        this.profileImageURL = URL;
    }
    public String getUserID(){
        return UserID;
    }
    public void setUserID(String ID){
        this.UserID = ID;
    }

    public String getName(){
        return name;
    }
    public void setName(String ID){
        this.name = ID;
    }

    public String getImageURL (){
        return profileImageURL;
    }
    public void setImageURL(String ID){
        this.profileImageURL = ID;
    }
}
