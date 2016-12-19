package com.jukta.jtahoe.resource;

import java.io.InputStream;

/**
 * @since 1.0
 */
public class DefaultResource implements Resource {

    private String name;
    private InputStream inputStream;

    public DefaultResource(String name, InputStream inputStream) {
        this.name = name;
        this.inputStream = inputStream;
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
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public String toString() {
        return name;
    }
}
