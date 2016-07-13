package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;

import java.util.Map;

/**
 * Created by aleph on 18.02.2016.
 */
public class IncludeHandler extends SimpleBlockHandler {

    public IncludeHandler(GenContext genContext, String name, Map<String, String> attrs, AbstractHandler parent) {
        super(genContext, name, attrs, parent);
    }

    @Override
    public void end() {
        String name = getAttrs().get("name");
        String ref = parseExp(getAttrs().get("ref"), true);
        String el;

        String attrs = "new Attrs(attrs)";
        if (name != null && ref == null) {
            for (String s : getAttrs().keySet()) {
                attrs += ".set(\"" + s + "\", " + parseExp(getAttrs().get(s), true) + ")";
            }
            el = "new com.jukta.jtahoe.IncludeBlock((String)" + parseExp(name, true) + ").body(" + attrs + ")";
        } else if (name == null && ref != null) {

            el = "((JBody)" + ref + ")";
        } else {
            throw new RuntimeException("Name or ref should be defined in include");
        }
        getParent().addElement(el);
    }

}
