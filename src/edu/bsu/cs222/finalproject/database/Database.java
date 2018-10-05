package edu.bsu.cs222.finalproject.database;

import java.util.ArrayList;

public interface Database{
    boolean isUsable();

    void insertUser(User newUser);//sets the "id" of the user to match the internal database id
    void updateUser(User user);
    void dropUser(long userId);
    User getUserWithId(long userId);
    ArrayList<User> searchUsersWithName(String name);

    void insertItem(Item newItem);
    void updateItem(Item item);
    void dropItem(long itemId);
    Item getItemWithId(long itemId);
    ArrayList<Item> searchItemsWithSerial(String serialNumber);

    void insertPurchase(Purchase newPurchase);
    void updatePurchase(Purchase purchase);
    void dropPurchase(long purchaseId);
    Purchase getPurchaseWithId(long purchaseId);
    ArrayList<Purchase> getPurchasesWithPurchaser(long purchaserId);
}