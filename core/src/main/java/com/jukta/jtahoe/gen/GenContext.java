package com.jukta.jtahoe.gen;

import javax.tools.JavaFileObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sergey Sidorov
 */
public class GenContext {

    private List<JavaFileObject> files;
    private Map<String, String> prefixes = new HashMap<>();
    private String currentNamespace;

    public GenContext(List<JavaFileObject> files) {
        this.files = files;
    }

    public String getCurrentNamespace() {
        return currentNamespace;
    }

    public void setCurrentNamespace(String currentNamespace) {
        this.currentNamespace = currentNamespace;
    }

    public List<JavaFileObject> getFiles() {
        return files;
    }

    public void setFiles(List<JavaFileObject> files) {
        this.files = files;
    }

    public Map<String, String> getPrefixes() {
        return prefixes;
    }

}
