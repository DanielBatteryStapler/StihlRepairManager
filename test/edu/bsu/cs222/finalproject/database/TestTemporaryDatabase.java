package edu.bsu.cs222.finalproject.database;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class TestTemporaryDatabase {
    @Test
    public void testCreateTemporaryDatabase() {
        Database database = TemporaryDatabase.createInstance();
        Assert.assertTrue(database.isUsable());
    }

    @Test
    public void testUserTableInTemporaryDatabase() {
        Database database = TemporaryDatabase.createInstance();
        User userA = new User();
        userA.name = "UserA";
        userA.phoneNumber = "5555555555";
        User userB = new User();
        userB.name = "UserB";
        userB.phoneNumber = "6666666666";
        database.insertUser(userA);
        database.insertUser(userB);
        User userC = database.getUserWithId(userA.id);
        User userD = database.getUserWithId(userB.id);
        Assert.assertEquals("UserA", userC.name);
        Assert.assertEquals("UserB", userD.name);
        User userG = database.getUserWithPhoneNumber("5555555555");
        User userF = database.getUserWithPhoneNumber("6666666666");
        Assert.assertEquals("UserA", userG.name);
        Assert.assertEquals("UserB", userF.name);
        userC.name = "UserNew";
        database.updateUser(userC);
        User userE = database.getUserWithId(userC.id);
        Assert.assertEquals("UserNew", userE.name);
        database.dropUser(userE.id);
        Assert.assertNull(database.getUserWithId(userE.id));
    }

    @Test
    public void testItemTableInTemporaryDatabase() {
        Database database = TemporaryDatabase.createInstance();
        Item itemA = new Item();
        itemA.modelNumber = "ItemA";
        Item itemB = new Item();
        itemB.modelNumber = "ItemB";
        database.insertItem(itemA);
        database.insertItem(itemB);
        Item itemC = database.getItemWithId(itemA.id);
        Item itemD = database.getItemWithId(itemB.id);
        Assert.assertEquals("ItemA", itemC.modelNumber);
        Assert.assertEquals("ItemB", itemD.modelNumber);
        itemC.modelNumber = "ItemNew";
        database.updateItem(itemC);
        Item itemE = database.getItemWithId(itemC.id);
        Assert.assertEquals("ItemNew", itemE.modelNumber);
        database.dropItem(itemE.id);
        Assert.assertNull(database.getItemWithId(itemE.id));
    }

    @Test
    public void testEmployeeTableInTemporaryDatabase() {
        Database database = TemporaryDatabase.createInstance();
        Employee employeeA = new Employee();
        employeeA.name = "EmployeeA";
        employeeA.number = "1";
        Employee employeeB = new Employee();
        employeeB.name = "EmployeeB";
        employeeB.number = "2";
        database.insertEmployee(employeeA);
        database.insertEmployee(employeeB);
        Employee employeeC = database.getEmployeeWithId(employeeA.id);
        Employee employeeD = database.getEmployeeWithId(employeeB.id);
        Assert.assertEquals("EmployeeA", employeeC.name);
        Assert.assertEquals("EmployeeB", employeeD.name);
        Employee employeeF = database.getEmployeeWithNumber("1");
        Employee employeeG = database.getEmployeeWithNumber("2");
        Assert.assertEquals("EmployeeA", employeeF.name);
        Assert.assertEquals("EmployeeB", employeeG.name);
        database.dropEmployee(employeeC.id);
        Assert.assertNull(database.getEmployeeWithId(employeeC.id));
    }

    @Test
    public void testPurchaseTableInTemporaryDatabase() {
        Database database = TemporaryDatabase.createInstance();
        Purchase purchaseA = new Purchase();
        purchaseA.itemId = 24;//just a random number
        database.insertPurchase(purchaseA);
        Purchase purchaseB = database.getPurchaseWithId(purchaseA.id);
        Assert.assertEquals(24, purchaseB.itemId);
        database.dropPurchase(purchaseB.id);
        Assert.assertNull(database.getPurchaseWithId(purchaseB.id));
    }

    @Test
    public void testRepairTableInTemporaryDatabase(){
        Database database = TemporaryDatabase.createInstance();
        Assert.assertNull(database.getLatestRepair());

        Repair repairA = new Repair();
        repairA.dateStarted = new Date(Calendar.getInstance().getTime().getTime());
        repairA.description = "RepairA";
        Repair repairB = new Repair();
        repairB.dateStarted = new Date(Calendar.getInstance().getTime().getTime() + 100);//make this one happen after repairA
        repairB.dateCompleted = new Date(Calendar.getInstance().getTime().getTime() + 200);
        repairB.description = "RepairB";
        database.insertRepair(repairA);
        database.insertRepair(repairB);
        Repair repairC = database.getRepairWithId(repairA.id);
        Repair repairD = database.getRepairWithId(repairB.id);
        Assert.assertEquals("RepairA", repairC.description);
        Assert.assertEquals("RepairB", repairD.description);
        Repair repairF = database.getInProgressRepairs().get(0);
        Assert.assertEquals("RepairA", repairF.description);
        Assert.assertEquals("RepairB", database.getLatestRepair().description);
        repairF.description = "RepairAUpdated";
        database.updateRepair(repairF);
        Repair repairG = database.getRepairWithId(repairF.id);
        Assert.assertEquals("RepairAUpdated", repairG.description);
        database.dropRepair(repairC.id);
        Assert.assertNull(database.getRepairWithId(repairC.id));
    }

    @Test
    public void testRepairPartTableInTemporaryDatabase(){
        Database database = TemporaryDatabase.createInstance();
        Assert.assertNull(database.getLatestRepair());

        Repair repair = new Repair();
        repair.dateStarted = new Date(Calendar.getInstance().getTime().getTime());
        database.insertRepair(repair);

        RepairPart repairPartA = new RepairPart();
        repairPartA.name = "RepairPartA";
        repairPartA.needToBuy = true;
        RepairPart repairPartB = new RepairPart();
        repairPartB.name = "RepairPartB";
        repairPartB.needToBuy = false;
        repairPartB.repairId = repair.id;
        database.insertRepairPart(repairPartA);
        database.insertRepairPart(repairPartB);

        Assert.assertEquals(1, database.getRepairPartsInQueue().size());

        RepairPart repairPartC = database.getRepairPartsInQueue().get(0);
        Assert.assertEquals("RepairPartA", repairPartC.name);
        repairPartC.name = "RepairPartAUpdated";
        database.updateRepairPart(repairPartC);
        RepairPart repairPartD = database.getRepairPartsInQueue().get(0);
        Assert.assertEquals("RepairPartAUpdated", repairPartD.name);

        RepairPart repairPartE = database.getRepairPartsOnRepair(repair.id).get(0);
        Assert.assertEquals("RepairPartB", repairPartE.name);

        database.dropRepairPart(repairPartA.id);
        Assert.assertEquals(0, database.getRepairPartsInQueue().size());
    }

    @Test
    public void testSearchNameTable() {
        Database database = TemporaryDatabase.createInstance();
        User userA = new User();
        userA.name = "John Smith";
        database.insertUser(userA);
        User userB = new User();
        userB.name = "Johnathan White";
        database.insertUser(userB);
        User userC = new User();
        userC.name = "Mary Smith";
        database.insertUser(userC);

        {
            ArrayList<String> users = new ArrayList<>();
            users.add(userA.name);
            users.add(userB.name);

            ArrayList<String> gotUsers = new ArrayList<>();
            {
                ArrayList<User> gotUsers_ = database.searchUsersWithName("john");
                for(User i : gotUsers_){
                    gotUsers.add(i.name);
                }
            }

            ArrayList<String> a = new ArrayList<>(users);
            ArrayList<String> b = new ArrayList<>(gotUsers);

            a.removeAll(gotUsers);
            b.removeAll(users);

            Assert.assertTrue(a.isEmpty());
            Assert.assertTrue(b.isEmpty());
        }
        {
            ArrayList<String> users = new ArrayList<>();
            users.add(userA.name);
            users.add(userC.name);

            ArrayList<String> gotUsers = new ArrayList<>();
            {
                ArrayList<User> gotUsers_ = database.searchUsersWithName("smith");
                for(User i : gotUsers_){
                    gotUsers.add(i.name);
                }
            }

            ArrayList<String> a = new ArrayList<>(users);
            ArrayList<String> b = new ArrayList<>(gotUsers);

            a.removeAll(gotUsers);
            b.removeAll(users);

            Assert.assertTrue(a.isEmpty());
            Assert.assertTrue(b.isEmpty());
        }
    }

    @Test
    public void testSearchItemTableWithSerial() {
        Database database = TemporaryDatabase.createInstance();
        Item itemA = new Item();
        itemA.serialNumber = "John Smith";//serial numbers are human names, it makes it easy to see what's going on
        database.insertItem(itemA);
        Item itemB = new Item();
        itemB.serialNumber = "Johnathan White";
        database.insertItem(itemB);
        Item itemC = new Item();
        itemC.serialNumber = "Mary Smith";
        database.insertItem(itemC);

        {
            ArrayList<String> items = new ArrayList<>();
            items.add(itemA.serialNumber);
            items.add(itemB.serialNumber);

            ArrayList<String> gotItems = new ArrayList<>();
            {
                ArrayList<Item> gotItems_ = database.searchItemsWithSerial("john");
                for(Item i : gotItems_){
                    gotItems.add(i.serialNumber);
                }
            }

            ArrayList<String> a = new ArrayList<>(items);
            ArrayList<String> b = new ArrayList<>(gotItems);

            a.removeAll(gotItems);
            b.removeAll(items);

            Assert.assertTrue(a.isEmpty());
            Assert.assertTrue(b.isEmpty());
        }
        {
            ArrayList<String> items = new ArrayList<>();
            items.add(itemA.serialNumber);
            items.add(itemC.serialNumber);

            ArrayList<String> gotItems = new ArrayList<>();
            {
                ArrayList<Item> gotItems_ = database.searchItemsWithSerial("smith");
                for(Item i : gotItems_){
                    gotItems.add(i.serialNumber);
                }
            }

            ArrayList<String> a = new ArrayList<>(items);
            ArrayList<String> b = new ArrayList<>(gotItems);

            a.removeAll(gotItems);
            b.removeAll(items);

            Assert.assertTrue(a.isEmpty());
            Assert.assertTrue(b.isEmpty());
        }
    }

    @Test
    public void testSearchPurchaseTable() {
        Database database = TemporaryDatabase.createInstance();
        Purchase purchaseA = new Purchase();
        purchaseA.purchaserId = 1;
        database.insertPurchase(purchaseA);
        Purchase purchaseB = new Purchase();
        purchaseB.purchaserId = 2;
        database.insertPurchase(purchaseB);
        Purchase purchaseC = new Purchase();
        purchaseC.purchaserId = 1;
        database.insertPurchase(purchaseC);

        {
            ArrayList<String> purchases = new ArrayList<>();
            purchases.add(String.valueOf(purchaseA.purchaserId));
            purchases.add(String.valueOf(purchaseC.purchaserId));

            ArrayList<String> gotPurchases = new ArrayList<>();
            {
                ArrayList<Purchase> gotPurchases_ = database.getPurchasesWithPurchaser(1);
                for(Purchase i : gotPurchases_){
                    gotPurchases.add(String.valueOf(i.purchaserId));
                }
            }

            ArrayList<String> a = new ArrayList<>(purchases);
            ArrayList<String> b = new ArrayList<>(gotPurchases);

            a.removeAll(gotPurchases);
            b.removeAll(purchases);

            Assert.assertTrue(a.isEmpty());
            Assert.assertTrue(b.isEmpty());
        }
        {
            ArrayList<String> purchases = new ArrayList<>();
            purchases.add(String.valueOf(purchaseB.purchaserId));

            ArrayList<String> gotPurchases = new ArrayList<>();
            {
                ArrayList<Purchase> gotPurchases_ = database.getPurchasesWithPurchaser(2);
                for(Purchase i : gotPurchases_){
                    gotPurchases.add(String.valueOf(i.purchaserId));
                }
            }

            ArrayList<String> a = new ArrayList<>(purchases);
            ArrayList<String> b = new ArrayList<>(gotPurchases);

            a.removeAll(gotPurchases);
            b.removeAll(purchases);

            Assert.assertTrue(a.isEmpty());
            Assert.assertTrue(b.isEmpty());
        }
    }
}
