package ru.vood.freemarker;

import java.sql.*;

public class Test {
    public static void main(String[] args)
            throws SQLException, ClassNotFoundException {
//        stringfirst_name,last_name ;
//        number salary ;
//        connection c = null;
        /* регистрация драйвера Oracle */
        Class.forName("oracle.jdbc.driver.OracleDriver");

        final Connection c = DriverManager.getConnection(
                "jdbc:oracle:thin:@prod1:1521:finprod",
                "user", "user_passwd");

        try {
            /* создание объекта statement */
            Statement s = c.createStatement();
            c.setAutoCommit(false);

            final ResultSet resultSet = s.executeQuery("select 1 from dual");
            s.executeUpdate("CREATE TABLE employees " +
                    "(first_name VARCHAR2(30), last_name VARCHAR2(20),salary    NUMBER)");
            s.executeUpdate("INSERT INTO employee VALUES " +
                    "('nicholas', 'Alapati', 50000 )");
            c.commit();
            c.setAutoCommit(true);
            /* результирующий набор */
            ResultSet rs = s.executeQuery("SELECT * FROM Employees");
            while (rs.next()) {
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                Float salary = rs.getFloat("salary");
            }
            /* обработчик исключений */
        } catch (SQLException ex) {
            if (c != null) {
                c.rollback();
                c.setAutoCommit(true);
            }
            System.out.println("SQLException caught");
            System.out.println("---");
            while (ex != null) {
                System.out.println("Message : " + ex.getMessage());
                System.out.println("SQLState : " + ex.getSQLState());
                System.out.println("ErrorCode : " + ex.getErrorCode());
                System.out.println("---");
                ex = ex.getNextException();
            }
        }
    }
}
