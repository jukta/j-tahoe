package com.jukta.jtahoe.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @since 1.0
 */
public class DefaultResource implements Resource {

    private String name;
    private URL url;

    public DefaultResource(String name, URL url) {
        this.name = name;
        this.url = url;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getResourceName() {
        return name;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return url.openStream();
    }

    @Override
    public String toString() {
        return name;
    }
}
