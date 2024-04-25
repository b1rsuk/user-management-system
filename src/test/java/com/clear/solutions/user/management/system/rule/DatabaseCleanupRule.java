package com.clear.solutions.user.management.system.rule;

import com.clear.solutions.user.management.system.rule.exception.DatabaseCleanupException;
import lombok.RequiredArgsConstructor;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
@RequiredArgsConstructor
public class DatabaseCleanupRule extends TestWatcher {

    private final DataSource dataSource;

    @Override
    protected void finished(Description description) {
        clearDB();
    }

    @Override
    protected void failed(Throwable e, Description description) {
        clearDB();
    }

    public void clearDB() {
        try (Connection dbConnection = dataSource.getConnection()) {
            Statement statement = dbConnection.createStatement();
            ResultSet tables = findAllTables(statement);

            deleteAllTable(dbConnection, tables);
        }
        catch (SQLException e) {
            throw new DatabaseCleanupException(e);
        }
    }

    private void deleteAllTable(Connection dbConnection, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String tableName = resultSet.getString("TABLE_NAME");

            try (Statement statement = dbConnection.createStatement()) {
                deleteTableByName(statement, tableName);
            }
            catch (SQLException sqlException) {
                throw new DatabaseCleanupException(sqlException);
            }
        }
    }

    private void deleteTableByName(Statement statement, String tableName) throws SQLException {
        String deleteTableQuery = String.format(SQL_QUERY.DROP_TABLE_BY_NAME.getQuery(), tableName);
        statement.execute(deleteTableQuery);
    }

    private ResultSet findAllTables(Statement statement) throws SQLException {
        String findAllTables = SQL_QUERY.FIND_ALL_TABLES.getQuery();
        return statement.executeQuery(findAllTables);
    }

}
