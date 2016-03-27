package com.jukta.jtahoe;

import com.jukta.jtahoe.handlers.*;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import javax.tools.JavaFileObject;
import java.util.*;

/**
 * Created by aleph on 26.03.2016.
 */
public class FileHandler implements ContentHandler {

    private Stack<AbstractHandler> stack = new Stack<AbstractHandler>();
    private List<JavaFileObject> files = new ArrayList<JavaFileObject>();
    private GenContext genContext;


    public FileHandler(GenContext genContext) {
        this.genContext = genContext;
    }

    public List<JavaFileObject> getFiles() {
        return files;
    }

    public void setDocumentLocator(Locator locator) {

    }

    public void startDocument() throws SAXException {

    }

    public void endDocument() throws SAXException {

    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {

    }

    public void endPrefixMapping(String prefix) throws SAXException {

    }

    public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
        AbstractHandler handler = null;
        Map<String, String> a = new HashMap<String, String>();
        AbstractHandler parent = null;
        if (!stack.isEmpty()) parent = stack.peek();
        for (int i = 0; i < attrs.getLength(); i++) {
            a.put(attrs.getLocalName(i), attrs.getValue(i));
        }
        if (qName.equals("sv:root")) {
            handler = new RootHandler(genContext, qName, a, parent);
        } else if (qName.equals("sv:block")) {
            handler = new BlockHandler(genContext, qName, a, parent);
        } else if (qName.startsWith("sv:def")) {
            handler = new DefHandler(genContext, qName, a, parent);
        } else if (qName.startsWith("sv:parent")) {
            handler = new ParentHandler(genContext, qName, a, parent);
        } else if (qName.startsWith("sv:")) {
            handler = new FuncHandler(genContext, qName, a, parent);
        } else {
            handler = new HtmlHandler(genContext, qName, a, parent);
        }
        stack.push(handler);
        handler.start();
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        stack.pop().end();
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        stack.peek().text(new String(ch, start, length));
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {

    }

    public void processingInstruction(String target, String data) throws SAXException {

    }

    public void skippedEntity(String name) throws SAXException {

    }
}
