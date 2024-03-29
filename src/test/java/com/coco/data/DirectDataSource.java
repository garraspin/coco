package com.coco.data;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import com.coco.database.CustomDatabase;
import org.apache.log4j.Logger;

public class DirectDataSource implements DataSource {

    private static final Logger log = Logger.getLogger(DirectDataSource.class);

    @Override
    public Connection getConnection() throws SQLException {
        try {
            return CustomDatabase.getConnectionFromDriver();
        } catch (ClassNotFoundException e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public Connection getConnection(String s, String s2) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setLogWriter(PrintWriter printWriter) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setLoginTimeout(int i) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T> T unwrap(Class<T> tClass) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isWrapperFor(Class<?> aClass) throws SQLException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
