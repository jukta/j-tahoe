package com.jukta.jtahoe;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.tools.JavaFileObject;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Sidorov
 */
public class DirHandler {

    private File rootDir;

    public DirHandler(File rootDir) {
        this.rootDir = rootDir;
    }

    public DirHandler(String rootPath) {
        this(new File(rootPath));
    }

    public List<JavaFileObject> getFiles() throws ParserConfigurationException, SAXException, IOException {
        List<File> l = new ArrayList<>();
        scan(rootDir, l, new FileExtensionFilter("xml"));
        List<JavaFileObject> files = new ArrayList<>();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(false);
        XMLReader xmlReader = factory.newSAXParser().getXMLReader();
        for (File f : l) {
            GenContext genContext = new GenContext(rootDir, files, f);
            FileHandler fileHandler = new FileHandler(genContext);
            xmlReader.setContentHandler(fileHandler);
            xmlReader.parse(f.getAbsolutePath());
        }
        return files;
    }

    public void generateSources(File targetDir) throws IOException, SAXException, ParserConfigurationException {
        List<JavaFileObject> files = getFiles();
        for (JavaFileObject f : files) {
            System.out.println(f.getName());
            BufferedReader reader = new BufferedReader(f.openReader(false));
            File file = new File(targetDir, f.getName());
            file.getParentFile().mkdirs();
            FileWriter w = new FileWriter(file);
            String line = null;
            while ((line = reader.readLine()) != null) {
                w.append(line);
            }
            w.close();
            reader.close();
        }
    }

    private void scan(File file, List<File> list, FileFilter filter) {
        for (File f : file.listFiles(filter)) {
            if (f.isDirectory()) {
                scan(f, list, filter);
            } else {
                list.add(f);
            }
        }
    }

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        DirHandler h = new DirHandler(new File("E:\\SANDBOX\\repos\\jtahoe\\j-tahoe\\samples\\blocks"));
        File target = new File("E:\\SANDBOX\\repos\\jtahoe\\j-tahoe\\samples\\gen");
        target.mkdirs();
        h.generateSources(target);
    }

    class FileExtensionFilter implements FileFilter {
        private String ext;

        FileExtensionFilter(String ext) {
            this.ext = ext;
        }

        @Override
        public boolean accept(File pathname) {
            return pathname.isDirectory() || pathname.getName().endsWith("." + ext);
        }
    }

}
