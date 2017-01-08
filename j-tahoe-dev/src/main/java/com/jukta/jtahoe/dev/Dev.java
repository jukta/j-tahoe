package com.jukta.jtahoe.dev;

import io.undertow.Undertow;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @since 1.0
 */
public class Dev {

    public static void main(String[] args) throws IOException {

        String pr = System.getProperty("config.properties");
        if (pr == null) pr = "config.properties";

        Properties p = new Properties();
        p.load(new FileReader(pr));

        Env env = new Env(p);

        PageHandler pageHandler = new PageHandler(env, null);
        StaticHandler staticHandler = new StaticHandler(env, pageHandler);
        CssHandler cssHandler = new CssHandler(env, staticHandler);
        JsHandler jsHandler = new JsHandler(env, cssHandler);

        Undertow server = Undertow.builder()
                .addHttpListener(env.getPort(), "localhost")
                .setHandler(jsHandler).build();
        server.start();
    }

}
