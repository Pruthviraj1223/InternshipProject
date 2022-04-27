package verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class apiHandler extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {

        Router router = Router.router(vertx);

        HttpServer httpServer = vertx.createHttpServer();

        router.route().handler(BodyHandler.create());

        router.post("/Discovery").handler(handler->{

            JsonObject jsonObject = handler.getBodyAsJson();

            vertx.eventBus().request("discovery",jsonObject,response->{

                if(response.succeeded()) {

                    handler.response().end(response.result().body().toString());

                }else{

                    handler.response().end("Failed");

                }
            });

        });

        httpServer.requestHandler(router).listen(8080);


    }
}
