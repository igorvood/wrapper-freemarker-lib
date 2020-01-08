package sfqtl;

import oracle.jdbc.pool.OracleDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;
import java.sql.SQLException;


@Configuration
@PropertySource("sfqtl/db_connection.properties")
public class TestConfig {

    @Value("${db.connection.url}")
    String dbConnectionUrl;

    @Value("${db.userId}")
    String dbConnectionUser;

    @Value("${db.password}")
    String dbConnectionPassword;

    @Bean
    public DataSource getTestDataSource() throws SQLException {
        OracleDataSource ds = new OracleDataSource();
        ds.setURL(dbConnectionUrl);
        ds.setUser(dbConnectionUser);
        ds.setPassword(dbConnectionPassword);
        return ds;
    }

    @Bean("dbConnectionUrl")
    public String getTestDbConnectionUrl() {
        return dbConnectionUrl;
    }

    @Bean("dbConnectionUser")
    public String getTestDbConnectionUser() {
        return dbConnectionUser;
    }

    @Bean("dbConnectionPassword")
    public String getTestDbConnectionPassword() {
        return dbConnectionPassword;
    }

}
