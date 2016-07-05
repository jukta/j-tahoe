package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.GenContext;

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
        String attrs = "new Attrs(attrs)";
        for (String s : getAttrs().keySet()) {
            attrs += ".set(\"" + s + "\", " + parseExp(getAttrs().get(s), true) + ")";
        }
        String name = getAttrs().get("name");
        String el = "new com.jukta.jtahoe.IncludeBlock((String)" + parseExp(name, true) + ").body(" + attrs + ")";
        getParent().addElement(el);
    }

}
