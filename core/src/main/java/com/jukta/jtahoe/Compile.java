package com.jukta.jtahoe;

import org.xml.sax.*;

import javax.tools.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * Created by aleph on 17.02.2016.
 */
public class Compile {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        List<JavaFileObject> files = new ArrayList<JavaFileObject>();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(false);
        XMLReader xmlReader = factory.newSAXParser().getXMLReader();
        GenContext genContext = new GenContext(null, files);
        genContext.setCurrentFile(new File("blocks.xml"));
        FileHandler fileHandler = new FileHandler(genContext);
        xmlReader.setContentHandler(fileHandler);
        xmlReader.parse("blocks.xml");

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
//        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(compile.files);
        JavaCompiler.CompilationTask task = compiler.getTask(null, null, diagnostics, null, null, fileHandler.getFiles());
        boolean success = task.call();

        for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
            System.out.println(diagnostic.getCode());
            System.out.println(diagnostic.getKind());
            System.out.println(diagnostic.getPosition());
            System.out.println(diagnostic.getStartPosition());
            System.out.println(diagnostic.getEndPosition());
            System.out.println(diagnostic.getSource());
            System.out.println(diagnostic.getMessage(null));

        }
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new File("").toURI().toURL()});

        long t = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            Block block = (Block) Class.forName("blockC", true, classLoader).newInstance();
            String s = block.body(new Attrs().set("x", 123)).toHtml();
            System.out.println(s);
        }
        System.out.println(System.currentTimeMillis() - t);
    }

}
