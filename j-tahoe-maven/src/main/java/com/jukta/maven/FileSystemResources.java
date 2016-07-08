package com.jukta.maven;

import com.jukta.jtahoe.resource.Resources;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @since 1.0
 */
public class FileSystemResources extends Resources {

    private String blocksFolder;

    public FileSystemResources(String blocksFolder) {
        super(blocksFolder);
        this.blocksFolder = blocksFolder;
    }

    @Override
    protected URL getRoot() {
        try {
            return new File(blocksFolder).toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
