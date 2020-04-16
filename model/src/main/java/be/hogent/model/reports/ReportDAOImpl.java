package be.hogent.model.reports;

import be.hogent.model.dao.BaseDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ReportDAOImpl extends BaseDAO implements ReportDAO {
    private final Logger logger = LogManager.getLogger(ReportDAOImpl.class.getName());
    private static ReportDAOImpl instance = new ReportDAOImpl();
    //private static final String GET_ALL_SALES_FROM_WAITER = "SELECT * from orders where ";

    private ReportDAOImpl() {
    }

    public static ReportDAOImpl getInstance() {
        if (instance == null)
            instance = new ReportDAOImpl();
        return instance;
    }
}
