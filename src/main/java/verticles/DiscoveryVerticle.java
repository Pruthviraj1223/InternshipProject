package verticles;

import io.vertx.core.AbstractVerticle;

import io.vertx.core.Promise;

import io.vertx.core.json.JsonObject;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import java.io.IOException;

import java.sql.Connection;

import java.sql.SQLException;

public class DiscoveryVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {

        Discovery discovery = new Discovery();

        final Logger LOG = LoggerFactory.getLogger(DiscoveryVerticle.class);

        vertx.eventBus().consumer("discovery",handler->{


            JsonObject jsonObject = new JsonObject(handler.body().toString());



                vertx.executeBlocking(req->{

                    boolean result = false;

                    try {

                        if(!Database.checkIp(jsonObject)) {

                             result = discovery.ping(jsonObject);

                        }else{

                            handler.reply("Already discovered");

                        }

                    } catch (IOException | SQLException e) {

                        throw new RuntimeException(e);

                    }

                    if(result){

                        String outcome = discovery.plugin(jsonObject);

                        boolean answer= outcome.equalsIgnoreCase("true");

                        if (answer) {

                            vertx.eventBus().request("database",jsonObject, data->{

                                Object response = data.result().body();

                                LOG.debug("Result  {} ", response);

                                if(response.toString().equalsIgnoreCase("true")){

                                    handler.reply("success Discovery " + " Added in database");

                                }
                                else {

                                    handler.reply("Already exists in database");

                                }

                            });

                            req.complete(jsonObject);

                        } else {

                            handler.reply("Failed Discovery " + " Error is " + outcome);

                        }

                    }else{

                        handler.reply("Failed Ping discovery");



                    }



                });
        });

        startPromise.complete();

    }

}
