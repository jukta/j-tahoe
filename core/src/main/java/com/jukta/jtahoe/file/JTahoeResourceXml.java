package com.jukta.jtahoe.file;

import org.xml.sax.InputSource;
import sun.misc.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * Created by Dmitriy Dobrovolskiy on 09.04.2016.
 *
 * @since *.*.*
 */
public class JTahoeResourceXml implements JTahoeXml {
    private final byte[] xmlBytes;
    private final URI parentUri;

    public JTahoeResourceXml(InputStream inputStream, String parentUri) throws IOException {
        this.xmlBytes = IOUtils.readFully(inputStream, -1, false);
        this.parentUri = URI.create(parentUri);
    }

    @Override
    public InputSource getInputSource() {
        return new InputSource(new ByteArrayInputStream(xmlBytes));
    }

    @Override
    public URI getParentUri() {
        return parentUri;
    }
}
