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

    public void updateUser(User user) {
        database.updateUser(user);
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

    public ArrayList<User> getUsersWithName(String name) {
        return database.searchUsersWithName(name);
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

        Repair latestRepair = database.getLatestRepair();
        if(latestRepair == null){
            repair.repairNumber = 100;
        }
        else{
            if(latestRepair.repairNumber == 999){
                repair.repairNumber = 100;
            }
            else{
                repair.repairNumber = latestRepair.repairNumber + 1;
            }
        }

        database.insertRepair(repair);
        return repair;
    }

    public void completeRepair(User user, Item item, Repair repair) {
        repair.dateCompleted = new Date(Calendar.getInstance().getTime().getTime());
        updateRepair(repair);
    }

    public Repair getRepairWithId(long repairId) {
        return database.getRepairWithId(repairId);
    }

    public ArrayList<Repair> getRepairsWithUser(long id) {
        return database.getRepairsWithUser(id);
    }

    public ArrayList<Repair> getRepairsOnItem(long itemId) {
        return database.getRepairsOnItem(itemId);
    }

    public void updateRepair(Repair repair) {
        database.updateRepair(repair);
    }

    public void deleteRepair(Repair repair) {
        database.dropRepair(repair.id);
    }

    public Repair getLatestRepair(){
        return database.getLatestRepair();
    }

    public ArrayList<Repair> getInProgressRepairs(){
        return database.getInProgressRepairs();
    }

/*  Purchase Methods
-------------------------------------------------------------------------------------------------
*/

    public Purchase makeNewPurchase(User existingPurchaser, Item existingItem){
        Purchase purchase = new Purchase();
        purchase.itemId = existingItem.id;
        purchase.purchaserId = existingPurchaser.id;
        purchase.date = new Date(Calendar.getInstance().getTime().getTime());//make it the current time for now
        database.insertPurchase(purchase);
        return purchase;
    }

    public ArrayList<Purchase> getPurchasesWithPurchaser(long purchaser) {
        return database.getPurchasesWithPurchaser(purchaser);
    }

    public void deletePurchase(Purchase purchase) {
        database.dropPurchase(purchase.purchaserId);
    }

    public Purchase getPurchaseWithId(long purchaseId) {
        return database.getPurchaseWithId(purchaseId);
    }

    public Purchase getPurchaseOnItem(long itemId) {
        return database.getPurchaseOnItem(itemId);
    }

/*  Employee Methods
-------------------------------------------------------------------------------------------------
*/

    public Employee getEmployeeWithNumber(String employeeNumber) {
        return database.getEmployeeWithNumber(employeeNumber);
    }

    public Employee getEmployeeWithId(long employeeId) {
        return database.getEmployeeWithId(employeeId);
    }

    public void insertEmployee(Employee employee) {
        database.insertEmployee(employee);
    }

    public void deleteEmployee(Employee employee) {
        database.dropEmployee(employee.id);
    }

    public ArrayList<Employee> getAllEmployees() {
        return database.getAllEmployees();
    }

/*  RepairPart Methods
----------------------------------
*/

    public void insertRepairPart(RepairPart newRepairPart){
        database.insertRepairPart(newRepairPart);
    }

    public void updateRepairPart(RepairPart repairPart){
        database.updateRepairPart(repairPart);
    }

    public void dropRepairPart(long repairPartId){
        database.dropRepairPart(repairPartId);
    }

    public ArrayList<RepairPart> getRepairPartsOnRepair(long repairId){
        return database.getRepairPartsOnRepair(repairId);
    }

    public ArrayList<RepairPart> getRepairPartsInQueue(){
        return database.getRepairPartsInQueue();
    }

}
