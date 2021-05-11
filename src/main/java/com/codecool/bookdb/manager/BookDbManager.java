package com.codecool.bookdb.manager;

import com.codecool.bookdb.model.AuthorDao;
import com.codecool.bookdb.model.AuthorDaoJdbc;
import com.codecool.bookdb.model.BookDao;
import com.codecool.bookdb.model.BookDaoJdbc;
import com.codecool.bookdb.view.UserInterface;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class BookDbManager {
    private static class DataSourceConfigurationProperties {
        private static final String DATABASE_PROPERTIES_FILE_NAME = "database.properties";
        private static final String POSTGRESQL_DB_DRIVER_CLASS_NAME = "postgresql.db.driver.className";
        private static final String POSTGRESQL_DB_URL_HOST_NAME = "postgresql.db.url.hostName";
        private static final String POSTGRESQL_DB_URL_PORT_NUMBER = "postgresql.db.url.portNumber";
        private static final String POSTGRESQL_DB_URL_DB_NAME = "postgresql.db.url.dbName";
        private static final String POSTGRESQL_DB_URL_OPTIONS = "postgresql.db.url.options";
        private static final String POSTGRESQL_DB_USER_NAME = "postgresql.db.userName";
        private static final String POSTGRESQL_DB_PASSWORD = "postgresql.db.password";

        public static final String HOST_NAME;
        public static final int PORT_NUMBER;
        public static final String DATABASE_NAME;
        public static final String URL;
        public static final String USER_NAME;
        public static final String PASSWORD;

        static {
            String hostName = null;
            int portNumber = -1;
            String databaseName = null;
            String userName = null;
            String password = null;
            String url = null;

            try {
                Properties properties = new Properties();
                properties.load(DataSourceConfigurationProperties.class.getClassLoader()
                        .getResourceAsStream(DATABASE_PROPERTIES_FILE_NAME));
                Class.forName(properties.getProperty(POSTGRESQL_DB_DRIVER_CLASS_NAME));
                hostName = properties.getProperty(POSTGRESQL_DB_URL_HOST_NAME);
                portNumber = Integer.parseInt(properties.getProperty(POSTGRESQL_DB_URL_PORT_NUMBER));
                databaseName = properties.getProperty(POSTGRESQL_DB_URL_DB_NAME);
                userName = properties.getProperty(POSTGRESQL_DB_USER_NAME);
                password = properties.getProperty(POSTGRESQL_DB_PASSWORD);
                String options = properties.getProperty(POSTGRESQL_DB_URL_OPTIONS);
                url = String.format("jdbc:postgresql://%s:%d/%s?%s", hostName, portNumber, databaseName, options);
            } catch (ClassNotFoundException | IOException e) { }

            HOST_NAME = hostName;
            PORT_NUMBER = portNumber;
            DATABASE_NAME = databaseName;
            USER_NAME = userName;
            PASSWORD = password;
            URL = url;
        }
    }

    private DataSource dataSource = null;

    private UserInterface ui;
    private AuthorDao authorDao;
    private BookDao bookDao;

    public BookDbManager(UserInterface ui) {
        this.ui = ui;
    }

    public void run() {
        try {
            setup();
        } catch (SQLException exception) {
            System.out.println("Could not connect to the database");
            return;
        }

        boolean running = true;

        while (running) {
            ui.printTitle("Main Menu");
            ui.printOption('a', "Authors");
            ui.printOption('b',"Books");
            ui.printOption('q', "Quit");

            switch (ui.choice("abq")) {
                case 'a':
                    new AuthorManager(ui, authorDao).run();
                    break;
                case 'b':
                    new BookManager(ui, bookDao, authorDao).run();
                    break;
                case 'q':
                    running = false;
                    break;
            }
        }
    }

    public DataSource connect() throws SQLException {
        if (dataSource == null) {
            PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
            pgSimpleDataSource.setURL(DataSourceConfigurationProperties.URL);
            pgSimpleDataSource.setUser(DataSourceConfigurationProperties.USER_NAME);
            pgSimpleDataSource.setPassword(DataSourceConfigurationProperties.PASSWORD);
            System.out.println("Trying to connect...");
            pgSimpleDataSource.getConnection().close();
            System.out.println("Connection OK");
            dataSource = pgSimpleDataSource;
        }

        return dataSource;
    }

    private void setup() throws SQLException {
        dataSource = connect();
        authorDao = new AuthorDaoJdbc(dataSource);
        bookDao = new BookDaoJdbc(dataSource);
    }
}
