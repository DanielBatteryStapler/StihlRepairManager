package edu.bsu.cs222.finalproject.backend;

import edu.bsu.cs222.finalproject.database.Repair;
import org.junit.Test;

public class printerTest {
    @Test
   public void printTest()
    {
        Repair repair = new Repair();
        repair.itemId = 123456;
        Print.printRepair(repair);
    }

}
