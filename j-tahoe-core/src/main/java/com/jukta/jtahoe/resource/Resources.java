package com.jukta.jtahoe.resource;

import com.jukta.jtahoe.gen.file.JTahoeResourceXml;
import com.jukta.jtahoe.gen.file.JTahoeXml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Sidorov
 */
public class Resources {

    private String blocksFolder;

    public Resources(String blocksFolder) {
        this.blocksFolder = blocksFolder;
    }

    public List<JTahoeXml> getFiles(ResourceType resourceType) {
        FilenameFilter filter = new Resources.ExtensionFilter(resourceType.getExtension());
        URL url = getRoot();
        List<JTahoeXml> xmlFilesList = new ArrayList<>();
        try {
            getXmlFiles(xmlFilesList, new File(url.getFile()), filter, url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return xmlFilesList;
    }

    protected URL getRoot() {
        return getClass().getClassLoader().getResource(blocksFolder);
    }


    protected void getXmlFiles(List<JTahoeXml> xmlFilesList, File file, FilenameFilter filter, URL root) throws IOException, URISyntaxException {
        if (file.isDirectory()) {
            for (File entry : file.listFiles(filter)) {
                getXmlFiles(xmlFilesList, entry, filter, root);
            }
        } else {
            JTahoeXml jTahoeXml = new JTahoeResourceXml(new FileInputStream(file), root.toURI().relativize(file.getParentFile().toURI()).toString());
            xmlFilesList.add(jTahoeXml);
        }
    }

    protected static class ExtensionFilter implements FilenameFilter {

        private String extension;

        public ExtensionFilter(String extension) {
            this.extension = extension;
        }

        @Override
        public boolean accept(File dir, String name) {
            return new File(dir, name).isDirectory() || name.endsWith("." + extension);
        }
    }

}