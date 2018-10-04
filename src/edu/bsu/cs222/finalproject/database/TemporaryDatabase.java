package edu.bsu.cs222.finalproject.database;

public class TemporaryDatabase implements Database {
    private TemporaryDatabase(){

    }

    public static TemporaryDatabase createInstance(){
        return new TemporaryDatabase();
    }

    public boolean isUsable(){
        return true;
        //there is no initialization on a TemporaryDatabase, it's all in memory, so it's always ready to be used
    }
}
