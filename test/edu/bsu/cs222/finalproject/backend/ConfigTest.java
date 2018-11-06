package edu.bsu.cs222.finalproject.backend;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class ConfigTest {

    @Test
    public void testLoadConfig() throws Exception{
        Config config = new Config();
        config.initialize(new File("test/resources/testConfig.json").toPath().toUri().toURL());
        Assert.assertEquals("exampleAddress", config.getDatabaseAddress());
        Assert.assertEquals("exampleUsername", config.getDatabaseUsername());
        Assert.assertEquals("examplePassword", config.getDatabasePassword());
        Assert.assertEquals("exampleName", config.getDatabaseName());
    }
}