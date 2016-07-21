package com.jukta.jtahoe.gen;

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
        factory.setNamespaceAware(true);
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

}
