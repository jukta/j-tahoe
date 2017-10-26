package com.jukta.jtahoe.gen.xml;

import com.jukta.jtahoe.BlockModelProvider;
import com.jukta.jtahoe.gen.model.NamedNode;
import com.jukta.jtahoe.resource.Resource;
import com.jukta.jtahoe.resource.ResourceResolver;
import com.jukta.jtahoe.resource.ResourceType;
import com.jukta.jtahoe.resource.Resources;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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
//            xmlList = xmlList.stream().filter(resource -> resource.getName().contains("parent.xth")).collect(Collectors.toList());
            return new BlockModelIterator(xmlList.iterator());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private class BlockModelIterator implements Iterator<NamedNode> {

        private Iterator<Resource> iterator;

        private BlockModelIterator(Iterator<Resource> iterator) throws ParserConfigurationException, SAXException {
            this.iterator = iterator;
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

                BasicParser parser = new BasicParser(fileHandler) {
                    @Override
                    protected void handle(String l) {
                        if (l.startsWith("<!DOCTYPE")) {
                            getHandler().text(l);
                            return;
                        }
                        NamedNode top = fileHandler.getTop();
                        if (top == null) {
                            super.handle(l);
                            return;
                        }
                        //TODO cleanup prefixes
                        if (top.getName().equals("escape") && !l.startsWith("</th:escape")) {
                            getHandler().text(l);
                        } else {
                            super.handle(l);
                        }
                    }
                };

                parser.parse(xml.getInputStream());

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
