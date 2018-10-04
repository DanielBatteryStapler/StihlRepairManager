package edu.bsu.cs222.finalproject.Database;

import org.junit.Assert;
import org.junit.Test;

import edu.bsu.cs222.finalproject.database.Database;
import edu.bsu.cs222.finalproject.database.TemporaryDatabase;

public class TestTemporaryDatabase {
    @Test
    public void testCreateTemporaryDatabase() {
        Database database = TemporaryDatabase.createInstance();
        Assert.assertTrue(database.isUsable());
    }
}
