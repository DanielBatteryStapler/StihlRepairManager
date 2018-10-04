package edu.bsu.cs222.finalproject.Database;

import edu.bsu.cs222.finalproject.database.*;

import org.junit.Assert;
import org.junit.Test;

import javax.xml.crypto.Data;
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
            ArrayList<User> users = new ArrayList<>();
            users.add(userA);
            users.add(userB);

            ArrayList<User> gotUsers = database.searchUsersWithName("john");

            ArrayList<User> a = new ArrayList<>(users);
            ArrayList<User> b = new ArrayList<>(gotUsers);

            a.removeAll(gotUsers);
            b.removeAll(users);

            Assert.assertTrue(a.isEmpty());
            Assert.assertTrue(b.isEmpty());
        }
        {
            ArrayList<User> users = new ArrayList<>();
            users.add(userA);
            users.add(userC);

            ArrayList<User> gotUsers = database.searchUsersWithName("smith");

            ArrayList<User> a = new ArrayList<>(users);
            ArrayList<User> b = new ArrayList<>(gotUsers);

            a.removeAll(gotUsers);
            b.removeAll(users);

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
            ArrayList<Purchase> purchases = new ArrayList<>();
            purchases.add(purchaseA);
            purchases.add(purchaseC);

            ArrayList<Purchase> gotPurchases = database.getPurchasesWithPurchaser(1);

            ArrayList<Purchase> a = new ArrayList<>(purchases);
            ArrayList<Purchase> b = new ArrayList<>(gotPurchases);

            a.removeAll(gotPurchases);
            b.removeAll(purchases);

            Assert.assertTrue(a.isEmpty());
            Assert.assertTrue(b.isEmpty());
        }
        {
            ArrayList<Purchase> purchases = new ArrayList<>();
            purchases.add(purchaseB);

            ArrayList<Purchase> gotPurchases = database.getPurchasesWithPurchaser(2);

            ArrayList<Purchase> a = new ArrayList<>(purchases);
            ArrayList<Purchase> b = new ArrayList<>(gotPurchases);

            a.removeAll(gotPurchases);
            b.removeAll(purchases);

            Assert.assertTrue(a.isEmpty());
            Assert.assertTrue(b.isEmpty());
        }
    }
}
