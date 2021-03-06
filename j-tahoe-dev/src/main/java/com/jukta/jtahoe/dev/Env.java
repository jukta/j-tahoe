package com.jukta.jtahoe.dev;

import com.jukta.jtahoe.resource.CompositeResourceResolver;
import com.jukta.jtahoe.resource.ResourceResolver;
import com.jukta.maven.FileSystemResources;

import java.util.Arrays;
import java.util.Properties;

/**
 * @since 1.0
 */
public class Env {

    private String blocksDir = "blocks";
    private String[] blocksDirs;
    private String dataDir = "data";
    private boolean wrap = true;
    private String[] js;
    private String[] css;
    private int port = 8080;
    private String resourceName = "all";
    private String[] staticDirs = new String[] {"public"};

    public Env(Properties p) {
        wrap = "true".equals(p.getProperty("wrapBlock", "" + wrap));
        blocksDirs = p.getProperty("blocksDir", blocksDir).split(",");
        dataDir = p.getProperty("dataDir", dataDir);
        js = p.getProperty("head.js", "").split(",");
        css = p.getProperty("head.css", "").split(",");
        port = Integer.parseInt(p.getProperty("port", "" + port));
        resourceName = p.getProperty("resourceName", resourceName);
        staticDirs = p.getProperty("staticDir", "public").split(",");
    }

    public String getBlocksDir() {
        return blocksDir;
    }

    public String getDataDir() {
        return dataDir;
    }

    public boolean isWrap() {
        return wrap;
    }

    public String[] getJs() {
        return js;
    }

    public String[] getCss() {
        return css;
    }

    public int getPort() {
        return port;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getJsResourceName() {
        return "/" + resourceName + ".js";
    }

    public String getCssResourceName() {
        return "/" + resourceName + ".css";
    }

    public String[] getStaticDir() {
        return staticDirs;
    }

    public ResourceResolver getResourceResolver() {
        CompositeResourceResolver resolver = new CompositeResourceResolver();
        for (String d : blocksDirs) {
            resolver.addResolver(new FileSystemResources(d.trim()));
        }
        return resolver;
    }

    @Override
    public String toString() {
        return  "\tblocksDir=" + String.join(",", Arrays.asList(blocksDirs)) + '\n' +
                "\tdataDir=" + dataDir + '\n' +
                "\twrapBlock=" + wrap + '\n' +
                "\thead.js=" + String.join(",", Arrays.asList(js)) + '\n' +
                "\thead.css=" + String.join(",", Arrays.asList(css)) + '\n' +
                "\tport=" + port + '\n' +
                "\tresourceName=" + resourceName + '\n' +
                "\tstaticDir=" + staticDirs;
    }
}
