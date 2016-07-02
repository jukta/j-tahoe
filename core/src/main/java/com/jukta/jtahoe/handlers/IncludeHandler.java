package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.GenContext;

import java.util.Map;

/**
 * Created by aleph on 18.02.2016.
 */
public class IncludeHandler extends BlockHandler {

    String body = "new JBody()";

    public IncludeHandler(GenContext genContext, String name, Map<String, String> attrs, AbstractHandler parent) {
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
        String name = getAttrs().get("name");
        String el = "new com.jukta.jtahoe.IncludeBlock((String)" + parseExp(name, true) + ", new String[] {";
        int i = 0;
        for (Map.Entry<String, String> entry : defs.entrySet()) {
            if (i++ > 0) el += ",";
            el += "\"" + entry.getKey() + entry.getValue() + "\"";
        }
        el += "})";
        el += ".body(" + attrs + ")";
        getParent().addElement(el);
    }


}
