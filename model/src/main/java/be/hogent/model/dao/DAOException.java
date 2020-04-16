package be.hogent.model.dao;

public class DAOException extends Exception {
    private static final long serialVersionUID = 19192L;
    private String message;

    public DAOException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
