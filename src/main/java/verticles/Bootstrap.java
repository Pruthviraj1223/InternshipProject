package verticles;

import io.vertx.core.Vertx;

public class Bootstrap {

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle("verticles.ApiHandler");

        vertx.deployVerticle("verticles.DiscoveryVerticle");

    }
}
