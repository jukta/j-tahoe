package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;

import java.util.Map;

/**
 * Created by aleph on 18.02.2016.
 */
public class ForHandler extends AbstractHandler {

    public ForHandler(GenContext genContext, String name, Map<String, String> attrs, AbstractHandler parent) {
        super(genContext, name, attrs, parent);
    }

    @Override
    public void end() {
        String var = getAttrs().get("var");
        String in = parseItExp(getAttrs().get("in"), false);
        String cd = "JBody " + getVarName() + " = new JBody();\n";
        cd += "for (Object " + var + " : " + in + ") {";
        cd += "attrs.set(\"" + var +"\", " + var + ");";
        cd += body;
        cd += "}";
        getParent().appendCode(cd);
//        String el = "new JTag(\"" + getName() + "\").setjBody(" + getVarName() + ")";
        getParent().addElement(getVarName());
    }

    String body = "";

    @Override
    public void addElement(String element) {
        body += getVarName()+".addElement(" + element + ");\n";
    }

    @Override
    public void appendCode(String code) {
        body += code;
    }
}
