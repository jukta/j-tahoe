package com.jukta.jtahoe.gen;

import javax.tools.JavaFileObject;
import java.util.*;

/**
 * @author Sergey Sidorov
 */
public class GenContext {

//    private List<JavaFileObject> files;
    private Map<String, String> prefixes = new HashMap<>();
    private String currentNamespace;
    private Map<String, Package> files = new HashMap<>();
    private boolean keepSpaces = false;

    private String buildId;

    public GenContext() {
//        this.files = files;
    }

    public String getCurrentNamespace() {
        return currentNamespace;
    }

    public void setCurrentNamespace(String currentNamespace) {
        this.currentNamespace = currentNamespace;
    }

    public Map<String, Package> getFiles() {
        return files;
    }

    public Map<String, String> getPrefixes() {
        return prefixes;
    }

    public static class Package {
        private String packageName;
        private List<JavaFileObject> javaFileObjects = new ArrayList<>();
        private Set<String> dependedPackages = new HashSet<>();

        public Package(String packageName) {
            this.packageName = packageName;
        }

        public String getPackageName() {
            return packageName;
        }

        public List<JavaFileObject> getJavaFileObjects() {
            return javaFileObjects;
        }

        public Set<String> getDependedPackages() {
            return dependedPackages;
        }

        public void merge(Package p) {
            if (!p.packageName.equals(packageName)) {
                throw new RuntimeException("Could not merge packages " + p.packageName + " and " + packageName);
            }
            javaFileObjects.addAll(p.javaFileObjects);
            dependedPackages.addAll(p.dependedPackages);
        }
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public String getBuildId() {
        return buildId;
    }

    public boolean isKeepSpaces() {
        return keepSpaces;
    }

    public void setKeepSpaces(boolean keepSpaces) {
        this.keepSpaces = keepSpaces;
    }
}
