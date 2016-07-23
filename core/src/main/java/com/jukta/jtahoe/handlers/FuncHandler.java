package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;
import com.jukta.jtahoe.model.NamedNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aleph on 19.02.2016.
 */
public class FuncHandler extends BlockHandler {

    private String body = "";
    private DefHandler defHandler;

    public FuncHandler(GenContext genContext, NamedNode node, AbstractHandler parent) {
        super(genContext, node, parent);
        NamedNode defNode = new NamedNode("", "def", new HashMap<String, String>(), getNode());
        defHandler = new DefHandler(genContext, defNode, this);

    }

    @Override
    public void addElement(String element) {
        if (defHandler != null)
            defHandler.addElement(element);
    }

    @Override
    public void appendCode(String code) {
        if (defHandler != null)
            defHandler.appendCode(code);
    }

    @Override
    public void end() {
        DefHandler dh = defHandler;
        defHandler = null;
        if (defs.size() == 0 && dh.body.length() > 0) dh.end();
        String attrs = "new Attrs(attrs)";
        for (String s : getAttrs().keySet()) {
            attrs += ".set(\"" + s + "\", " + parseExp(getAttrs().get(s), true) + ")";
        }
        String name = getName();
        name = getPackage(getNode().getNamespace()) + "." + name;
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
