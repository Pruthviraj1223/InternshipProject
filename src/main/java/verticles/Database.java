package verticles;

import io.vertx.core.json.JsonObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Database {

    static Connection con;

    static void connection() throws SQLException, ClassNotFoundException {

        Class.forName("com.mysql.cj.jdbc.Driver");

        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/NMS", "root", "password");

        Statement stmt = con.createStatement();

        DatabaseMetaData dbm = con.getMetaData();

        ResultSet tables = dbm.getTables(null, null, "Discovery", null);

        ResultSet resultSet = dbm.getTables(null, null, "Metric", null);

        if (!tables.next()) {

            stmt.executeUpdate("create table Discovery (port int,ip varchar(255),name varchar(255),password varchar(255),community varchar(255),version varchar(255),metricType varchar(255))");

        }

        if (!resultSet.next()) {

            int a = stmt.executeUpdate("create table Metric (metricType varchar(255),counter varchar(255),time int)");

            HashMap<String, Integer> deviceAndPort = new HashMap<>();
            deviceAndPort.put("linux", 22);
            deviceAndPort.put("windows", 5985);

            HashMap<String, Integer> counterTime = new HashMap<>();
            counterTime.put("CPU", 60000);
            counterTime.put("Disk", 120000);
            counterTime.put("Process", 40000);
            counterTime.put("Memory", 50000);
            counterTime.put("SystemInfo", 200000);

            Iterator<String> keyset = deviceAndPort.keySet().iterator();

            while (keyset.hasNext()) {

                PreparedStatement preparedStatement = con.prepareStatement("insert into Metric (metricType,counter,time) values (?,?,?)");

                String key = keyset.next();

                Iterator<String> counterKeyset = counterTime.keySet().iterator();

                while (counterKeyset.hasNext()) {

                    String counter = counterKeyset.next();

                    int time = counterTime.get(counter);

                    preparedStatement.setString(1, key);

                    preparedStatement.setString(2, counter);

                    preparedStatement.setInt(3, time);

                    int aa = preparedStatement.executeUpdate();

                }

            }

            PreparedStatement preparedStatement = con.prepareStatement("insert into Metric (metricType,counter,time) values (?,?,?)");

            preparedStatement.setString(1,"networking");
            preparedStatement.setString(2,"systemInfo");
            preparedStatement.setInt(3,100000);
            preparedStatement.executeUpdate();

            preparedStatement.setString(1,"networking");
            preparedStatement.setString(2,"Interface");
            preparedStatement.setInt(3,20000);
            preparedStatement.executeUpdate();


        }
    }


     static boolean checkIp(JsonObject jsonObject) throws SQLException {

        String query = "select * from Discovery where ip='" + jsonObject.getString("ip") + "'";

        ResultSet resultSet = con.createStatement().executeQuery(query);

        return resultSet.next();

    }

     static boolean insert(JsonObject jsonObject) throws SQLException {

        if(!checkIp(jsonObject)) {

            PreparedStatement preparedStatement = con.prepareStatement("insert into Discovery (port,name,password,community,version,ip,metricType) values (?,?,?,?,?,?,?)");

            preparedStatement.setInt(1, jsonObject.getInteger("port"));

            preparedStatement.setString(2, jsonObject.getString("name"));

            preparedStatement.setString(3, jsonObject.getString("password"));

            preparedStatement.setString(4,jsonObject.getString("community"));

            preparedStatement.setString(5,jsonObject.getString("version"));

            preparedStatement.setString(6, jsonObject.getString("ip"));

            preparedStatement.setString(7, jsonObject.getString("metricType"));

            preparedStatement.executeUpdate();

            return true;

        }else{

            return false;

        }
    }

}
