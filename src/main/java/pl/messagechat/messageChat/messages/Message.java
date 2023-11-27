package pl.messagechat.messageChat.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Message implements Serializable {
    private String sender;
    private String receiver;
    private MessageType type;
    private String messageBody;
    private Status status;
    private String picture;

    private int count;
    private ArrayList<User> list;
    private ArrayList<User> users;



    public byte[] getVoiceMsg() {
        return voiceMsg;
    }
    private byte[] voiceMsg;


    public String getPicture() {
        return picture;
    }

    public Message() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessageBody() {

        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public ArrayList<User> getUserlist() {
        return list;
    }

    public void setUserlist(HashMap<String, User> userList) {
        this.list = new ArrayList<>(userList.values());
    }

    public void setOnlineCount(int count){
        this.count = count;
    }

    public int getOnlineCount(){
        return this.count;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }


    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setVoiceMsg(byte[] voiceMsg) {
        this.voiceMsg = voiceMsg;
    }


}
