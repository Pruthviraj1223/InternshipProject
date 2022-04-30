package verticles;

import io.vertx.core.Vertx;

public class Bootstrap {

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle("verticles.ApiHandler").onComplete(handler ->
        {
            vertx.deployVerticle("verticles.DiscoveryVerticle").onComplete(discoveryHandler ->
            {
                vertx.deployVerticle("verticles.DatabaseVerticle").onComplete(databaseHandler ->
                {
                    if (databaseHandler.succeeded()) {
                        System.out.println("deployed successfully");
                    }
                });
            });
        });
    }
}
