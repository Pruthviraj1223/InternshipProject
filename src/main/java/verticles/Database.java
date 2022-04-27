package verticles;

import java.sql.*;

public class Database {

    static Connection connection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/NMS","root","password");

        Statement stmt = con.createStatement();

        DatabaseMetaData dbm = con.getMetaData();

        ResultSet tables = dbm.getTables(null, null, "qqq", null);

        if (!tables.next()) {
            stmt.executeUpdate("create table Discovery (port int,ip varchar(255),name varchar(255),password varchar(255),metricType varchar(255))");
        }
        else {
            System.out.println("exists");
        }

        return con;
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {


            Connection con = connection();



    }


}
