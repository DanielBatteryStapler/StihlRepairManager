package edu.bsu.cs222.finalproject.database;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;
import java.util.Calendar;

public class TestWorkingLayer {
    @Test
    public void testNewPurchaseInDatabase() {
        Database database = TemporaryDatabase.createInstance();
        User userA = new User();
        userA.id = 1;
        Item itemA = new Item();
        itemA.id = 2;
        Date date = new Date(Calendar.getInstance().getTime().getTime());

        Purchase purchase = new Purchase();
        purchase.itemId = itemA.id;
        purchase.purchaserId = userA.id;
        purchase.date = date;
        database.insertPurchase(purchase);

        Assert.assertEquals(purchase.itemId, itemA.id);
        Assert.assertEquals(purchase.purchaserId, userA.id);
        Assert.assertEquals(purchase.date.toLocalDate().toString(), date.toLocalDate().toString());


    }
}
