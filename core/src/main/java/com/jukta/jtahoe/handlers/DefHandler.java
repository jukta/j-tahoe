package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.GenContext;

import java.util.Map;

/**
 * Created by aleph on 20.02.2016.
 */
public class DefHandler extends AbstractHandler {

    String body = "new com.jukta.jtahoe.jschema.JBody()";
    private String name;

    public DefHandler(GenContext genContext, String name, Map<String, String> attrs, AbstractHandler parent) {
        super(genContext, name, attrs, parent);
        this.name = getAttrs().get("name");
    }

    public String getDefName() {
        return name;
    }

    @Override
    public void addElement(String element) {
        body += ".addElement(" + element + ")";
    }

    @Override
    public void end() {
//        String attrs = "new com.jukta.jtahoe.Attrs()";
//        for (String s : getAttrs().keySet()) {
//            attrs += ".set(\"" + s + "\", \"" + getAttrs().get(s) + "\")";
//        }
        String defName = name == null ? "def" : "def_" + name;
        String el = "this." + defName + "(attrs)";
        getParent().addElement(el);

        BlockHandler blockHandler = getBlock();
        if (blockHandler instanceof FuncHandler) {
            blockHandler.addDef("com.jukta.jtahoe.jschema.JElement " + defName + "(com.jukta.jtahoe.Attrs _attrs)", "{return " + body + ";}");
        } else {
            blockHandler.addDef("com.jukta.jtahoe.jschema.JElement " + defName + "(com.jukta.jtahoe.Attrs attrs)", "{return " + body + ";}");
        }
    }
}
