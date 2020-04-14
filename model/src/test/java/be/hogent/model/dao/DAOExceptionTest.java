package be.hogent.model.dao;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class DAOExceptionTest {

    @Test
    public void daoExceptionTest(){
        DAOException e = new DAOException();
        assertThat(e.getMessage()).isEqualTo("Error in database ");
    }

    @Test
    public void daoExceptionWithParameterTest(){
        DAOException e = new DAOException("Constructor test");
        assertThat(e.getMessage()).isEqualTo("Constructor test");
    }
}
