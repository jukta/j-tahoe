package com.jukta.jtahoe;

import com.jukta.jtahoe.file.JTahoeXml;

import javax.tools.JavaFileObject;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sergey Sidorov
 */
public class GenContext {

    private File rootDir;
    private List<JavaFileObject> files;
    private JTahoeXml currentFile;
    private Map<String, String> prefixes = new HashMap<>();

    public GenContext(File root, List<JavaFileObject> files) {
        this.rootDir = root;
        this.files = files;
    }

    public File getRootDir() {
        return rootDir;
    }

    public void setRootDir(File rootDir) {
        this.rootDir = rootDir;
    }

    public List<JavaFileObject> getFiles() {
        return files;
    }

    public void setFiles(List<JavaFileObject> files) {
        this.files = files;
    }

    public JTahoeXml getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(JTahoeXml currentFile) {
        this.currentFile = currentFile;
    }

    public Map<String, String> getPrefixes() {
        return prefixes;
    }
}
