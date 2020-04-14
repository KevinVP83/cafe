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
    private boolean isLoggedOn;
    private Waiter loggedOnWaiter;
    private Table activeTable;
    private int latestOrderNr;

    public Cafe(){

        initializeCafe();
    }

    private void initializeCafe(){
        waiters = WaiterDAOImpl.getInstance().getWaiters();
        beverages = BeverageDAOImpl.getInstance().getBeverages();
        setTables();
        latestOrderNr = OrderDAOImpl.getInstance().getLatestOrderNr() + 1;
        logger.info("Cafe successfully initialized. ");
    }

    public void assignWaiter(Table table) throws alreadyOtherWaiterAssignedException {
        if (table.getAssignedWaiter()==null || table.getAssignedWaiter().equals(loggedOnWaiter)){
            table.setAssignedWaiter(loggedOnWaiter);
            logger.info(loggedOnWaiter.toString() + " successfully assigned to " + table.toString() + "!");
        }
        else {
            logger.error("There is already another waiter assigned to " + table.toString() + "!");
            throw new alreadyOtherWaiterAssignedException("There is already another waiter assigned to this table !");
        }
    }

    public int getLatestOrderNr(){
        return latestOrderNr;
    }

    public Table getActiveTable() {
        return activeTable;
    }

    public void setLatestOrderNr(){
        latestOrderNr += latestOrderNr;
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
            logger.info("Login failed! There is already another user logged on!");
            throw new AlreadyLoggedOnException("There is already a user logged on !");
        }
        for (Waiter waiter : waiters) {
            if ((waiter.toString().equals(name)) &&  (waiter.getPassword().equals(password))) {
                isLoggedOn = true;
                loggedOnWaiter = waiter;
                logger.info(waiter + " successfully logged on!");
            }
        }
        if (!(isLoggedOn())) {
            logger.error("Error logging in. Combination of username and password not found!");
            throw new WrongCredentialsException("Combination of username and password not found");
        }
    }

    public void logoff(){
        logger.info(loggedOnWaiter.toString() + " successfully logged off!");
        isLoggedOn = false;
        loggedOnWaiter = null;
    }

    public void pay(Table table)throws alreadyOtherWaiterAssignedException {
        if (table.getAssignedWaiter().equals(loggedOnWaiter)) {
            try {
                OrderDAOImpl orderDAOImpl = OrderDAOImpl.getInstance();
                orderDAOImpl.insertOrder(table);
            } catch (Exception e) {
                logger.error(table.toString() + " pay failed");
            }
            table.clearTable();
            logger.info(table.toString() + " successfully payed the bill !");
        } else {
            logger.error("Error paying bill " + table.toString() + ". Other waiter assigned to this table!");
            throw new alreadyOtherWaiterAssignedException("Error paying bill. Other waiter assigned to this table!");
        }
    }

    public void order(Beverage beverage, int qty)throws alreadyOtherWaiterAssignedException{
        if (activeTable.getOrder()==null){
            activeTable.createOrder();
            activeTable.getOrder().setOrderNr(getLatestOrderNr());
            setLatestOrderNr();
            assignWaiter(activeTable);
            activeTable.getOrder().setWaiter(loggedOnWaiter);
            activeTable.getOrder().addOrderItem(new OrderItem(beverage,qty));
        }
        else{
            if (activeTable.getAssignedWaiter().equals(loggedOnWaiter)){
                activeTable.getOrder().addOrderItem(new OrderItem(beverage,qty));
                logger.info(activeTable.toString() + " order successfully created by " + loggedOnWaiter.toString() + "!");
            }
            else {
                logger.error("Error placing order on table " + activeTable.getTableNr() + " Other waiter assigned to this table!");
                throw new alreadyOtherWaiterAssignedException("Other waiter assigned to this table!");
            }
        }
    }

    public Set<Table> getTables() {
        return tables;
    }

    public void setTables() {
        Table table1 = new Table(1);
        tables.add(table1);
        Table table2 = new Table(2);
        tables.add(table2);
        Table table3 = new Table(3);
        tables.add(table3);
        Table table4 = new Table(4);
        tables.add(table4);
        Table table5 = new Table(5);
        tables.add(table5);
        Table table6 = new Table(6);
        tables.add(table6);
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
    public static class alreadyOtherWaiterAssignedException extends Exception {
        public alreadyOtherWaiterAssignedException(String message){super(message);}
    }
}
