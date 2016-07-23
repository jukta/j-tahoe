package com.jukta.jtahoe.gen.xml;

import com.jukta.jtahoe.BlockModelProvider;
import com.jukta.jtahoe.gen.file.JTahoeXml;
import com.jukta.jtahoe.gen.model.NamedNode;
import com.jukta.jtahoe.resource.ResourceType;
import com.jukta.jtahoe.resource.Resources;
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

    private String blocksFolder;

    public XmlBlockModelProvider(String blocksFolder) {
        this.blocksFolder = blocksFolder;
    }

    @Override
    public Iterator<NamedNode> iterator() {
        try {
            List<JTahoeXml> xmlList = new Resources(blocksFolder).getFiles(ResourceType.XML);
            return new BlockModelIterator(xmlList.iterator());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private class BlockModelIterator implements Iterator<NamedNode> {

        private Iterator<JTahoeXml> iterator;
        private XMLReader xmlReader;

        private BlockModelIterator(Iterator<JTahoeXml> iterator) throws ParserConfigurationException, SAXException {
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
                JTahoeXml xml = iterator.next();
                FileHandler fileHandler = new FileHandler();
                xmlReader.setContentHandler(fileHandler);
                xmlReader.parse(xml.getInputSource());
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
