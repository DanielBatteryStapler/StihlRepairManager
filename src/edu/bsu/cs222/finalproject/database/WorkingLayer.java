package edu.bsu.cs222.finalproject.database;

import java.sql.Date;
import java.util.Calendar;

public class WorkingLayer {
    private Database database;

    public WorkingLayer(){
        database = null;
    }

    public void initialize(Database workingDatabase){
        if(workingDatabase.isUsable() == false){
            throw new RuntimeException("Attempted to initialize a WorkingLayer with an unusable database");
        }
        database = workingDatabase;
    }

    public User getUserWithPhoneNumber(String phoneNumber){
        return database.getUserWithPhoneNumber(phoneNumber);
    }

    public void addNewUser(User newUser){
        database.insertUser(newUser);
    }

    public Purchase addNewPurchase(User existingPurchaser, Item newItem){
        database.insertItem(newItem);
        Purchase purchase = new Purchase();
        purchase.itemId = newItem.id;
        purchase.purchaserId = existingPurchaser.id;
        purchase.date = new Date(Calendar.getInstance().getTime().getTime());//make it the current time for now, this will be added to the ui later
        purchase.notes = "";//right now there are no notes, this will be added to ui later
        database.insertPurchase(purchase);
        return purchase;
    }
}
