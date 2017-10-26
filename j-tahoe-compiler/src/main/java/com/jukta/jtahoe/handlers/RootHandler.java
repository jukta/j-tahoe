package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;
import com.jukta.jtahoe.gen.model.NamedNode;

import java.util.Map;

/**
 * Created by aleph on 18.02.2016.
 */
public class RootHandler extends AbstractHandler {

    public RootHandler(GenContext genContext, NamedNode node, AbstractHandler parent) {
        super(genContext, node, parent);
    }

    @Override
    public void start() {
        getGenContext().getPrefixes().putAll(getNode().getPrefixes());
        String namespace = getAttrs().get("namespace");

        for (Map.Entry<String, String> pr : getGenContext().getPrefixes().entrySet()) {
            if (".".equals(pr.getValue())) {
                getGenContext().getPrefixes().put(pr.getKey(), namespace);
            }
        }
        getGenContext().setCurrentNamespace(namespace);
    }

    @Override
    public void end() {

    }

    @Override
    public void text(String text) {}
}
