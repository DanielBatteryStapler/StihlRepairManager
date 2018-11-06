package edu.bsu.cs222.finalproject.backend;

import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Config {
    public Config(){

    }

    public void initialize(URL configFile) throws Exception{
        File file = new File(configFile.toURI());
        String jsonData = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
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
