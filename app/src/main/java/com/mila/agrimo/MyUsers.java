package com.mila.agrimo;

public class MyUsers {
    String id, name, email, phoneNo, password, country, location, dateOfReg, timeOfReg,image;



    public MyUsers(String id, String name, String email, String phoneNo, String password,
                    String image,String country, String location, String dateOfReg
    ,String timeOfReg) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.password = password;
        this.country = country;
        this.location = location;
        this.dateOfReg = dateOfReg;
        this.timeOfReg = timeOfReg;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimeOfReg() {
        return timeOfReg;
    }

    public void setTimeOfReg(String timeOfReg) {
        this.timeOfReg = timeOfReg;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateOfReg() {
        return dateOfReg;
    }

    public void setDateOfReg(String dateOfReg) {
        this.dateOfReg = dateOfReg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
