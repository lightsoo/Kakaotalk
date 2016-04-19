package com.sample.lightsoo.kakaotalk.Data;

/**
 * Created by LG on 2016-04-18.
 */
public class User {

    public String id;
    public String type;
    public User(String id, String type){
        this.id = id;
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }


    //getter
    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }



}
