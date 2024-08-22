package com.example.mightworthit.Chat;

public class ChatObject {
    private String message;
    private  Boolean isCurrentUser;
    public ChatObject(String message,  Boolean isCurrentUser){
        this.message = message;
        this.isCurrentUser = isCurrentUser;
    }
    public String getMessage(){
        return message;
    }
    public void setMessage(String s){
        this.message = s;
    }

    public Boolean checkIsCurrentUser(){
        return isCurrentUser;
    }
    public void setIsCurrentUser(boolean a){
        this.isCurrentUser = a;
    }
}
