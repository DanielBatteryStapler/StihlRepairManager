package edu.bsu.cs222.finalproject.backend;

import edu.bsu.cs222.finalproject.database.*;

import java.lang.reflect.Field;
import java.lang.Class;

import org.junit.*;

public class TestWorkingLayer {
    private User initExampleUser() {
        User user = new User();

        user.id = 1234;
        user.name = "John Doe";
        user.address = "123 Cherry Road";
        user.phoneNumber = "123-123-1234";

        return user;
    }

    private Item initExampleItem() {
        Item item = new Item();

        item.id = 1234;
        item.modelNumber = "1234567890";
        item.serialNumber = item.modelNumber;

        return item;
    }

    @Test
    public void testInit() {
        try {
            TemporaryDatabase temp = TemporaryDatabase.createInstance();

            WorkingLayer workingLayer = new WorkingLayer();
            workingLayer.initialize(temp);
    
            Class<WorkingLayer> workingLayerClass = WorkingLayer.class;
            Field field = null;

            field = workingLayerClass.getDeclaredField("database");
            field.setAccessible(true);

            Assert.assertEquals(temp, field.get(workingLayer));
        } catch (NoSuchFieldException e) {
            Assert.fail();
        } catch (IllegalAccessException e) {
            Assert.fail();
        }
    }

    @Test
    public void testMakeNewPurchase() {
        User user = initExampleUser();
        Item item = initExampleItem();

        WorkingLayer workingLayer = new WorkingLayer();
        TemporaryDatabase temp = TemporaryDatabase.createInstance();

        workingLayer.initialize(temp);

        Purchase outOfDatabasePurchase = workingLayer.makeNewPurchase(user, item);

        Assert.assertEquals(outOfDatabasePurchase.id, temp.getPurchaseWithId(outOfDatabasePurchase.id).id);
    }

    @Test
    public void testMakeNewRepair() {
        User user = initExampleUser();
        Item item = initExampleItem();

        WorkingLayer workingLayer = new WorkingLayer();
        TemporaryDatabase temp = TemporaryDatabase.createInstance();

        workingLayer.initialize(temp);

        Repair outOfDatabaseRepair = workingLayer.makeNewRepair(user, item);

        Assert.assertEquals(outOfDatabaseRepair.id, temp.getRepairWithId(outOfDatabaseRepair.id).id);
    }
}