package edu.bsu.cs222.finalproject.database;

import java.util.ArrayList;

public class TemporaryDatabase implements Database {
    private ArrayList<Employee> employeeTable;
    private long employeeNextInsert;
    private ArrayList<User> userTable;
    private long userNextInsert;
    private ArrayList<Item> itemTable;
    private long itemNextInsert;
    private ArrayList<Purchase> purchaseTable;
    private long purchaseNextInsert;
    private ArrayList<Repair> repairTable;
    private long repairNextInsert;

    private TemporaryDatabase(){
        employeeTable = new ArrayList<>();
        employeeNextInsert = 0;
        userTable = new ArrayList<>();
        userNextInsert = 0;
        itemTable = new ArrayList<>();
        itemNextInsert = 0;
        purchaseTable = new ArrayList<>();
        purchaseNextInsert = 0;
        repairTable = new ArrayList<>();
        repairNextInsert = 0;
    }

    public static TemporaryDatabase createInstance(){
        return new TemporaryDatabase();
    }

    public boolean isUsable(){
        return true;
        //there is no auxiliary initialization on a TemporaryDatabase, it's all in memory, so it's always ready to be used
    }

    public String connectToServer(String address, String username, String password, String database){
        //you don't login to this database, so just
        return null;
    }

    public void insertEmployee(Employee newEmployee){
        newEmployee.id = employeeNextInsert;
        employeeNextInsert++;
        employeeTable.add(new Employee(newEmployee));
    }

    public void updateEmployee(Employee employee){
        for(Employee i : employeeTable){
            if(i.id == employee.id){
                employeeTable.remove(i);
                employeeTable.add(new Employee(employee));
                return;
            }
        }
        throw new RuntimeException("Attempted to update an Employee, but no employee with that id exists");
    }

    public void dropEmployee(long employeeId){
        for(Employee i : employeeTable){
            if(i.id == employeeId){
                employeeTable.remove(i);
                return;
            }
        }
        throw new RuntimeException("Attempted to remove an Employee, but no employee with that id exists");
    }

    public Employee getEmployeeWithId(long employeeId){
        for(Employee i : employeeTable){
            if(i.id == employeeId){
                return new Employee(i);
            }
        }
        return null;
    }

    public Employee getEmployeeWithNumber(String employeeNumber){
        for(Employee i : employeeTable){
            if(i.number.equals(employeeNumber)){
                return new Employee(i);
            }
        }
        return null;
    }

    public ArrayList<Employee> getAllEmployees(){
        ArrayList<Employee> output = new ArrayList<>();
        for(Employee i : employeeTable){
            output.add(new Employee(i));
        }
        return output;
    }

    public void insertUser(User newUser){
        newUser.id = userNextInsert;
        userNextInsert++;
        userTable.add(new User(newUser));
    }

    public void updateUser(User user){
        for(User i : userTable){
            if(i.id == user.id){
                userTable.remove(i);
                userTable.add(new User(user));
                return;
            }
        }
        throw new RuntimeException("Attempted to update a User, but no user with that id exists");
    }

    public void dropUser(long userId){
        for(User i : userTable){
            if(i.id == userId){
                userTable.remove(i);
                return;
            }
        }
        throw new RuntimeException("Attempted to remove a User, but no user with that id exists");
    }

    public User getUserWithId(long userId){
        for(User i : userTable){
            if(i.id == userId){
                return new User(i);
            }
        }
        return null;
    }

    public User getUserWithPhoneNumber(String phoneNumber){
        for(User i : userTable){
            if(i.phoneNumber.equals(phoneNumber)){
                return new User(i);
            }
        }
        return null;
    }

    public ArrayList<User> searchUsersWithName(String name){
        ArrayList<User> output = new ArrayList<>();
        for(User i : userTable){
            if(i.name.toLowerCase().contains(name.toLowerCase())){
                output.add(new User(i));
            }
        }
        return output;
    }


    public void insertItem(Item newItem){
        newItem.id = itemNextInsert;
        itemNextInsert++;
        itemTable.add(new Item(newItem));
    }

    public void updateItem(Item item){
        for(Item i : itemTable){
            if(i.id == item.id){
                itemTable.remove(i);
                itemTable.add(new Item(item));
                return;
            }
        }
        throw new RuntimeException("Attempted to update an Item, but no item with that id exists");
    }

    public void dropItem(long itemId){
        for(Item i : itemTable){
            if(i.id == itemId){
                itemTable.remove(i);
                return;
            }
        }
        throw new RuntimeException("Attempted to remove an Item, but no item with that id exists");
    }

    public Item getItemWithId(long itemId){
        for(Item i : itemTable){
            if(i.id == itemId){
                return new Item(i);
            }
        }
        return null;
    }

    public ArrayList<Item> searchItemsWithSerial(String serialNumber){
        ArrayList<Item> output = new ArrayList<>();
        for(Item i : itemTable){
            if(i.serialNumber.toLowerCase().contains(serialNumber.toLowerCase())){
                output.add(new Item(i));
            }
        }
        return output;
    }

    public void insertPurchase(Purchase newPurchase){
        newPurchase.id = purchaseNextInsert;
        purchaseNextInsert++;
        purchaseTable.add(new Purchase(newPurchase));
    }

    public void dropPurchase(long purchaseId){
        for(Purchase i : purchaseTable){
            if(i.id == purchaseId){
                purchaseTable.remove(i);
                return;
            }
        }
        throw new RuntimeException("Attempted to remove a Purchase, but no purchase with that id exists");
    }

    public Purchase getPurchaseWithId(long purchaseId){
        for(Purchase i : purchaseTable){
            if(i.id == purchaseId){
                return new Purchase(i);
            }
        }
        return null;
    }

    public ArrayList<Purchase> getPurchasesWithPurchaser(long purchaserId){
        ArrayList<Purchase> output = new ArrayList<>();
        for(Purchase i : purchaseTable){
            if(i.purchaserId == purchaserId){
                output.add(new Purchase(i));
            }
        }
        return output;
    }

    public Purchase getPurchaseOnItem(long itemId){
        Purchase output = null;
        for(Purchase i : purchaseTable){
            if(i.itemId == itemId){
                output = new Purchase(i);
                break;
            }
        }
        return output;
    }

    public void insertRepair(Repair newRepair){
        newRepair.id = repairNextInsert;
        repairNextInsert++;
        repairTable.add(new Repair(newRepair));
    }

    public void updateRepair(Repair repair){
        for(Repair i : repairTable){
            if(i.id == repair.id){
                repairTable.remove(i);
                repairTable.add(new Repair(repair));
                return;
            }
        }
        throw new RuntimeException("Attempted to update a Repair, but no repair with that id exists");
    }

    public void dropRepair(long repairId){
        for(Repair i : repairTable){
            if(i.id == repairId){
                repairTable.remove(i);
                return;
            }
        }
        throw new RuntimeException("Attempted to remove a Repair, but no Repair with that id exists");
    }

    public Repair getRepairWithId(long repairId){
        for(Repair i : repairTable){
            if(i.id == repairId){
                return new Repair(i);
            }
        }
        return null;
    }

    public ArrayList<Repair> getRepairsOnItem(long itemId){
        ArrayList<Repair> output = new ArrayList<>();
        for(Repair i : repairTable){
            if(i.itemId == itemId){
                output.add(new Repair(i));
            }
        }
        return output;
    }

    public ArrayList<Repair> getRepairsWithUser(long userId){
        ArrayList<Repair> output = new ArrayList<>();
        for(Repair i : repairTable){
            if(i.userId == userId){
                output.add(new Repair(i));
            }
        }
        return output;
    }


    public Repair getLatestRepair(){
        Repair latestRepair = null;
        for(Repair i : repairTable){
            if(latestRepair == null){
                latestRepair = i;
            }
            else if(i.dateStarted.compareTo(latestRepair.dateCompleted) > 0){
                latestRepair = i;
            }
        }
        return latestRepair;
    }

    public ArrayList<Repair> getInProgressRepairs(){
        ArrayList<Repair> repairs = new ArrayList<>();
        for(Repair i : repairTable){
            if(i.dateCompleted == null){
                repairs.add(i);
            }
        }
        return repairs;
    }
}
