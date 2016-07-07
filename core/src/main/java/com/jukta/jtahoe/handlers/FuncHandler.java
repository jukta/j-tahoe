package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;

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
        body += getVarName() + ".addElement(" + element + ");";
    }

    @Override
    public void end() {
        String attrs = "new Attrs(attrs)";
        for (String s : getAttrs().keySet()) {
            attrs += ".set(\"" + s + "\", " + parseExp(getAttrs().get(s), true) + ")";
        }
        String name = getName();
        name = processPrefix(name);
        String el = "new " + name + "()";
        if (!defs.isEmpty()) {
            el += "{";
            for (Map.Entry<String, String> entry : defs.entrySet()) {
                el += entry.getKey() + entry.getValue();
            }
            el += "}";
        }
        el += ".body(" + attrs + ")";
        getParent().addElement(el);
    }

}
