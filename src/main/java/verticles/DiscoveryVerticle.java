package verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;

import java.io.IOException;

public class DiscoveryVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        pingDiscovery pingDiscovery = new pingDiscovery();

        vertx.eventBus().consumer("discovery",handler->{

                vertx.executeBlocking(req->{

                    boolean result;

                    try {

                        result = pingDiscovery.ping(handler.body().toString());

                    } catch (IOException e) {

                        throw new RuntimeException(e);

                    }
                    if(result){

                        boolean discovery = pingDiscovery.ssh(handler.body().toString());

                        if (discovery) {

                            handler.reply("Successful Discovery");

                        } else {

                            handler.reply("Failed SSH Discovery");

                        }

                    }else{

                        handler.reply("Failed Ping discovery");

                    }

                });

        });
    }
}
