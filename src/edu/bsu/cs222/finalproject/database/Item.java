package edu.bsu.cs222.finalproject.database;

//represents one row in the "Purchase" database table
public class Item {
    public Item(){
        id = -1;
        ownerId = -1;
        modelNumber = "";
        serialNumber = "";
    }

    public Item(Item cpy){
        id = cpy.id;
        ownerId = cpy.ownerId;
        modelNumber = cpy.modelNumber;
        serialNumber = cpy.serialNumber;
    }

    public long id;
    public long ownerId;
    public String modelNumber;
    public String serialNumber;
}
