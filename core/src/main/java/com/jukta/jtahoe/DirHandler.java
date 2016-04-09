package com.jukta.jtahoe;

import com.jukta.jtahoe.file.JTahoeFileXml;
import com.jukta.jtahoe.file.JTahoeXml;
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

    public List<JavaFileObject> getJavaFiles(List<JTahoeXml> xmlList) throws ParserConfigurationException, SAXException, IOException {
        List<JavaFileObject> javaFiles = new ArrayList<>();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(false);
        XMLReader xmlReader = factory.newSAXParser().getXMLReader();
        GenContext genContext = new GenContext(rootDir, javaFiles);
        for (JTahoeXml xml : xmlList) {
            genContext.setCurrentFile(xml);
            FileHandler fileHandler = new FileHandler(genContext);
            xmlReader.setContentHandler(fileHandler);
            xmlReader.parse(xml.getInputSource());
        }
        return javaFiles;
    }

    public void generateSources(File targetDir) throws IOException, SAXException, ParserConfigurationException {
        List<JTahoeXml> xmlFilesList = new ArrayList<>();
        scan(rootDir, xmlFilesList, new FileExtensionFilter("xml"));
        List<JavaFileObject> files = getJavaFiles(xmlFilesList);
        for (JavaFileObject f : files) {
            System.out.println(f.getName());
            BufferedReader reader = new BufferedReader(f.openReader(false));
            File file = new File(targetDir, f.getName());
            file.getParentFile().mkdirs();
            FileWriter w = new FileWriter(file);
            String line;
            while ((line = reader.readLine()) != null) {
                w.append(line);
            }
            w.close();
            reader.close();
        }
    }

    private void scan(File file, List<JTahoeXml> list, FileFilter filter) {
        for (File f : file.listFiles(filter)) {
            if (f.isDirectory()) {
                scan(f, list, filter);
            } else {
                list.add(new JTahoeFileXml(f));
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
