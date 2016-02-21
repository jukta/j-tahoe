package com.jukta.jtahoe;

import com.jukta.jtahoe.handlers.*;
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
public class Compile implements ContentHandler {

    List<JavaFileObject> files = new ArrayList<JavaFileObject>();

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(false);
        XMLReader xmlReader = factory.newSAXParser().getXMLReader();
        Compile compile = new Compile();
        xmlReader.setContentHandler(compile);
        xmlReader.parse("blocks.xml");

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
//        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(compile.files);
        JavaCompiler.CompilationTask task = compiler.getTask(null, null, diagnostics, null, null, compile.files);
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

        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { new File("").toURI().toURL() });
        Block block = (Block) Class.forName("blockB", true, classLoader).newInstance();
        System.out.println(block.body(new Attrs().set("x", 123)).toHtml());

    }


    private Stack<AbstractHandler> stack = new Stack<AbstractHandler>();


    @Override
    public void setDocumentLocator(Locator locator) {

    }

    @Override
    public void startDocument() throws SAXException {

    }

    @Override
    public void endDocument() throws SAXException {

    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {

    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
        AbstractHandler handler = null;
        Map<String, String> a = new HashMap<String, String>();
        AbstractHandler parent = null;
        if (!stack.isEmpty()) parent = stack.peek();
        for (int i = 0; i < attrs.getLength(); i++) {
            a.put(attrs.getLocalName(i), attrs.getValue(i));
        }
        if (qName.equals("sv:root")) {
            handler = new RootHandler(qName, a, parent);
        } else if (qName.equals("sv:block")) {
            handler = new BlockHandler(qName, a, parent);
            ((BlockHandler) handler).setFiles(files);
        } else if (qName.startsWith("sv:def")) {
            handler = new DefHandler(qName, a, parent);
        } else if (qName.startsWith("sv:")) {
            handler = new FuncHandler(qName, a, parent);
        } else {
            handler = new HtmlHandler(qName, a, parent);
        }
        stack.push(handler);
        handler.start();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        stack.pop().end();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        stack.peek().text(new String(ch, start, length));
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {

    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {

    }

    @Override
    public void skippedEntity(String name) throws SAXException {

    }
}
