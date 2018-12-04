package edu.bsu.cs222.finalproject.database;

//represents one row in the "RepairPart" database table
public class RepairPart {
    public RepairPart(){
        id = -1;
        repairId = -1;
        name = "";
        price = 0;
        quantity = 0;
        needToBuy = true;
    }

    public RepairPart(RepairPart cpy){
        id = cpy.id;
        name = cpy.name;
        price = cpy.price;
        quantity = cpy.quantity;
        needToBuy = cpy.needToBuy;
    }

    public long id;
    public long repairId;
    public String name;
    public int price;//price is in cents
    public int quantity;
    public boolean needToBuy;
}
