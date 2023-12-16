package dev.denismasterherobrine.forgeprotect.database.util;

import dev.denismasterherobrine.forgeprotect.database.DatabaseInitializer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableChecks {
    public static boolean tableExists(String tableName) {
        try {
            Connection connection = DatabaseInitializer.getDatabaseConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT count(*) FROM sqlite_master WHERE type='table' AND name=?"
            );

            statement.setString(1, tableName);

            ResultSet rs = statement.executeQuery();
            int tableCount = rs.getInt(1);
            rs.close();
            statement.close();

            return tableCount > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
