package edu.bsu.cs222.finalproject.backend;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;

public class Config {
    public Config(){

    }

    public void initialize(InputStream configFile) throws Exception{
        // File file = new File(configFile.toURI());
        StringWriter writer = new StringWriter();
        IOUtils.copy(configFile, writer, "UTF-8");
        String jsonData = writer.toString();
        JSONObject pageJson = new JSONObject(jsonData);

        databaseAddress = pageJson.getString("databaseAddress");
        databaseUsername = pageJson.getString("databaseUsername");
        databasePassword = pageJson.getString("databasePassword");
        databaseName = pageJson.getString("databaseName");
    }


    private String databaseAddress;
    private String databaseUsername;
    private String databasePassword;
    private String databaseName;

    public String getDatabaseAddress() {
        return databaseAddress;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public String getDatabaseName() {
        return databaseName;
    }
}
