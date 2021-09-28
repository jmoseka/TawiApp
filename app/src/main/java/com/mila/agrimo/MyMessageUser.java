package com.mila.agrimo;

public class MyMessageUser {
    private String messageUsername,lastMessage, lastTime, Id, image;


    public MyMessageUser(String messageUser, String lastMessage, String lastTime, String Id,String image) {
        this.messageUsername = messageUser;
        this.lastMessage = lastMessage;
        this.lastTime = lastTime;
        this.Id = Id;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public MyMessageUser(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getMessageUsername() {
        return messageUsername;
    }

    public void setMessageUsername(String messageUser) {
        this.messageUsername = messageUser;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }
}
