package verticles;

import io.vertx.core.AbstractVerticle;

import io.vertx.core.Promise;

import io.vertx.core.json.JsonObject;

import java.sql.*;

public class DatabaseVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws SQLException, ClassNotFoundException {

        Database.connection();

        vertx.eventBus().consumer("database",handler->{

            JsonObject jsonObject = new JsonObject(handler.body().toString());

            vertx.executeBlocking(req->{
                boolean result;

                try {

                    result = Database.insert(jsonObject);

                } catch (SQLException e) {

                    throw new RuntimeException(e);

                }

                handler.reply(result);

//                handler.fail();

            });


        });

        startPromise.complete();

    }

}
