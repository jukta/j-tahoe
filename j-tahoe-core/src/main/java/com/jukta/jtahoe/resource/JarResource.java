package com.jukta.jtahoe.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @since 1.0
 */
public class JarResource implements Resource {
    private ZipFile zipFile;
    private ZipEntry zipEntry;

    public JarResource(ZipFile zipFile, ZipEntry zipEntry) {
        this.zipFile = zipFile;
        this.zipEntry = zipEntry;
    }

    @Override
    public String getName() {
        return zipEntry.getName();
    }

    @Override
    public String getResourceName() {
        return getName();
    }

    @Override
    public InputStream getInputStream() {
        try {
            return zipFile.getInputStream(zipEntry);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return getName();
    }
}
