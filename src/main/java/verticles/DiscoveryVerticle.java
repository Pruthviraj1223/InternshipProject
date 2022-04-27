package verticles;

import io.vertx.core.AbstractVerticle;

import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DiscoveryVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {

        Discovery discovery = new Discovery();

        final Logger LOG = LoggerFactory.getLogger(DiscoveryVerticle.class);

        vertx.eventBus().consumer("discovery",handler->{

                vertx.executeBlocking(req->{

                    boolean result;

                    try {

                        result = discovery.ping(handler.body().toString());

                    } catch (IOException e) {

                        throw new RuntimeException(e);

                    }
                    if(result){

                        boolean outcome = discovery.ssh(handler.body().toString());

                        if (outcome) {

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
