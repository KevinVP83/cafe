package be.hogent.model;

public class Table {

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

    public void setAssignedWaiter (Waiter assignedWaiter) throws alreadyOtherWaiterAssignedExeption {
        if ((assignedWaiter.equals(this.getAssignedWaiter()) || (this.getAssignedWaiter() == null))) {
            this.assignedWaiter = assignedWaiter;
        } else {
            throw new alreadyOtherWaiterAssignedExeption("There is already another waiter assigned to this table !");
        }
    }

    public Table (int tableNr){ this.tableNr = tableNr;}

    @Override
    public String toString() {
        return "Table " + tableNr;
    }

    static class alreadyOtherWaiterAssignedExeption extends Exception {
        public alreadyOtherWaiterAssignedExeption(String message){super(message);}
    }

    public void clearTable(){
        order.getOrderItems().clear();
        assignedWaiter = null;
    }
}
