package com.jukta.jtahoe.gen.xml;

import com.jukta.jtahoe.model.NamedNode;
import com.jukta.jtahoe.model.TextNode;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.util.*;

/**
 * Created by aleph on 26.03.2016.
 */
public class FileHandler implements ContentHandler {


    public static final String XHTML = "http://www.w3.org/1999/xhtml";
    private Stack<NamedNode> stack = new Stack<>();
    private NamedNode node;
    private Map<String, String> prefixes = new HashMap<>();

    public NamedNode getNode() {
        return node;
    }

    public void setDocumentLocator(Locator locator) {

    }

    public void startDocument() throws SAXException {

    }

    public void endDocument() throws SAXException {

    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        if (uri.equals(XHTML)) {
            uri = "__html__";
        }
        prefixes.put(prefix, uri);
    }

    public void endPrefixMapping(String prefix) throws SAXException {

    }

    public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
        Map<String, String> attributes = new HashMap<>();
        for (int i = 0; i < attrs.getLength(); i++) {
            attributes.put(attrs.getLocalName(i), attrs.getValue(i));
        }
        NamedNode parent = null;
        if (!stack.isEmpty()) {
            parent = stack.peek();
        }
        if (uri.equals(XHTML)) uri = "__html__";
        NamedNode node = new NamedNode(uri, localName, attributes, parent);
        node.getPrefixes().putAll(prefixes);
        prefixes.clear();
        if (parent == null) {
            this.node = node;
        } else {
            parent.getChildren().add(node);
        }
        stack.push(node);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        stack.pop();
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        stack.peek().getChildren().add(new TextNode(new String(ch, start, length)));
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {

    }

    public void processingInstruction(String target, String data) throws SAXException {

    }

    public void skippedEntity(String name) throws SAXException {

    }
}
