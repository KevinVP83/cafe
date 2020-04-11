package be.hogent.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Table {
    private final Logger logger = LogManager.getLogger(Table.class.getName());
    private final int tableNr;
    private Waiter assignedWaiter;
    private Order order;

    public int getTableNr() {
        return tableNr;
    }

    public Order getOrder() {
        return order;
    }

    public Waiter getAssignedWaiter() {
        return assignedWaiter;
    }

    public void setAssignedWaiter (Waiter waiter) {
        this.assignedWaiter = waiter;
    }

    public Table (int tableNr){ this.tableNr = tableNr;}

    @Override
    public String toString() {
        return "Table " + tableNr;
    }

    public void clearTable(){
        order.getOrderItems().clear();
        assignedWaiter = null;
        logger.debug(this.toString() + " successfully cleared");
    }
}
