package be.hogent.model;

import java.util.Objects;

public class Waiter {
    private final int waiterID;
    private final String lastName;
    private final String firsName;
    private final String password;


    public Waiter(int waiterID, String lastName, String firsName, String password){
        this.waiterID = waiterID;
        this.firsName = firsName;
        this.lastName = lastName;
        this.password = password;
    }

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getFirsName()).append(" ").append(getLastName());
        return sb.toString();
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
