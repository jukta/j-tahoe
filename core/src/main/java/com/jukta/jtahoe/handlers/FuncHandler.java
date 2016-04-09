package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.GenContext;

import java.util.Map;

/**
 * Created by aleph on 19.02.2016.
 */
public class FuncHandler extends BlockHandler {

    String body = "new JBody()";

    public FuncHandler(GenContext genContext, String name, Map<String, String> attrs, AbstractHandler parent) {
        super(genContext, name, attrs, parent);
    }

    @Override
    public void addElement(String element) {
        body += ".addElement(" + element + ")";
    }

    @Override
    public void end() {
        String attrs = "new Attrs()";
        for (String s : getAttrs().keySet()) {
            attrs += ".set(\"" + s + "\", \"" + getAttrs().get(s) + "\")";
        }
        String name = getName();
        name = processPrefix(name) + "." + name.substring(name.indexOf(":")+1);
        String el = "new " + name + "()";
        el += "{";
        for (Map.Entry<String, String> entry : defs.entrySet()) {
             el += entry.getKey() + entry.getValue();
        }
        el += "}";
        el += ".body(" + attrs + ")";
        getParent().addElement(el);
    }

    private String processPrefix(String name) {
        String prefix = name.split(":")[0];
        return getGenContext().getPrefixes().get(prefix);
    }
}
