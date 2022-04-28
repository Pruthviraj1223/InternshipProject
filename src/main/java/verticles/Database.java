package verticles;

import io.vertx.core.json.JsonObject;

import java.sql.*;

public class Database {

    public static Connection con;
    public static Connection connection() throws SQLException, ClassNotFoundException {

        Class.forName("com.mysql.cj.jdbc.Driver");

        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/NMS","root","password");

        Statement stmt = con.createStatement();

        DatabaseMetaData dbm = con.getMetaData();

        ResultSet tables = dbm.getTables(null, null, "Discovery", null);

        if (!tables.next()) {
            stmt.executeUpdate("create table Discovery (port int,ip varchar(255),name varchar(255),password varchar(255),metricType varchar(255))");
        }

        return con;

    }

    public static boolean checkIp(Connection connection,JsonObject jsonObject) throws SQLException {
        String query = "select * from Discovery where ip='" + jsonObject.getString("ip") + "'";

        ResultSet resultSet = connection.createStatement().executeQuery(query);

        return resultSet.next();
    }

    public static boolean insert(Connection connection, JsonObject jsonObject) throws SQLException {


        if(!checkIp(connection,jsonObject)) {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into Discovery (port,name,password,ip,metricType) values (?,?,?,?,?)");

            preparedStatement.setInt(1, jsonObject.getInteger("port"));

            preparedStatement.setString(2, jsonObject.getString("name"));

            preparedStatement.setString(3, jsonObject.getString("password"));

            preparedStatement.setString(4, jsonObject.getString("ip"));

            preparedStatement.setString(5, jsonObject.getString("metricType"));

            preparedStatement.executeUpdate();

            return true;

        }else{
            return false;
        }
    }

}
