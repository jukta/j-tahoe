package com.jukta.jtahoe.dev;

import com.jukta.jtahoe.resource.*;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.util.Collections;
import java.util.List;

/**
 * @since 1.0
 */
public class CssHandler implements HttpHandler {

    private final HttpHandler next;
    private Env env;

    public CssHandler(Env env, HttpHandler next) {
        this.next = next;
        this.env = env;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        String res = exchange.getRelativePath();

        if (!res.endsWith(".css")) {
            next.handleRequest(exchange);
            return;
        }

        LibraryResources lr = new LibraryResources();
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/css");

        if (env.getCssResourceName().equals(res)) {
            List<Resource> files = lr.getFiles(ResourceType.CSS);
            files.addAll(env.getResourceResolver().getResources(ResourceType.CSS));
            StringBuilder sb = ResourceAppender.append(files);
            exchange.getResponseSender().send(sb.toString());
        } else {
            Resource r = lr.getFile(res);
            if (r == null) {
                next.handleRequest(exchange);
                return;
            }
            StringBuilder sb = ResourceAppender.append(Collections.singletonList(r));
            exchange.getResponseSender().send(sb.toString());
        }
    }
}
