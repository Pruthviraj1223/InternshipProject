package verticles;

import io.vertx.core.Vertx;

public class MainVerticle {

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle("verticles.apiHandler");

        for(int i=0;i<5;i++) {

            vertx.deployVerticle("verticles.DiscoveryVerticle");

        }
    }
}
