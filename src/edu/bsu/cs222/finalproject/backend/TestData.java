package edu.bsu.cs222.finalproject.backend;

import edu.bsu.cs222.finalproject.Main;
import edu.bsu.cs222.finalproject.database.*;

import java.sql.Date;
import java.util.Calendar;

public class TestData {

    //this entire class and this method should not be used in production, it is only used when loading a TemporaryDatabase, and it's all just test data
    //this is for testing purposes only, production should not run any of this at all
    public static void insertTestDataIntoDatabase() {
        Main main = Main.getInstance();

        {
            User johnSmith = new User();
            {
                johnSmith.name = "John Smith";
                johnSmith.phoneNumber = PhoneNumber.toNormalized("555-555-5555");
                johnSmith.address = "555 Fifth Avenue\nMuncie\nIndiana";
                main.workingLayer.insertUser(johnSmith);
            }
            Item chainSaw = new Item();
            {
                chainSaw.serialNumber = "CS3866";
                chainSaw.modelNumber = "6A Chain Saw";
                main.workingLayer.insertItem(chainSaw);
                main.workingLayer.makeNewPurchase(johnSmith, chainSaw);
            }
            {
                Repair repair = main.workingLayer.makeNewRepair(johnSmith, chainSaw);
                repair.description = "Doesn't Start, Leaks Fuel";
                main.workingLayer.updateRepair(repair);

                {
                    RepairPart repairPart = new RepairPart();
                    repairPart.needToBuy = true;
                    repairPart.repairId = repair.id;
                    repairPart.price = Currency.toNormalized("$1.10");
                    repairPart.quantity = 5;
                    repairPart.name = "2.5\" Screw";
                    main.workingLayer.insertRepairPart(repairPart);
                }
                {
                    RepairPart repairPart = new RepairPart();
                    repairPart.needToBuy = true;
                    repairPart.repairId = repair.id;
                    repairPart.price = Currency.toNormalized("$25.50");
                    repairPart.quantity = 5;
                    repairPart.name = "50\" Chain";
                    main.workingLayer.insertRepairPart(repairPart);
                }
            }
        }
        {
            User maryBeth = new User();
            {
                maryBeth.name = "Mary Beth";
                maryBeth.phoneNumber = PhoneNumber.toNormalized("444-444-4444");
                maryBeth.address = "250 Weller Drive\nBrazil\nIndiana";
                main.workingLayer.insertUser(maryBeth);
            }
            {
                Item lawnMower = new Item();
                lawnMower.serialNumber = "LM8745";
                lawnMower.modelNumber = "7E Lawn Mower";
                main.workingLayer.insertItem(lawnMower);
                main.workingLayer.makeNewPurchase(maryBeth, lawnMower);
            }
            Item electricDrill = new Item();
            {
                electricDrill.serialNumber = "ED9854";
                electricDrill.modelNumber = "5K Electric Drill";
                main.workingLayer.insertItem(electricDrill);
                main.workingLayer.makeNewPurchase(maryBeth, electricDrill);
            }
            {
                Repair repair = main.workingLayer.makeNewRepair(maryBeth, electricDrill);
                repair.description = "Low Torque";
                repair.descriptionCompleted = "Fixed by replacing motor controller.";
                repair.dateCompleted = new Date(Calendar.getInstance().getTime().getTime());//add the time completed field to mark it as finished
                main.workingLayer.updateRepair(repair);

                {
                    RepairPart repairPart = new RepairPart();
                    repairPart.needToBuy = false;
                    repairPart.repairId = repair.id;
                    repairPart.price = Currency.toNormalized("$20.89");
                    repairPart.quantity = 1;
                    repairPart.name = "20 Watt Motor";
                    main.workingLayer.insertRepairPart(repairPart);
                }
            }
            {
                Repair repair = main.workingLayer.makeNewRepair(maryBeth, electricDrill);
                repair.description = "Motor stops working after extended use";
                main.workingLayer.updateRepair(repair);
            }
        }
        {
            Employee employee = new Employee();
            employee.name = "Eugene Smithson";
            employee.number = "88";
            main.workingLayer.insertEmployee(employee);
        }
    }
}
