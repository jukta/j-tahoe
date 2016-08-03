package com.jukta.jtahoe.resource;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @since 1.0
 */
public class Res {

    public List<Resource> getResources(ResourceFilter resourceFilter) {
        try {
            return getResources1(resourceFilter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<Resource> getResources1(ResourceFilter resourceFilter) throws IOException, URISyntaxException {
        List<URL> roots = new ArrayList<>();
        List<Resource> resources = new ArrayList<>();
        ClassLoader cl = this.getClass().getClassLoader();
        Enumeration<URL> resourceUrls = (cl != null ? cl.getResources("") : ClassLoader.getSystemResources(""));
        while (resourceUrls.hasMoreElements()) {
            URL url = resourceUrls.nextElement();
            roots.add(url);
        }
        addAllUrls(cl, roots);

        for (URL url : roots) {
            String file = url.getFile();
            if (file.startsWith("/")) {
                file = file.substring(1);
            }
            if (file.endsWith(".jar")) {
                URLConnection con = url.openConnection();
                JarFile jarFile;
                if (con instanceof JarURLConnection) {
                    JarURLConnection jarCon = (JarURLConnection) con;
                    jarFile = jarCon.getJarFile();
                } else {
                    file = url.toURI().getSchemeSpecificPart();
                    jarFile = new JarFile(file);
                }
                Enumeration<JarEntry> en = jarFile.entries();
                while (en.hasMoreElements()) {
                    JarEntry jarEntry = en.nextElement();
                    Resource r = new JarResource(jarFile, jarEntry);
                    if (resourceFilter.accept(r)) {
                        resources.add(r);
                    }

                }
            } else {
                List<File> files = new ArrayList<>();
                scanDir(new File(file).getAbsoluteFile(), files);
                for (File f : files) {
                    Resource r = new FSResource(f);
                    if (resourceFilter.accept(r)) {
                        resources.add(r);
                    }
                }
            }
        }

        return resources;
    }

    private void scanDir(File f, List<File> files) {
        if (f.isDirectory()) {
            for (File f1 : f.listFiles()) {
                scanDir(f1, files);
            }
        } else {
            files.add(f);
        }
    }

    private void addAllUrls(ClassLoader classLoader, List<URL> result) {
        if (classLoader instanceof URLClassLoader) {
            for (URL url : ((URLClassLoader) classLoader).getURLs()) {
                if ("file".equals(url.getProtocol()) && url.getPath().toLowerCase().endsWith(".jar")) {
                    result.add(url);
                }
            }
        }
        if (classLoader != null) {
            addAllUrls(classLoader.getParent(), result);
        }
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        Res res = new Res();
        List<Resource> q = res.getResources(new ResourceFilter() {
            @Override
            public boolean accept(Resource resource) {
                return resource.getName().endsWith(".xml");
            }
        });
        System.out.println(q);
    }

}
