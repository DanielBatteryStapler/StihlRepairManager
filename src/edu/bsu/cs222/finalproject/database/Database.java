package edu.bsu.cs222.finalproject.database;

import java.util.ArrayList;

public interface Database{
    boolean isUsable();

    void insertUser(User newUser);//sets the "id" of the user to match the internal database id
    void dropUser(long userId);
    User getUserWithId(long userId);
    ArrayList<User> searchUsersWithName(String name);

    void insertItem(Item newItem);
    void dropItem(long itemId);
    Item getItemWithId(long itemId);

    void insertPurchase(Purchase newPurchase);
    void dropPurchase(long purchaseId);
    Purchase getPurchaseWithId(long purchaseId);
    ArrayList<Purchase> getPurchasesWithPurchaser(long purchaserId);
}