package com.jukta.jtahoe.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @since 1.0
 */
public class LibraryResources {
    private List<String> libs = new ArrayList<>();

    public LibraryResources() {

        CpResourceResolver res = new CpResourceResolver();
        List<Resource> r  = res.getResources(new ResourceFilter() {
            @Override
            public boolean accept(com.jukta.jtahoe.resource.Resource resource) {
                return resource.getName().endsWith("jtahoe.properties");
            }
        });
        try {
            for (Resource r1 : r) {
                Properties p = new Properties();
                p.load(r1.getInputStream());
                String libId = p.getProperty("lib.id");
                libs.add(libId);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Resource> getFiles(final ResourceType resourceType) {
        CpResourceResolver resolver = new CpResourceResolver();
        List<Resource> res = new ArrayList<>();
        for (final String id : libs) {
            List<Resource> r = resolver.getResources(new ResourceFilter() {
                @Override
                public boolean accept(Resource resource) {
                    return resource.getName().endsWith(id + "." + resourceType.getExtension());
                }
            });
            res.addAll(r);
        }
        return res;
    }
}
