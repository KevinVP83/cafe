package be.hogent.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class Table implements Comparable<Table>{
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

    public Table (int tableNr){
        this.tableNr = tableNr;
    }

    @Override
    public String toString() {
        return "Table " + tableNr;
    }

    public void clearTable(){
        order = null;
        assignedWaiter = null;
        logger.debug(this.toString() + " successfully cleared");
    }

    public void createOrder() {
        order = new Order(getTableNr());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return tableNr == table.tableNr;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableNr);
    }

    @Override
    public int compareTo(Table t) {
        if(this.getTableNr() > t.getTableNr()) return 1;
        if(this.getTableNr() < t.getTableNr()) return -1;
        else return 0;
    }
}
