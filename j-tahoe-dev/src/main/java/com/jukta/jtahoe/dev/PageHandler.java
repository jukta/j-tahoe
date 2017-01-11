package com.jukta.jtahoe.dev;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.Block;
import com.jukta.jtahoe.DataHandlerProvider;
import com.jukta.jtahoe.RuntimeBlockFactory;
import com.jukta.jtahoe.gen.xml.XthBlockModelProvider;
import com.jukta.jtahoe.jschema.JAttrs;
import com.jukta.jtahoe.jschema.JBody;
import com.jukta.jtahoe.jschema.JElement;
import com.jukta.jtahoe.jschema.JTag;
import com.jukta.maven.FileSystemResources;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.io.File;
import java.io.IOException;
import java.util.Deque;
import java.util.Map;

/**
 * @since 1.0
 */
public class PageHandler implements HttpHandler {

    private final HttpHandler next;
    private Env env;

    public PageHandler(Env env, HttpHandler next) {
        this.next = next;
        this.env = env;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {

        String blockName = exchange.getRelativePath();
        Deque<String> d = exchange.getQueryParameters().get("data");
        String data = null;
        if (d != null) {
            data = d.getFirst();
        }

        blockName = blockName.replaceAll("/", ".");
        if (blockName.startsWith(".")) blockName = blockName.substring(1);
        RuntimeBlockFactory factory = null;
        try {
            factory = new RuntimeBlockFactory(new XthBlockModelProvider(env.getResourceResolver()));

            Block block = factory.create(blockName);

            Attrs attrs = new Attrs();
            attrs.setDataHandlerProvider(new DataHandlerProvider() {
                @Override
                public void getData(String dataHandler, Attrs attrs, Block.Callback callback) {
                    Attrs a = new Attrs();
                    try {
                        loadData(dataHandler, a);
                    } catch (Exception e) {}
                    callback.call();
                }
            });
            if (data != null) {
                loadData(data, attrs);
            }
            JElement b = block.body(attrs);
            if (env.isWrap()) {
                b = buildWrapper(b);
            }

            String html = b.toHtml();
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
            exchange.getResponseSender().send(html);
            return;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            if (factory != null) factory.close();
        }

//        next.handleRequest(exchange);
    }

    private JElement buildWrapper(JElement block) {
        JTag body = new JTag("body");
        JTag head = new JTag("head");
        JBody headBody = new JBody();
        head.setjBody(headBody);

        for (String c : env.getCss()) {
            headBody.addElement(new JTag("link").setAttrs(new JAttrs().addAttr("rel", "stylesheet").addAttr("href", c).addAttr("type", "text/css")));
        }

        for (String c : env.getJs()) {
            headBody.addElement(new JTag("script").setAttrs(new JAttrs().addAttr("src", c).addAttr("type", "text/javascript")).setjBody(new JBody()));
        }


        JElement page = new JTag("html").setjBody(new JBody().addElement(head).addElement(body));
        body.setjBody(new JBody().addElement(block));
        return page;
    }

    private void loadData(String file, Attrs attrs) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File jsonFile = new File(env.getDataDir(), file + ".json");
        if (jsonFile.exists()) {
            Map<String, Object> mapObject = mapper.readValue(jsonFile, new TypeReference<Map<String, Object>>() {
            });

            for (Map.Entry<String, Object> entry : mapObject.entrySet()) {
                attrs.set(entry.getKey(), entry.getValue());
            }
        }
    }

}
