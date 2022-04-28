package verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.impl.pool.ConnectResult;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseVerticle extends AbstractVerticle {


    public DatabaseVerticle() throws SQLException, ClassNotFoundException {
    }

    @Override
    public void start(Promise<Void> startPromise) throws SQLException, ClassNotFoundException {

        Connection connection = Database.connection();


        System.out.println("databse verticle start");

        vertx.eventBus().consumer("database",handler->{

            JsonObject jsonObject = new JsonObject(handler.body().toString());

            System.out.println("json == > " + jsonObject);

            boolean result;

            try {
                result = Database.insert(connection,jsonObject);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            handler.reply(result);

        });

        startPromise.complete();

    }

}
