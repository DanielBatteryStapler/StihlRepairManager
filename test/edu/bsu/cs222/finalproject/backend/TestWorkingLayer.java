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

    private RepairPart initExampleRepairPart() {
        RepairPart part = new RepairPart();

        part.id = 1234;
        part.repairId = 5678;
        part.name = "name";
        part.price = 10;
        part.quantity = 5;
        part.needToBuy = true;

        return part;
    }

    private Employee initExampleEmployee() {
        Employee employee = new Employee();

        employee.id = 1234;
        employee.name = "John Smith";
        employee.number = "123";

        return employee;
    }

    @Test
    public void testInit() {
        try {
            TemporaryDatabase temp = TemporaryDatabase.createInstance();

            WorkingLayer workingLayer = new WorkingLayer();
            workingLayer.initialize(temp);
    
            Class<WorkingLayer> workingLayerClass = WorkingLayer.class;
            Field field = workingLayerClass.getDeclaredField("database");

            field.setAccessible(true);

            Assert.assertEquals(temp, field.get(workingLayer));
        } catch (NoSuchFieldException | IllegalAccessException e) {
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


    @Test
    public void testInsertAndDrop() {
        User user = initExampleUser();
        Item item = initExampleItem();
        RepairPart part = initExampleRepairPart();
        Employee employee = initExampleEmployee();

        WorkingLayer workingLayer = new WorkingLayer();
        TemporaryDatabase temp = TemporaryDatabase.createInstance();

        workingLayer.initialize(temp);

        workingLayer.insertItem(item);
        workingLayer.insertUser(user);
        workingLayer.insertRepairPart(part);
        workingLayer.insertEmployee(employee);

        Assert.assertEquals(item.id, workingLayer.getItemWithId(item.id).id);
        Assert.assertEquals(user.id, workingLayer.getUserWithId(user.id).id);
        Assert.assertEquals(part.id, workingLayer.getRepairPartsInQueue().get(0).id);
        Assert.assertEquals(employee.id, workingLayer.getEmployeeWithId(employee.id).id);

        Assert.assertEquals(1, workingLayer.getRepairPartsInQueue().size());
        Assert.assertEquals(1, workingLayer.getAllEmployees().size());

        workingLayer.dropRepairPart(part.id);
        workingLayer.deleteEmployee(employee);

        Assert.assertEquals(0, workingLayer.getRepairPartsInQueue().size());
        Assert.assertEquals(0, workingLayer.getAllEmployees().size());
        //TODO: Add drop tests
    }
}