package edu.bsu.cs222.finalproject.database;

import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class MySqlDatabase implements Database {
    private Connection connection = null;
    private Statement statement = null;

    public static MySqlDatabase createInstance(){
        return new MySqlDatabase();
    }

    public boolean isUsable(){
        return connection != null;
    }

    public void connectToServer(String address, String username, String password, String database) {
        try {
            String databaseConnectionURI = "jdbc:mysql://" + address + "/" + database + "?user=" + username + "&password=" + password;

            connection = DriverManager.getConnection(databaseConnectionURI);
            connection.setAutoCommit(false);
            statement = connection.createStatement();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            statement.close();
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createDatabaseTables(){
        System.out.println("Creating MySql Database Tables, this might be very desctructive and can NOT be undone");
        System.out.print("Do you wish to continue? (y/n):");

        Scanner in = new Scanner(System.in);

        String input = in.nextLine();
        if(!input.equals("y") && !input.equals("Y")){
            System.out.println("Aborting database table creation!");
            throw new RuntimeException("User aborted database table creation");
        }

        try {
            statement.execute(""
                    + "CREATE TABLE employee("
                    + "id BIGINT NOT NULL AUTO_INCREMENT,"
                    + "PRIMARY KEY (id),"
                    + "name TEXT NOT NULL,"
                    + "number TEXT NOT NULL"
                    + ") ENGINE = InnoDB"
            );

            statement.execute(""
                    + "CREATE TABLE user("
                    + "id BIGINT NOT NULL AUTO_INCREMENT,"
                    + "PRIMARY KEY (id),"
                    + "name TEXT NOT NULL,"
                    + "address TEXT NOT NULL,"
                    + "phoneNumber TEXT NOT NULL"
                    + ") ENGINE = InnoDB"
            );

            statement.execute(""
                    + "CREATE TABLE item("
                    + "id BIGINT NOT NULL AUTO_INCREMENT,"
                    + "PRIMARY KEY (id),"
                    + "modelNumber TEXT NOT NULL,"
                    + "serialNumber TEXT NOT NULL"
                    + ") ENGINE = InnoDB"
            );

            statement.execute(""
                    + "CREATE TABLE purchase("
                    + "id BIGINT NOT NULL AUTO_INCREMENT,"
                    + "PRIMARY KEY (id),"
                    + "purchaserId BIGINT NOT NULL,"
                    + "INDEX (purchaserId),"
                    + "itemId BIGINT NOT NULL,"
                    + "INDEX (itemId),"
                    + "date DATETIME NOT NULL"
                    + ") ENGINE = InnoDB"
            );

            statement.execute(""
                    + "CREATE TABLE repair("
                    + "id BIGINT NOT NULL AUTO_INCREMENT,"
                    + "PRIMARY KEY (id),"
                    + "repairNumber BIGINT NOT NULL,"
                    + "userId BIGINT NOT NULL,"
                    + "INDEX (userId),"
                    + "itemId BIGINT NOT NULL,"
                    + "INDEX (itemId),"
                    + "dateStarted DATETIME NOT NULL,"
                    + "dateCompleted DATETIME,"//this one actually can be NULL
                    + "description TEXT NOT NULL,"
                    + "descriptionCompleted TEXT NOT NULL"
                    + ") ENGINE = InnoDB"
            );

            statement.execute(""
                    + "CREATE TABLE repairPart("
                    + "id BIGINT NOT NULL AUTO_INCREMENT,"
                    + "PRIMARY KEY (id),"
                    + "repairId BIGINT NOT NULL,"
                    + "INDEX (repairId),"
                    + "name TEXT NOT NULL,"
                    + "price INT NOT NULL,"
                    + "quantity INT NOT NULL,"
                    + "needToBuy BOOL NOT NULL"
                    + ") ENGINE = InnoDB"
            );

            commitChange();
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        //okay, all of the tables are now created, lets make a default user so the person can make the initial login

        Employee employee = new Employee();
        employee.name = "Temporary Employee";
        employee.number = "00";
        System.out.println("Creating temporary employee with employee number '00' for login use");
        insertEmployee(employee);

        System.out.println("Done with database table creation!");
    }

    private void commitChange() throws SQLException{
        connection.commit();
    }

    private void beforeQuery() throws SQLException{
        statement.execute("RESET QUERY CACHE;");
    }

    static String escapeForLike(String string) {
        return string.replaceAll("!", "!!").replaceAll("%", "!%").replaceAll("_", "!_").replace("[", "![");
    }

    public void insertEmployee(Employee newEmployee){
        try {
            PreparedStatement prepStmt = connection.prepareStatement("INSERT INTO employee (name, number) VALUES (?, ?)");
            prepStmt.setString(1, newEmployee.name);
            prepStmt.setString(2, newEmployee.number);
            prepStmt.execute();
            prepStmt.close();

            ResultSet res = statement.executeQuery("SELECT LAST_INSERT_ID()");
            res.first();
            newEmployee.id = res.getLong(1);
            res.close();

            commitChange();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void dropEmployee(long employeeId){
        try {
            PreparedStatement prepStmt = connection.prepareStatement("DELETE FROM employee WHERE id = ?");
            prepStmt.setLong(1, employeeId);
            prepStmt.execute();
            prepStmt.close();

            commitChange();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public Employee getEmployeeWithId(long employeeId){
        Employee employee = null;
        try {
            beforeQuery();

            PreparedStatement prepStmt = connection.prepareStatement("SELECT id, name, number FROM employee WHERE id = ?");
            prepStmt.setLong(1, employeeId);
            ResultSet res = prepStmt.executeQuery();

            res.beforeFirst();
            if(res.next()){
                employee = new Employee();
                employee.id = res.getLong("id");
                employee.name = res.getString("name");
                employee.number = res.getString("number");
            }
            res.close();
            prepStmt.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return employee;
    }

    public Employee getEmployeeWithNumber(String employeeNumber){
        Employee employee = null;
        try {
            beforeQuery();

            PreparedStatement prepStmt = connection.prepareStatement("SELECT id, name, number FROM employee WHERE number = ?");
            prepStmt.setString(1, employeeNumber);
            ResultSet res = prepStmt.executeQuery();

            res.beforeFirst();
            if(res.next()){
                employee = new Employee();
                employee.id = res.getLong("id");
                employee.name = res.getString("name");
                employee.number = res.getString("number");
            }
            res.close();
            prepStmt.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return employee;
    }

    public ArrayList<Employee> getAllEmployees(){
        ArrayList<Employee> output = new ArrayList<>();

        try {
            beforeQuery();

            ResultSet res = statement.executeQuery("SELECT id, name, number FROM employee");

            res.beforeFirst();
            while(res.next()){
                Employee employee = new Employee();
                employee.id = res.getLong("id");
                employee.name = res.getString("name");
                employee.number = res.getString("number");
                output.add(employee);
            }

            res.close();
        } catch(SQLException e){
            e.printStackTrace();
        }

        return output;
    }

    public void insertUser(User newUser){
        try {
            PreparedStatement prepStmt = connection.prepareStatement("INSERT INTO user (name, address, phoneNumber) VALUES (?, ?, ?)");
            prepStmt.setString(1, newUser.name);
            prepStmt.setString(2, newUser.address);
            prepStmt.setString(3, newUser.phoneNumber);
            prepStmt.execute();
            prepStmt.close();

            ResultSet res = statement.executeQuery("SELECT LAST_INSERT_ID()");
            res.first();
            newUser.id = res.getLong(1);
            res.close();

            commitChange();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void updateUser(User user){
        try {
            PreparedStatement prepStmt = connection.prepareStatement("UPDATE user SET name = ?, address = ?, phoneNumber = ? WHERE id = ?");
            prepStmt.setString(1, user.name);
            prepStmt.setString(2, user.address);
            prepStmt.setString(3, user.phoneNumber);
            prepStmt.setLong(4, user.id);
            prepStmt.execute();
            prepStmt.close();

            commitChange();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }


    public void dropUser(long userId){
        try {
            PreparedStatement prepStmt = connection.prepareStatement("DELETE FROM user WHERE id = ?");
            prepStmt.setLong(1, userId);
            prepStmt.execute();
            prepStmt.close();

            commitChange();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public User getUserWithId(long userId){
        User user = null;
        try {
            beforeQuery();

            PreparedStatement prepStmt = connection.prepareStatement("SELECT id, name, address, phoneNumber FROM user WHERE id = ?");
            prepStmt.setLong(1, userId);
            ResultSet res = prepStmt.executeQuery();

            res.beforeFirst();
            if(res.next()){
                user = new User();
                user.id = res.getLong("id");
                user.name = res.getString("name");
                user.address = res.getString("address");
                user.phoneNumber = res.getString("phoneNumber");
            }
            res.close();
            prepStmt.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return user;
    }

    public User getUserWithPhoneNumber(String phoneNumber){
        User user = null;
        try {
            beforeQuery();

            PreparedStatement prepStmt = connection.prepareStatement("SELECT id, name, address, phoneNumber FROM user WHERE phoneNumber = ?");
            prepStmt.setString(1, phoneNumber);
            ResultSet res = prepStmt.executeQuery();

            res.beforeFirst();
            if(res.next()){
                user = new User();
                user.id = res.getLong("id");
                user.name = res.getString("name");
                user.address = res.getString("address");
                user.phoneNumber = res.getString("phoneNumber");
            }
            res.close();
            prepStmt.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return user;
    }


    public ArrayList<User> searchUsersWithName(String name){
        ArrayList<User> output = new ArrayList<>();

        try {
            beforeQuery();

            PreparedStatement prepStmt = connection.prepareStatement("SELECT id, name, address, phoneNumber FROM user WHERE name LIKE ? ESCAPE '!'");
            prepStmt.setString(1, "%" + escapeForLike(name) + "%");
            ResultSet res = prepStmt.executeQuery();

            res.beforeFirst();
            while(res.next()){
                User user = new User();
                user.id = res.getLong("id");
                user.name = res.getString("name");
                user.address = res.getString("address");
                user.phoneNumber = res.getString("phoneNumber");
                output.add(user);
            }
            res.close();
            prepStmt.close();
        } catch(SQLException e){
            e.printStackTrace();
        }

        return output;
    }

    public void insertItem(Item newItem){
        try {
            PreparedStatement prepStmt = connection.prepareStatement("INSERT INTO item (modelNumber, serialNumber) VALUES (?, ?)");
            prepStmt.setString(1, newItem.modelNumber);
            prepStmt.setString(2, newItem.serialNumber);
            prepStmt.execute();
            prepStmt.close();

            ResultSet res = statement.executeQuery("SELECT LAST_INSERT_ID()");
            res.first();
            newItem.id = res.getLong(1);
            res.close();

            commitChange();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void updateItem(Item item){
        try {
            PreparedStatement prepStmt = connection.prepareStatement("UPDATE item SET modelNumber = ?, serialNumber = ? WHERE id = ?");
            prepStmt.setString(1, item.modelNumber);
            prepStmt.setString(2, item.serialNumber);
            prepStmt.setLong(3, item.id);
            prepStmt.execute();
            prepStmt.close();

            commitChange();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void dropItem(long itemId){
        try {
            PreparedStatement prepStmt = connection.prepareStatement("DELETE FROM item WHERE id = ?");
            prepStmt.setLong(1, itemId);
            prepStmt.execute();
            prepStmt.close();

            commitChange();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public Item getItemWithId(long itemId){
        Item item = null;
        try {
            beforeQuery();

            PreparedStatement prepStmt = connection.prepareStatement("SELECT id, modelNumber, serialNumber FROM item WHERE id = ?");
            prepStmt.setLong(1, itemId);
            ResultSet res = prepStmt.executeQuery();

            res.beforeFirst();
            if(res.next()){
                item = new Item();
                item.id = res.getLong("id");
                item.modelNumber = res.getString("modelNumber");
                item.serialNumber = res.getString("serialNumber");
            }
            res.close();
            prepStmt.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return item;
    }

    public ArrayList<Item> searchItemsWithSerial(String serialNumber){
        ArrayList<Item> output = new ArrayList<>();

        try {
            beforeQuery();

            PreparedStatement prepStmt = connection.prepareStatement("SELECT id, modelNumber, serialNumber FROM item WHERE serialNumber LIKE ? ESCAPE '!'");
            prepStmt.setString(1, "%" + escapeForLike(serialNumber) + "%");
            ResultSet res = prepStmt.executeQuery();

            res.beforeFirst();
            while(res.next()){
                Item item = new Item();
                item.id = res.getLong("id");
                item.modelNumber = res.getString("modelNumber");
                item.serialNumber = res.getString("serialNumber");
                output.add(item);
            }
            res.close();
            prepStmt.close();
        } catch(SQLException e){
            e.printStackTrace();
        }

        return output;
    }

    public void insertPurchase(Purchase newPurchase){
        try {
            PreparedStatement prepStmt = connection.prepareStatement("INSERT INTO purchase (purchaserId, itemId, date) VALUES (?, ?, ?)");
            prepStmt.setLong(1, newPurchase.purchaserId);
            prepStmt.setLong(2, newPurchase.itemId);
            prepStmt.setDate(3, newPurchase.date);
            prepStmt.execute();
            prepStmt.close();

            ResultSet res = statement.executeQuery("SELECT LAST_INSERT_ID()");
            res.first();
            newPurchase.id = res.getLong(1);
            res.close();

            commitChange();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void dropPurchase(long purchaseId){
        try {
            PreparedStatement prepStmt = connection.prepareStatement("DELETE FROM purchase WHERE id = ?");
            prepStmt.setLong(1, purchaseId);
            prepStmt.execute();
            prepStmt.close();

            commitChange();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public Purchase getPurchaseWithId(long purchaseId){
        Purchase purchase = null;
        try {
            beforeQuery();

            PreparedStatement prepStmt = connection.prepareStatement("SELECT id, purchaserId, itemId, date FROM purchase WHERE id = ?");
            prepStmt.setLong(1, purchaseId);
            ResultSet res = prepStmt.executeQuery();

            res.beforeFirst();
            if(res.next()){
                purchase = new Purchase();
                purchase.id = res.getLong("id");
                purchase.purchaserId = res.getLong("purchaserId");
                purchase.itemId = res.getLong("itemId");
                purchase.date = res.getDate("date");
            }
            res.close();
            prepStmt.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return purchase;
    }

    public ArrayList<Purchase> getPurchasesWithPurchaser(long purchaserId){
        ArrayList<Purchase> output = new ArrayList<>();

        try {
            beforeQuery();

            PreparedStatement prepStmt = connection.prepareStatement("SELECT id, purchaserId, itemId, date FROM purchase WHERE purchaserId = ?");
            prepStmt.setLong(1, purchaserId);
            ResultSet res = prepStmt.executeQuery();

            res.beforeFirst();
            while(res.next()){
                Purchase purchase = new Purchase();
                purchase.id = res.getLong("id");
                purchase.purchaserId = res.getLong("purchaserId");
                purchase.itemId = res.getLong("itemId");
                purchase.date = res.getDate("date");
                output.add(purchase);
            }
            res.close();
            prepStmt.close();
        } catch(SQLException e){
            e.printStackTrace();
        }

        return output;
    }

    public Purchase getPurchaseOnItem(long itemId) {
        Purchase purchase = null;
        try {
            beforeQuery();

            PreparedStatement prepStmt = connection.prepareStatement("SELECT id, purchaserId, itemId, date FROM purchase WHERE itemId = ?");
            prepStmt.setLong(1, itemId);
            ResultSet res = prepStmt.executeQuery();

            res.beforeFirst();
            if(res.next()){
                purchase = new Purchase();
                purchase.id = res.getLong("id");
                purchase.purchaserId = res.getLong("purchaserId");
                purchase.itemId = res.getLong("itemId");
                purchase.date = res.getDate("date");
            }
            res.close();
            prepStmt.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return purchase;
    }

    public void insertRepair(Repair newRepair){
        try {
            PreparedStatement prepStmt = connection.prepareStatement("INSERT INTO repair (repairNumber, userId, itemId, dateStarted, dateCompleted, description, descriptionCompleted) VALUES (?, ?, ?, ?, ?, ?, ?)");
            prepStmt.setLong(1, newRepair.repairNumber);
            prepStmt.setLong(2, newRepair.userId);
            prepStmt.setLong(3, newRepair.itemId);
            prepStmt.setDate(4, newRepair.dateStarted);
            prepStmt.setDate(5, newRepair.dateCompleted);
            prepStmt.setString(6, newRepair.description);
            prepStmt.setString(7, newRepair.descriptionCompleted);
            prepStmt.execute();
            prepStmt.close();

            ResultSet res = statement.executeQuery("SELECT LAST_INSERT_ID()");
            res.first();
            newRepair.id = res.getLong(1);
            res.close();

            commitChange();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void updateRepair(Repair repair){
        try {
            PreparedStatement prepStmt = connection.prepareStatement("UPDATE repair SET repairNumber = ?, userId = ?, itemId = ?, dateStarted = ?, dateCompleted = ?, description = ?, descriptionCompleted = ? WHERE id = ?");
            prepStmt.setLong(1, repair.repairNumber);
            prepStmt.setLong(2, repair.userId);
            prepStmt.setLong(3, repair.itemId);
            prepStmt.setDate(4, repair.dateStarted);
            prepStmt.setDate(5, repair.dateCompleted);
            prepStmt.setString(6, repair.description);
            prepStmt.setString(7, repair.descriptionCompleted);
            prepStmt.setLong(8, repair.id);
            prepStmt.execute();
            prepStmt.close();

            commitChange();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void dropRepair(long repairId){
        try {
            PreparedStatement prepStmt = connection.prepareStatement("DELETE FROM repair WHERE id = ?");
            prepStmt.setLong(1, repairId);
            prepStmt.execute();
            prepStmt.close();

            commitChange();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public Repair getRepairWithId(long repairId){
        Repair repair = null;
        try {
            beforeQuery();

            PreparedStatement prepStmt = connection.prepareStatement("SELECT id, repairNumber, userId, itemId, dateStarted, dateCompleted, description, descriptionCompleted FROM repair WHERE id = ?");
            prepStmt.setLong(1, repairId);
            ResultSet res = prepStmt.executeQuery();
            res.beforeFirst();
            if(res.next()){
                repair = new Repair();
                repair.id = res.getLong("id");
                repair.repairNumber = res.getLong("repairNumber");
                repair.userId = res.getLong("userId");
                repair.itemId = res.getLong("itemId");
                repair.dateStarted = res.getDate("dateStarted");
                repair.dateCompleted = res.getDate("dateCompleted");
                repair.description = res.getString("description");
                repair.descriptionCompleted = res.getString("descriptionCompleted");
            }
            res.close();
            prepStmt.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return repair;
    }

    public ArrayList<Repair> getRepairsOnItem(long itemId){
        ArrayList<Repair> output = new ArrayList<>();
        try {
            beforeQuery();

            PreparedStatement prepStmt = connection.prepareStatement("SELECT id, repairNumber, userId, itemId, dateStarted, dateCompleted, description, descriptionCompleted FROM repair WHERE itemId = ?");
            prepStmt.setLong(1, itemId);
            ResultSet res = prepStmt.executeQuery();

            res.beforeFirst();
            while(res.next()){
                Repair repair = new Repair();
                repair.id = res.getLong("id");
                repair.repairNumber = res.getLong("repairNumber");
                repair.userId = res.getLong("userId");
                repair.itemId = res.getLong("itemId");
                repair.dateStarted = res.getDate("dateStarted");
                repair.dateCompleted = res.getDate("dateCompleted");
                repair.description = res.getString("description");
                repair.descriptionCompleted = res.getString("descriptionCompleted");
                output.add(repair);
            }
            res.close();
            prepStmt.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return output;
    }

    public ArrayList<Repair> getRepairsWithUser(long userId){
        ArrayList<Repair> output = new ArrayList<>();
        try {
            beforeQuery();

            PreparedStatement prepStmt = connection.prepareStatement("SELECT id, repairNumber, userId, itemId, dateStarted, dateCompleted, description, descriptionCompleted FROM repair WHERE userId = ?");
            prepStmt.setLong(1, userId);
            ResultSet res = prepStmt.executeQuery();

            res.beforeFirst();
            while(res.next()){
                Repair repair = new Repair();
                repair.id = res.getLong("id");
                repair.repairNumber = res.getLong("repairNumber");
                repair.userId = res.getLong("userId");
                repair.itemId = res.getLong("itemId");
                repair.dateStarted = res.getDate("dateStarted");
                repair.dateCompleted = res.getDate("dateCompleted");
                repair.description = res.getString("description");
                repair.descriptionCompleted = res.getString("descriptionCompleted");
                output.add(repair);
            }
            res.close();
            prepStmt.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return output;
    }

    public Repair getLatestRepair(){
        Repair repair = null;
        try {
            beforeQuery();

            ResultSet res = statement.executeQuery("SELECT id, repairNumber, userId, itemId, dateStarted, dateCompleted, description, descriptionCompleted FROM repair ORDER BY dateStarted ASC");

            res.beforeFirst();
            if(res.next()){
                repair = new Repair();
                repair.id = res.getLong("id");
                repair.repairNumber = res.getLong("repairNumber");
                repair.userId = res.getLong("userId");
                repair.itemId = res.getLong("itemId");
                repair.dateStarted = res.getDate("dateStarted");
                repair.dateCompleted = res.getDate("dateCompleted");
                repair.description = res.getString("description");
                repair.descriptionCompleted = res.getString("descriptionCompleted");
            }
            res.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return repair;
    }

    public ArrayList<Repair> getInProgressRepairs(){
        ArrayList<Repair> output = new ArrayList<>();
        try {
            beforeQuery();

            ResultSet res = statement.executeQuery("SELECT id, repairNumber, userId, itemId, dateStarted, dateCompleted, description, descriptionCompleted FROM repair WHERE dateCompleted IS NULL");

            res.beforeFirst();
            while(res.next()){
                Repair repair = new Repair();
                repair.id = res.getLong("id");
                repair.repairNumber = res.getLong("repairNumber");
                repair.userId = res.getLong("userId");
                repair.itemId = res.getLong("itemId");
                repair.dateStarted = res.getDate("dateStarted");
                repair.dateCompleted = res.getDate("dateCompleted");
                repair.description = res.getString("description");
                repair.descriptionCompleted = res.getString("descriptionCompleted");
                output.add(repair);
            }
            res.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return output;
    }

    public void insertRepairPart(RepairPart newRepairPart){
        try {
            PreparedStatement prepStmt = connection.prepareStatement("INSERT INTO repairPart (repairId, name, price, quantity, needToBuy) VALUES (?, ?, ?, ?, ?)");
            prepStmt.setLong(1, newRepairPart.repairId);
            prepStmt.setString(2, newRepairPart.name);
            prepStmt.setInt(3, newRepairPart.price);
            prepStmt.setInt(4, newRepairPart.quantity);
            prepStmt.setBoolean(5, newRepairPart.needToBuy);
            prepStmt.execute();
            prepStmt.close();

            ResultSet res = statement.executeQuery("SELECT LAST_INSERT_ID()");
            res.first();
            newRepairPart.id = res.getLong(1);
            res.close();

            commitChange();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void updateRepairPart(RepairPart repairPart){
        try {
            PreparedStatement prepStmt = connection.prepareStatement("UPDATE repairPart SET repairId = ?, name = ?, price = ?, quantity = ?, needToBuy = ? WHERE id = ?");
            prepStmt.setLong(1, repairPart.repairId);
            prepStmt.setString(2, repairPart.name);
            prepStmt.setInt(3, repairPart.price);
            prepStmt.setInt(4, repairPart.quantity);
            prepStmt.setBoolean(5, repairPart.needToBuy);
            prepStmt.setLong(6, repairPart.id);
            prepStmt.execute();
            prepStmt.close();

            commitChange();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void dropRepairPart(long repairPartId){
        try {
            PreparedStatement prepStmt = connection.prepareStatement("DELETE FROM repairPart WHERE id = ?");
            prepStmt.setLong(1, repairPartId);
            prepStmt.execute();
            prepStmt.close();

            commitChange();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public ArrayList<RepairPart> getRepairPartsOnRepair(long repairId){
        ArrayList<RepairPart> output = new ArrayList<>();
        try {
            beforeQuery();

            PreparedStatement prepStmt = connection.prepareStatement("SELECT id, name, price, quantity, needToBuy FROM repairPart WHERE repairId = ?");
            prepStmt.setLong(1, repairId);
            ResultSet res = prepStmt.executeQuery();

            res.beforeFirst();
            while(res.next()){
                RepairPart repairPart = new RepairPart();
                repairPart.id = res.getLong("id");
                repairPart.name = res.getString("name");
                repairPart.price = res.getInt("price");
                repairPart.quantity = res.getInt("quantity");
                repairPart.needToBuy = res.getBoolean("needToBuy");
                output.add(repairPart);
            }
            res.close();
            prepStmt.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return output;
    }

    public ArrayList<RepairPart> getRepairPartsInQueue(){
        ArrayList<RepairPart> output = new ArrayList<>();
        try {
            beforeQuery();

            ResultSet res = statement.executeQuery("SELECT id, name, price, quantity, needToBuy FROM repairPart WHERE needToBuy = TRUE");

            res.beforeFirst();
            while(res.next()){
                RepairPart repairPart = new RepairPart();
                repairPart.id = res.getLong("id");
                repairPart.name = res.getString("name");
                repairPart.price = res.getInt("price");
                repairPart.quantity = res.getInt("quantity");
                repairPart.needToBuy = res.getBoolean("needToBuy");
                output.add(repairPart);
            }
            res.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return output;
    }
}