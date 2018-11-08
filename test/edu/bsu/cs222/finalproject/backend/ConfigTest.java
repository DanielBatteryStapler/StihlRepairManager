package edu.bsu.cs222.finalproject.backend;

import org.junit.Assert;
import org.junit.Test;

public class ConfigTest {

    @Test
    public void testLoadConfig() throws Exception{
        Config config = new Config();
        config.initialize(getClass().getResource("/testConfig.json"));
        Assert.assertEquals("exampleAddress", config.getDatabaseAddress());
        Assert.assertEquals("exampleUsername", config.getDatabaseUsername());
        Assert.assertEquals("examplePassword", config.getDatabasePassword());
        Assert.assertEquals("exampleName", config.getDatabaseName());
    }
}