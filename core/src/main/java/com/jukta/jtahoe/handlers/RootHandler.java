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
        for (Map.Entry<String, String> entry : getNode().getPrefixes().entrySet()) {
            getGenContext().getPrefixes().put(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, String> pr : getGenContext().getPrefixes().entrySet()) {
            if (".".equals(pr.getValue())) {
                getGenContext().getPrefixes().put(pr.getKey(), getAttrs().get("namespace"));
            }
        }
        getGenContext().setCurrentNamespace(getAttrs().get("namespace"));
    }

    @Override
    public void end() {

    }


}
