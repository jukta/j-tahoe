package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;
import com.jukta.jtahoe.gen.model.NamedNode;

/**
 * Created by aleph on 18.02.2016.
 */
public class IncludeHandler extends SimpleBlockHandler {

    public IncludeHandler(GenContext genContext, NamedNode node, AbstractHandler parent) {
        super(genContext, node, parent);
    }

    @Override
    public void end() {
        String name = getAttrs().get("name");
        String ref = getAttrs().get("ref");
        String el;

        String attrs = "new Attrs(attrs)";
        if (name != null && ref == null) {
            for (String s : getAttrs().keySet()) {
                attrs += ".set(\"" + s + "\", " + parseExp(getAttrs().get(s), true) + ")";
            }
            el = "new com.jukta.jtahoe.IncludeBlock((String)" + parseExp(name, true) + ").body(" + attrs + ")";
        } else if (name == null && ref != null) {
            ref = parseExp(ref, true);
            el = "((JBody)" + ref + ")";
        } else {
            throw new RuntimeException("Name or ref should be defined in include");
        }
        getParent().addElement(el);
    }

}
