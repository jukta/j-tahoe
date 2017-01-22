package com.jukta.jtahoe.dev;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
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

        String[] dirs = env.getStaticDir();

        for (String d : dirs) {
            File f = new File(d.trim(), res);
            if (f.exists()) {
                FileInputStream fio = new FileInputStream(f);
                String cont = new String(IOUtils.readFully(fio, -1, false));
                exchange.getResponseSender().send(cont);
                return;
            }
        }
        next.handleRequest(exchange);
        return;

    }
}
