package edu.bsu.cs222.finalproject.database;

public class WorkingLayer {
    private Database database;

    public WorkingLayer(){
        database = null;
    }

    public void initialize(Database workingDatabase){
        if(workingDatabase.isUsable() == false){
            throw new RuntimeException("Attempted to initialize a WorkingLayer with an unusable database");
        }
        database = workingDatabase;
    }

    public User getUserWithPhoneNumber(String phoneNumber){
        return database.getUserWithPhoneNumber(phoneNumber);
    }
}
