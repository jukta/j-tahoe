package com.jukta.jtahoe.resource;

import java.io.InputStream;

/**
 * @since 1.0
 */
public interface Resource {

    String getName();

    InputStream getInputStream();
}