package com.jukta.jtahoe.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @since 1.0
 */
public class FSResource implements Resource {

    private File file;

    public FSResource(File file) {
        this.file = file;
    }

    @Override
    public String getName() {
        return file.getAbsolutePath();
    }

    @Override
    public InputStream getInputStream() {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return getName();
    }
}
