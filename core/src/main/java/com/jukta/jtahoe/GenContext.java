package com.jukta.jtahoe;

import javax.tools.JavaFileObject;
import java.io.File;
import java.util.List;

/**
 * @author Sergey Sidorov
 */
public class GenContext {

    private File rootDir;
    private List<JavaFileObject> files;
    private File currentFile;

    public GenContext(File root, List<JavaFileObject> files, File currentFile) {
        this.rootDir = root;
        this.files = files;
        this.currentFile = currentFile;
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

    public File getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
    }
}
