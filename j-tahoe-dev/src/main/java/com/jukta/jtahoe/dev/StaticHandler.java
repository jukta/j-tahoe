package com.jukta.jtahoe.dev;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import sun.misc.IOUtils;

import java.io.File;
import java.io.FileInputStream;

/**
 * @since 1.0
 */
public class StaticHandler implements HttpHandler {

    private final HttpHandler next;
    private Env env;

    public StaticHandler(Env env, HttpHandler next) {
        this.next = next;
        this.env = env;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        String res = exchange.getRelativePath();

        File f = new File(env.getStaticDir(), res);

        if (!f.exists()) {
            next.handleRequest(exchange);
            return;
        }

        FileInputStream fio = new FileInputStream(f);
        String cont = new String(IOUtils.readFully(fio, -1, false));

//        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/css");
        exchange.getResponseSender().send(cont);

    }
}
