package net.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public final class JDBCUtils {
    //directly executes a select query to the database and returns a ResultSetHandler
    public static <T> T select(Connection c, String sql, ResultSetHandler<T> resultSetHandler, Object... parameters) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            populatePreparedStatement(ps, parameters);
            ResultSet rs = ps.executeQuery();
            return resultSetHandler.handle(rs);
        }
    }
    //directly executes an insert query to the database and returns the ResultSetHandler that was inserted
    public static <T> T insert(Connection c, String sql, ResultSetHandler<T> resultSetHandler, Object... parameters) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            populatePreparedStatement(ps, parameters);
            int result = ps.executeUpdate();
            if (result != 1) {
                throw new SQLException("Can't insert row to database. Result=" + result);
            }
            ResultSet rs = ps.getGeneratedKeys();
            return resultSetHandler.handle(rs);
        }
    }
    //add parameters from the parameter list from the search form to the query string
    public static void populateSqlAndParams(StringBuilder sql, List<Object> params, List<Integer> list, String expression) {
        if (list != null && !list.isEmpty()) {
            sql.append(" and (");
            for (int i = 0; i < list.size(); i++) {
                sql.append(expression);
                params.add(list.get(i));
                if (i != list.size() - 1) {
                    sql.append(" or ");
                }
            }
            sql.append(")");
        }
    }
        //fills the database request with parameters from the list
       private static void populatePreparedStatement(PreparedStatement ps, Object... parameters) throws SQLException {
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                ps.setObject(i + 1, parameters[i]);
            }
        }
    }
    //Fulfills a single request package
    public static void insertBatch(Connection c, String sql, List<Object[]> parametersList) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            for (Object[] parameters : parametersList) {
                populatePreparedStatement(ps, parameters);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }


    private JDBCUtils() {
    }
}
