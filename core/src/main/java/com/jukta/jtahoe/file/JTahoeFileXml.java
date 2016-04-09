package com.jukta.jtahoe.file;

import org.xml.sax.InputSource;

import java.io.File;
import java.net.URI;

/**
 * Created by Dmitriy Dobrovolskiy on 09.04.2016.
 *
 * @since *.*.*
 */
public class JTahoeFileXml implements JTahoeXml {
    private final File xmlFile;

    public JTahoeFileXml(File file) {
        this.xmlFile = file;
    }

    @Override
    public InputSource getInputSource() {
        return new InputSource(xmlFile.toURI().toString());
    }

    @Override
    public URI getParentUri() {
        return xmlFile.getParentFile().toURI();
    }
}
