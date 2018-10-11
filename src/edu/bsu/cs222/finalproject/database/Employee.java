package edu.bsu.cs222.finalproject.database;

//represents one row in the "Purchase" database table
public class Employee {
    public Employee(){
        id = -1;
        name = "";
        number = "";
    }

    public Employee(Employee cpy){
        id = cpy.id;
        name = cpy.name;
        number = cpy.number;
    }

    public long id;
    public String name;
    public String number;
}
