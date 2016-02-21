package com.jukta.jtahoe.handlers;

import java.util.Map;

/**
 * Created by aleph on 20.02.2016.
 */
public class DefHandler extends AbstractHandler {

    String body = "new com.jukta.jtahoe.jschema.JBody()";

    public DefHandler(String name, Map<String, String> attrs, AbstractHandler parent) {
        super(name, attrs, parent);
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
        String name = getAttrs().get("name");
        String el = "this.def_" + name + "(" + attrs +")";
        getParent().addElement(el);

        BlockHandler blockHandler = getBlock();
        if (blockHandler instanceof FuncHandler) {
            blockHandler.addDef("com.jukta.jtahoe.jschema.JElement def_" + name + "(com.jukta.jtahoe.Attrs _attrs)", "{return " + body + ";}");
        } else {
            blockHandler.addDef("com.jukta.jtahoe.jschema.JElement def_" + name + "(com.jukta.jtahoe.Attrs attrs)", "{return " + body + ";}");
        }


    }
}
