package com.nowcoder.model;

/**
 * Created by 12274 on 2018/9/3.
 */
public class testUser {
    private int id;
    private String name;
    private String password;
    private String salt;
    private String headUrl;

    public testUser() {
    }
    public testUser(String name) {
        this.name = name;
        this.password ="";
        this.salt = "";
        this.headUrl = "";
    }
    public testUser(String name, String pasword, String salt, String headUrl) {
        this.name = name;
        this.password = pasword;
        this.salt = salt;
        this.headUrl = headUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }
}
