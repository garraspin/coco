package com.coco.database;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionTemplate {

    public interface Template<T> {
        T execute(Connection con);
    }

    private static final Logger log = LoggerFactory.getLogger(ConnectionTemplate.class);

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
