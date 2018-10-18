package edu.bsu.cs222.finalproject.database;

import java.sql.Date;

//represents one row in the "Purchase" database table
public class Purchase {
    public Purchase(){
        id = -1;
        purchaserId = -1;
        itemId = -1;
        date = null;
        notes = "";
    }

    public Purchase(Purchase cpy){
        id = cpy.id;
        purchaserId = cpy.purchaserId;
        itemId = cpy.itemId;
        date = cpy.date;
        notes = cpy.notes;
    }

    public long id;
    long purchaserId;
    long itemId;
    Date date;
    String notes;
}
