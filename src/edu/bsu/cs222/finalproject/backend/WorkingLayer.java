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

/*  User Methods
-------------------------------------------------------------------------------------------------
*/

    public void insertUser(User user){
        database.insertUser(user);
    }

    public void deleteUser(User user) {
        database.dropUser(user.id);
    }

    public User getUserWithPhoneNumber(String phoneNumber){
        return database.getUserWithPhoneNumber(phoneNumber);
    }

    public User getUserWithId(long purchaserId){
        return database.getUserWithId(purchaserId);
    }

/*  Item Methods
-------------------------------------------------------------------------------------------------
*/

    public Item getItemWithId(long itemId) {
        return database.getItemWithId(itemId);
    }

    public void updateItem(Item item) {
        database.updateItem(item);
    }

    public void insertItem(Item item) {
        database.insertItem(item);
    }

    public void deleteItem(Item item) {
        database.dropItem(item.id);
    }

    public ArrayList<Item> searchItemsWithSerial(String serialNumber) {
        return database.searchItemsWithSerial(serialNumber);
    }

/*  Repair Methods
-------------------------------------------------------------------------------------------------
*/

    public Repair makeNewRepair(User existingUser, Item existingItem){
        Repair repair = new Repair();
        repair.itemId = existingItem.id;
        repair.userId = existingUser.id;
        repair.dateStarted = new Date(Calendar.getInstance().getTime().getTime());//make it the current time for now, this will be added to the ui later
        database.insertRepair(repair);
        return repair;
    }

    public ArrayList<Repair> getRepairsWithUser(long id) {
        return database.getRepairsWithUser(id);
    }

    public void updateRepair(Repair repair) {
        database.updateRepair(repair);
    }

    public void deleteRepair(Repair repair) {
        database.dropRepair(repair.id);
    }

/*  Purchase Methods
-------------------------------------------------------------------------------------------------
*/

    public Purchase makeNewPurchase(User existingPurchaser, Item existingItem){
        Purchase purchase = new Purchase();
        purchase.itemId = existingItem.id;
        purchase.purchaserId = existingPurchaser.id;
        purchase.date = new Date(Calendar.getInstance().getTime().getTime());//make it the current time for now, this will be added to the ui later
        purchase.notes = "";//right now there are no notes, this will be added to ui later
        database.insertPurchase(purchase);
        return purchase;
    }

    public ArrayList<Purchase> getPurchasesWithPurchaser(long purchaser) {
        return database.getPurchasesWithPurchaser(purchaser);
    }

    public void deletePurchase(Purchase purchase) {
        database.dropPurchase(purchase.purchaserId);
    }

/*  Employee Methods
-------------------------------------------------------------------------------------------------
*/

    public Employee getEmployeeWithNumber(String employeeNumber) {
        return database.getEmployeeWithNumber(employeeNumber);
    }

}
