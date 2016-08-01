package com.jukta.jtahoe.gen.file;

import org.xml.sax.InputSource;

import java.net.URI;

/**
 * Created by Dmitriy Dobrovolskiy on 04.04.2016.
 *
 * @since *.*.*
 */
public interface JTahoeXml {

    InputSource getInputSource();

     URI getParentUri();
}
