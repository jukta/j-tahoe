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
public class XmlBlockModelProvider implements BlockModelProvider {

    private ResourceResolver resources;

    public XmlBlockModelProvider(ResourceResolver resources) {
        this.resources = resources;
    }

    public XmlBlockModelProvider(String blocksFolder) {
        this.resources = new Resources(blocksFolder);
    }

    @Override
    public Iterator<NamedNode> iterator() {
        try {
            List<Resource> xmlList = resources.getResources(new ResourceExtensionFilter(ResourceType.XML));
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
            try {
                Resource xml = iterator.next();
                FileHandler fileHandler = new FileHandler();
                xmlReader.setContentHandler(fileHandler);
                xmlReader.parse(new InputSource(xml.getInputStream()));
                return fileHandler.getNode();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

}
