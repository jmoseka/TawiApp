package com.mila.agrimo;

public class MessageModel {
    String message;
    String senderId;
    String receiverId;
    String time;
    String date;
    long timeStamp;
    String senderTime, receiverTime, senderSeen, receiverSeen;
    boolean isSeen;


    public MessageModel() {
    }


    public MessageModel(String message, String senderId, String receiverId, String time, String date) {
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.time = time;
        this.date = date;

    }


    public MessageModel(String message, String senderId, long timeStamp) {

        this.message = message;
        this.senderId = senderId;
        this.timeStamp = timeStamp;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public String getSenderTime() {
        return senderTime;
    }

    public void setSenderTime(String senderTime) {
        this.senderTime = senderTime;
    }

    public String getReceiverTime() {
        return receiverTime;
    }

    public void setReceiverTime(String receiverTime) {
        this.receiverTime = receiverTime;
    }

    public String getSenderSeen() {
        return senderSeen;
    }

    public void setSenderSeen(String senderSeen) {
        this.senderSeen = senderSeen;
    }

    public String getReceiverSeen() {
        return receiverSeen;
    }

    public void setReceiverSeen(String receiverSeen) {
        this.receiverSeen = receiverSeen;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
