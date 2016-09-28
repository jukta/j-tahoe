package com.jukta.jtahoe.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * @since 1.0
 */
public class LibraryResources {
    private List<String> libs = new ArrayList<>();

    public LibraryResources() {
        ClassLoader cl = this.getClass().getClassLoader();
        try {
            Enumeration<URL> enumeration = cl.getResources("jtahoe.properties");
            while (enumeration.hasMoreElements()) {
                InputStream is = enumeration.nextElement().openStream();
                Properties p = new Properties();
                p.load(is);
                String libId = p.getProperty("lib.id");
                libs.add(libId);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Resource> getFiles(final ResourceType resourceType) {
        List<Resource> res = new ArrayList<>();
        ClassLoader cl = this.getClass().getClassLoader();
        for (final String id : libs) {
            InputStream stream = cl.getResourceAsStream(id + "." + resourceType.getExtension());
            if (stream != null) {
                res.add(new DefaultResource(id, stream));
            }
        }
        return res;
    }

    public Resource getFile(String name) {
        ClassLoader cl = this.getClass().getClassLoader();
        for (final String id : libs) {
            InputStream stream = cl.getResourceAsStream(id + name);
            if (stream != null) {
                return new DefaultResource(name, stream);
            }
        }
        return null;
    }
}
