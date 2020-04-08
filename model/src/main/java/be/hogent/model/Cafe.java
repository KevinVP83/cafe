package be.hogent.model;

import be.hogent.model.dao.BeverageDAOImpl;
import be.hogent.model.dao.OrderDAOImpl;
import be.hogent.model.dao.WaiterDAOImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class Cafe {
    private final Logger logger = LogManager.getLogger(Cafe.class.getName());
    private Set<Waiter> waiters = new HashSet<>();
    private Set<Beverage> beverages = new HashSet<>();
    private Set<Table> tables = new HashSet<>();
    private Set<OrderItem> orderItems = new HashSet<>();
    private Map<Integer, Waiter> assignedTables = new HashMap<>();
    private boolean isLoggedOn;
    private Waiter loggedOnWaiter;
    private Table activeTable;

    private void initializeCafe(){
        waiters = WaiterDAOImpl.getInstance().getWaiters();
        beverages = BeverageDAOImpl.getInstance().getBeverages();
        setTables();
    }

//    public boolean assignWaiter(Integer tableNr, Waiter waiter){
//        if (assignedTables.containsKey(tableNr)){
//            if (assignedTables.get(tableNr) == waiter){
//                //This waiter is already assigned to this table.
//                return true;
//            }
//            else {
//                //This waiter cannot be assigned, table is already in use by another waiter.
//                return false;
//            }
//        }
//        else {
//            //Assign this waiter to this table.
//            assignedTables.put(tableNr, waiter);
//            for (Table table: tables) {
//                if (table.getTableNr() == tableNr){
//                    table.setAssignedWaiter(waiter);
//                    break;
//                }
//            }
//            return true;
//        }
//    }

    public Table getActiveTable() {
        return activeTable;
    }

    public Set<Waiter> getWaiters() {
        return waiters;
    }

    public Set<Beverage> getBeverages() {
        return beverages;
    }

    public void setActiveTable(Table table) {
        this.activeTable = table;
    }

    public void setBeverages() {
        beverages.add(new Beverage(1, "Cola", 2.40));
        beverages.add(new Beverage(2, "Leffe", 3.00));
        beverages.add(new Beverage(3, "Koffie", 2.40));
        beverages.add(new Beverage(4, "Cola-Light", 2.40));
        beverages.add(new Beverage(5, "Whisky", 12.00));
    }

    public void addWaiter(Waiter waiter) {
        waiters.add(waiter);
    }

    public boolean isLoggedOn() {
        return isLoggedOn;
    }

    public Waiter getLoggedOnWaiter() {
        return loggedOnWaiter;
    }

    public void login(String name, String password) throws WrongCredentialsException, AlreadyLoggedOnException {
        if (isLoggedOn()) {
            throw new AlreadyLoggedOnException("There is already a user logged on !");
        }
        for (Waiter waiter : waiters) {
            if ((waiter.toString().equals(name)) &&  (waiter.getPassword().equals(password))) {
                isLoggedOn = true;
                loggedOnWaiter = waiter;
            }
        }
        if (!(isLoggedOn())) {
            throw new WrongCredentialsException("Combination of username and password not found");
        }
    }

    public void logoff(){
        isLoggedOn = false;
        loggedOnWaiter = null;
    }

    public void pay(Table table) {
        try {
            OrderDAOImpl orderDAOImpl = OrderDAOImpl.getInstance();
            orderDAOImpl.insertOrder(table);
        } catch (Exception e){
            logger.error("error pay method");
        }
        table.clearTable();
    }



    public Set<Table> getTables() {
        return tables;
    }

    public void setTables() {
        tables.add(new Table(1));
        tables.add(new Table(2));
        tables.add(new Table(3));
        tables.add(new Table(4));
        tables.add(new Table(5));
        tables.add(new Table(6));
    }

    public static class WrongCredentialsException extends Exception {
        public WrongCredentialsException(String message) {
            super(message);
        }
    }
    public  static class AlreadyLoggedOnException extends Exception {
        public AlreadyLoggedOnException(String message) {
            super(message);
        }
    }
}
