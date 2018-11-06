package edu.bsu.cs222.finalproject.backend;

import edu.bsu.cs222.finalproject.database.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class WorkingLayer {
    private Database database;

    public WorkingLayer(){
        database = null;
    }

    public void initialize(Database workingDatabase){
        if(!workingDatabase.isUsable()){
            throw new RuntimeException("Attempted to initialize a WorkingLayer with an unusable database");
        }
        database = workingDatabase;
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

    public void insertUser(User user) throws Exception{
        user.phoneNumber = PhoneNumber.toNormalized(user.phoneNumber);
        database.insertUser(user);
    }

    public User getUserWithPhoneNumber(String phoneNumber) throws Exception{
        return database.getUserWithPhoneNumber(PhoneNumber.toNormalized(phoneNumber));
    }


    public Employee getEmployeeWithNumber(String employeeNumber) {
        return database.getEmployeeWithNumber(employeeNumber);
    }

    public ArrayList<Purchase> getPurchasesWithPurchaser(long purchaser) {
        return database.getPurchasesWithPurchaser(purchaser);
    }

    public Item getItemWithId(long itemId) {
        return database.getItemWithId(itemId);
    }

    public User getUserWithId(long purchaserId) throws Exception{
        User user = database.getUserWithId(purchaserId);
        user.phoneNumber = PhoneNumber.toFormatted(user.phoneNumber);
        return user;
    }

    public void updateItem(Item item) {
        database.updateItem(item);
    }

    public void deletePurchase(Purchase purchase) {
        database.dropPurchase(purchase.purchaserId);
    }

    public void deleteItem(Item item) {
        database.dropItem(item.id);
    }
}
