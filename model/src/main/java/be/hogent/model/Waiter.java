package be.hogent.model;

import java.io.Serializable;
import java.util.Objects;

public class Waiter implements Serializable {

    private static final long serialVersionUID = 1718408326711969967L;
    private final int waiterID;
    private final String lastName;
    private final String firsName;
    private final String password;

    //Constructor

    public Waiter(int waiterID, String lastName, String firsName, String password){
        this.waiterID = waiterID;
        this.firsName = firsName;
        this.lastName = lastName;
        this.password = password;
    }

    //Getters and setters

    public int getWaiterID() {
        return waiterID;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirsName() {
        return firsName;
    }

    public String getPassword() {
        return password;
    }

    //Overrides

    @Override
    public String toString() {
        return getFirsName() + " " + getLastName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Waiter waiter = (Waiter) o;
        return waiterID == waiter.waiterID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(waiterID);
    }
}
