package com.coco.database;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class ConnectionTemplate {

    public interface Template<T> {
        T execute(Connection con);
    }

    private static final Logger log = Logger.getLogger(ConnectionTemplate.class);

    private final DataSource dataSource;

    public ConnectionTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> T execute(Template<T> template) {
        T t = null;
        Connection con = null;

        try {
            con = dataSource.getConnection();

            t = template.execute(con);
        } catch (SQLException e) {
            log.error("Error when opening connection to database.", e);
        }

        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            log.error("Error when closing connection to database.", e);
        }

        return t;
    }
}
