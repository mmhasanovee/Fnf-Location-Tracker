package xyz.mmhasanovee.fnflocationtracker.Model;

import java.util.HashMap;

public class User {

    private String uid,email,image;
    private HashMap<String,User> acceptList; //list of user friends

    public User() {
    }

    public User(String uid, String email) {
        this.uid = uid;
        this.email = email;

        //modified---------
        this.image=image;


        acceptList = new HashMap<>();
    }

    //modified---------

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //modified---------


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HashMap<String, User> getAcceptList() {
        return acceptList;
    }

    public void setAcceptList(HashMap<String, User> acceptList) {
        this.acceptList = acceptList;
    }
}
