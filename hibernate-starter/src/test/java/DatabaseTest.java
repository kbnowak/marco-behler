import com.nowakk.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class DatabaseTest {

    private SessionFactory sessionFactory;

    @Before
    public void init() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    @Test
    public void shouldOpenConnection() throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:")) {
            assertThat(connection).isNotNull();
            assertThat(connection.isValid(0)).isTrue();
        }
    }

    @Test
    public void shouldOpenHibernateSession() throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            assertThat(session).isNotNull();
            assertThat(session.isOpen()).isTrue();
        }
    }

    @Test
    public void shouldDoBasicQuery() {
        try (Session session = sessionFactory.openSession()) {
            Integer result = (Integer) session.createNativeQuery("select 1 from dual").uniqueResult();
            assertThat(result).isEqualTo(1);
        }
    }

    @Test
    public void shouldSaveUser() {
        User user = new User();
        user.setEmail("hans@gmail.com");
        user.setPassword("pass");

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        }

        assertThat(user.getId()).isNotNull();
        System.out.println("user = " + user);
    }
}
