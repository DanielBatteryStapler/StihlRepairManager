package edu.bsu.cs222.finalproject.database;

import java.util.ArrayList;

public interface Database{
    boolean isUsable();

    String connectToServer(String address, String username, String password, String database);//returns a string with an error for the database, null on no error

    void insertEmployee(Employee newEmployee);
    void updateEmployee(Employee employee);
    void dropEmployee(long employeeId);
    Employee getEmployeeWithId(long employeeId);
    Employee getEmployeeWithNumber(String employeeNumber);

    void insertUser(User newUser);//sets the "id" of the user to match the internal database id
    void updateUser(User user);
    void dropUser(long userId);
    User getUserWithId(long userId);
    User getUserWithPhoneNumber(String phoneNumber);
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

    void insertRepair(Repair newPurchase);
    void updateRepair(Repair purchase);
    void dropRepair(long purchaseId);
    Repair getRepairWithId(long repairId);
    ArrayList<Repair> getRepairsOnItem(long itemId);
    ArrayList<Repair> getRepairsWithUser(long userId);
}