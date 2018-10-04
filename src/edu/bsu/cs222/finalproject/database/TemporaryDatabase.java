package edu.bsu.cs222.finalproject.database;

import java.util.ArrayList;

public class TemporaryDatabase implements Database {
    private ArrayList<User> userTable;
    private long userNextInsert;
    private ArrayList<Item> itemTable;
    private long itemNextInsert;
    private ArrayList<Purchase> purchaseTable;
    private long purchaseNextInsert;

    private TemporaryDatabase(){
        userTable = new ArrayList<>();
        userNextInsert = 0;
        itemTable = new ArrayList<>();
        itemNextInsert = 0;
        purchaseTable = new ArrayList<>();
        purchaseNextInsert = 0;
    }

    public static TemporaryDatabase createInstance(){
        return new TemporaryDatabase();
    }

    public boolean isUsable(){
        return true;
        //there is no auxiliary initialization on a TemporaryDatabase, it's all in memory, so it's always ready to be used
    }

    public void insertUser(User newUser){
        newUser.id = userNextInsert;
        userNextInsert++;
        userTable.add(newUser);
    }

    public void updateUser(User user){
        for(User i : userTable){
            if(i.id == user.id){
                userTable.remove(i);
                userTable.add(user);
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
                return i;
            }
        }
        return null;
    }

    public ArrayList<User> searchUsersWithName(String name){
        ArrayList<User> output = new ArrayList<>();
        for(User i : userTable){
            if(i.name.toLowerCase().contains(name.toLowerCase())){
                output.add(i);
            }
        }
        return output;
    }


    public void insertItem(Item newItem){
        newItem.id = itemNextInsert;
        itemNextInsert++;
        itemTable.add(newItem);
    }

    public void updateItem(Item item){
        for(Item i : itemTable){
            if(i.id == item.id){
                itemTable.remove(i);
                itemTable.add(item);
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
                return i;
            }
        }
        return null;
    }

    public void insertPurchase(Purchase newPurchase){
        newPurchase.id = purchaseNextInsert;
        purchaseNextInsert++;
        purchaseTable.add(newPurchase);
    }

    public void updatePurchase(Purchase purchase){
        for(Purchase i : purchaseTable){
            if(i.id == purchase.id){
                purchaseTable.remove(i);
                purchaseTable.add(purchase);
                return;
            }
        }
        throw new RuntimeException("Attempted to update a Purchase, but no purchase with that id exists");
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
                return i;
            }
        }
        return null;
    }

    public ArrayList<Purchase> getPurchasesWithPurchaser(long purchaserId){
        ArrayList<Purchase> output = new ArrayList<>();
        for(Purchase i : purchaseTable){
            if(i.purchaserId == purchaserId){
                output.add(i);
            }
        }
        return output;
    }
}
