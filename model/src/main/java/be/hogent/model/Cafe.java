package be.hogent.model;

import be.hogent.model.dao.BeverageDAOImpl;
import be.hogent.model.dao.DAOException;
import be.hogent.model.dao.OrderDAOImpl;
import be.hogent.model.dao.WaiterDAOImpl;
import be.hogent.model.reports.PDFReport;
import be.hogent.model.reports.PieChartReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static java.util.stream.Collectors.toMap;

public class Cafe {
    private final Logger logger = LogManager.getLogger(Cafe.class.getName());
    private Set<Waiter> waiters = new HashSet<>();
    private List<Beverage> beverages = new ArrayList<>();
    private Set<Table> tables = new HashSet<>();
    private boolean isLoggedOn;
    private Waiter loggedOnWaiter;
    private Table activeTable;
    private int nrOfTables;
    private int latestOrderNr;

    //Constructor

    public Cafe(){
        initializeCafe();
    }

    //Getters and setters

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

    public List<Beverage> getBeverages() {
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

    public Set<Table> getTables() {
        return tables;
    }

    public void setTables() {
        readProperties("cafe.properties");
        for (int i = 0; i < nrOfTables; i++){
            Table table = new Table(i+1);
            tables.add(table);
        }
    }

    //Methods

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

    public boolean login(String name, String password) throws WrongCredentialsException, AlreadyLoggedOnException {
        boolean result = false;
        if (isLoggedOn()) {
            logger.info("Login failed! There is already another user logged on!");
            result = false;

            throw new AlreadyLoggedOnException("There is already a user logged on !");
        }
        for (Waiter waiter : waiters) {
            if ((waiter.toString().equals(name)) &&  (waiter.getPassword().equals(password))) {
                isLoggedOn = true;
                loggedOnWaiter = waiter;
                logger.info(waiter + " successfully logged on!");
                result = true;
            }
        }
        if (!(isLoggedOn())) {
            logger.error("Error logging in. Combination of username and password not found!");
            result = false;
            throw new WrongCredentialsException("Combination of username and password not found");
        }
        return result;
    }

    public void logoff() {
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
            activeTable.getOrder().setWaiterID(loggedOnWaiter);
            activeTable.getOrder().addOrderItem(new OrderItem(beverage,qty));
            logger.info(activeTable.toString() + " order successfully created by " + loggedOnWaiter.toString() + "!");
        }
        else{
            if (activeTable.getAssignedWaiter()==null || (activeTable.getAssignedWaiter().equals(loggedOnWaiter))){
                activeTable.setAssignedWaiter(loggedOnWaiter);
                activeTable.getOrder().addOrderItem(new OrderItem(beverage,qty));
                logger.info(activeTable.toString() + " order successfully updated by " + loggedOnWaiter.toString() + "!");
            }
            else {
                logger.error("Error placing order on table " + activeTable.getTableNr() + " Other waiter assigned to this table!");
                throw new alreadyOtherWaiterAssignedException("Other waiter assigned to this table!");
            }
        }
    }

    public void removeOrder(Beverage beverage, int qty)throws alreadyOtherWaiterAssignedException{
        if (activeTable.getOrder()==null){
            logger.error("No orders to delete found!");
        }
        else{
            if (activeTable.getAssignedWaiter().equals(loggedOnWaiter)){
                activeTable.getOrder().removeOrderItem(new OrderItem(beverage,qty));
                logger.info(activeTable.toString() + " order successfully removed by " + loggedOnWaiter.toString() + "!");
            }
            else {
                logger.error("Error removing order on table " + activeTable.getTableNr() + " Other waiter assigned to this table!");
                throw new alreadyOtherWaiterAssignedException("Other waiter assigned to this table!");
            }
        }
    }

    private  void readProperties (String propFile) {
        Properties tableProperties = new Properties ();
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream (propFile)) {
            tableProperties.load (inputStream);
            nrOfTables = Integer.valueOf(tableProperties.getProperty("nrOfTables"));
            logger.info("Nr of tables loaded from properties file");
        } catch (IOException ioe) {
            logger.error("Error getting nr of tables " + ioe.getMessage());
        }
    }

    public List<OrderItem> getAllOrderItemsForWaiter(Waiter waiter){
        Set<Order> orders = OrderDAOImpl.getInstance().getAllOrders();
        List<OrderItem> orderItems = new ArrayList<>();
        orders.stream()
                .filter(order -> waiter.getWaiterID() == order.getWaiterID())
                .flatMap(order -> order.getOrderItems().stream()).forEach(orderItems::add);
        return orderItems;
    }

    public List<OrderItem> getAllOrderItemsByDate(Waiter waiter, LocalDate date){
        Set<Order> orders = OrderDAOImpl.getInstance().getAllOrders();
        List<OrderItem> orderItems = new ArrayList<>();
        orders.stream()
                .filter(order -> waiter.getWaiterID() == order.getWaiterID())
                .filter(order -> order.getDate().equals(date))
                .flatMap(order -> order.getOrderItems().stream()).forEach(orderItems::add);

        return orderItems;
    }

    public Map<Waiter,Double> getTop3WaiterSales(){
        List<OrderItem> orderItems;
        Map<Waiter,Double> temp = new HashMap<>();
        Map<Waiter,Double> top3Sales;
        for (Waiter w: getWaiters()) {
            orderItems = getAllOrderItemsForWaiter(w);
            double sum = orderItems.stream().mapToDouble(OrderItem::getPrice).sum();
            temp.put(w,sum);
        }
        top3Sales = temp
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(3)
                .collect(toMap(Map.Entry::getKey,Map.Entry::getValue, (e1,e2) -> e2, LinkedHashMap::new));
        return top3Sales;
    }

    public boolean createPDF(Waiter waiter,List<OrderItem> orderItems){
        boolean result = false;
        if (PDFReport.getInstance().exportToPDF(waiter,orderItems)){
            result = true;
        }
        return result;
    }

    public boolean showTopWaitersReport(Map<Waiter,Double> totalSales) throws IOException {
        if(PieChartReport.getInstance().makeChart(totalSales)){return true;}
        else{return false;}
    }

    public TreeSet<LocalDate> getAllDatesForWaiter(Waiter waiter) throws DAOException {
        return OrderDAOImpl.getInstance().getAllDates(waiter);
    }

    //Exceptions

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
