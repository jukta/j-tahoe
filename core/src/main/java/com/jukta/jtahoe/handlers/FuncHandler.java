package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.GenContext;

import java.util.Map;

/**
 * Created by aleph on 19.02.2016.
 */
public class FuncHandler extends BlockHandler {

    String body = "new com.jukta.jtahoe.jschema.JBody()";

    public FuncHandler(GenContext genContext, String name, Map<String, String> attrs, AbstractHandler parent) {
        super(genContext, name, attrs, parent);
    }

    @Override
    public void addElement(String element) {
        body += ".addElement(" + element + ")";
    }

    @Override
    public void end() {
        String attrs = "new com.jukta.jtahoe.Attrs()";
        for (String s : getAttrs().keySet()) {
            attrs += ".set(\"" + s + "\", \"" + getAttrs().get(s) + "\")";
        }
        String name = getName().substring(3);
        String el = "new " + name + "()";
        el += "{";
        for (Map.Entry<String, String> entry : defs.entrySet()) {
             el += entry.getKey() + entry.getValue();
        }
        el += "}";
        el += ".body(" + attrs + ")";
        getParent().addElement(el);
    }
}
