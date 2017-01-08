package com.jukta.jtahoe.dev;

import com.jukta.jtahoe.resource.LibraryResources;
import com.jukta.jtahoe.resource.Resource;
import com.jukta.jtahoe.resource.ResourceAppender;
import com.jukta.jtahoe.resource.ResourceType;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.io.IOException;
import java.util.List;

/**
 * @since 1.0
 */
public class JsHandler implements HttpHandler {

    private final HttpHandler next;
    private Env env;

    public JsHandler(Env env, HttpHandler next) throws IOException {
        this.next = next;
        this.env = env;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        String res = exchange.getRelativePath();
        if (!env.getJsResourceName().equals(res)) {
            next.handleRequest(exchange);
            return;
        }

        LibraryResources lr = new LibraryResources();
        List<Resource> files = lr.getFiles(ResourceType.JS);
        files.addAll(env.getResourceResolver().getResources(ResourceType.JS));
        StringBuilder sb = ResourceAppender.append(files);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/javascript");
        exchange.getResponseSender().send(sb.toString());

    }
}
