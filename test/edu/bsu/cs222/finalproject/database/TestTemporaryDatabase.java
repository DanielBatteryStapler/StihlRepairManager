package edu.bsu.cs222.finalproject.database;

import edu.bsu.cs222.finalproject.database.*;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

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
        User userB = new User();
        userB.name = "UserB";
        database.insertUser(userA);
        database.insertUser(userB);
        User userC = database.getUserWithId(userA.id);
        User userD = database.getUserWithId(userB.id);
        Assert.assertEquals("UserA", userC.name);
        Assert.assertEquals("UserB", userD.name);
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
    public void testPurchaseTableInTemporaryDatabase() {
        Database database = TemporaryDatabase.createInstance();
        Purchase purchaseA = new Purchase();
        purchaseA.itemId = 24;//just a random number
        database.insertPurchase(purchaseA);
        Purchase purchaseB = database.getPurchaseWithId(purchaseA.id);
        Assert.assertEquals(24, purchaseB.itemId);
        purchaseB.itemId = 26;//a different random number
        database.updatePurchase(purchaseB);
        Purchase purchaseC = database.getPurchaseWithId(purchaseB.id);
        Assert.assertEquals(26, purchaseC.itemId);
        database.dropPurchase(purchaseC.id);
        Assert.assertNull(database.getPurchaseWithId(purchaseC.id));
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
