package verticles;

import io.vertx.core.json.JsonObject;

import java.sql.*;

public class Database {

    static Connection con;

    static void connection() throws SQLException, ClassNotFoundException {

        Class.forName("com.mysql.cj.jdbc.Driver");

        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/NMS","root","password");

        Statement stmt = con.createStatement();

        DatabaseMetaData dbm = con.getMetaData();

        ResultSet tables = dbm.getTables(null, null, "Discovery", null);

        if (!tables.next()) {

            stmt.executeUpdate("create table Discovery (port int,ip varchar(255),name varchar(255),password varchar(255),metricType varchar(255))");

        }
    }


     static boolean checkIp(JsonObject jsonObject) throws SQLException {

        String query = "select * from Discovery where ip='" + jsonObject.getString("ip") + "'";

        ResultSet resultSet = con.createStatement().executeQuery(query);

        return resultSet.next();

    }

     static boolean insert(JsonObject jsonObject) throws SQLException {

        if(!checkIp(jsonObject)) {

            PreparedStatement preparedStatement = con.prepareStatement("insert into Discovery (port,name,password,ip,metricType) values (?,?,?,?,?)");

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
