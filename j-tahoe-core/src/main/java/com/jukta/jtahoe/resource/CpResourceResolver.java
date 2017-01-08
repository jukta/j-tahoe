package com.jukta.jtahoe.resource;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @since 1.0
 */
public class CpResourceResolver implements ResourceResolver {

    private static Map<ResourceType, List<Resource>> resourceMap = new HashMap<>();
    private static AtomicBoolean load = new AtomicBoolean(false);

    private ResourceType blockResourceType;
    private List<ResourceType> relatedResourceType;

    public CpResourceResolver() {
        blockResourceType = ResourceType.XTH;
        relatedResourceType = new ArrayList<>();
        relatedResourceType.add(ResourceType.CSS);
        relatedResourceType.add(ResourceType.JS);
        init();
    }

    private void init() {
        try {
            if (load.get()) return;
            ResourceFilter rFilter = new ResourceExtensionFilter(blockResourceType);
            List<Resource> l = getResources1(rFilter);

            resourceMap.put(blockResourceType, l);
            ClassLoader cl = this.getClass().getClassLoader();
            for (Resource res : l) {
                String n = res.getResourceName();
                n = n.substring(0, n.length() - blockResourceType.getExtension().length() - 1);

                for (ResourceType type : relatedResourceType) {
                    URL url = cl.getResource(n + "." + type.getExtension());
                    if (url != null) {
                        List<Resource> _r = resourceMap.get(type);
                        if (_r == null) {
                            _r = new ArrayList<>();
                            resourceMap.put(type, _r);
                        }
                        _r.add(new DefaultResource(n + "." + type.getExtension(), url.openStream()));
                    }
                }
            }
            load.set(true);
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
                File root = new File(file).getAbsoluteFile();
                scanDir(root, root, resources, resourceFilter);
            }
        }

        return resources;
    }

    protected void scanDir(File root, File f, List<Resource> resources, ResourceFilter resourceFilter) {
        if (f.isDirectory()) {
            for (File f1 : f.listFiles()) {
                scanDir(root, f1, resources, resourceFilter);
            }
        } else {
            Resource r = new FSResource(root, f);
            if (resourceFilter.accept(r)) {
                resources.add(r);
            }
        }
    }

    private void addAllUrls(ClassLoader classLoader, List<URL> result) {
        if (classLoader instanceof URLClassLoader) {
            for (URL url : ((URLClassLoader) classLoader).getURLs()) {
                if ("file".equals(url.getProtocol()) && url.getPath().toLowerCase().endsWith(".jar")) {
                    result.add(url);
                }
            }
        } else if (classLoader != null && "org.jboss.modules.ModuleClassLoader".equals(classLoader.getClass().getName())) {
            addVfsUrls(classLoader, result);
        }
        if (classLoader != null) {
            addAllUrls(classLoader.getParent(), result);
        }
    }

    private void addVfsUrls(ClassLoader classLoader, List<URL> result) {
        try {
            Enumeration<URL> resourceUrls = classLoader.getResources("");
            while (resourceUrls.hasMoreElements()) {
                URL url = resourceUrls.nextElement();
                File vfile = VfsUtils.getFile(url);
                for (File file : vfile.getParentFile().listFiles()) {
                    result.add(file.toURI().toURL());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Resource> getResources(ResourceType resourceType) {
        return resourceMap.get(resourceType);
    }
}
