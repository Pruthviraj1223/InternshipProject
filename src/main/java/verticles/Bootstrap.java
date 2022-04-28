package verticles;

import io.vertx.core.Vertx;

import java.util.logging.LogManager;

public class Bootstrap {

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle("verticles.ApiHandler").onComplete(handler ->
        {
            vertx.deployVerticle("verticles.DiscoveryVerticle").onComplete(handle1 ->
            {
                vertx.deployVerticle("verticles.DatabaseVerticle").onComplete(han ->
                {
                    if (han.succeeded())
                    {
                        System.out.println("deployed successfully");
                    }
                });

            });
        });



    }
}
