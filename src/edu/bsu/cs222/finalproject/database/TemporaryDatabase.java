package edu.bsu.cs222.finalproject.database;

import java.util.ArrayList;

public class TemporaryDatabase implements Database {
    ArrayList<User> userTable;
    ArrayList<Item> itemTable;
    ArrayList<Purchase> purchaseTable;

    private TemporaryDatabase(){

    }

    public static TemporaryDatabase createInstance(){
        return new TemporaryDatabase();
    }

    public boolean isUsable(){
        return true;
        //there is no initialization on a TemporaryDatabase, it's all in memory, so it's always ready to be used
    }

    public void insertUser(User newUser){

    }

    public void dropUser(long userId){

    }

    public User getUserWithId(long userId){
        return null;
    }

    public ArrayList<User> searchUsersWithName(String name){
        return null;
    }


    public void insertItem(Item newItem){

    }

    public void dropItem(long itemId){

    }

    public Item getItemWithId(long itemId){
        return null;
    }

    public void insertPurchase(Purchase newPurchase){

    }

    public void dropPurchase(long purchaseId){

    }

    public Purchase getPurchaseWithId(long purchaseId){
        return null;
    }

    public ArrayList<Purchase> getPurchasesWithPurchaser(long purchaserId){
        return null;
    }
}
