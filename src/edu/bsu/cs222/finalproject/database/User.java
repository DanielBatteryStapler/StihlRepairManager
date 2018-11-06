package edu.bsu.cs222.finalproject.database;

//represents one row in the "User" database table
public class User {
    public User(){
        id = -1;
        name = "";
        address = "";
        phoneNumber = "";
    }

    public User(User cpy){
        id = cpy.id;
        name = cpy.name;
        address = cpy.address;
        phoneNumber = cpy.phoneNumber;
    }

    public long id;
    public String name;
    public String address;
    public String phoneNumber;
}
