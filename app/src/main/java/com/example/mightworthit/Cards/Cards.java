package com.example.mightworthit.Cards;

public class Cards {
    private String UserID;
    private String name;
    private String profileImageURL;

    public Cards(String ID,String name, String URL){
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
    public void setName(String name){
        this.name = name;
    }

    public String getProfileImageURL(){
        return profileImageURL;
    }
    public void setProfileImageURL(String URL){
        this.profileImageURL = URL;
    }
}
