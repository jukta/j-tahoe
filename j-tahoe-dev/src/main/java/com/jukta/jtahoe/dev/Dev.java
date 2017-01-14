package com.jukta.jtahoe.dev;

import io.undertow.Undertow;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @since 1.0
 */
public class Dev {

    public static final String CONFIG_PROPERTIES = "config.properties";

    public static void main(String[] args) throws IOException {

        String pr = System.getProperty(CONFIG_PROPERTIES);
        if (pr == null) {
            pr = CONFIG_PROPERTIES;
            System.out.println("Could not find system property \"" + CONFIG_PROPERTIES + "\"");
        }

        Properties p = new Properties();

        if (new File(pr).exists()) {
            p.load(new FileReader(pr));
            System.out.println("Loading \"" + CONFIG_PROPERTIES + "\" from working directory");
        } else if (Dev.class.getClassLoader().getResource(CONFIG_PROPERTIES) != null) {
            p.load(Dev.class.getResourceAsStream(CONFIG_PROPERTIES));
            System.out.println("Loading \"" + CONFIG_PROPERTIES + "\" from classpath");
        } else {
            System.out.println("Could not find file \"" + CONFIG_PROPERTIES + "\" in classpath/working directory");
        }

        Env env = new Env(p);
        System.out.println("Using properties: ");
        System.out.println(env);

        PageHandler pageHandler = new PageHandler(env, null);
        StaticHandler staticHandler = new StaticHandler(env, pageHandler);
        CssHandler cssHandler = new CssHandler(env, staticHandler);
        JsHandler jsHandler = new JsHandler(env, cssHandler);

        Undertow server = Undertow.builder().addHttpListener(env.getPort(), "localhost", jsHandler).build();
        server.start();
        System.out.println("http://localhost:" + env.getPort() + "/<block_namespace_and_name>");
    }

}
