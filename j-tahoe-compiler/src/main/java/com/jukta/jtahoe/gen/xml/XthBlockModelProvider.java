package com.jukta.jtahoe.gen.xml;

import com.jukta.jtahoe.BlockModelProvider;
import com.jukta.jtahoe.gen.model.NamedNode;
import com.jukta.jtahoe.resource.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.util.Iterator;
import java.util.List;

/**
 * @since 1.0
 */
public class XthBlockModelProvider implements BlockModelProvider {

    private ResourceResolver resources;

    public XthBlockModelProvider(ResourceResolver resources) {
        this.resources = resources;
    }

    public XthBlockModelProvider() {
        this.resources = new Resources();
    }

    @Override
    public Iterator<NamedNode> iterator() {
        try {
            List<Resource> xmlList = resources.getResources(ResourceType.XTH);
            return new BlockModelIterator(xmlList.iterator());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private class BlockModelIterator implements Iterator<NamedNode> {

        private Iterator<Resource> iterator;
        private XMLReader xmlReader;

        private BlockModelIterator(Iterator<Resource> iterator) throws ParserConfigurationException, SAXException {
            this.iterator = iterator;
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            xmlReader = factory.newSAXParser().getXMLReader();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public NamedNode next() {
            Resource xml = iterator.next();
            try {
                FileHandler fileHandler = new FileHandler();
                xmlReader.setContentHandler(fileHandler);
                xmlReader.parse(new InputSource(xml.getInputStream()));
                return fileHandler.getNode();
            } catch (Exception e) {
                throw new RuntimeException("Error parsing file \"" + xml.getName() + "\"", e);
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

}
