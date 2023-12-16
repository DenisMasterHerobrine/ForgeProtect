package dev.denismasterherobrine.forgeprotect.database;

import dev.denismasterherobrine.forgeprotect.database.util.TableChecks;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private static String DB_PATH;
    private static Connection connection = null;

    public static void createDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH + "/forgeprotect.db");

            Statement statement = connection.createStatement();

            if (!TableChecks.tableExists("block_events")) {
                statement.execute(Statements.CREATE_BLOCK_EVENTS_TABLE);
            }

            if (!TableChecks.tableExists("mob_events")) {
                statement.execute(Statements.CREATE_MOB_EVENTS_TABLE);
            }

            if (!TableChecks.tableExists("player_deaths")) {
                statement.execute(Statements.CREATE_PLAYER_DEATHS_TABLE);
            }

            if (!TableChecks.tableExists("item_events")) {
                statement.execute(Statements.CREATE_ITEM_EVENTS_TABLE);
            }

            if (!TableChecks.tableExists("item_container_events")) {
                statement.execute(Statements.CREATE_ITEM_CONTAINER_EVENTS_TABLE);
            }

            statement.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getDatabaseConnection() {
        if (connection == null) {
            createDatabase();
        }

        return connection;
    }

    public static void closeDatabase() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setPath(Path path) {
        DatabaseInitializer.DB_PATH = path.toString();
    }
}
